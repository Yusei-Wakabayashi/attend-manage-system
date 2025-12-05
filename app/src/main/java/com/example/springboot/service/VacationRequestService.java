package com.example.springboot.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.dto.change.StringToDuration;
import com.example.springboot.dto.change.StringToLocalDateTime;
import com.example.springboot.dto.input.VacationInput;
import com.example.springboot.model.Account;
import com.example.springboot.model.OverTimeRequest;
import com.example.springboot.model.PaydHoliday;
import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftListOverTime;
import com.example.springboot.model.VacationRequest;
import com.example.springboot.model.VacationType;
import com.example.springboot.repository.VacationRequestRepository;

@Service
public class VacationRequestService
{
    private final VacationRequestRepository vacationRequestRepository;
    private final StringToLocalDateTime stringToLocalDateTime;
    private final StringToDuration stringToDuration;
    private final ShiftService shiftService;
    private final ShiftListOverTimeService shiftListOverTimeService;
    private final OverTimeRequestService overTimeRequestService;
    private final PaydHolidayService paydHolidayService;
    private final VacationTypeService vacationTypeService;

    @Autowired
    public VacationRequestService
    (
        VacationRequestRepository vacationRequestRepository,
        StringToLocalDateTime stringToLocalDateTime,
        StringToDuration stringToDuration,
        ShiftService shiftService,
        ShiftListOverTimeService shiftListOverTimeService,
        OverTimeRequestService overTimeRequestService,
        PaydHolidayService paydHolidayService,
        VacationTypeService vacationTypeService
    )
    {
        this.vacationRequestRepository = vacationRequestRepository;
        this.stringToLocalDateTime = stringToLocalDateTime;
        this.stringToDuration = stringToDuration;
        this.shiftService = shiftService;
        this.shiftListOverTimeService = shiftListOverTimeService;
        this.overTimeRequestService = overTimeRequestService;
        this.paydHolidayService = paydHolidayService;
        this.vacationTypeService = vacationTypeService;
    }

    @Transactional
    public int createVacationRequest(Account account, VacationInput vacationInput)
    {
        // 休暇の時刻が形式に沿っているか
        LocalDateTime beginVacation = stringToLocalDateTime.stringToLocalDateTime(vacationInput.getBeginVacation());
        LocalDateTime endVacation = stringToLocalDateTime.stringToLocalDateTime(vacationInput.getEndVacation());

        // 休暇開始の後に休暇終了の形になっているか
        if(endVacation.isAfter(beginVacation))
        {
            // 形式通りなら何もしない
        }
        else
        {
            // それ以外ならエラー
            return 3;
        }
        // 現在時刻より後なら正常
        LocalDateTime nowTime = LocalDateTime.now();
        if(beginVacation.isAfter(nowTime))
        {
            // 正常
        }
        else
        {
            return 4;
        }
        // シフトの時間内に収まっているのか
        Shift shift = shiftService.findByAccountIdAndShiftId(account, vacationInput.getShiftId());
        // 始業時間より休暇の開始または終了が前ならエラー、終業時間より休暇の開始または終了が後ならエラー
        if(beginVacation.isBefore(shift.getBeginWork()) || endVacation.isBefore(shift.getBeginWork()) || beginVacation.isAfter(shift.getEndWork()) || endVacation.isAfter(shift.getEndWork()))
        {
            return 3;
        }
        // すでに申請されている休暇と重複していないか
        List<VacationRequest> vacationRequests = findByAccountIdAndShiftId(account, shift);
        for(VacationRequest vacationRequest : vacationRequests)
        {
            // 申請済みの休暇の休暇開始と休暇終了の間に、今回申請する休暇開始、休暇終了のどちらかが存在していればエラー
            if((vacationRequest.getBeginVacation().isBefore(beginVacation) && vacationRequest.getEndVacation().isAfter(beginVacation)) || (vacationRequest.getBeginVacation().isBefore(endVacation) && vacationRequest.getEndVacation().isAfter(endVacation)))
            {
                return 3;
            }
        }
        // 残業の前後(始業前の残業直後、終業後の残業直前)でないこと
        List<ShiftListOverTime> shiftListOverTimes = shiftListOverTimeService.findByShiftId(shift);
        for(ShiftListOverTime shiftListOverTime : shiftListOverTimes)
        {
            LocalDateTime beginOverTime = shiftListOverTime.getOverTimeId().getBeginWork();
            LocalDateTime endOverTime = shiftListOverTime.getOverTimeId().getEndWork();
            // 残業の終了の後に休暇の開始(同じ時刻ならfalse)、残業の開始の前に休暇の終了(同じ時刻ならfalse)
            if(beginVacation.isAfter(endOverTime) || endVacation.isBefore(beginOverTime))
            {
                // 正常動作
            }
            else
            {
                // 残業終了の前に休暇の開始、または休暇の終了の後に終業後の残業開始が存在していればエラー
                return 3;     
            }
        }

        // 残業申請の承認待ちでも行う
        List<OverTimeRequest> overTimeRequests = overTimeRequestService.findByAccountIdAndShiftIdAndRequestStatusWait(account, shift);
        for(OverTimeRequest overTimeRequest : overTimeRequests)
        {
            LocalDateTime requestBeginOverTime = overTimeRequest.getBeginWork();
            LocalDateTime requestEndOverTime = overTimeRequest.getEndWork();
            // 残業の終了の後に休暇の開始(同じ時刻ならfalse)、残業の開始の前に休暇の終了(同じ時刻ならfalse)
            if(beginVacation.isAfter(requestEndOverTime) || endVacation.isBefore(requestBeginOverTime))
            {
                // 正常動作
            }
            else
            {
                // 残業終了の前に休暇の開始、または休暇の終了の後に終業後の残業開始が存在していればエラー
                return 3;            
            }
        }
        // 有給の場合承認待ちの有給申請の時間も含め足りるかを計算
        switch (vacationInput.getVacationType().intValue())
        {
            // 有給の場合
            case 1:
                // 使用可能な有給を取得
                List<PaydHoliday> paydHolidays = paydHolidayService.findByAccountIdAndLimitAfter(account);
                Duration availableHoliday = Duration.ZERO;
                for(PaydHoliday paydHoliday : paydHolidays)
                {
                    availableHoliday = availableHoliday.plus(stringToDuration.stringToDuration(paydHoliday.getTime()));
                }
                // 申請によって予約済みの有給を取得
                List<VacationRequest> vacationRequestPaydHolidays = findByAccountIdAndRequestStatusWaitAndVacationTypePaydHoiday(account);
                Duration usedHoliday = Duration.ZERO;
                for(VacationRequest vacationRequest : vacationRequestPaydHolidays)
                {
                    usedHoliday = usedHoliday.plus(Duration.between(vacationRequest.getBeginVacation(), vacationRequest.getEndVacation()));
                }
                // 使用可能な有給から予約済みの有給を減算
                availableHoliday = availableHoliday.minus(usedHoliday);
                // 申請された有給と使用可能な有給を比較し等しいか、大きければtrue
                Duration requestHoliday = Duration.between(beginVacation, endVacation);
                if(availableHoliday.compareTo(requestHoliday) >= 0)
                {
                    // 正常
                }
                else
                {
                    return 3;
                }
                break;
            
            // 代休の場合
            case 2:
                break;

            // 欠勤の場合
            case 3:
                break;

            // 忌引きの場合
            case 4:
                break;
            
            // 特別休暇の場合
            case 5:
                break;
            
            // 子供休暇
            case 6:
                break;
            // 介護休暇
            case 7:
                break;
            // 保存休暇
            case 8:
                break;

            default:
                // どれにも当てはまらなければエラー
                return 3;
        }
        VacationRequest vacationRequest = new VacationRequest();
        vacationRequest.setAccountId(account);
        vacationRequest.setVacationTypeId(vacationTypeService.findById(Long.valueOf(vacationInput.getVacationType())));
        vacationRequest.setBeginVacation(beginVacation);
        vacationRequest.setEndVacation(endVacation);
        vacationRequest.setRequestDate(stringToLocalDateTime.stringToLocalDateTime(vacationInput.getRequestDate()));
        vacationRequest.setRequestComment(vacationInput.getRequestComment());
        vacationRequest.setRequestStatus(1);
        vacationRequest.setApprover(null);
        vacationRequest.setApprovalTime(null);
        vacationRequest.setApproverComment(null);
        vacationRequest.setShiftId(shift);
        VacationRequest resultVacationRequest = save(vacationRequest);
        if(Objects.isNull(resultVacationRequest) || Objects.isNull(resultVacationRequest.getVacationId()))
        {
            return 3;
        }
        return 1;
    }

    public VacationRequest findById(Long id)
    {
        return vacationRequestRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("休暇申請が見つかりません"));
    }

    public VacationRequest findByAccountIdAndVacationId(Account account, Long id)
    {
        return vacationRequestRepository.findByAccountIdAndVacationId(account, id)
            .orElseThrow(() -> new RuntimeException("休暇申請が見つかりません"));
    }

    public VacationRequest findByAccountIdAndVacationId(Long accountId, Long id)
    {
        Account account = new Account();
        account.setId(accountId);
        return vacationRequestRepository.findByAccountIdAndVacationId(account, id)
            .orElseThrow(() -> new RuntimeException("休暇申請が見つかりません"));
    }

    public List<VacationRequest> findByAccountIdAndShiftId(Long accountId, Long shiftId)
    {
        Account account = new Account();
        account.setId(accountId);
        Shift shift = new Shift();
        shift.setShiftId(shiftId);
        List<VacationRequest> vacationRequests = vacationRequestRepository.findByAccountIdAndShiftId(account, shift);
        return vacationRequests;
    }

    public List<VacationRequest> findByAccountIdAndShiftId(Account account, Long shiftId)
    {
        Shift shift = new Shift();
        shift.setShiftId(shiftId);
        List<VacationRequest> vacationRequests = vacationRequestRepository.findByAccountIdAndShiftId(account, shift);
        return vacationRequests;
    }

    public List<VacationRequest> findByAccountIdAndShiftId(Long accountId, Shift shift)
    {
        Account account = new Account();
        account.setId(accountId);
        List<VacationRequest> vacationRequests = vacationRequestRepository.findByAccountIdAndShiftId(account, shift);
        return vacationRequests;
    }

    public List<VacationRequest> findByAccountIdAndShiftId(Account account, Shift shift)
    {
        List<VacationRequest> vacationRequests = vacationRequestRepository.findByAccountIdAndShiftId(account, shift);
        return vacationRequests;
    }

    public List<VacationRequest> findByAccountIdAndRequestStatusWaitAndVacationTypePaydHoliday(Long accountId)
    {
        Account account = new Account();
        account.setId(accountId);
        // 承認待ちのidで検索
        VacationType vacationType = new VacationType();
        // 有給のidをセット
        vacationType.setVacationTypeId(1L);
        List<VacationRequest> vacationRequests = vacationRequestRepository.findByAccountIdAndRequestStatusAndVacationTypeId(account, 1, vacationType);
        return vacationRequests;
    }

    public List<VacationRequest> findByAccountIdAndRequestStatusWaitAndVacationTypePaydHoiday(Account account)
    {
        // 有給のidをセット
        VacationType vacationType = new VacationType();
        vacationType.setVacationTypeId(1L);
        // 承認待ちのidで検索
        List<VacationRequest> vacationRequests = vacationRequestRepository.findByAccountIdAndRequestStatusAndVacationTypeId(account, 1, vacationType);
        return vacationRequests;
    }

    public List<VacationRequest> findByAccountIdAndVacationTypeIdPaydHolidayAndRequestStatusApprovalAndBeginVacationBetween(Long accountId, int year, int month)
    {
        Account account = new Account();
        account.setId(accountId);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        VacationType vacationType = new VacationType();
        // 有給のid
        vacationType.setVacationTypeId(1L);
        // 許可ステータス
        int requestStatus = 2;
        List<VacationRequest> vacationRequests = vacationRequestRepository.findByAccountIdAndVacationTypeIdAndRequestStatusAndBeginVacationBetween(account, vacationType, requestStatus, startPeriod, endPeriod);
        return vacationRequests;
    }

    public List<VacationRequest> findByAccountIdAndVacationTypeIdPaydHolidayAndRequestStatusApprovalAndBeginVacationBetween(Account account, int year, int month)
    {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        VacationType vacationType = new VacationType();
        // 有給のid
        vacationType.setVacationTypeId(1L);
        // 許可ステータス
        int requestStatus = 2;
        List<VacationRequest> vacationRequests = vacationRequestRepository.findByAccountIdAndVacationTypeIdAndRequestStatusAndBeginVacationBetween(account, vacationType, requestStatus, startPeriod, endPeriod);
        return vacationRequests;
    }
    
    public List<VacationRequest> findByAccountId(Long accountId)
    {
        Account account = new Account();
        account.setId(accountId);
        return vacationRequestRepository.findByAccountId(account);
    }

    public List<VacationRequest> findByAccountId(Account account)
    {
        return vacationRequestRepository.findByAccountId(account);
    }

    public List<VacationRequest> findByAccountIdIn(List<Account> accounts)
    {
        List<VacationRequest> vacationRequests = vacationRequestRepository.findByAccountIdIn(accounts);
        return vacationRequests;
    }

    public List<VacationRequest> findByAccountIdAndBeginVacationBetweenAndRequestStatusWait(Long accountId, int year, int month)
    {
        Account account = new Account();
        account.setId(accountId);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        int wait = 1;
        List<VacationRequest> vacationRequests = vacationRequestRepository.findByAccountIdAndRequestStatusAndBeginVacationBetween(account, wait, startPeriod, endPeriod);
        return vacationRequests;
    }

    public List<VacationRequest> findByAccountIdAndBeginVacationBetweenAndRequestStatusWait(Account account, int year, int month)
    {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        int wait = 1;
        List<VacationRequest> vacationRequests = vacationRequestRepository.findByAccountIdAndRequestStatusAndBeginVacationBetween(account, wait, startPeriod, endPeriod);
        return vacationRequests;
    }

    public VacationRequest save(VacationRequest vacationRequest)
    {
        return vacationRequestRepository.save(vacationRequest);
    }
}