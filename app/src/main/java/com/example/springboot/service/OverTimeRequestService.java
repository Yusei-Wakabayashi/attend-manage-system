package com.example.springboot.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.dto.change.StringToDuration;
import com.example.springboot.dto.change.StringToLocalDateTime;
import com.example.springboot.dto.input.OverTimeInput;
import com.example.springboot.model.Account;
import com.example.springboot.model.LegalTime;
import com.example.springboot.model.OverTimeRequest;
import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftListOtherTime;
import com.example.springboot.repository.OverTimeRequestRepository;

@Service
public class OverTimeRequestService
{
    private final OverTimeRequestRepository overTimeRequestRepository;
    private final ShiftService shiftService;
    private final ShiftListOtherTimeService shiftListOtherTimeService;
    private final StringToLocalDateTime stringToLocalDateTime;
    private final StringToDuration stringToDuration;
    private final LegalTimeService legalTimeService;

    @Autowired
    public OverTimeRequestService
    (
        OverTimeRequestRepository overTimeRequestRepository,
        ShiftService shiftService,
        ShiftListOtherTimeService shiftListOtherTimeService,
        StringToLocalDateTime stringToLocalDateTime,
        StringToDuration stringToDuration,
        LegalTimeService legalTimeService
    )
    {
        this.overTimeRequestRepository = overTimeRequestRepository;
        this.shiftService = shiftService;
        this.shiftListOtherTimeService = shiftListOtherTimeService;
        this.stringToLocalDateTime = stringToLocalDateTime;
        this.stringToDuration = stringToDuration;
        this.legalTimeService = legalTimeService;
    }

    public int createOverTimeRequest(Account account, OverTimeInput overTimeInput)
    {
        Shift shift = shiftService.findByAccountIdAndShiftId(account, overTimeInput.getShiftId());
        if(Objects.isNull(shift))
        {
            return 3;
        }
        // 残業の開始の後に残業の終了があること
        LocalDateTime startTimeOverWork = stringToLocalDateTime.stringToLocalDateTime(overTimeInput.getBeginOverTime());
        LocalDateTime endTimeOverWork = stringToLocalDateTime.stringToLocalDateTime(overTimeInput.getEndOverTime());
        if(endTimeOverWork.isAfter(startTimeOverWork))
        {
            // 条件通りなら何もしない
        }
        else
        {
            return 3;
        }
        // 既に存在する残業と重複する場合申請NG
        List<OverTimeRequest> overTimeRequests = findByAccounIdAndRequestStatusWaitOrApprovedAndBeginWorkOrEndWorkBetween(account, startTimeOverWork, endTimeOverWork);
        if(overTimeRequests.size() > 0)
        {
            // 1件でも存在すれば重複があったということなのでエラー
            return 3;
        }
        // シフトと時間が重なっていないこと
        List<Shift> shifts = shiftService.shiftOverLapping(account, startTimeOverWork, endTimeOverWork);
        if(shifts.size() > 0)
        {
            // 1件でも存在すれば重複があったということなのでエラー
            return 3;
        }
        // 遅刻早退との重複NG
        List<ShiftListOtherTime> shiftListOtherTimes = shiftListOtherTimeService.findByShiftId(shift);
        List<ShiftListOtherTime> shiftListOtherTimesLateness = new ArrayList<ShiftListOtherTime>();
        List<ShiftListOtherTime> shiftListOtherTimesLeaveEarly = new ArrayList<ShiftListOtherTime>();
        // 勤怠例外のうち遅刻と早退を取得
        for(ShiftListOtherTime shiftListOtherTime : shiftListOtherTimes)
        {
            if(shiftListOtherTime.getAttendanceExceptionId().getAttendanceExceptionTypeId().getAttendanceExceptionTypeId() == 2L)
            {
                shiftListOtherTimesLateness.add(shiftListOtherTime);
            }
            else if(shiftListOtherTime.getAttendanceExceptionId().getAttendanceExceptionTypeId().getAttendanceExceptionTypeId() == 3L)
            {
                shiftListOtherTimesLeaveEarly.add(shiftListOtherTime);
            }
        }
        // 申請する残業が始業前で遅刻が存在する場合
        if(endTimeOverWork.isEqual(shift.getBeginWork()) && shiftListOtherTimesLateness.size() > 0)
        {
            return 3;
        }
        // 申請する残業が終業後で早退が存在する場合
        if(startTimeOverWork.isEqual(shift.getEndWork()) && shiftListOtherTimesLeaveEarly.size() > 0)
        {
            return 3;
        }
        // 法定時間を超える申請(承認待ち、承認済み)はNG
        // 申請する月でそのアカウントの残業申請(承認待ち、承認済み)を取得法定時間を超えないことを確認
        List<OverTimeRequest> overTimeRequestMonthList = findByAccountIdAndRequestStatusWaitOrApprovedAndBeginWorkBetweenMonth(account, startTimeOverWork.getYear(), startTimeOverWork.getMonthValue());
        Duration monthOverWorkTime = Duration.between(startTimeOverWork, endTimeOverWork);
        for(OverTimeRequest overTimeRequest : overTimeRequestMonthList)
        {
            Duration overTime = Duration.between(overTimeRequest.getBeginWork(), overTimeRequest.getEndWork());
            monthOverWorkTime = monthOverWorkTime.plus(overTime);
        }
        LegalTime legalTime = legalTimeService.findFirstByOrderByBeginDesc();
        // メソッド内の値より後(大きい)ならエラー
        if(monthOverWorkTime.compareTo(stringToDuration.stringToDuration(legalTime.getMonthlyOverWorkTime())) > 0)
        {
            return 3;
        }
        // 残業申請登録(サービス層で行うべき?)
        OverTimeRequest overTimeRequest = new OverTimeRequest();
        overTimeRequest.setAccountId(account);
        overTimeRequest.setBeginWork(startTimeOverWork);
        overTimeRequest.setEndWork(endTimeOverWork);
        overTimeRequest.setRequestComment(overTimeInput.getRequestComment());
        overTimeRequest.setRequestDate(overTimeInput.getRequestDate() == null ? null : stringToLocalDateTime.stringToLocalDateTime(overTimeInput.getRequestDate()));
        overTimeRequest.setRequestStatus(1);
        overTimeRequest.setShiftId(shift);
        OverTimeRequest requestOverTimeRequest = save(overTimeRequest);
        if(Objects.isNull(requestOverTimeRequest) || Objects.isNull(requestOverTimeRequest.getOverTimeId()))
        {
            return 3;
        }
        return 1;
    }
    
    public OverTimeRequest findById(Long requestId)
    {
        return overTimeRequestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("残業申請が見つかりません"));
    }
    public OverTimeRequest findByAccountIdAndOverTimeRequestId(Long accountId, Long id)
    {
        Account account = new Account();
        account.setId(accountId);
        return overTimeRequestRepository.findByAccountIdAndOverTimeId(account, id)
            .orElseThrow(() -> new RuntimeException("残業申請が見つかりません"));
    }

    public OverTimeRequest findByAccountIdAndOverTimeRequestId(Account account, Long id)
    {
        return overTimeRequestRepository.findByAccountIdAndOverTimeId(account, id)
            .orElseThrow(() -> new RuntimeException("残業申請が見つかりません"));
    }

    public List<OverTimeRequest> findByAccountIdAndShiftIdAndRequestStatusWait(Long accountId, Long shiftId)
    {
        Account account = new Account();
        account.setId(accountId);
        Shift shift = new Shift();
        shift.setShiftId(shiftId);
        int requestStatus = 1;
        List<OverTimeRequest> overTimeRequests = overTimeRequestRepository.findByAccountIdAndShiftIdAndRequestStatus(account, shift, requestStatus);
        return overTimeRequests;
    }

    public List<OverTimeRequest> findByAccountIdAndShiftIdAndRequestStatusWait(Account account, Long shiftId)
    {
        Shift shift = new Shift();
        shift.setShiftId(shiftId);
        int requestStatus = 1;
        List<OverTimeRequest> overTimeRequests = overTimeRequestRepository.findByAccountIdAndShiftIdAndRequestStatus(account, shift, requestStatus);
        return overTimeRequests;
    }

    public List<OverTimeRequest> findByAccountIdAndShiftIdAndRequestStatusWait(Long accountId, Shift shift)
    {
        Account account = new Account();
        account.setId(accountId);
        int requestStatus = 1;
        List<OverTimeRequest> overTimeRequests = overTimeRequestRepository.findByAccountIdAndShiftIdAndRequestStatus(account, shift, requestStatus);
        return overTimeRequests;
    }

    public List<OverTimeRequest> findByAccountIdAndShiftIdAndRequestStatusWait(Account account, Shift shift)
    {
        int requestStatus = 1;
        List<OverTimeRequest> overTimeRequests = overTimeRequestRepository.findByAccountIdAndShiftIdAndRequestStatus(account, shift, requestStatus);
        return overTimeRequests;
    }

    public List<OverTimeRequest> findByAccountId(Long accountId)
    {
        Account account = new Account();
        account.setId(accountId);
        return overTimeRequestRepository.findByAccountId(account);
    }

    public List<OverTimeRequest> findByAccountId(Account account)
    {
        return overTimeRequestRepository.findByAccountId(account);
    }

    public List<OverTimeRequest> findByAccountIdIn(List<Account> accounts)
    {
        List<OverTimeRequest> overTimeRequests = overTimeRequestRepository.findByAccountIdIn(accounts);
        return overTimeRequests;
    }

    public List<OverTimeRequest> findByAccounIdAndRequestStatusWaitOrApprovedAndBeginWorkOrEndWorkBetween(Long accountId, LocalDateTime startPeriod, LocalDateTime endPeriod)
    {
        Account account = new Account();
        account.setId(accountId);
        int wait = 1;
        int approved = 2;
        // 検索する際申請のリクエストが19時開始20時終了で残業申請が18時開始19時終了の際18時を含みたくないため1秒ずらしている
        startPeriod.plusSeconds(1);
        endPeriod.minusSeconds(1);
        List<OverTimeRequest> waitOverTimeRequestBeginList = overTimeRequestRepository.findByAccountIdAndRequestStatusAndBeginWorkBetween(account, wait, startPeriod, endPeriod);
        List<OverTimeRequest> waitOverTimeRequestEndList = overTimeRequestRepository.findByAccountIdAndRequestStatusAndEndWorkBetween(account, wait, startPeriod, endPeriod);
        List<OverTimeRequest> approvedOverTimeRequestBeginList = overTimeRequestRepository.findByAccountIdAndRequestStatusAndBeginWorkBetween(account, approved, startPeriod, endPeriod);
        List<OverTimeRequest> approvedOverTimeRequestEndList = overTimeRequestRepository.findByAccountIdAndRequestStatusAndEndWorkBetween(account, approved, startPeriod, endPeriod);
        List<OverTimeRequest> waitOverTimeRequestList = Stream.concat(waitOverTimeRequestBeginList.stream(), waitOverTimeRequestEndList.stream()).distinct().collect(Collectors.toList());
        List<OverTimeRequest> approvedOverTimeRequestList = Stream.concat(approvedOverTimeRequestBeginList.stream(), approvedOverTimeRequestEndList.stream()).distinct().collect(Collectors.toList());
        List<OverTimeRequest> overTimeRequestList = Stream.concat(waitOverTimeRequestList.stream(), approvedOverTimeRequestList.stream()).distinct().collect(Collectors.toList());
        return overTimeRequestList;
    }

    public List<OverTimeRequest> findByAccounIdAndRequestStatusWaitOrApprovedAndBeginWorkOrEndWorkBetween(Account account, LocalDateTime startPeriod, LocalDateTime endPeriod)
    {
        int wait = 1;
        int approved = 2;
        // 検索する際申請のリクエストが19時開始20時終了で残業申請が18時開始19時終了の際18時を含みたくないため1秒ずらしている
        startPeriod.plusSeconds(1);
        endPeriod.minusSeconds(1);
        List<OverTimeRequest> waitOverTimeRequestBeginList = overTimeRequestRepository.findByAccountIdAndRequestStatusAndBeginWorkBetween(account, wait, startPeriod, endPeriod);
        List<OverTimeRequest> waitOverTimeRequestEndList = overTimeRequestRepository.findByAccountIdAndRequestStatusAndEndWorkBetween(account, wait, startPeriod, endPeriod);
        List<OverTimeRequest> approvedOverTimeRequestBeginList = overTimeRequestRepository.findByAccountIdAndRequestStatusAndBeginWorkBetween(account, approved, startPeriod, endPeriod);
        List<OverTimeRequest> approvedOverTimeRequestEndList = overTimeRequestRepository.findByAccountIdAndRequestStatusAndEndWorkBetween(account, approved, startPeriod, endPeriod);
        List<OverTimeRequest> waitOverTimeRequestList = Stream.concat(waitOverTimeRequestBeginList.stream(), waitOverTimeRequestEndList.stream()).distinct().collect(Collectors.toList());
        List<OverTimeRequest> approvedOverTimeRequestList = Stream.concat(approvedOverTimeRequestBeginList.stream(), approvedOverTimeRequestEndList.stream()).distinct().collect(Collectors.toList());
        List<OverTimeRequest> overTimeRequestList = Stream.concat(waitOverTimeRequestList.stream(), approvedOverTimeRequestList.stream()).distinct().collect(Collectors.toList());
        return overTimeRequestList;
    }

    public List<OverTimeRequest> findByAccountIdAndRequestStatusWaitOrApprovedAndBeginWorkBetweenMonth(Long accountId, int year, int month)
    {
        Account account = new Account();
        account.setId(accountId);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        int wait = 1;
        int approved = 2;
        List<OverTimeRequest> overTimeRequestWait = overTimeRequestRepository.findByAccountIdAndRequestStatusAndBeginWorkBetween(account, wait, startPeriod, endPeriod);
        List<OverTimeRequest> overTimeRequestApproved = overTimeRequestRepository.findByAccountIdAndRequestStatusAndBeginWorkBetween(account, approved, startPeriod, endPeriod);
        List<OverTimeRequest> overTimeRequests = Stream.concat(overTimeRequestWait.stream(), overTimeRequestApproved.stream()).distinct().collect(Collectors.toList());
        return overTimeRequests;
    }

    public List<OverTimeRequest> findByAccountIdAndRequestStatusWaitOrApprovedAndBeginWorkBetweenMonth(Account account, int year, int month)
    {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        int wait = 1;
        int approved = 2;
        List<OverTimeRequest> overTimeRequestWait = overTimeRequestRepository.findByAccountIdAndRequestStatusAndBeginWorkBetween(account, wait, startPeriod, endPeriod);
        List<OverTimeRequest> overTimeRequestApproved = overTimeRequestRepository.findByAccountIdAndRequestStatusAndBeginWorkBetween(account, approved, startPeriod, endPeriod);
        List<OverTimeRequest> overTimeRequests = Stream.concat(overTimeRequestWait.stream(), overTimeRequestApproved.stream()).distinct().collect(Collectors.toList());
        return overTimeRequests;
    }

    public List<OverTimeRequest> findByAccountIdAndBeginWorkAndRequstStatusWait(Long accountId, int year, int month)
    {
        Account account = new Account();
        account.setId(accountId);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        int wait = 1;
        List<OverTimeRequest> overTimeRequests = overTimeRequestRepository.findByAccountIdAndRequestStatusAndBeginWorkBetween(account, wait, startPeriod, endPeriod);
        return overTimeRequests;
    }

    public List<OverTimeRequest> findByAccountIdAndBeginWorkAndRequstStatusWait(Account account, int year, int month)
    {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        int wait = 1;
        List<OverTimeRequest> overTimeRequests = overTimeRequestRepository.findByAccountIdAndRequestStatusAndBeginWorkBetween(account, wait, startPeriod, endPeriod);
        return overTimeRequests;
    }

    public OverTimeRequest save(OverTimeRequest overTimeRequest)
    {
        return overTimeRequestRepository.save(overTimeRequest);
    }

    @Transactional
    public void resetAllTables()
    {
        overTimeRequestRepository.deleteAll();;
        overTimeRequestRepository.resetAutoIncrement();
    }
}