package com.example.springboot.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.dto.change.StringToLocalDateTime;
import com.example.springboot.dto.input.ShiftChangeInput;
import com.example.springboot.model.Account;
import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftChangeRequest;
import com.example.springboot.repository.ShiftChangeRequestRepository;

@Service
public class ShiftChangeRequestService
{
    // 再代入不可で定義
    private final ShiftChangeRequestRepository shiftChangeRequestRepository;
    private final StringToLocalDateTime stringToLocalDateTime;
    private final ShiftService shiftService;

    // コンストラクタで依存性注入
    @Autowired
    public ShiftChangeRequestService
    (
        ShiftChangeRequestRepository shiftChangeRequestRepository,
        StringToLocalDateTime stringToLocalDateTime,
        ShiftService shiftService
    )
    {
        this.shiftChangeRequestRepository = shiftChangeRequestRepository;
        this.stringToLocalDateTime = stringToLocalDateTime;
        this.shiftService = shiftService;
    }

    public int createShiftChangeRequest(Account account, ShiftChangeInput shiftChangeInput)
    {
        // アカウントの情報とシフトの情報を基に検索
        Shift shift = shiftService.findByAccountIdAndShiftId(account, shiftChangeInput.getShiftId());
        if(Objects.isNull(shift))
        {
            return 4;
        }
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime beginWork = stringToLocalDateTime.stringToLocalDateTime(shiftChangeInput.getBeginWork());
        LocalDateTime endWork = stringToLocalDateTime.stringToLocalDateTime(shiftChangeInput.getEndWork());
        LocalDateTime beginBreak = stringToLocalDateTime.stringToLocalDateTime(shiftChangeInput.getBeginBreak());
        LocalDateTime endBreak = stringToLocalDateTime.stringToLocalDateTime(shiftChangeInput.getEndBreak());
        // シフト時間変更申請で同じシフトに承認待ちの申請がないことを確認
        List<ShiftChangeRequest> shiftChangeRequestWaits = findByAccountIdAndShiftIdAndRequestStatusWait(account, shift.getShiftId());
        if(shiftChangeRequestWaits.size() > 0)
        {
            return 3;
        }        
        // 始業時間より終業時間が後になっていること、休憩開始時間より休憩終了時間が後になっていること、始業時間より休憩開始時間が後になっていること、休憩終了時間より終業時間が後になっていること
        if(endWork.isAfter(beginWork) && endBreak.isAfter(beginBreak) && beginBreak.isAfter(beginWork) && endWork.isAfter(endBreak))
        {
            // 条件通りなら何もしない
        }
        else
        {
            // 条件に沿っていなかったらエラー
            return 3;
        }

        // 始業時間が1年先までは許容する
        LocalDateTime nextYear = nowTime.plusYears(1L);
        // 現在時刻より後かつ1年後(nextYear)より前
        if(beginWork.isAfter(nowTime) && beginWork.isBefore(nextYear))
        {
            // 条件に従っていれば何もしない
        }
        else
        {
            // 1年前後に収まっていない始業時間ならエラー
            return 3;
        }
        // シフト時間変更申請(サービス層で行うべき?)
        ShiftChangeRequest shiftChangeRequest = new ShiftChangeRequest();
        shiftChangeRequest.setAccountId(account);
        shiftChangeRequest.setBeginWork(beginWork);
        shiftChangeRequest.setEndWork(endWork);
        shiftChangeRequest.setBeginBreak(beginBreak);
        shiftChangeRequest.setEndBreak(endBreak);
        shiftChangeRequest.setRequestComment(shiftChangeInput.getRequestComment());
        shiftChangeRequest.setRequestDate(stringToLocalDateTime.stringToLocalDateTime(shiftChangeInput.getRequestDate()));
        shiftChangeRequest.setRequestStatus(1);
        shiftChangeRequest.setApprover(null);
        shiftChangeRequest.setApprovalTime(null);
        shiftChangeRequest.setApproverComment(null);
        shiftChangeRequest.setShiftId(shift);
        ShiftChangeRequest resultShiftChangeRequest = save(shiftChangeRequest);
        if(Objects.isNull(resultShiftChangeRequest) || Objects.isNull(resultShiftChangeRequest.getShiftChangeId()))
        {
            return 3;
        }
        return 1;
    }

    public List<ShiftChangeRequest> getAllShiftChangeRequest()
    {
        return shiftChangeRequestRepository.findAll();
    }

    public ShiftChangeRequest findById(Long id)
    {
        return shiftChangeRequestRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("シフト時間変更申請が見つかりません"));
    }
    public ShiftChangeRequest findByAccountIdAndShiftChangeRequestId(Account account, Long id)
    {
        return shiftChangeRequestRepository.findByAccountIdAndShiftChangeId(account, id)
            .orElseThrow(() -> new RuntimeException("シフト時間変更申請が見つかりません"));
    }
    public ShiftChangeRequest findByAccountIdAndShiftChangeRequestId(Long accountId, Long id)
    {
        Account account = new Account();
        account.setId(accountId);
        return shiftChangeRequestRepository.findByAccountIdAndShiftChangeId(account, id)
            .orElseThrow(() -> new RuntimeException("シフト時間変更申請が見つかりません"));
    }

    public List<ShiftChangeRequest> findByAccountIdAndShiftIdAndRequestStatusWait(Account account, Long id)
    {
        int requestStatus = 1;
        Shift shift = new Shift();
        shift.setShiftId(id);
        List<ShiftChangeRequest> shiftChangeRequests = shiftChangeRequestRepository.findByAccountIdAndShiftIdAndRequestStatus(account, shift, requestStatus);
        return shiftChangeRequests;
    }

    public List<ShiftChangeRequest> findByAccountIdAndShiftIdAndRequestStatusWait(Long accountId, Long id)
    {
        Account account = new Account();
        account.setId(accountId);
        int requestStatus = 1;
        Shift shift = new Shift();
        shift.setShiftId(id);
        List<ShiftChangeRequest> shiftChangeRequests = shiftChangeRequestRepository.findByAccountIdAndShiftIdAndRequestStatus(account, shift, requestStatus);
        return shiftChangeRequests;
    }

    public List<ShiftChangeRequest> findAccountIdAndBeginWorkBetweenAndRequestStatusWaitWeek(Account account, LocalDateTime begin)
    {
        LocalDateTime beginWork = begin.toLocalDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
        LocalDateTime endWork = begin.toLocalDate().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).atTime(LocalTime.MAX);
        int requestStatus = 1;
        List<ShiftChangeRequest> shiftChangeRequests = shiftChangeRequestRepository.findByAccountIdAndBeginWorkBetweenAndRequestStatus(account, beginWork, endWork, requestStatus);
        return shiftChangeRequests;
    }

    public List<ShiftChangeRequest> findAccountIdAndBeginWorkBetweenAndRequestStatusWaitWeek(Long id, LocalDateTime begin)
    {
        Account account = new Account();
        account.setId(id);
        LocalDateTime beginWork = begin.toLocalDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
        LocalDateTime endWork = begin.toLocalDate().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).atTime(LocalTime.MAX);
        int requestStatus = 1;
        List<ShiftChangeRequest> shiftChangeRequests = shiftChangeRequestRepository.findByAccountIdAndBeginWorkBetweenAndRequestStatus(account, beginWork, endWork, requestStatus);
        return shiftChangeRequests;
    }

    public List<ShiftChangeRequest> findAccountIdAndShiftIdInAndRequestStatusWait(Account account, List<Shift> shifts)
    {
        int status = 1;
        List<ShiftChangeRequest> shiftChangeRequests = shiftChangeRequestRepository.findByAccountIdAndShiftIdInAndRequestStatus(account, shifts, status);
        return shiftChangeRequests;
    }

    public List<ShiftChangeRequest> findByAccountId(Long accountId)
    {
        Account account = new Account();
        account.setId(accountId);
        return shiftChangeRequestRepository.findByAccountId(account);
    }

    public List<ShiftChangeRequest> findByAccountId(Account account)
    {
        return shiftChangeRequestRepository.findByAccountId(account);
    }

    public List<ShiftChangeRequest> findByAccountIdIn(List<Account> accounts)
    {
        List<ShiftChangeRequest> shiftChangeRequests = shiftChangeRequestRepository.findByAccountIdIn(accounts);
        return shiftChangeRequests;
    }

    public List<ShiftChangeRequest> findByAccountIdAndBeginWorkBetweenAndRequestStatusWait(Long accountId, int year, int month)
    {
        Account account = new Account();
        account.setId(accountId);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        int wait = 1;
        List<ShiftChangeRequest> shiftChangeRequests = shiftChangeRequestRepository.findByAccountIdAndBeginWorkBetweenAndRequestStatus(account, startPeriod, endPeriod, wait);
        return shiftChangeRequests;
    }

    public List<ShiftChangeRequest> findByAccountIdAndBeginWorkBetweenAndRequestStatusWait(Account account, int year, int month)
    {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        int wait = 1;
        List<ShiftChangeRequest> shiftChangeRequests = shiftChangeRequestRepository.findByAccountIdAndBeginWorkBetweenAndRequestStatus(account, startPeriod, endPeriod, wait);
        return shiftChangeRequests;
    }

    public Shift shiftChangeRequestToShift(ShiftChangeRequest shiftChangeRequest)
    {
        Shift shift = shiftChangeRequest.getShiftId();
        shift.setBeginWork(shiftChangeRequest.getBeginWork());
        shift.setEndWork(shiftChangeRequest.getEndWork());
        shift.setBeginBreak(shiftChangeRequest.getBeginBreak());
        shift.setEndBreak(shiftChangeRequest.getEndBreak());
        return shift;
    } 

    public ShiftChangeRequest save(ShiftChangeRequest shiftChangeRequest)
    {
        return shiftChangeRequestRepository.save(shiftChangeRequest);
    }

    @Transactional
    public void resetAllTables()
    {
        shiftChangeRequestRepository.deleteAll();
        shiftChangeRequestRepository.resetAutoIncrement();
    }

    public void init()
    {
        
    }
}