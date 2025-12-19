package com.example.springboot.service;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.springboot.dto.change.DurationToString;
import com.example.springboot.dto.change.LocalDateTimeToString;
import com.example.springboot.dto.change.StringToDuration;
import com.example.springboot.dto.change.StringToLocalDateTime;
import com.example.springboot.dto.input.MonthlyInput;
import com.example.springboot.dto.input.OtherTimeInput;
import com.example.springboot.dto.input.OverTimeInput;
import com.example.springboot.dto.input.RequestJudgmentInput;
import com.example.springboot.dto.input.ShiftChangeInput;
import com.example.springboot.dto.input.ShiftInput;
import com.example.springboot.dto.input.StampInput;
import com.example.springboot.dto.input.VacationInput;
import com.example.springboot.dto.input.WithDrowInput;
import com.example.springboot.dto.response.RequestDetailMonthlyResponse;
import com.example.springboot.dto.response.RequestDetailOtherTimeResponse;
import com.example.springboot.dto.response.RequestDetailOverTimeResponse;
import com.example.springboot.dto.response.RequestDetailShiftChangeResponse;
import com.example.springboot.dto.response.RequestDetailShiftResponse;
import com.example.springboot.dto.response.RequestDetailStampResponse;
import com.example.springboot.dto.response.RequestDetailVacationResponse;
import com.example.springboot.dto.response.RequestListResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.AccountApprover;
import com.example.springboot.model.Attend;
import com.example.springboot.model.AttendanceExceptionRequest;
import com.example.springboot.model.AttendanceListSource;
import com.example.springboot.model.LegalTime;
import com.example.springboot.model.MonthlyRequest;
import com.example.springboot.model.NewsList;
import com.example.springboot.model.OverTimeRequest;
import com.example.springboot.model.PaydHoliday;
import com.example.springboot.model.PaydHolidayUse;
import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftChangeRequest;
import com.example.springboot.model.ShiftListOtherTime;
import com.example.springboot.model.ShiftListOverTime;
import com.example.springboot.model.ShiftListShiftRequest;
import com.example.springboot.model.ShiftListVacation;
import com.example.springboot.model.ShiftRequest;
import com.example.springboot.model.StampRequest;
import com.example.springboot.model.Vacation;
import com.example.springboot.model.VacationRequest;

@Service
public class RequestService
{
    private final ShiftRequestService shiftRequestService;
    private final ShiftChangeRequestService shiftChangeRequestService;
    private final StampRequestService stampRequestService;
    private final VacationRequestService vacationRequestService;
    private final AttendanceExceptionRequestService attendanceExceptionRequestService;
    private final OverTimeRequestService overTimeRequestService;
    private final MonthlyRequestService monthlyRequestService;
    private final NewsListService newsListService;
    private final ShiftListOtherTimeService shiftListOtherTimeService;
    private final ShiftListVacationService shiftListVacationService;
    private final ShiftListOverTimeService shiftListOverTimeService;
    private final ShiftListShiftRequestService shiftListShiftRequestService;
    private final ShiftService shiftService;
    private final AttendService attendService;
    private final AttendanceListSourceService attendanceListSourceService;
    private final VacationService vacationService;
    private final DurationToString durationToString;
    private final StringToLocalDateTime stringToLocalDateTime;
    private final PaydHolidayService paydHolidayService;
    private final VacationTypeService vacationTypeService;
    private final StringToDuration stringToDuration;
    private final AttendanceExceptionTypeService attendanceExceptionTypeService;
    private final LegalTimeService legalTimeService;
    private final AccountApproverService accountApproverService;
    private final PaydHolidayUseService paydHolidayUseService;
    private final LocalDateTimeToString localDateTimeToString;

    public RequestService
    (
        ShiftRequestService shiftRequestService,
        ShiftChangeRequestService shiftChangeRequestService,
        StampRequestService stampRequestService,
        VacationRequestService vacationRequestService,
        AttendanceExceptionRequestService attendanceExceptionRequestService,
        OverTimeRequestService overTimeRequestService,
        MonthlyRequestService monthlyRequestService,
        NewsListService newsListService,
        ShiftListOtherTimeService shiftListOtherTimeService,
        ShiftListVacationService shiftListVacationService,
        ShiftListOverTimeService shiftListOverTimeService,
        ShiftListShiftRequestService shiftListShiftRequestService,
        ShiftService shiftService,
        AttendService attendService,
        AttendanceListSourceService attendanceListSourceService,
        VacationService vacationService,
        DurationToString durationToString,
        StringToLocalDateTime stringToLocalDateTime,
        PaydHolidayService paydHolidayService,
        VacationTypeService vacationTypeService,
        StringToDuration stringToDuration,
        AttendanceExceptionTypeService attendanceExceptionTypeService,
        LegalTimeService legalTimeService,
        AccountApproverService accountApproverService,
        PaydHolidayUseService paydHolidayUseService,
        LocalDateTimeToString localDateTimeToString
    )
    {
        this.shiftRequestService = shiftRequestService;
        this.shiftChangeRequestService = shiftChangeRequestService;
        this.stampRequestService = stampRequestService;
        this.vacationRequestService = vacationRequestService;
        this.attendanceExceptionRequestService = attendanceExceptionRequestService;
        this.overTimeRequestService = overTimeRequestService;
        this.monthlyRequestService = monthlyRequestService;
        this.newsListService = newsListService;
        this.shiftListOtherTimeService = shiftListOtherTimeService;
        this.shiftListVacationService = shiftListVacationService;
        this.shiftListOverTimeService = shiftListOverTimeService;
        this.shiftListShiftRequestService = shiftListShiftRequestService;
        this.shiftService = shiftService;
        this.attendService = attendService;
        this.attendanceListSourceService = attendanceListSourceService;
        this.vacationService = vacationService;
        this.durationToString = durationToString;
        this.stringToLocalDateTime = stringToLocalDateTime;
        this.paydHolidayService = paydHolidayService;
        this.vacationTypeService = vacationTypeService;
        this.stringToDuration = stringToDuration;
        this.attendanceExceptionTypeService = attendanceExceptionTypeService;
        this.legalTimeService = legalTimeService;
        this.accountApproverService = accountApproverService;
        this.paydHolidayUseService = paydHolidayUseService;
        this.localDateTimeToString = localDateTimeToString;
    }

    @Transactional
    public int createShiftRequest(Account account, ShiftInput shiftInput)
    {
        // 始業時間、終業時間、休憩開始時間、休憩終了時間が現在取得時間より後になっていることを確認
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime beginWork = stringToLocalDateTime.stringToLocalDateTime(shiftInput.getBeginWork());
        LocalDateTime endWork = stringToLocalDateTime.stringToLocalDateTime(shiftInput.getEndWork());
        LocalDateTime beginBreak = stringToLocalDateTime.stringToLocalDateTime(shiftInput.getBeginBreak());
        LocalDateTime endBreak = stringToLocalDateTime.stringToLocalDateTime(shiftInput.getEndBreak());

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
        // 承認されたシフトで同じ日に重複していないことを確認
        List<Shift> shifts = shiftService.findByAccountIdAndDayBeginWorkBetween(account, beginWork);
        // シフト申請で同じ日に申請が出ていないことを確認
        List<ShiftRequest> shiftRequests = shiftRequestService.findAccountIdAndBeginWorkBetweenDay(account, beginWork);
        // 0より大きかったら申請できない
        if(shiftRequests.size() > 0 || shifts.size() > 0)
        {
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
            // 1年後に収まっていない始業時間ならエラー
            return 3;
        }
        // シフト申請に登録(サービス層で行うべきこと？)
        ShiftRequest shiftRequest = new ShiftRequest();
        shiftRequest.setAccountId(account);
        shiftRequest.setBeginWork(beginWork);
        shiftRequest.setBeginBreak(beginBreak);
        shiftRequest.setEndBreak(endBreak);
        shiftRequest.setEndWork(endWork);
        shiftRequest.setVacationWork(false);
        shiftRequest.setRequestComment(shiftInput.getRequestComment());
        shiftRequest.setRequestDate(stringToLocalDateTime.stringToLocalDateTime(shiftInput.getRequestDate()));
        shiftRequest.setRequestStatus(1);
        shiftRequest.setApprover(null);
        shiftRequest.setApprovalTime(null);
        shiftRequest.setApproverComment(null);
        ShiftRequest resultShiftRequest = shiftRequestService.save(shiftRequest);
        if(Objects.isNull(resultShiftRequest) || Objects.isNull(resultShiftRequest.getShiftRequestId()))
        {
            return 3;
        }
        return 1;
    }

    @Transactional
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
        List<ShiftChangeRequest> shiftChangeRequestWaits = shiftChangeRequestService.findByAccountIdAndShiftIdAndRequestStatusWait(account, shift.getShiftId());
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
        shiftChangeRequest.setVacationWork(false);
        shiftChangeRequest.setRequestComment(shiftChangeInput.getRequestComment());
        shiftChangeRequest.setRequestDate(stringToLocalDateTime.stringToLocalDateTime(shiftChangeInput.getRequestDate()));
        shiftChangeRequest.setRequestStatus(1);
        shiftChangeRequest.setApprover(null);
        shiftChangeRequest.setApprovalTime(null);
        shiftChangeRequest.setApproverComment(null);
        shiftChangeRequest.setShiftId(shift);
        ShiftChangeRequest resultShiftChangeRequest = shiftChangeRequestService.save(shiftChangeRequest);
        if(Objects.isNull(resultShiftChangeRequest) || Objects.isNull(resultShiftChangeRequest.getShiftChangeId()))
        {
            return 3;
        }
        return 1;
    }

    @Transactional
    public int createStampRequest(Account account, StampInput stampInput)
    {
        // アカウントの情報とシフトの情報を基に検索
        Shift shift = shiftService.findByAccountIdAndShiftId(account, stampInput.getShiftId());
        if(Objects.isNull(shift))
        {
            return 4;
        }
        // 打刻漏れ申請で同じシフトで既に承認済みのものがないか確認
        List<StampRequest> stampRequests = stampRequestService.findByShiftIdAndRequestStatusWait(shift);
        if(stampRequests.size() > 0)
        {
            return 3;
        }
        LocalDateTime nowTime = LocalDateTime.now();
        // シフトの終業時刻が現在時刻より前であること
        if(shift.getEndWork().isBefore(nowTime))
        {
            // 条件通りなら何もしない
        }
        else
        {
            return 3;
        }

        // 申請するシフトの年月で月次申請が申請されていればエラー
        int searchYear = shift.getBeginWork().getYear();
        int searchMonth = shift.getBeginWork().getMonthValue();
        List<MonthlyRequest> monthlyRequests = monthlyRequestService.findByAccountIdAndYearAndMonth(account, searchYear, searchMonth);
        if(monthlyRequests.size() > 0)
        {
            return 3;
        }

        LocalDateTime beginWork = stringToLocalDateTime.stringToLocalDateTime(stampInput.getBeginWork());
        LocalDateTime endWork = stringToLocalDateTime.stringToLocalDateTime(stampInput.getEndWork());
        LocalDateTime beginBreak = stringToLocalDateTime.stringToLocalDateTime(stampInput.getBeginBreak());
        LocalDateTime endBreak = stringToLocalDateTime.stringToLocalDateTime(stampInput.getEndBreak());
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

        // 始業と終業、休憩の開始と終了がシフトの時間と一致していること
        if(shift.getBeginWork().compareTo(beginWork) == 0 && shift.getEndWork().compareTo(endWork) == 0 && shift.getBeginBreak().compareTo(beginBreak) == 0 && shift.getEndBreak().compareTo(endBreak) == 0)
        {
            // 一致していたら何もしない
        }
        else
        {
            return 3;
        }
        int requestStatus = 1;
        StampRequest stampRequest = new StampRequest();
        stampRequest.setAccountId(account);
        stampRequest.setBeginWork(beginWork);
        stampRequest.setEndWork(endWork);
        stampRequest.setBeginBreak(beginBreak);
        stampRequest.setEndBreak(endBreak);
        stampRequest.setRequestComment(stampInput.getRequestComment());
        stampRequest.setRequestDate(Objects.isNull(stampInput.getRequestDate()) ? null : stringToLocalDateTime.stringToLocalDateTime(stampInput.getRequestDate()));
        stampRequest.setRequestStatus(requestStatus);
        stampRequest.setApprover(null);
        stampRequest.setApprovalTime(null);
        stampRequest.setApproverComment(null);
        stampRequest.setShiftId(shift);
        StampRequest resultStampRequest = stampRequestService.save(stampRequest);
        if(Objects.isNull(resultStampRequest) || Objects.isNull(resultStampRequest.getStampId()))
        {
            return 3;
        }
        return 1;
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
        List<VacationRequest> vacationRequests = vacationRequestService.findByAccountIdAndShiftId(account, shift);
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
                List<VacationRequest> vacationRequestPaydHolidays = vacationRequestService.findByAccountIdAndRequestStatusWaitAndVacationTypePaydHoiday(account);
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
        VacationRequest resultVacationRequest = vacationRequestService.save(vacationRequest);
        if(Objects.isNull(resultVacationRequest) || Objects.isNull(resultVacationRequest.getVacationId()))
        {
            return 3;
        }
        return 1;
    }

    @Transactional
    public int createAttendanceExceptionRequest(Account account, OtherTimeInput otherTimeInput)
    {
        Shift shift = shiftService.findByAccountIdAndShiftId(account, otherTimeInput.getShiftId());
        if(Objects.isNull(shift))
        {
            return 3;
        }
        // 開始の後に終了が存在すること
        LocalDateTime startTime = stringToLocalDateTime.stringToLocalDateTime(otherTimeInput.getBeginOtherTime());
        LocalDateTime endTime = stringToLocalDateTime.stringToLocalDateTime(otherTimeInput.getEndOtherTime());

        if(endTime.isAfter(startTime))
        {
            // 条件通りなら何もしない
        }
        else
        {
            return 3;
        }
        // 始業時間が現在時刻より後であること
        LocalDateTime nowTime = LocalDateTime.now();
        if(shift.getBeginWork().isAfter(nowTime))
        {
            // 条件通りなら何もしない
        }
        else
        {
            // 現在時刻より後ならエラー
            return 3;
        }
        // shiftidを基に反映されている残業申請の一覧を取得
        List<ShiftListOverTime> shiftListOverTimes = shiftListOverTimeService.findByShiftId(shift);
        // shiftidを基にシフトに関する申請の取得
        ShiftListShiftRequest shiftListShiftRequest = shiftListShiftRequestService.findByShiftId(shift);
        switch (otherTimeInput.getOtherType().intValue())
        {
            // 外出申請の場合
            // シフト内か確認
            // 重複NG(承認待ち、承認済み問わず)
            // すでに申請されている申請をシフトidとステータス(申請済み、承認済み)で検索
            // shiftIdと申請したい時間帯で検索時間帯の中にその他の外出申請の開始または、終了があればエラー
            case 1:
                // 重なる時間を許容したいのでequalで等しい場合にもtrueになるように
                if (((startTime.isAfter(shift.getBeginWork()) || startTime.isEqual(shift.getBeginWork())) && (endTime.isBefore(shift.getBeginBreak()) || endTime.isEqual(shift.getBeginBreak()))) ||
                ((startTime.isAfter(shift.getEndBreak()) || startTime.isEqual(shift.getEndBreak())) && (endTime.isBefore(shift.getEndWork()) || endTime.isEqual(shift.getEndWork()))))
                {
                    // 条件通りなら何もしない
                }
                else
                {
                    return 3;
                }
                // 重複する外出申請の取得
                List<AttendanceExceptionRequest> attendanceExceptionRequests = attendanceExceptionRequestService.findByAccountIdAndShiftIdAndOutingAndBeginTimeBetweenOrEndTimeBetweenAndRequestStatusWaitOrRequestStatusApproved(account, shift, startTime, endTime);
                if(attendanceExceptionRequests.size() >0)
                {
                    // 0より大きければ重複する内容があったことになるのでエラー
                    return 3;
                }
                break;
            // 遅刻申請の場合
            // 始業時間は元となるシフト申請の始業時間
            // 始業時間と一致すること
            // 始業前の残業が存在する場合申請NG
            // 休暇と被る場合も申請NG
            case 2:
                // 始業前の残業が存在しないか確認
                for(ShiftListOverTime shiftListOverTime : shiftListOverTimes)
                {
                    // 遅刻の開始の前に残業の終了が存在すればエラー
                    if(shiftListOverTime.getOverTimeId().getEndWork().isBefore(startTime))
                    {
                        return 3;
                    }
                }
                LocalDateTime beginTime;
                // シフト申請、時間変更申請どちらもなければエラー
                if(Objects.isNull(shiftListShiftRequest.getShiftChangeRequestId()) && Objects.isNull(shiftListShiftRequest.getShiftRequestId()))
                {
                    return 3;
                }
                else if(Objects.isNull(shiftListShiftRequest.getShiftRequestId()))
                {
                    // シフト申請がなければエラー
                    return 3;
                }
                else if(Objects.isNull(shiftListShiftRequest.getShiftChangeRequestId()))
                {
                    // シフト時間変更申請請がなければシフト申請で
                    ShiftRequest shiftRequest = shiftListShiftRequest.getShiftRequestId();
                    beginTime = shiftRequest.getBeginWork();
                }
                else
                {
                    // シフト時間変更申請を利用
                    ShiftChangeRequest shiftChangeRequest = shiftListShiftRequest.getShiftChangeRequestId();
                    beginTime = shiftChangeRequest.getBeginWork();
                }
                // 元となる申請の取得をしたら条件と合致するかシフトの就業まで(終業時間調度は許容)に終了するか
                if(startTime.isEqual(beginTime) && (startTime.isBefore(shift.getEndWork()) || startTime.isEqual(shift.getEndWork())))
                {
                    // 一致すれば何もしない
                }
                else
                {
                    return 3;
                }
                break;
            // 早退申請の場合
            // 終業時間は元となるシフト申請の終業時間
            // 終業時間と一致すること
            // 始業より後に早退が存在すること
            // 終業後の残業が存在する場合申請NG
            // 休暇と被る場合も申請NG
            case 3:
                // 始業前の残業が存在しないか確認
                for(ShiftListOverTime shiftListOverTime : shiftListOverTimes)
                {
                    // 早退の終了後に残業が存在すればエラー
                    if(shiftListOverTime.getOverTimeId().getBeginWork().isAfter(endTime))
                    {
                        return 3;
                    }
                }

                LocalDateTime closeTime;
                // シフト時間変更申請、シフト申請どちらもなければエラー
                if(Objects.isNull(shiftListShiftRequest.getShiftChangeRequestId()) && Objects.isNull(shiftListShiftRequest.getShiftRequestId()))
                {
                    return 3;
                }
                else if(Objects.isNull(shiftListShiftRequest.getShiftRequestId()))
                {
                    // シフト申請がなければエラー
                    return 3;
                }
                else if(Objects.isNull(shiftListShiftRequest.getShiftChangeRequestId()))
                {
                    // シフト時間変更申請がなければシフト申請を
                    ShiftRequest shiftRequest = shiftListShiftRequest.getShiftRequestId();
                    closeTime = shiftRequest.getEndWork();
                }
                else
                {
                    // シフト時間変更申請がある
                    ShiftChangeRequest shiftChangeRequest = shiftListShiftRequest.getShiftChangeRequestId();
                    closeTime = shiftChangeRequest.getEndWork();
                }
                // 元となる申請の取得をしたら条件と合致するかシフトの開始(シフト開始直後は許容)の後始まるか
                if(endTime.isEqual(closeTime) && (startTime.isAfter(shift.getBeginWork()) || startTime.isEqual(shift.getBeginWork())))
                {
                    // 一致すれば何もしない
                }
                else
                {
                    return 3;
                }
                break;
            default:
                // どれにも当てはまらなければエラー
                return 3;
        }
        AttendanceExceptionRequest attendanceExceptionRequest = new AttendanceExceptionRequest();
        attendanceExceptionRequest.setAccountId(account);
        attendanceExceptionRequest.setBeginTime(startTime);
        attendanceExceptionRequest.setEndTime(endTime);
        attendanceExceptionRequest.setAttendanceExceptionTypeId(attendanceExceptionTypeService.findByAttendanceExceptionTypeId(otherTimeInput.getOtherType()));
        attendanceExceptionRequest.setRequestComment(otherTimeInput.getRequestComment());
        attendanceExceptionRequest.setRequestDate(otherTimeInput.getRequestDate() == null ? null : stringToLocalDateTime.stringToLocalDateTime(otherTimeInput.getRequestDate()));
        attendanceExceptionRequest.setRequestStatus(1);
        attendanceExceptionRequest.setShiftId(shift);
        AttendanceExceptionRequest resultAttendanceExceptionRequest = attendanceExceptionRequestService.save(attendanceExceptionRequest);
        if(Objects.isNull(resultAttendanceExceptionRequest) || Objects.isNull(resultAttendanceExceptionRequest.getAttendanceExceptionId()))
        {
            return 3;
        }
        return 1;
    }

    @Transactional
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
        // 始業時間が現在時刻より後であること
        LocalDateTime nowTime = LocalDateTime.now();
        if(shift.getBeginWork().isAfter(nowTime))
        {
            // 想定通りなら何もしない
        }
        else
        {
            return 3;
        }
        // 既に存在する残業と重複する場合申請NG
        List<OverTimeRequest> overTimeRequests = overTimeRequestService.findByAccounIdAndRequestStatusWaitOrApprovedAndBeginWorkOrEndWorkBetween(account, startTimeOverWork, endTimeOverWork);
        if(overTimeRequests.size() > 0)
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
        List<OverTimeRequest> overTimeRequestMonthList = overTimeRequestService.findByAccountIdAndRequestStatusWaitOrApprovedAndBeginWorkBetweenMonth(account, startTimeOverWork.getYear(), startTimeOverWork.getMonthValue());
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
        OverTimeRequest overTimeRequest = new OverTimeRequest();
        overTimeRequest.setAccountId(account);
        overTimeRequest.setBeginWork(startTimeOverWork);
        overTimeRequest.setEndWork(endTimeOverWork);
        overTimeRequest.setRequestComment(overTimeInput.getRequestComment());
        overTimeRequest.setRequestDate(overTimeInput.getRequestDate() == null ? null : stringToLocalDateTime.stringToLocalDateTime(overTimeInput.getRequestDate()));
        overTimeRequest.setRequestStatus(1);
        overTimeRequest.setShiftId(shift);
        OverTimeRequest requestOverTimeRequest = overTimeRequestService.save(overTimeRequest);
        if(Objects.isNull(requestOverTimeRequest) || Objects.isNull(requestOverTimeRequest.getOverTimeId()))
        {
            return 3;
        }
        return 1;
    }

    @Transactional
    public int createMonthlyRequest(Account account, MonthlyInput monthlyInput)
    {
        // お知らせから月次申請を行う年月の情報が不足している場合申請不可
        List<NewsList> newsLists = newsListService.findByAccountIdAndDateBetweenMonthly(account, monthlyInput.getYear(), monthlyInput.getMonth());
        if(newsLists.size() > 0)
        {
            return 3;
        }
        // 各申請に承認待ちステータスがない確認
        List<ShiftRequest> shiftRequests = shiftRequestService.findByAccountIdAndBeginWorkBetweenAndRequestStatusWait(account, monthlyInput.getYear(), monthlyInput.getMonth());
        List<ShiftChangeRequest> shiftChangeRequests = shiftChangeRequestService.findByAccountIdAndBeginWorkBetweenAndRequestStatusWait(account, monthlyInput.getYear(), monthlyInput.getMonth());
        List<StampRequest> stampRequests = stampRequestService.findByAccountIdAndBeginWorkBetweenAndRequestStatusWait(account, monthlyInput.getYear(), monthlyInput.getMonth());
        List<AttendanceExceptionRequest> attendanceExceptionRequests = attendanceExceptionRequestService.findByAccountIdAndBeginTimeBetweenAndRequestStatusWait(account, monthlyInput.getYear(), monthlyInput.getMonth());
        List<OverTimeRequest> overTimeRequests = overTimeRequestService.findByAccountIdAndBeginWorkAndRequstStatusWait(account, monthlyInput.getYear(), monthlyInput.getMonth());
        List<VacationRequest> vacationRequests = vacationRequestService.findByAccountIdAndBeginVacationBetweenAndRequestStatusWait(account, monthlyInput.getYear(), monthlyInput.getMonth());
        if(shiftRequests.size() > 0 || shiftChangeRequests.size() > 0 || stampRequests.size() > 0 || attendanceExceptionRequests.size() > 0 || overTimeRequests.size() > 0 || vacationRequests.size() > 0)
        {
            return 3;
        }
        // その月のシフトを取得
        List<Shift> shifts = shiftService.findByAccountIdAndBeginWorkBetween(account, monthlyInput.getYear(), monthlyInput.getMonth());
        // シフトから関連テーブルを検索
        List<ShiftListOtherTime> shiftListOtherTimes = shiftListOtherTimeService.findByShiftIdIn(shifts);
        List<ShiftListOverTime> shiftListOverTimes = shiftListOverTimeService.findByShiftIdIn(shifts);
        List<ShiftListShiftRequest> shiftListShiftRequests = shiftListShiftRequestService.findByShiftIdIn(shifts);
        List<ShiftListVacation> shiftListVacations = shiftListVacationService.findByShiftIdIn(shifts);
        // 最終的に取得した申請のステータスを承認から月次申請済みに変更
        int monthlyRequestStatus = 6;
        for(ShiftListOtherTime shiftListOtherTime : shiftListOtherTimes)
        {
            AttendanceExceptionRequest attendanceExceptionRequest = shiftListOtherTime.getAttendanceExceptionId();
            attendanceExceptionRequest.setRequestStatus(monthlyRequestStatus);
            attendanceExceptionRequestService.save(attendanceExceptionRequest);
        }
        for(ShiftListOverTime shiftListOverTime : shiftListOverTimes)
        {
            OverTimeRequest overTimeRequest = shiftListOverTime.getOverTimeId();
            overTimeRequest.setRequestStatus(monthlyRequestStatus);
            overTimeRequestService.save(overTimeRequest);
        }
        for(ShiftListShiftRequest shiftListShiftRequest : shiftListShiftRequests)
        {
            // シフト時間変更申請、シフト申請がなければエラー
            if(Objects.isNull(shiftListShiftRequest.getShiftChangeRequestId()) && Objects.isNull(shiftListShiftRequest.getShiftRequestId()))
            {
                return 3;
            }
            else if(Objects.isNull(shiftListShiftRequest.getShiftRequestId()))
            {
                // シフト申請がなければエラー
                return 3;
            }
            else if(Objects.isNull(shiftListShiftRequest.getShiftChangeRequestId()))
            {
                // シフト時間変更申請がなければシフト申請を利用
                ShiftRequest shiftRequest = shiftListShiftRequest.getShiftRequestId();
                shiftRequest.setRequestStatus(monthlyRequestStatus);
                shiftRequestService.save(shiftRequest);
            }
            else
            {
                // シフト時間変更申請を利用
                ShiftChangeRequest shiftChangeRequest = shiftListShiftRequest.getShiftChangeRequestId();
                shiftChangeRequest.setRequestStatus(monthlyRequestStatus);
                shiftChangeRequestService.save(shiftChangeRequest);
            }
        }
        for(ShiftListVacation shiftListVacation : shiftListVacations)
        {
            VacationRequest vacationRequest = shiftListVacation.getVacationId();
            vacationRequest.setRequestStatus(monthlyRequestStatus);
            vacationRequestService.save(vacationRequest);
        }

        // その月の勤怠情報を取得
        List<Attend> attends = attendService.findByAccountIdAndBeginWorkBetween(account, monthlyInput.getYear(), monthlyInput.getMonth());
        // 勤怠情報から関連テーブルを検索
        List<AttendanceListSource> attendanceListSources = attendanceListSourceService.findByAttendIdIn(attends);
        // 最終的に取得した申請のステータスを承認から月次申請済みに変更
        for(AttendanceListSource attendanceListSource : attendanceListSources)
        {
            StampRequest stampRequest = attendanceListSource.getStampRequestId();
            stampRequest.setRequestStatus(monthlyRequestStatus);
            stampRequestService.save(stampRequest);
        }
        
        // 申請したい月が現在より前(過去)であること
        if(YearMonth.of(monthlyInput.getYear(), monthlyInput.getMonth()).isBefore(YearMonth.from(LocalDateTime.now())))
        {
            // 過去なら正常
        }
        else
        {
            // そうでなければエラー
            return 3;
        }

        // 必要な情報を再度取得し月次申請に登録
        Duration monthWorkTime = Duration.ZERO;
        Duration monthLateness = Duration.ZERO;
        Duration monthLeaveEarly = Duration.ZERO;
        Duration monthOuting = Duration.ZERO;
        Duration monthOverWork = Duration.ZERO;
        Duration monthAbsenceTime = Duration.ZERO;
        Duration monthSpecialTime = Duration.ZERO;
        Duration monthLateNightWorkTime = Duration.ZERO;
        Duration monthHolidayWorkTime = Duration.ZERO;
        Duration monthPaydHolidayTime = Duration.ZERO;

        // 休暇の分け以外は取得できる
        for(Attend attend : attends)
        {
            monthWorkTime = monthWorkTime.plus(Duration.between(LocalTime.MIDNIGHT, attend.getWorkTime().toLocalTime()));
            monthLateness = monthLateness.plus(Duration.between(LocalTime.MIDNIGHT, attend.getLateness().toLocalTime()));
            monthLeaveEarly = monthLeaveEarly.plus(Duration.between(LocalTime.MIDNIGHT, attend.getLeaveEarly().toLocalTime()));
            monthOuting = monthOuting.plus(Duration.between(LocalTime.MIDNIGHT, attend.getOuting().toLocalTime()));
            monthOverWork = monthOverWork.plus(Duration.between(LocalTime.MIDNIGHT, attend.getOverWork().toLocalTime()));
            monthAbsenceTime = monthAbsenceTime.plus(Duration.between(LocalTime.MIDNIGHT, attend.getAbsenceTime().toLocalTime()));
            monthSpecialTime = monthSpecialTime.plus(Duration.between(LocalTime.MIDNIGHT, attend.getVacationTime().toLocalTime()));
            monthLateNightWorkTime = monthLateNightWorkTime.plus(Duration.between(LocalTime.MIDNIGHT, attend.getLateNightWork().toLocalTime()));
            monthHolidayWorkTime = monthHolidayWorkTime.plus(Duration.between(LocalTime.MIDNIGHT, attend.getHolidayWork().toLocalTime()));
        }
        // 休暇はvacationlistからその月の有給の時間を取得、休暇時間から減算
        List<Vacation> vacations = vacationService.findByAccountIdAndBeginVacationBetweenMonthAndPaydHoliday(account, monthlyInput.getYear(), monthlyInput.getMonth());
        for(Vacation vacation : vacations)
        {
            monthPaydHolidayTime = monthPaydHolidayTime.plus(Duration.between(vacation.getBeginVacation(), vacation.getEndVacation()));
            monthPaydHolidayTime = getVacation(vacation.getBeginVacation(), vacation.getEndVacation(), null, null);
        }
        monthSpecialTime.minus(monthPaydHolidayTime);
        // 月次申請登録(サービス層で行うべき?)
        MonthlyRequest monthlyRequest = new MonthlyRequest();
        monthlyRequest.setAccountId(account);
        monthlyRequest.setWorkTime(durationToString.durationToString(monthWorkTime));
        monthlyRequest.setOverTime(durationToString.durationToString(monthOverWork));
        monthlyRequest.setEarlyTime(durationToString.durationToString(monthLateness));
        monthlyRequest.setLeavingTime(durationToString.durationToString(monthLeaveEarly));
        monthlyRequest.setOutingTime(durationToString.durationToString(monthOuting));
        monthlyRequest.setAbsenceTime(durationToString.durationToString(monthAbsenceTime));
        monthlyRequest.setPaydHolidayTime(durationToString.durationToString(monthPaydHolidayTime));
        monthlyRequest.setSpecialTime(durationToString.durationToString(monthSpecialTime));
        monthlyRequest.setHolidayWorkTime(durationToString.durationToString(monthHolidayWorkTime));
        monthlyRequest.setLateNightWorkTime(durationToString.durationToString(monthLateNightWorkTime));
        monthlyRequest.setYear(monthlyInput.getYear());
        monthlyRequest.setMonth(monthlyInput.getMonth());
        monthlyRequest.setRequestComment(monthlyInput.getRequestComment());
        monthlyRequest.setRequestDate(stringToLocalDateTime.stringToLocalDateTime(monthlyInput.getRequestDate()));
        monthlyRequest.setRequestStatus(1);
        monthlyRequest.setApprover(null);
        monthlyRequest.setApprovalDate(null);
        monthlyRequest.setApproverComment(null);
        MonthlyRequest resultMonthlyRequest = monthlyRequestService.save(monthlyRequest);
        if(Objects.isNull(resultMonthlyRequest) || Objects.isNull(resultMonthlyRequest.getMonthRequestId()))
        {
            return 3;
        }
        return 1;
    }

    public RequestDetailShiftResponse getShiftDetail(Account account, Long requestId)
    {
        ShiftRequest shiftRequest = shiftRequestService.findByAccountIdAndShiftRequestId(account, requestId);
        return shiftRequestService.mapToDetailResponse(shiftRequest);
    }

    public RequestDetailShiftChangeResponse getShiftChangeDetail(Account account, Long requestId)
    {
        ShiftChangeRequest shiftChangeRequest = shiftChangeRequestService.findByAccountIdAndShiftChangeRequestId(account, requestId);
        return shiftChangeRequestService.mapToDetailResponse(shiftChangeRequest);
    }

    public RequestDetailStampResponse getStampDetail(Account account, Long requestId)
    {
        StampRequest stampRequest = stampRequestService.findByAccountIdAndStampId(account, requestId);
        return stampRequestService.mapToDetailResponse(stampRequest);
    }

    public RequestDetailVacationResponse getVacationDetail(Account account, Long requestId)
    {
        VacationRequest vacationRequest = vacationRequestService.findByAccountIdAndVacationId(account, requestId);
        return vacationRequestService.mapToDetailResponse(vacationRequest);
    }

    public RequestDetailOverTimeResponse getOverTimeDetail(Account account, Long requestId)
    {
        OverTimeRequest overTimeRequest = overTimeRequestService.findByAccountIdAndOverTimeRequestId(account, requestId);
        return overTimeRequestService.mapToDetailResponse(overTimeRequest);
    }

    public RequestDetailOtherTimeResponse getOtherTimeDetail(Account account, Long requestId)
    {
        AttendanceExceptionRequest attendanceExceptionRequest = attendanceExceptionRequestService.findByAccountIdAndAttendanceExceptionId(account, requestId);
        return attendanceExceptionRequestService.mapTorequestDetail(attendanceExceptionRequest);
    }

    public RequestDetailMonthlyResponse getMonthlyDetail(Account account, Long requestId)
    {
        MonthlyRequest monthlyRequest = monthlyRequestService.findByAccountIdAndMothlyRequestId(account, requestId);
        return monthlyRequestService.mapToDetailResponse(monthlyRequest);
    }

    public List<RequestListResponse> getRequestList(Account account)
    {
        List<RequestListResponse> requestListResponse = new ArrayList<RequestListResponse>();
        // そのアカウントの申請(シフト、シフト時間変更、打刻漏れ、勤怠例外、残業、休暇、月次)をそれぞれ取得し必要な情報だけを設定
        for(ShiftRequest shiftRequest : shiftRequestService.findByAccountId(account))
        {
            RequestListResponse requestListResponseShiftRequest = new RequestListResponse();
            requestListResponseShiftRequest.setId(shiftRequest.getShiftRequestId().intValue());
            requestListResponseShiftRequest.setRequestType(1);
            requestListResponseShiftRequest.setRequestDate(localDateTimeToString.localDateTimeToString(shiftRequest.getRequestDate()));
            requestListResponseShiftRequest.setRequestStatus(shiftRequest.getRequestStatus());
            requestListResponse.add(requestListResponseShiftRequest);
        }
        for(ShiftChangeRequest shiftChangeRequest : shiftChangeRequestService.findByAccountId(account))
        {
            RequestListResponse requestListResponseShiftChangeRequest = new RequestListResponse();
            requestListResponseShiftChangeRequest.setId(shiftChangeRequest.getShiftChangeId().intValue());
            requestListResponseShiftChangeRequest.setRequestType(2);
            requestListResponseShiftChangeRequest.setRequestDate(localDateTimeToString.localDateTimeToString(shiftChangeRequest.getRequestDate()));
            requestListResponseShiftChangeRequest.setRequestStatus(shiftChangeRequest.getRequestStatus());
            requestListResponse.add(requestListResponseShiftChangeRequest);
        }
        for(StampRequest stampRequest : stampRequestService.findByAccountId(account))
        {
            RequestListResponse requestListResponseStampRequest = new RequestListResponse();
            requestListResponseStampRequest.setId(stampRequest.getStampId().intValue());
            requestListResponseStampRequest.setRequestType(2);
            requestListResponseStampRequest.setRequestDate(localDateTimeToString.localDateTimeToString(stampRequest.getRequestDate()));
            requestListResponseStampRequest.setRequestStatus(stampRequest.getRequestStatus());
            requestListResponse.add(requestListResponseStampRequest);
        }
        for(AttendanceExceptionRequest attendanceExceptionRequest : attendanceExceptionRequestService.findByAccountId(account))
        {
            RequestListResponse requestListResponseAttendanceExceptionRequest = new RequestListResponse();
            requestListResponseAttendanceExceptionRequest.setId(attendanceExceptionRequest.getAttendanceExceptionId().intValue());
            requestListResponseAttendanceExceptionRequest.setRequestType(2);
            requestListResponseAttendanceExceptionRequest.setRequestDate(localDateTimeToString.localDateTimeToString(attendanceExceptionRequest.getRequestDate()));
            requestListResponseAttendanceExceptionRequest.setRequestStatus(attendanceExceptionRequest.getRequestStatus());
            requestListResponse.add(requestListResponseAttendanceExceptionRequest);
        }
        for(OverTimeRequest overTimeRequest : overTimeRequestService.findByAccountId(account))
        {
            RequestListResponse requestListResponseOverTimeRequest = new RequestListResponse();
            requestListResponseOverTimeRequest.setId(overTimeRequest.getOverTimeId().intValue());
            requestListResponseOverTimeRequest.setRequestType(2);
            requestListResponseOverTimeRequest.setRequestDate(localDateTimeToString.localDateTimeToString(overTimeRequest.getRequestDate()));
            requestListResponseOverTimeRequest.setRequestStatus(overTimeRequest.getRequestStatus());
            requestListResponse.add(requestListResponseOverTimeRequest);
        }
        for(VacationRequest vacationRequest : vacationRequestService.findByAccountId(account))
        {
            RequestListResponse requestListResponseVacationRequest = new RequestListResponse();
            requestListResponseVacationRequest.setId(vacationRequest.getVacationId().intValue());
            requestListResponseVacationRequest.setRequestType(2);
            requestListResponseVacationRequest.setRequestDate(localDateTimeToString.localDateTimeToString(vacationRequest.getRequestDate()));
            requestListResponseVacationRequest.setRequestStatus(vacationRequest.getRequestStatus());
            requestListResponse.add(requestListResponseVacationRequest);
        }
        for(MonthlyRequest monthlyRequest : monthlyRequestService.findByAccountId(account))
        {
            RequestListResponse requestListResponseMonthRequest = new RequestListResponse();
            requestListResponseMonthRequest.setId(monthlyRequest.getMonthRequestId().intValue());
            requestListResponseMonthRequest.setRequestType(2);
            requestListResponseMonthRequest.setRequestDate(localDateTimeToString.localDateTimeToString(monthlyRequest.getRequestDate()));
            requestListResponseMonthRequest.setRequestStatus(monthlyRequest.getRequestStatus());
            requestListResponse.add(requestListResponseMonthRequest);
        }
        // 申請日時を基にソート
        requestListResponse.sort(Comparator.comparing(RequestListResponse::getRequestDate));
        return requestListResponse;
    }


    @Transactional
    public int withdrow(Account account, WithDrowInput withDrowInput)
    {
        int requestType = withDrowInput.getRequestType();
        Long requestId = withDrowInput.getRequestId();

        // 月次申請以外はその申請のステータスを3の却下に変更する
        // 月次申請はその範囲の月次申請済みの申請を承認に戻す作業が必要
        switch (requestType)
        {
            // シフト申請
            case 1:
                ShiftRequest shiftRequest = shiftRequestService.findByAccountIdAndShiftRequestId(account, requestId);
                // 申請がない場合
                if(Objects.isNull(shiftRequest))
                {
                    return 3;
                }
                // 申請の状態チェック
                if(shiftRequest.getRequestStatus() == 1)
                {
                    shiftRequest.setRequestStatus(5);
                    // 正常に更新できたか
                    ShiftRequest resultShiftRequest = shiftRequestService.save(shiftRequest);
                    if(Objects.isNull(resultShiftRequest))
                    {
                        return 3;
                    }
                    return 1;
                }
                return 3;
            // シフト時間変更申請
            case 2:
                ShiftChangeRequest shiftChangeRequest = shiftChangeRequestService.findByAccountIdAndShiftChangeRequestId(account, requestId);
                // 申請がない場合
                if(Objects.isNull(shiftChangeRequest))
                {
                    return 3;
                }
                // 申請の状態チェック
                if(shiftChangeRequest.getRequestStatus() == 1)
                {
                    shiftChangeRequest.setRequestStatus(5);
                    // 正常に更新できたか
                    ShiftChangeRequest resultShiftChangeRequest = shiftChangeRequestService.save(shiftChangeRequest);
                    if(Objects.isNull(resultShiftChangeRequest))
                    {
                        return 3;
                    }
                    return 1;
                }
                else
                {
                    return 3;
                }
            // 打刻漏れ申請
            case 3:
                StampRequest stampRequest = stampRequestService.findByAccountIdAndStampId(account, requestId);
                // 申請がない場合
                if(Objects.isNull(stampRequest))
                {
                    return 3;
                }
                // 申請の状態チェック
                if(stampRequest.getRequestStatus() == 1)
                {
                    stampRequest.setRequestStatus(5);
                    // 正常に更新できたか
                    StampRequest resultStampRequest = stampRequestService.save(stampRequest);
                    if(Objects.isNull(resultStampRequest))
                    {
                        return 3;
                    }
                    return 1;
                }
                else
                {
                    return 3;
                }
            // 休暇申請
            case 4:
                VacationRequest vacationRequest = vacationRequestService.findByAccountIdAndVacationId(account, requestId);
                // 申請がない場合
                if(Objects.isNull(vacationRequest))
                {
                    return 3;
                }
                // 申請の状態チェック
                if(vacationRequest.getRequestStatus() == 1)
                {
                    vacationRequest.setRequestStatus(5);
                    // 正常に更新できたか
                    VacationRequest resultVacationRequest = vacationRequestService.save(vacationRequest);
                    if(Objects.isNull(resultVacationRequest))
                    {
                        return 3;
                    }
                    return 1;
                }
                else
                {
                    return 3;
                }
            // 残業申請
            case 5:
                OverTimeRequest overTimeRequest = overTimeRequestService.findByAccountIdAndOverTimeRequestId(account, requestId);
                // 申請がない場合
                if(Objects.isNull(overTimeRequest))
                {
                    return 3;
                }
                // 申請の状態チェック
                if(overTimeRequest.getRequestStatus() == 1)
                {
                    overTimeRequest.setRequestStatus(5);
                    OverTimeRequest resultOverTimeRequest = overTimeRequestService.save(overTimeRequest);
                    if(Objects.isNull(resultOverTimeRequest))
                    {
                        return 3;
                    }
                    return 1;
                }
                else
                {
                    return 3;
                }
            // 勤怠例外申請
            case 6:
                AttendanceExceptionRequest attendanceExceptionRequest = attendanceExceptionRequestService.findByAccountIdAndAttendanceExceptionId(account, requestId);
                // 申請がない場合
                if(Objects.isNull(attendanceExceptionRequest))
                {
                    return 3;
                }
                // 申請の状態チェック
                if(attendanceExceptionRequest.getRequestStatus() == 1)
                {
                    attendanceExceptionRequest.setRequestStatus(5);
                    // 正常に更新できたか
                    AttendanceExceptionRequest resultAttendanceExceptionRequest = attendanceExceptionRequestService.save(attendanceExceptionRequest);
                    if(Objects.isNull(resultAttendanceExceptionRequest))
                    {
                        return 3;
                    }
                    return 1;
                }
                else
                {
                    return 3;
                }
            // 月次申請
            case 7:
                MonthlyRequest monthlyRequest = monthlyRequestService.findByAccountIdAndMothlyRequestId(account, requestId);
                // 申請がない場合
                if(Objects.isNull(monthlyRequest))
                {
                    return 3;
                }
                // 申請の状態チェック
                if(monthlyRequest.getRequestStatus() == 1)
                {
                    // 正常
                }
                else
                {
                    return 3;
                }
                // 月次申請の期間内のシフト取得
                List<Shift> shifts = shiftService.findByAccountIdAndBeginWorkBetween(account, monthlyRequest.getYear(), monthlyRequest.getMonth());
                // シフトから関連テーブルを検索
                List<ShiftListOtherTime> shiftListOtherTimes = shiftListOtherTimeService.findByShiftIdIn(shifts);
                List<ShiftListOverTime> shiftListOverTimes = shiftListOverTimeService.findByShiftIdIn(shifts);
                List<ShiftListShiftRequest> shiftListShiftRequests = shiftListShiftRequestService.findByShiftIdIn(shifts);
                List<ShiftListVacation> shiftListVacations = shiftListVacationService.findByShiftIdIn(shifts);
                // 最終的に取得した申請のステータスを月次申請済みから承認に変更
                int monthlyRequestStatus = 2;
                for(ShiftListOtherTime shiftListOtherTime : shiftListOtherTimes)
                {
                    AttendanceExceptionRequest attendanceExceptionRequestMonthly = shiftListOtherTime.getAttendanceExceptionId();
                    attendanceExceptionRequestMonthly.setRequestStatus(monthlyRequestStatus);
                    attendanceExceptionRequestService.save(attendanceExceptionRequestMonthly);
                }
                for(ShiftListOverTime shiftListOverTime : shiftListOverTimes)
                {
                    OverTimeRequest overTimeRequestMonthly = shiftListOverTime.getOverTimeId();
                    overTimeRequestMonthly.setRequestStatus(monthlyRequestStatus);
                    overTimeRequestService.save(overTimeRequestMonthly);
                }
                for(ShiftListShiftRequest shiftListShiftRequest : shiftListShiftRequests)
                {
                    // どちらも存在しなければエラー
                    if(Objects.isNull(shiftListShiftRequest.getShiftChangeRequestId()) && Objects.isNull(shiftListShiftRequest.getShiftRequestId()))
                    {
                        return 3;
                    }
                    else if(Objects.isNull(shiftListShiftRequest.getShiftRequestId()))
                    {
                        // シフト申請がなければエラー
                        return 3;
                    }
                    // シフト時間変更申請がなければシフト申請を
                    else if(Objects.isNull(shiftListShiftRequest.getShiftChangeRequestId()))
                    {
                        ShiftRequest shiftRequestMonthly = shiftListShiftRequest.getShiftRequestId();
                        shiftRequestMonthly.setRequestStatus(monthlyRequestStatus);
                        shiftRequestService.save(shiftRequestMonthly);
                    }
                    // どちらも存在すればエラー
                    else
                    {
                        ShiftChangeRequest shiftChangeRequestMonthly = shiftListShiftRequest.getShiftChangeRequestId();
                        shiftChangeRequestMonthly.setRequestStatus(monthlyRequestStatus);
                        shiftChangeRequestService.save(shiftChangeRequestMonthly);
                    }
                }
                for(ShiftListVacation shiftListVacation : shiftListVacations)
                {
                    VacationRequest vacationRequestMonthly = shiftListVacation.getVacationId();
                    vacationRequestMonthly.setRequestStatus(monthlyRequestStatus);
                    vacationRequestService.save(vacationRequestMonthly);
                }
                // 月次申請の期間内の勤怠情報取得
                List<Attend> attends = attendService.findByAccountIdAndBeginWorkBetween(account, monthlyRequest.getYear(), monthlyRequest.getMonth());
                // 勤怠情報から関連テーブルを検索
                List<AttendanceListSource> attendanceListSources = attendanceListSourceService.findByAttendIdIn(attends);
                // 最終的に取得した申請のステータスを月次申請済みから承認に変更
                for(AttendanceListSource attendanceListSource : attendanceListSources)
                {
                    StampRequest stampRequestMonthly = attendanceListSource.getStampRequestId();
                    stampRequestMonthly.setRequestStatus(monthlyRequestStatus);
                    stampRequestService.save(stampRequestMonthly);
                }
                // 正常に更新できたか
                monthlyRequest.setRequestStatus(5);
                MonthlyRequest resultMonthlyRequest = monthlyRequestService.save(monthlyRequest);
                if(Objects.isNull(resultMonthlyRequest))
                {
                    return 3;
                }
                return 1;
            default:
                return 3;
        }
    }

    @Transactional
    public int reject(Account account, RequestJudgmentInput requestJudgmentInput)
    {
        // 申請を取得
        // 申請者の承認者であることを確認
        // 申請の状態が申請待ち状態であることを確認

        int request = requestJudgmentInput.getRequestType();
        switch (request)
        {
            // シフト申請
            case 1:
                ShiftRequest shiftRequest = shiftRequestService.findById(requestJudgmentInput.getRequestId());
                if(Objects.isNull(shiftRequest))
                {
                    return 3;
                }
                Account shiftAccount = shiftRequest.getAccountId();
                AccountApprover shiftAccountApprover = accountApproverService.findAccountAndApprover(shiftAccount, account);
                if(Objects.isNull(shiftAccountApprover))
                {
                    return 3;
                }
                // 申請の状態を変更
                if(shiftRequest.getRequestStatus() == 1)
                {
                    shiftRequest.setRequestStatus(3);
                    shiftRequest.setApprover(account);
                    shiftRequest.setApproverComment(requestJudgmentInput.getApprovalComment());
                    shiftRequest.setApprovalTime(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime()));
                    ShiftRequest resultShiftRequest = shiftRequestService.save(shiftRequest);
                    if(Objects.isNull(resultShiftRequest))
                    {
                        return 3;
                    }
                    return 1;
                }
                return 3;
            // シフト時間変更申請
            case 2:
                ShiftChangeRequest shiftChangeRequest = shiftChangeRequestService.findById(requestJudgmentInput.getRequestId());
                if(Objects.isNull(shiftChangeRequest))
                {
                    return 3;
                }
                Account shiftChangeAccount = shiftChangeRequest.getAccountId();
                AccountApprover shiftChangeApprover = accountApproverService.findAccountAndApprover(shiftChangeAccount, account);
                if(Objects.isNull(shiftChangeApprover))
                {
                    return 3;
                }
                // 申請の状態を変更
                if(shiftChangeRequest.getRequestStatus() == 1)
                {
                    shiftChangeRequest.setRequestStatus(3);
                    shiftChangeRequest.setApprover(account);
                    shiftChangeRequest.setApproverComment(requestJudgmentInput.getApprovalComment());
                    shiftChangeRequest.setApprovalTime(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime()));
                    ShiftChangeRequest resultShiftChangeRequest = shiftChangeRequestService.save(shiftChangeRequest);
                    if(Objects.isNull(resultShiftChangeRequest))
                    {
                        return 3;
                    }
                    return 1;
                }
                return 3;
            // 打刻漏れ申請
            case 3:
                StampRequest stampRequest = stampRequestService.findById(requestJudgmentInput.getRequestId());
                if(Objects.isNull(stampRequest))
                {
                    return 3;
                }
                Account stampAccount = stampRequest.getAccountId();
                AccountApprover stampAccountApprover = accountApproverService.findAccountAndApprover(stampAccount, account);
                if(Objects.isNull(stampAccountApprover))
                {
                    return 3;
                }
                // 申請の状態を変更
                if(stampRequest.getRequestStatus() == 1)
                {
                    stampRequest.setRequestStatus(3);
                    stampRequest.setApprover(account);
                    stampRequest.setApproverComment(requestJudgmentInput.getApprovalComment());
                    stampRequest.setApprovalTime(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime()));
                    StampRequest resultStampRequest = stampRequestService.save(stampRequest);
                    if(Objects.isNull(resultStampRequest))
                    {
                        return 3;
                    }
                    return 1;
                }
                return 3;
            // 休暇申請
            case 4:
                VacationRequest vacationRequest = vacationRequestService.findById(requestJudgmentInput.getRequestId());
                if(Objects.isNull(vacationRequest))
                {
                    return 3;
                }
                Account vacationAccount = vacationRequest.getAccountId();
                AccountApprover vacationAccountApprover = accountApproverService.findAccountAndApprover(vacationAccount, account);
                if(Objects.isNull(vacationAccountApprover))
                {
                    return 3;
                }
                // 申請の状態を変更
                if(vacationRequest.getRequestStatus() == 1)
                {
                    vacationRequest.setRequestStatus(3);
                    vacationRequest.setApprover(account);
                    vacationRequest.setApproverComment(requestJudgmentInput.getApprovalComment());
                    vacationRequest.setApprovalTime(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime()));
                    VacationRequest resultVacationRequest = vacationRequestService.save(vacationRequest);
                    if(Objects.isNull(resultVacationRequest))
                    {
                        return 3;
                    }
                    return 1;
                }
                return 1;
            // 残業申請
            case 5:
                OverTimeRequest overTimeRequest = overTimeRequestService.findById(requestJudgmentInput.getRequestId());
                if(Objects.isNull(overTimeRequest))
                {
                    return 3;
                }
                Account overTimeAccount = overTimeRequest.getAccountId();
                AccountApprover overTimeAccountApprover = accountApproverService.findAccountAndApprover(overTimeAccount, account);
                if(Objects.isNull(overTimeAccountApprover))
                {
                    return 3;
                }
                // 申請の状態を変更
                if(overTimeRequest.getRequestStatus() == 1)
                {
                    overTimeRequest.setRequestStatus(3);
                    overTimeRequest.setApprover(account);
                    overTimeRequest.setApproverComment(requestJudgmentInput.getApprovalComment());
                    overTimeRequest.setApprovalTime(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime()));
                    OverTimeRequest resultOverTimeRequest = overTimeRequestService.save(overTimeRequest);
                    if(Objects.isNull(resultOverTimeRequest))
                    {
                        return 3;
                    }
                    return 1;
                }
                return 3;
            // 遅刻、早退、残業申請
            case 6:
                AttendanceExceptionRequest attendanceExceptionRequest = attendanceExceptionRequestService.findById(requestJudgmentInput.getRequestId());
                if(Objects.isNull(attendanceExceptionRequest))
                {
                    return 3;
                }
                Account attendanceExceptionAccount = attendanceExceptionRequest.getAccountId();
                AccountApprover attendanceExceptionAccountApprover = accountApproverService.findAccountAndApprover(attendanceExceptionAccount, account);
                if(Objects.isNull(attendanceExceptionAccountApprover))
                {
                    return 3;
                }
                // 申請の状態を変更
                if(attendanceExceptionRequest.getRequestStatus() == 1)
                {
                    attendanceExceptionRequest.setRequestStatus(3);
                    attendanceExceptionRequest.setApprover(account);
                    attendanceExceptionRequest.setApproverComment(requestJudgmentInput.getApprovalComment());
                    attendanceExceptionRequest.setApprovalTime(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime()));
                    AttendanceExceptionRequest resultAttendanceExceptionRequest = attendanceExceptionRequestService.save(attendanceExceptionRequest);
                    if(Objects.isNull(resultAttendanceExceptionRequest))
                    {
                        return 3;
                    }
                    return 1;
                }
                return 3;
            // 月次申請
            case 7:
                MonthlyRequest monthlyRequest = monthlyRequestService.findById(requestJudgmentInput.getRequestId());
                if(Objects.isNull(monthlyRequest))
                {
                    return 3;
                }
                Account monthlyAccount = monthlyRequest.getAccountId();
                AccountApprover monthlyAccountApprover = accountApproverService.findAccountAndApprover(monthlyAccount, account);
                if(Objects.isNull(monthlyAccountApprover))
                {
                    return 3;
                }
                // 申請の状態チェック
                if(monthlyRequest.getRequestStatus() == 1)
                {
                    // 正常
                }
                else
                {
                    return 3;
                }
                // 月次申請の期間内のシフト取得
                List<Shift> shifts = shiftService.findByAccountIdAndBeginWorkBetween(monthlyAccount, monthlyRequest.getYear(), monthlyRequest.getMonth());
                // シフトから関連テーブルを検索
                List<ShiftListOtherTime> shiftListOtherTimes = shiftListOtherTimeService.findByShiftIdIn(shifts);
                List<ShiftListOverTime> shiftListOverTimes = shiftListOverTimeService.findByShiftIdIn(shifts);
                List<ShiftListShiftRequest> shiftListShiftRequests = shiftListShiftRequestService.findByShiftIdIn(shifts);
                List<ShiftListVacation> shiftListVacations = shiftListVacationService.findByShiftIdIn(shifts);
                // 最終的に取得した申請のステータスを月次申請済みから承認に変更
                int monthlyRequestStatus = 2;
                for(ShiftListOtherTime shiftListOtherTime : shiftListOtherTimes)
                {
                    AttendanceExceptionRequest attendanceExceptionRequestMonthly = shiftListOtherTime.getAttendanceExceptionId();
                    attendanceExceptionRequestMonthly.setRequestStatus(monthlyRequestStatus);
                    attendanceExceptionRequestService.save(attendanceExceptionRequestMonthly);
                }
                for(ShiftListOverTime shiftListOverTime : shiftListOverTimes)
                {
                    OverTimeRequest overTimeRequestMonthly = shiftListOverTime.getOverTimeId();
                    overTimeRequestMonthly.setRequestStatus(monthlyRequestStatus);
                    overTimeRequestService.save(overTimeRequestMonthly);
                }
                for(ShiftListShiftRequest shiftListShiftRequest : shiftListShiftRequests)
                {
                    // どちらも存在しなければエラー
                    if(Objects.isNull(shiftListShiftRequest.getShiftChangeRequestId()) && Objects.isNull(shiftListShiftRequest.getShiftRequestId()))
                    {
                        return 3;
                    }
                    else if(Objects.isNull(shiftListShiftRequest.getShiftRequestId()))
                    {
                        // シフト申請がなければエラー
                        return 3;
                    }
                    // シフト時間変更申請がなければシフト申請を
                    else if(Objects.isNull(shiftListShiftRequest.getShiftChangeRequestId()))
                    {
                        ShiftRequest shiftRequestMonthly = shiftListShiftRequest.getShiftRequestId();
                        shiftRequestMonthly.setRequestStatus(monthlyRequestStatus);
                        shiftRequestService.save(shiftRequestMonthly);
                    }
                    // どちらも存在すればエラー
                    else
                    {
                        ShiftChangeRequest shiftChangeRequestMonthly = shiftListShiftRequest.getShiftChangeRequestId();
                        shiftChangeRequestMonthly.setRequestStatus(monthlyRequestStatus);
                        shiftChangeRequestService.save(shiftChangeRequestMonthly);
                    }
                }
                for(ShiftListVacation shiftListVacation : shiftListVacations)
                {
                    VacationRequest vacationRequestMonthly = shiftListVacation.getVacationId();
                    vacationRequestMonthly.setRequestStatus(monthlyRequestStatus);
                    vacationRequestService.save(vacationRequestMonthly);
                }
                // 月次申請の期間内の勤怠情報取得
                List<Attend> attends = attendService.findByAccountIdAndBeginWorkBetween(monthlyAccount, monthlyRequest.getYear(), monthlyRequest.getMonth());
                // 勤怠情報から関連テーブルを検索
                List<AttendanceListSource> attendanceListSources = attendanceListSourceService.findByAttendIdIn(attends);
                // 最終的に取得した申請のステータスを月次申請済みから承認に変更
                for(AttendanceListSource attendanceListSource : attendanceListSources)
                {
                    StampRequest stampRequestMonthly = attendanceListSource.getStampRequestId();
                    stampRequestMonthly.setRequestStatus(monthlyRequestStatus);
                    stampRequestService.save(stampRequestMonthly);
                }
                // 正常に更新できたか
                monthlyRequest.setRequestStatus(5);
                monthlyRequest.setApprover(account);
                monthlyRequest.setApproverComment(requestJudgmentInput.getApprovalComment());
                monthlyRequest.setApprovalDate(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime()));
                MonthlyRequest resultMonthlyRequest = monthlyRequestService.save(monthlyRequest);
                if(Objects.isNull(resultMonthlyRequest))
                {
                    return 3;
                }
                return 1;
            default:
                return 3;
        }
    }

    @Transactional
    public int approval(Account account, RequestJudgmentInput requestJudgmentInput)
    {
        // 申請を取得
        // 申請者の承認者であることを確認
        // 申請の状態が申請待ち状態であることを確認

        int request = requestJudgmentInput.getRequestType();
        switch (request)
        {
            // シフト申請
            case 1:
                ShiftRequest shiftRequest = shiftRequestService.findById(requestJudgmentInput.getRequestId());
                if(Objects.isNull(shiftRequest))
                {
                    return 3;
                }
                Account shiftGeneralAccount = shiftRequest.getAccountId();
                AccountApprover shiftAccountApprover = accountApproverService.findAccountAndApprover(shiftGeneralAccount, account);
                // 申請者の承認者の情報がなければエラー
                if(Objects.isNull(shiftAccountApprover))
                {
                    return 3;
                }
                // 申請の状態確認
                if(shiftRequest.getRequestStatus() == 1)
                {
                    // 承認に変更
                    shiftRequest.setRequestStatus(2);
                    shiftRequest.setApprover(account);
                    shiftRequest.setApproverComment(requestJudgmentInput.getApprovalComment());
                    shiftRequest.setApprovalTime(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime()));
                    if(shiftService.findByAccountIdAndBeginWorkBetweenWeek(shiftGeneralAccount, shiftRequest.getBeginWork()).size() >= 6)
                    {
                        // 6以上なら休日出勤のためフラグを立てる
                        shiftRequest.setVacationWork(true);
                    }
                    ShiftRequest resultShiftRequest = shiftRequestService.save(shiftRequest);
                    if(Objects.isNull(resultShiftRequest))
                    {
                        return 3;
                    }
                }
                else
                {
                    return 3;
                }
                // シフトに記録
                Shift shiftRequestShift = shiftRequestService.shiftRequestToShift(shiftRequest);
                Shift resultShiftRequestShift = shiftService.save(shiftRequestShift);
                if(Objects.isNull(resultShiftRequestShift))
                {
                    return 3;
                }
                // シフトとシフトに関する申請テーブル登録
                ShiftListShiftRequest shiftShiftListShiftRequest = new ShiftListShiftRequest();
                // シフト設定
                shiftShiftListShiftRequest.setShiftId(resultShiftRequestShift);
                // シフト申請設定
                shiftShiftListShiftRequest.setShiftRequestId(shiftRequest);

                ShiftListShiftRequest resultShiftShiftListShiftRequest = shiftListShiftRequestService.save(shiftShiftListShiftRequest);
                // 結果をチェック
                if(Objects.isNull(resultShiftShiftListShiftRequest))
                {
                    return 3;
                }
                return 1;
            // シフト時間変更申請
            case 2:
                ShiftChangeRequest shiftChangeRequest = shiftChangeRequestService.findById(requestJudgmentInput.getRequestId());
                if(Objects.isNull(shiftChangeRequest))
                {
                    return 3;
                }
                Account shiftChangeGeneralAccount = shiftChangeRequest.getAccountId();
                AccountApprover shiftChangeAccountApprover = accountApproverService.findAccountAndApprover(shiftChangeGeneralAccount, account);
                if(Objects.isNull(shiftChangeAccountApprover))
                {
                    return 3;
                }
                // シフトで既に勤怠情報が確定していればエラー
                AttendanceListSource shiftChangeAttendanceListSource = attendanceListSourceService.findByShiftId(shiftChangeRequest.getShiftId());
                if(Objects.isNull(shiftChangeAttendanceListSource))
                {
                    // nullが想定通り
                }
                else
                {
                    return 3;
                }
                // 申請の状態確認
                if(shiftChangeRequest.getRequestStatus() == 1)
                {
                    // 承認に変更
                    shiftChangeRequest.setRequestStatus(2);
                    shiftChangeRequest.setApprover(account);
                    shiftChangeRequest.setApproverComment(requestJudgmentInput.getApprovalComment());
                    shiftChangeRequest.setApprovalTime(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime()));
                    if(shiftService.findByAccountIdAndBeginWorkBetweenWeek(shiftChangeGeneralAccount, shiftChangeRequest.getBeginWork()).size() >= 6)
                    {
                        // 6以上なら休日出勤フラグを立てる
                        shiftChangeRequest.setVacationWork(true);
                    }
                    ShiftChangeRequest resultShiftChangeRequest = shiftChangeRequestService.save(shiftChangeRequest);
                    if(Objects.isNull(resultShiftChangeRequest))
                    {
                        return 3;
                    }
                }
                else
                {
                    return 3;
                }
                // シフトに記録
                Shift shiftChangeRequestShift = shiftChangeRequestService.shiftChangeRequestToShift(shiftChangeRequest);
                Shift resultShiftChangeRequestShift = shiftService.save(shiftChangeRequestShift);
                if(Objects.isNull(resultShiftChangeRequestShift))
                {
                    return 3;
                }
                // シフトとシフトに関する申請テーブル
                ShiftListShiftRequest shiftChangeShiftListShiftRequest = shiftListShiftRequestService.findByShiftId(resultShiftChangeRequestShift);
                // 時間変更申請を登録
                shiftChangeShiftListShiftRequest.setShiftChangeRequestId(shiftChangeRequest);
                ShiftListShiftRequest resultShiftChangeShiftListShiftRequest = shiftListShiftRequestService.save(shiftChangeShiftListShiftRequest);
                // 結果をチェック
                if(Objects.isNull(resultShiftChangeShiftListShiftRequest))
                {
                    return 3;
                }
                // シフトに関連する申請で反映状態のものがあれば却下し関連テーブルは削除する
                List<ShiftListOtherTime> shiftChangeShiftListOtherTimes = shiftListOtherTimeService.findByShiftId(resultShiftChangeRequestShift);
                for(ShiftListOtherTime shiftChangeShiftListOtherTime : shiftChangeShiftListOtherTimes)
                {
                    AttendanceExceptionRequest shiftChangeAttendanceExceptionRequest = shiftChangeShiftListOtherTime.getAttendanceExceptionId();
                    shiftChangeAttendanceExceptionRequest.setRequestStatus(3);
                    AttendanceExceptionRequest resultShiftChangeAttendanceExceptionRequest = attendanceExceptionRequestService.save(shiftChangeAttendanceExceptionRequest);
                    if(Objects.isNull(resultShiftChangeAttendanceExceptionRequest))
                    {
                        return 3;
                    }
                    shiftListOtherTimeService.deleteByShiftListOtherTime(shiftChangeShiftListOtherTime);
                }
                List<ShiftListOverTime> shiftChangeShiftListOverTimes = shiftListOverTimeService.findByShiftId(resultShiftChangeRequestShift);
                for(ShiftListOverTime shiftChangeShiftListOverTime : shiftChangeShiftListOverTimes)
                {
                    OverTimeRequest shiftChangeOverTimeRequest = shiftChangeShiftListOverTime.getOverTimeId();
                    shiftChangeOverTimeRequest.setRequestStatus(3);
                    OverTimeRequest resultShiftChangeOverTimeRequest = overTimeRequestService.save(shiftChangeOverTimeRequest);
                    if(Objects.isNull(resultShiftChangeOverTimeRequest))
                    {
                        return 3;
                    }
                    shiftListOverTimeService.deleteByShiftListOverTime(shiftChangeShiftListOverTime);
                }
                List<ShiftListVacation> shiftChangeShiftListVacations = shiftListVacationService.findByShiftId(resultShiftChangeRequestShift);
                for(ShiftListVacation shiftChangeShiftListVacation : shiftChangeShiftListVacations)
                {
                    VacationRequest shiftChangeVacationRequest = shiftChangeShiftListVacation.getVacationId();
                    shiftChangeVacationRequest.setRequestStatus(3);
                    VacationRequest resultShiftChangeVacationRequest = vacationRequestService.save(shiftChangeVacationRequest);
                    if(Objects.isNull(resultShiftChangeVacationRequest))
                    {
                        return 3;
                    }
                    shiftListVacationService.deleteByShiftListVacationId(shiftChangeShiftListVacation);
                }
                return 1;
            // 打刻漏れ申請
            case 3:
                StampRequest stampRequest = stampRequestService.findById(requestJudgmentInput.getRequestId());
                if(Objects.isNull(stampRequest))
                {
                    return 3;
                }

                Account stampGeneralAccount = stampRequest.getAccountId();
                AccountApprover stampAccountApprover = accountApproverService.findAccountAndApprover(stampGeneralAccount, account);
                if(Objects.isNull(stampAccountApprover))
                {
                    return 3;
                }

                AttendanceListSource stampAttendanceListSource = attendanceListSourceService.findByShiftId(stampRequest.getShiftId());
                if(Objects.isNull(stampAttendanceListSource))
                {
                    // nullなら正常
                }
                else
                {
                    return 3;
                }
                // 申請の状態確認
                StampRequest resultStampRequest = new StampRequest();
                if(stampRequest.getRequestStatus() == 1)
                {
                    stampRequest.setRequestStatus(2);
                    stampRequest.setApprover(account);
                    stampRequest.setApproverComment(requestJudgmentInput.getApprovalComment());
                    stampRequest.setApprovalTime(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime()));
                    resultStampRequest = stampRequestService.save(stampRequest);
                    if(Objects.isNull(resultStampRequest))
                    {
                        return 3;
                    }
                }
                else
                {
                    return 3;
                }

                // 勤怠テーブルに記録
                // 遅刻時間、早退時間、外出時間、労働時間、休憩時間、残業時間、休日労働時間(法定時間より)、深夜労働時間(法定時間より取得した内容から計算)、休暇時間()、欠勤時間()
                // 労働時間、休憩時間、休日労働時間、深夜労働時間
                ShiftListShiftRequest stampShiftListShiftRequest = shiftListShiftRequestService.findByShiftId(resultStampRequest.getShiftId());
                // 休暇時間、欠勤時間
                List<ShiftListVacation> stampShiftListVacations = shiftListVacationService.findByShiftId(resultStampRequest.getShiftId());
                // 残業時間
                List<ShiftListOverTime> stampShiftListOverTimes = shiftListOverTimeService.findByShiftId(resultStampRequest.getShiftId());
                // 遅刻、早退、外出時間
                List<ShiftListOtherTime> stampShiftListOtherTimes = shiftListOtherTimeService.findByShiftId(resultStampRequest.getShiftId());

                // 労働時間を休憩前と休憩後で取得し足し算
                Duration stampWorkTime = Duration.between(resultStampRequest.getShiftId().getBeginWork(), resultStampRequest.getShiftId().getBeginBreak()).plus(Duration.between(resultStampRequest.getShiftId().getEndBreak(), resultStampRequest.getShiftId().getEndWork()));
                // 休憩時間を取得
                Duration stampBreakTime = Duration.between(resultStampRequest.getShiftId().getBeginBreak(), resultStampRequest.getShiftId().getEndBreak());

                // 深夜労働を取得
                Time stampLateNightWorkTime = getLateNight(resultStampRequest.getBeginWork(), resultStampRequest.getEndWork(), resultStampRequest.getBeginBreak(), resultStampRequest.getEndBreak(), stampShiftListVacations);

                // 休暇時間
                Duration stampVacationTime = Duration.ZERO;
                //　欠勤時間
                Duration stampAbsenceTime = Duration.ZERO;
                for(ShiftListVacation stampShiftListVacation : stampShiftListVacations)
                {
                    if(stampShiftListVacation.getVacationId().getVacationTypeId().getVacationTypeId().intValue() == 3)
                    {
                        // 休暇申請が欠勤なら欠勤時間にプラス
                        stampAbsenceTime = getVacation(stampShiftListVacation.getVacationId().getBeginVacation(), stampShiftListVacation.getVacationId().getEndVacation(), stampShiftListVacation.getShiftId().getBeginBreak(), stampShiftListVacation.getShiftId().getEndBreak());
                    }
                    else
                    {
                        // その他なら休暇時間にプラス
                        stampVacationTime = getVacation(stampShiftListVacation.getVacationId().getBeginVacation(), stampShiftListVacation.getVacationId().getEndVacation(), stampShiftListVacation.getShiftId().getBeginBreak(), stampShiftListVacation.getShiftId().getEndBreak());
                    }
                }

                // 労働時間から休暇時間と欠勤時間を減算
                stampWorkTime = stampWorkTime.minus(stampVacationTime);
                stampWorkTime = stampWorkTime.minus(stampAbsenceTime);

                // 残業時間
                Duration stampOverTime = Duration.ZERO;
                for(ShiftListOverTime stampShiftListOverTime : stampShiftListOverTimes)
                {
                    stampOverTime = stampOverTime.plus(Duration.between(stampShiftListOverTime.getOverTimeId().getBeginWork(), stampShiftListOverTime.getOverTimeId().getEndWork()));
                }

                // 遅刻時間
                Duration stampLatenessTime = Duration.ZERO;
                // 早退時間
                Duration stampLeavearlyTime = Duration.ZERO;
                // 外出時間
                Duration stampOutingTime = Duration.ZERO;
                for(ShiftListOtherTime stampShiftListOtherTime : stampShiftListOtherTimes)
                {
                    if(stampShiftListOtherTime.getAttendanceExceptionId().getAttendanceExceptionTypeId().getAttendanceExceptionTypeId().intValue() == 1)
                    {
                        // 外出時間に加算
                        stampOutingTime = stampOutingTime.plus(Duration.between(stampShiftListOtherTime.getAttendanceExceptionId().getBeginTime(), stampShiftListOtherTime.getAttendanceExceptionId().getEndTime()));
                    }
                    else if(stampShiftListOtherTime.getAttendanceExceptionId().getAttendanceExceptionTypeId().getAttendanceExceptionTypeId().intValue() == 2)
                    {
                        // 遅刻時間に加算
                        stampLatenessTime = stampLatenessTime.plus(Duration.between(stampShiftListOtherTime.getAttendanceExceptionId().getBeginTime(), stampShiftListOtherTime.getAttendanceExceptionId().getEndTime()));
                    }
                    else if(stampShiftListOtherTime.getAttendanceExceptionId().getAttendanceExceptionTypeId().getAttendanceExceptionTypeId().intValue() == 3)
                    {  
                        // 早退時間に加算
                        stampLeavearlyTime = stampLeavearlyTime.plus(Duration.between(stampShiftListOtherTime.getAttendanceExceptionId().getBeginTime(), stampShiftListOtherTime.getAttendanceExceptionId().getEndTime()));
                    }
                }

                Attend stampAttend = new Attend();
                stampAttend.setAccountId(stampGeneralAccount);
                stampAttend.setBeginWork(resultStampRequest.getShiftId().getBeginWork());
                stampAttend.setEndWork(resultStampRequest.getShiftId().getEndWork());
                stampAttend.setBeginBreak(resultStampRequest.getShiftId().getBeginBreak());
                stampAttend.setEndBreak(resultStampRequest.getShiftId().getEndBreak());
                boolean isVacationWork = false;
                if(Objects.isNull(stampShiftListShiftRequest.getShiftRequestId()) && Objects.isNull(stampShiftListShiftRequest.getShiftChangeRequestId()))
                {
                    // シフト申請,シフト時間変更申請がなければエラー
                    return 3;
                }
                else if(Objects.isNull(stampShiftListShiftRequest.getShiftRequestId()))
                {
                    // シフト申請がなければエラー
                    return 3;
                }
                else if(Objects.isNull(stampShiftListShiftRequest.getShiftChangeRequestId()))
                {
                    // シフト申請があればシフト申請を利用
                    isVacationWork = stampShiftListShiftRequest.getShiftRequestId().isVacationWork();
                }
                else
                {
                    // シフト時間変更申請があればシフト時間変更申請を利用
                    isVacationWork = stampShiftListShiftRequest.getShiftChangeRequestId().isVacationWork();
                }
                if(isVacationWork)
                {
                    // 休日出勤なら休日労働に加算
                    stampAttend.setHolidayWork(new Time(stampWorkTime.toSeconds()));
                }
                else
                {
                    // 休日出勤でなければ労働時間に加算
                    stampAttend.setWorkTime(new Time(stampWorkTime.toSeconds()));
                }
                stampAttend.setBreakTime(new Time(stampBreakTime.getSeconds()));
                stampAttend.setLateness(new Time(stampLatenessTime.toSeconds()));
                stampAttend.setLeaveEarly(new Time(stampLeavearlyTime.toSeconds()));
                stampAttend.setOuting(new Time(stampOutingTime.toSeconds()));
                stampAttend.setOverWork(new Time(stampOverTime.toSeconds()));
                stampAttend.setLateNightWork(stampLateNightWorkTime);
                stampAttend.setVacationTime(new Time(stampVacationTime.toSeconds()));
                stampAttend.setAbsenceTime(new Time(stampAbsenceTime.toSeconds()));

                Attend resultStampAttend = attendService.save(stampAttend);
                if(Objects.isNull(resultStampAttend))
                {
                    return 3;
                }
                // 勤怠と勤怠に関する情報源テーブル
                AttendanceListSource newStampAttendanceListSource = new AttendanceListSource();
                newStampAttendanceListSource.setAttendanceId(resultStampAttend);
                newStampAttendanceListSource.setShiftId(stampRequest.getShiftId());
                newStampAttendanceListSource.setStampRequestId(resultStampRequest);
                AttendanceListSource resultNewStampAttendanceListSource = attendanceListSourceService.save(newStampAttendanceListSource);
                if(Objects.isNull(resultNewStampAttendanceListSource))
                {
                    return 3;
                }
                else
                {
                    return 1;
                }

            // 休暇申請
            case 4:
                VacationRequest vacationRequest = vacationRequestService.findById(requestJudgmentInput.getRequestId());
                if(Objects.isNull(vacationRequest))
                {
                    return 3;
                }
                Account vacationGeneralAccount = vacationRequest.getAccountId();
                AccountApprover vacationAccountApprover = accountApproverService.findAccountAndApprover(vacationGeneralAccount, account);
                if(Objects.isNull(vacationAccountApprover))
                {
                    return 3;
                }
                // 申請の状態を確認
                VacationRequest resultVacationRequest = new VacationRequest();
                if(vacationRequest.getRequestStatus() == 1)
                {
                    vacationRequest.setRequestStatus(2);
                    vacationRequest.setApprover(account);
                    vacationRequest.setApproverComment(requestJudgmentInput.getApprovalComment());
                    vacationRequest.setApprovalTime(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime()));
                    resultVacationRequest = vacationRequestService.save(vacationRequest);
                    if(Objects.isNull(resultVacationRequest))
                    {
                        return 3;
                    }
                }
                else
                {
                    return 3;
                }
                // シフトと休暇申請テーブル
                ShiftListVacation vacationShiftListVacation = new ShiftListVacation();
                vacationShiftListVacation.setShiftId(resultVacationRequest.getShiftId());
                vacationShiftListVacation.setVacationId(resultVacationRequest);
                ShiftListVacation resultVacationShiftListVacation = shiftListVacationService.save(vacationShiftListVacation);
                if(Objects.isNull(resultVacationShiftListVacation))
                {
                    return 3;
                }
                // 休暇テーブル
                Vacation vacation = new Vacation();
                vacation.setAccountId(account);
                vacation.setVacationId(resultVacationRequest);
                vacation.setVacationTypeId(resultVacationRequest.getVacationTypeId());
                vacation.setBeginVacation(resultVacationRequest.getBeginVacation());
                vacation.setEndVacation(resultVacationRequest.getEndVacation());
                Vacation resultVacation = vacationService.save(vacation);
                if(Objects.isNull(resultVacation))
                {
                    return 3;
                }
                // 有給休暇消費テーブル
                PaydHolidayUse vacationPaydHolidayUse = new PaydHolidayUse();
                vacationPaydHolidayUse.setAccountId(account);
                vacationPaydHolidayUse.setVacationId(resultVacationRequest);
                vacationPaydHolidayUse.setTime(new Time(Duration.between(resultVacationRequest.getBeginVacation(), resultVacationRequest.getEndVacation()).toSeconds()));
                PaydHolidayUse resultVacationPaydHolidayUse = paydHolidayUseService.save(vacationPaydHolidayUse);
                if(Objects.isNull(resultVacationPaydHolidayUse))
                {
                    return 3;
                }
                else
                {
                    return 1;
                }
            // 残業申請
            case 5:
                OverTimeRequest overTimeRequest = overTimeRequestService.findById(requestJudgmentInput.getRequestId());
                if(Objects.isNull(overTimeRequest))
                {
                    return 3;
                }
                Account overTimeGeneralAccount = overTimeRequest.getAccountId();
                AccountApprover overTimeAccountApprover = accountApproverService.findAccountAndApprover(overTimeGeneralAccount, account);
                if(Objects.isNull(overTimeAccountApprover))
                {
                    return 3;
                }
                // 申請の状態確認
                OverTimeRequest resultOverTimeRequest = new OverTimeRequest();
                if(overTimeRequest.getRequestStatus() == 1)
                {
                    overTimeRequest.setRequestStatus(2);
                    overTimeRequest.setApprover(account);
                    overTimeRequest.setApproverComment(requestJudgmentInput.getApprovalComment());
                    overTimeRequest.setApprovalTime(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime()));
                    resultOverTimeRequest = overTimeRequestService.save(overTimeRequest);
                    if(Objects.isNull(resultOverTimeRequest))
                    {
                        return 3;
                    }
                }
                else
                {
                    return 3;
                }
                // シフトの残業時間
                Shift overTimeShift = resultOverTimeRequest.getShiftId();
                overTimeShift.setOverWork(Time.valueOf(resultOverTimeRequest.getShiftId().getOverWork().toLocalTime().plus(Duration.between(resultOverTimeRequest.getBeginWork(), resultOverTimeRequest.getEndWork()))));
                Shift resultOverTimeShift = shiftService.save(overTimeShift);
                if(Objects.isNull(resultOverTimeShift))
                {
                    return 3;
                }
                // シフトと残業テーブル
                ShiftListOverTime overTimeShiftListOverTime = new ShiftListOverTime();
                overTimeShiftListOverTime.setOverTimeId(resultOverTimeRequest);
                overTimeShiftListOverTime.setShiftListId(resultOverTimeShift);
                ShiftListOverTime resultOverTimeShiftListOverTime = shiftListOverTimeService.save(overTimeShiftListOverTime);
                if(Objects.isNull(resultOverTimeShiftListOverTime))
                {
                    return 3;
                }
                else
                {
                    return 1;
                }
            // 遅刻、早退、残業申請
            case 6:
                AttendanceExceptionRequest attendanceExceptionRequest = attendanceExceptionRequestService.findById(requestJudgmentInput.getRequestId());
                if(Objects.isNull(attendanceExceptionRequest))
                {
                    return 3;
                }
                Account attendanceExceptionGeneralAccount = attendanceExceptionRequest.getAccountId();
                AccountApprover attendanceExceptionAccountApprover = accountApproverService.findAccountAndApprover(attendanceExceptionGeneralAccount, account);
                if(Objects.isNull(attendanceExceptionAccountApprover))
                {
                    return 3;
                }
                // 申請の状態確認
                AttendanceExceptionRequest resultAttendanceExceptionRequest = new AttendanceExceptionRequest();
                if(attendanceExceptionRequest.getRequestStatus() == 1)
                {
                    attendanceExceptionRequest.setRequestStatus(2);
                    attendanceExceptionRequest.setApprover(account);
                    attendanceExceptionRequest.setApproverComment(requestJudgmentInput.getApprovalComment());
                    attendanceExceptionRequest.setApprovalTime(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime()));
                    resultAttendanceExceptionRequest = attendanceExceptionRequestService.save(attendanceExceptionRequest);
                    if(Objects.isNull(resultAttendanceExceptionRequest))
                    {
                        return 3;
                    }
                }
                // シフトの遅刻時間、早退時間、外出時間
                Shift attendanceExceptionRequestShift = resultAttendanceExceptionRequest.getShiftId();
                switch (resultAttendanceExceptionRequest.getAttendanceExceptionTypeId().getAttendanceExceptionTypeId().intValue())
                {
                    // 外出申請
                    case 1:
                        attendanceExceptionRequestShift.setOuting(Time.valueOf(resultAttendanceExceptionRequest.getShiftId().getOuting().toLocalTime().plus(Duration.between(resultAttendanceExceptionRequest.getBeginTime(), resultAttendanceExceptionRequest.getEndTime()))));
                        break;
                    // 遅刻申請
                    case 2:
                        attendanceExceptionRequestShift.setLateness(Time.valueOf(resultAttendanceExceptionRequest.getShiftId().getLateness().toLocalTime().plus(Duration.between(resultAttendanceExceptionRequest.getBeginTime(), resultAttendanceExceptionRequest.getEndTime()))));
                        break;
                    // 早退申請
                    case 3:
                        attendanceExceptionRequestShift.setLeaveEarly(Time.valueOf(resultAttendanceExceptionRequest.getShiftId().getLeaveEarly().toLocalTime().plus(Duration.between(resultAttendanceExceptionRequest.getBeginTime(), resultAttendanceExceptionRequest.getEndTime()))));
                        break;
                    default:
                        break;
                }
                Shift resultAttendanceExceptionShift = shiftService.save(attendanceExceptionRequestShift);
                if(Objects.isNull(resultAttendanceExceptionShift))
                {
                    return 3;
                }
                // シフトと勤怠例外テーブル
                ShiftListOtherTime attendanceExceptionShiftListOtherTime = new ShiftListOtherTime();
                attendanceExceptionShiftListOtherTime.setShiftId(resultAttendanceExceptionShift);
                attendanceExceptionShiftListOtherTime.setAttendanceExceptionId(resultAttendanceExceptionRequest);
                ShiftListOtherTime resultAttendanceExceptionShiftListOtherTime = shiftListOtherTimeService.save(attendanceExceptionShiftListOtherTime);
                if(Objects.isNull(resultAttendanceExceptionShiftListOtherTime))
                {
                    return 3;
                }
                else
                {
                    return 1;
                }
            // 月次申請
            case 7:
                MonthlyRequest monthlyRequest = monthlyRequestService.findById(requestJudgmentInput.getRequestId());
                if(Objects.isNull(monthlyRequest))
                {
                    return 3;
                }
                // 申請者の承認者か
                Account monthGeneralAccount = monthlyRequest.getAccountId();
                AccountApprover monthAccountApprover = accountApproverService.findAccountAndApprover(monthGeneralAccount, account);
                if(Objects.isNull(monthAccountApprover))
                {
                    return 3;
                }
                // 申請の状態は?
                MonthlyRequest resultMonthlyRequest = new MonthlyRequest();
                if(monthlyRequest.getRequestStatus() == 1)
                {
                    monthlyRequest.setRequestStatus(2);
                    monthlyRequest.setApprover(account);
                    monthlyRequest.setApproverComment(requestJudgmentInput.getApprovalComment());
                    monthlyRequest.setApprovalDate(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime()));
                    resultMonthlyRequest = monthlyRequestService.save(monthlyRequest);
                    if(Objects.isNull(resultMonthlyRequest))
                    {
                        return 3;
                    }
                }
                else
                {
                    return 3;
                }
                return 1;
            default:
                return 3;
        }
    }

    @Transactional
    public int approvalCancel(Account account, RequestJudgmentInput requestJudgmentInput)
    {
        // 申請を取得
        // 申請者の承認者であることを確認
        // 申請の状態が承認状態であることを確認
        int request = requestJudgmentInput.getRequestType();
        switch (request)
        {
            // シフト申請
            // 勤怠情報が確定していれば行えない
            // シフトに関連する情報が確定済みなら行えない
            case 1:
                ShiftRequest shiftRequest = shiftRequestService.findById(requestJudgmentInput.getRequestId());
                if(Objects.isNull(shiftRequest))
                {
                    return 3;
                }
                Account shiftAccount = shiftRequest.getAccountId();
                AccountApprover shiftAccountApprover = accountApproverService.findAccountAndApprover(shiftAccount, account);
                if(Objects.isNull(shiftAccountApprover))
                {
                    return 3;
                }
                // 申請状態確認
                if(shiftRequest.getRequestStatus() == 2)
                {
                    // 承認状態
                }
                else
                {
                    return 3;
                }
                // 勤怠情報確認
                ShiftListShiftRequest shiftShiftListShiftRequest = shiftListShiftRequestService.findByShiftRequest(shiftRequest);
                Shift shiftShift = shiftShiftListShiftRequest.getShiftId();
                AttendanceListSource shiftAttendanceListSource = attendanceListSourceService.findByShiftId(shiftShift);
                if(Objects.isNull(shiftAttendanceListSource))
                {
                    // nullなら処理を行う
                }
                else
                {
                    // nullでなければ勤怠情報が存在したということなのでエラー
                    return 3;
                }
                // 関連テーブル情報確認
                List<ShiftListVacation> shiftShiftListVacations = shiftListVacationService.findByShiftId(shiftShift);
                List<ShiftListOverTime> shiftShiftListOverTimes = shiftListOverTimeService.findByShiftId(shiftShift);
                List<ShiftListOtherTime> shiftShiftListOtherTimes = shiftListOtherTimeService.findByShiftId(shiftShift);
                if(shiftShiftListVacations.size() > 0 || shiftShiftListOverTimes.size() > 0 || shiftShiftListOtherTimes.size() > 0)
                {
                    // 関連テーブルに情報が存在する場合処理しない
                    return 3;
                }
                // シフトとシフトに関する申請テーブル
                // シフト時間変更申請が存在しないなら削除
                if(Objects.isNull(shiftShiftListShiftRequest.getShiftChangeRequestId()))
                {
                    // シフトの削除
                    shiftService.delete(shiftShift);
                    shiftListShiftRequestService.deleteByShiftListShiftRequest(shiftShiftListShiftRequest);
                }
                else
                {
                    // 存在するならエラー
                    return 3;
                }

                // 申請の状態変更
                shiftRequest.setApprovalTime(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime()));
                shiftRequest.setApproverComment(requestJudgmentInput.getApprovalComment());
                shiftRequest.setRequestStatus(4);
                ShiftRequest resultShiftRequest = shiftRequestService.save(shiftRequest);
                if(Objects.isNull(resultShiftRequest))
                {
                    return 3;
                }
                return 1;
            // シフト時間変更申請
            // 勤怠情報が確定していれば行えない
            case 2:
                ShiftChangeRequest shiftChangeRequest = shiftChangeRequestService.findById(requestJudgmentInput.getRequestId());
                if(Objects.isNull(shiftChangeRequest))
                {
                    return 3;
                }
                // 承認者確認
                Account shiftChangeAccount = shiftChangeRequest.getAccountId();
                AccountApprover shiftChangeAccountApprover = accountApproverService.findAccountAndApprover(shiftChangeAccount, account);
                if(Objects.isNull(shiftChangeAccountApprover))
                {
                    return 3;
                }
                // 申請の状態確認
                if(shiftChangeRequest.getRequestStatus() == 2)
                {
                    // 承認状態
                }
                else
                {
                    return 3;
                }
                // 勤怠情報確認
                Shift shiftChangeShift = shiftChangeRequest.getShiftId();
                AttendanceListSource shiftChangeAttendanceListSource = attendanceListSourceService.findByShiftId(shiftChangeShift);
                if(Objects.isNull(shiftChangeAttendanceListSource))
                {
                    // nullなら処理を行う
                }
                else
                {
                    return 3;
                }
                // 関連テーブル情報確認
                List<ShiftListVacation> shiftChangeShiftListVacations = shiftListVacationService.findByShiftId(shiftChangeShift);
                List<ShiftListOverTime> shiftChangeShiftListOverTimes = shiftListOverTimeService.findByShiftId(shiftChangeShift);
                List<ShiftListOtherTime> shiftChangeShiftListOtherTimes = shiftListOtherTimeService.findByShiftId(shiftChangeShift);
                if(shiftChangeShiftListVacations.size() > 0 || shiftChangeShiftListOverTimes.size() > 0 || shiftChangeShiftListOtherTimes.size() > 0)
                {
                    return 3;
                }
                // シフトとシフトに関する申請テーブル
                // シフトとシフトに関する申請テーブルの時間変更申請にnullを設定
                ShiftListShiftRequest shiftChangeShiftListShiftRequest = shiftListShiftRequestService.findByShiftId(shiftChangeShift);
                shiftChangeShiftListShiftRequest.setShiftChangeRequestId(null);
                ShiftListShiftRequest resultShiftChangeShiftListShiftRequest = shiftListShiftRequestService.save(shiftChangeShiftListShiftRequest);
                if(Objects.isNull(resultShiftChangeShiftListShiftRequest))
                {
                    return 3;
                }
                // 申請の状態変更
                shiftChangeRequest.setApprovalTime(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime()));
                shiftChangeRequest.setApproverComment(requestJudgmentInput.getApprovalComment());
                shiftChangeRequest.setRequestStatus(4);
                ShiftChangeRequest resultShiftChangeRequest = shiftChangeRequestService.save(shiftChangeRequest);
                if(Objects.isNull(resultShiftChangeRequest))
                {
                    return 3;
                }
                return 1;
            // 打刻漏れ申請
            // 勤怠情報が確定していれば行えない
            case 3:
                StampRequest stampRequest = stampRequestService.findById(requestJudgmentInput.getRequestId());
                if(Objects.isNull(stampRequest))
                {
                    return 3;
                }
                // 承認者確認
                Account stampAccount = stampRequest.getAccountId();
                AccountApprover stampAccountApprover = accountApproverService.findAccountAndApprover(stampAccount, account);
                if(Objects.isNull(stampAccountApprover))
                {
                    return 3;
                }
                // 申請の状態確認
                if(stampRequest.getRequestStatus() == 2)
                {
                    
                }
                else
                {
                    return 3;
                }
                // 勤怠と勤怠に関する情報源テーブル
                Shift stampShift = stampRequest.getShiftId();
                AttendanceListSource stampAttendanceListSource = attendanceListSourceService.findByShiftId(stampShift);
                Attend attend = stampAttendanceListSource.getAttendanceId();
                attendanceListSourceService.delete(stampAttendanceListSource);
                // 勤怠と勤怠に関する情報源テーブルを基に勤怠テーブルから削除
                attendService.delete(attend);
                // 申請の状態変更
                stampRequest.setApprovalTime(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime()));
                stampRequest.setApproverComment(requestJudgmentInput.getApprovalComment());
                stampRequest.setRequestStatus(4);
                StampRequest resultStampRequest = stampRequestService.save(stampRequest);
                if(Objects.isNull(resultStampRequest))
                {
                    return 3;
                }
                return 1;
            // 休暇申請
            // 勤怠情報が確定していれば行えない
            case 4:
                VacationRequest vacationRequest = vacationRequestService.findById(requestJudgmentInput.getRequestId());
                if(Objects.isNull(vacationRequest))
                {
                    return 3;
                }
                // 承認者確認
                Account vacationAccount = vacationRequest.getAccountId();
                AccountApprover vacationAccountApprover = accountApproverService.findAccountAndApprover(vacationAccount, account);
                if(Objects.isNull(vacationAccountApprover))
                {
                    return 3;
                }
                // 申請の状態確認
                if(vacationRequest.getRequestStatus() == 2)
                {
                    // 
                }
                else
                {
                    return 3;
                }
                // 勤怠情報確認
                Shift vacationShift = vacationRequest.getShiftId();
                AttendanceListSource vacationAttendanceListSource = attendanceListSourceService.findByShiftId(vacationShift);
                if(Objects.isNull(vacationAttendanceListSource))
                {
                    // 
                }
                else
                {
                    return 3;
                }
                // シフトと休暇テーブル
                ShiftListVacation shiftListVacation = shiftListVacationService.findByVacation(vacationRequest);
                shiftListVacationService.deleteByShiftListVacationId(shiftListVacation);
                // 休暇テーブルから削除
                Vacation vacation = vacationService.findByVacation(vacationRequest);
                vacationService.delete(vacation);
                // 有給申請なら有給休暇消費テーブルから対象を削除
                if(vacationRequest.getVacationTypeId().getVacationTypeId().intValue() == 1)
                {
                    PaydHolidayUse paydHolidayUse = paydHolidayUseService.findByVacationId(vacationRequest);
                    paydHolidayUseService.delete(paydHolidayUse);
                }
                // 申請の状態変更
                vacationRequest.setApprovalTime(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime()));
                vacationRequest.setApproverComment(requestJudgmentInput.getApprovalComment());
                vacationRequest.setRequestStatus(4);
                VacationRequest resultVacationRequest = vacationRequestService.save(vacationRequest);
                if(Objects.isNull(resultVacationRequest))
                {
                    return 3;
                }
                return 1;
            // 残業申請
            // 勤怠情報が確定していれば行えない
            case 5:
                OverTimeRequest overTimeRequest = overTimeRequestService.findById(requestJudgmentInput.getRequestId());
                if(Objects.isNull(overTimeRequest))
                {
                    return 3;
                }
                // 承認者確認
                Account overTimeAccount = overTimeRequest.getAccountId();
                AccountApprover overTimeAccountApprover = accountApproverService.findAccountAndApprover(overTimeAccount, account);
                if(Objects.isNull(overTimeAccountApprover))
                {
                    return 3;
                }
                // 申請の状態確認
                if(overTimeRequest.getRequestStatus() == 2)
                {
                    
                }
                else
                {
                    return 3;
                }
                // 勤怠情報確認
                Shift overTimeShift = overTimeRequest.getShiftId();
                AttendanceListSource overTimeAttendanceListSource = attendanceListSourceService.findByShiftId(overTimeShift);
                if(Objects.isNull(overTimeAttendanceListSource))
                {

                }
                else
                {
                    return 3;
                }
                // シフトと残業テーブル
                ShiftListOverTime shiftListOverTime = shiftListOverTimeService.findByOverTime(overTimeRequest);
                shiftListOverTimeService.deleteByShiftListOverTime(shiftListOverTime);
                // シフトの残業時間
                Time overTime = Time.valueOf(overTimeShift.getOverWork().toLocalTime().minus(Duration.between(overTimeRequest.getBeginWork(), overTimeRequest.getEndWork())));
                overTimeShift.setOverWork(overTime);
                Shift resultOverTimeShift = shiftService.save(overTimeShift);
                if(Objects.isNull(resultOverTimeShift))
                {
                    return 3;
                }
                // 申請の状態変更
                overTimeRequest.setApprovalTime(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime()));
                overTimeRequest.setApproverComment(requestJudgmentInput.getApprovalComment());
                overTimeRequest.setRequestStatus(4);
                OverTimeRequest resultOverTimeRequest = overTimeRequestService.save(overTimeRequest);
                if(Objects.isNull(resultOverTimeRequest))
                {
                    return 3;
                }
                return 1;
            // 遅刻、早退、残業申請
            // 勤怠情報が確定していれば行えない
            case 6:
                AttendanceExceptionRequest attendanceExceptionRequest = attendanceExceptionRequestService.findById(requestJudgmentInput.getRequestId());
                if(Objects.isNull(attendanceExceptionRequest))
                {
                    return 3;
                }
                // 承認者確認
                Account attendaceExceptionAccount = attendanceExceptionRequest.getAccountId();
                AccountApprover attendanceExceptionAccountApprover = accountApproverService.findAccountAndApprover(attendaceExceptionAccount, account);
                if(Objects.isNull(attendanceExceptionAccountApprover))
                {
                    return 3;
                }
                // 申請の状態確認
                if(attendanceExceptionRequest.getRequestStatus() == 2)
                {

                }
                else
                {
                    return 3;
                }
                // 勤怠状態確認
                Shift attendanceExceptionShift = attendanceExceptionRequest.getShiftId();
                AttendanceListSource attendanceExceptionAttendanceListSource = attendanceListSourceService.findByShiftId(attendanceExceptionShift);
                if(Objects.isNull(attendanceExceptionAttendanceListSource))
                {
                    // 
                }
                else
                {
                    return 3;
                }
                // シフトと勤怠例外テーブル
                ShiftListOtherTime shiftListOtherTime = shiftListOtherTimeService.findByOtherTimeId(attendanceExceptionRequest);
                shiftListOtherTimeService.deleteByShiftListOtherTime(shiftListOtherTime);
                // シフトの遅刻時間、早退時間、外出時間
                switch (attendanceExceptionRequest.getAttendanceExceptionTypeId().getAttendanceExceptionTypeId().intValue())
                {
                    // 外出
                    case 1:
                        Time outingTime = Time.valueOf(attendanceExceptionShift.getOuting().toLocalTime().minus(Duration.between(attendanceExceptionRequest.getBeginTime(), attendanceExceptionRequest.getEndTime())));
                        attendanceExceptionShift.setOuting(outingTime);
                        Shift resultOutingAttendanceExceptionShift = shiftService.save(attendanceExceptionShift);
                        if(Objects.isNull(resultOutingAttendanceExceptionShift))
                        {
                            return 3;
                        }
                        break;
                    // 遅刻
                    case 2:
                        Time latenessTime = Time.valueOf(attendanceExceptionShift.getLateness().toLocalTime().minus(Duration.between(attendanceExceptionRequest.getBeginTime(), attendanceExceptionRequest.getEndTime())));
                        attendanceExceptionShift.setLateness(latenessTime);
                        Shift resultLatenessAttendanceExceptionShift = shiftService.save(attendanceExceptionShift);
                        if(Objects.isNull(resultLatenessAttendanceExceptionShift))
                        {
                            return 3;
                        }
                        break;
                    // 早退
                    case 3:
                        Time leaveEarlyTime = Time.valueOf(attendanceExceptionShift.getLeaveEarly().toLocalTime().minus(Duration.between(attendanceExceptionRequest.getBeginTime(), attendanceExceptionRequest.getEndTime())));
                        attendanceExceptionShift.setLeaveEarly(leaveEarlyTime);
                        Shift resultLeaveEarlyAttendanceExceptionShift = shiftService.save(attendanceExceptionShift);
                        if(Objects.isNull(resultLeaveEarlyAttendanceExceptionShift))
                        {
                            return 3;
                        }
                        break;
                    default:
                        return 3;
                }
                // 申請の状態変更
                attendanceExceptionRequest.setApprovalTime(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime()));
                attendanceExceptionRequest.setApproverComment(requestJudgmentInput.getApprovalComment());
                attendanceExceptionRequest.setRequestStatus(4);
                AttendanceExceptionRequest resulAttendanceExceptionRequest = attendanceExceptionRequestService.save(attendanceExceptionRequest);
                if(Objects.isNull(resulAttendanceExceptionRequest))
                {
                    return 3;
                }
                return 1;
            // 月次申請
            case 7:
                MonthlyRequest monthlyRequest = monthlyRequestService.findById(requestJudgmentInput.getRequestId());
                if(Objects.isNull(monthlyRequest))
                {
                    return 3;
                }
                // 承認者確認
                Account monthlyAccount = monthlyRequest.getAccountId();
                AccountApprover monthlyAccountApprover = accountApproverService.findAccountAndApprover(monthlyAccount, account);
                if(Objects.isNull(monthlyAccountApprover))
                {
                    return 3;
                }
                // 申請の状態確認
                if(monthlyRequest.getRequestStatus() == 2)
                {
                    //
                }
                else
                {
                    return 3;
                }
                // 月次申請の範囲の月次申請済みの申請を承認に変更
                // 月次申請の期間内のシフト取得
                List<Shift> shifts = shiftService.findByAccountIdAndBeginWorkBetween(account, monthlyRequest.getYear(), monthlyRequest.getMonth());
                // シフトから関連テーブルを検索
                List<ShiftListOtherTime> shiftListOtherTimes = shiftListOtherTimeService.findByShiftIdIn(shifts);
                List<ShiftListOverTime> shiftListOverTimes = shiftListOverTimeService.findByShiftIdIn(shifts);
                List<ShiftListShiftRequest> shiftListShiftRequests = shiftListShiftRequestService.findByShiftIdIn(shifts);
                List<ShiftListVacation> shiftListVacations = shiftListVacationService.findByShiftIdIn(shifts);
                // 最終的に取得した申請のステータスを月次申請済みから承認に変更
                int monthlyRequestStatus = 2;
                for(ShiftListOtherTime monthlyShiftListOtherTime : shiftListOtherTimes)
                {
                    AttendanceExceptionRequest attendanceExceptionRequestMonthly = monthlyShiftListOtherTime.getAttendanceExceptionId();
                    attendanceExceptionRequestMonthly.setRequestStatus(monthlyRequestStatus);
                    attendanceExceptionRequestService.save(attendanceExceptionRequestMonthly);
                }
                for(ShiftListOverTime monthlyShiftListOverTime : shiftListOverTimes)
                {
                    OverTimeRequest overTimeRequestMonthly = monthlyShiftListOverTime.getOverTimeId();
                    overTimeRequestMonthly.setRequestStatus(monthlyRequestStatus);
                    overTimeRequestService.save(overTimeRequestMonthly);
                }
                for(ShiftListShiftRequest monthlyShiftListShiftRequest : shiftListShiftRequests)
                {
                    // どちらも存在しなければエラー
                    if(Objects.isNull(monthlyShiftListShiftRequest.getShiftChangeRequestId()) && Objects.isNull(monthlyShiftListShiftRequest.getShiftRequestId()))
                    {
                        return 3;
                    }
                    else if(Objects.isNull(monthlyShiftListShiftRequest.getShiftRequestId()))
                    {
                        // シフト申請がなければエラー
                        return 3;
                    }
                    // シフト時間変更申請がなければシフト申請を
                    else if(Objects.isNull(monthlyShiftListShiftRequest.getShiftChangeRequestId()))
                    {
                        ShiftRequest shiftRequestMonthly = monthlyShiftListShiftRequest.getShiftRequestId();
                        shiftRequestMonthly.setRequestStatus(monthlyRequestStatus);
                        shiftRequestService.save(shiftRequestMonthly);
                    }
                    // シフト時間変更申請があればシフト時間申請を
                    else
                    {
                        ShiftChangeRequest shiftChangeRequestMonthly = monthlyShiftListShiftRequest.getShiftChangeRequestId();
                        shiftChangeRequestMonthly.setRequestStatus(monthlyRequestStatus);
                        shiftChangeRequestService.save(shiftChangeRequestMonthly);
                    }
                }
                for(ShiftListVacation monthlyShiftListVacation : shiftListVacations)
                {
                    VacationRequest vacationRequestMonthly = monthlyShiftListVacation.getVacationId();
                    vacationRequestMonthly.setRequestStatus(monthlyRequestStatus);
                    vacationRequestService.save(vacationRequestMonthly);
                }
                // 月次申請の期間内の勤怠情報取得
                List<Attend> attends = attendService.findByAccountIdAndBeginWorkBetween(account, monthlyRequest.getYear(), monthlyRequest.getMonth());
                // 勤怠情報から関連テーブルを検索
                List<AttendanceListSource> attendanceListSources = attendanceListSourceService.findByAttendIdIn(attends);
                // 最終的に取得した申請のステータスを月次申請済みから承認に変更
                for(AttendanceListSource attendanceListSource : attendanceListSources)
                {
                    StampRequest stampRequestMonthly = attendanceListSource.getStampRequestId();
                    stampRequestMonthly.setRequestStatus(monthlyRequestStatus);
                    stampRequestService.save(stampRequestMonthly);
                }
                // 申請の状態変更
                monthlyRequest.setApprovalDate(stringToLocalDateTime.stringToLocalDateTime(requestJudgmentInput.getRequestTime()));
                monthlyRequest.setApproverComment(requestJudgmentInput.getApprovalComment());
                monthlyRequest.setRequestStatus(4);
                MonthlyRequest resultMonthlyRequest = monthlyRequestService.save(monthlyRequest);
                if(Objects.isNull(resultMonthlyRequest))
                {
                    return 3;
                }
                return 1;
            default:
                return 3;
        }
    }

    // 休暇申請含む深夜労働時間
    public Time getLateNight(LocalDateTime beginWork, LocalDateTime endWork, LocalDateTime beginBreak, LocalDateTime endBreak, List<ShiftListVacation> vacations)
    {
        LegalTime legalTime = legalTimeService.findFirstByOrderByBeginDesc();

        // 深夜労働開始の時間を設定
        LocalDateTime beginLateNight = beginWork.withHour(legalTime.getLateNightWorkBegin().toLocalTime().getHour());
        beginLateNight = beginLateNight.withMinute(legalTime.getLateNightWorkBegin().toLocalTime().getMinute());
        beginLateNight = beginLateNight.withSecond(legalTime.getLateNightWorkBegin().toLocalTime().getSecond());

        // 深夜労働終了の時間を設定、シフト開始の翌日の5時のため1日加算してある
        LocalDateTime endLateNight = beginWork.plusDays(1L).withHour(legalTime.getLateNightWorkEnd().toLocalTime().getHour());
        endLateNight = endLateNight.withMinute(legalTime.getLateNightWorkEnd().toLocalTime().getMinute());
        endLateNight = endLateNight.withSecond(legalTime.getLateNightWorkEnd().toLocalTime().getSecond());

        // 深夜労働時間
        long totalSeconds = 0L;

        // 深夜前に労働が終了している場合
        if(!endWork.isAfter(beginLateNight))
        {
            // 深夜前に労働が終了しているため計算しない
        }
        // 労働の開始が深夜の前かつ労働の終了が深夜間である場合(労働の開始が深夜の開始と同じ場合はfalse)
        else if(beginWork.isBefore(beginLateNight) && !endWork.isBefore(beginLateNight) && !endWork.isAfter(endLateNight))
        {
            // 深夜労働の開始から労働の終了までが深夜労働時間のため加算
            totalSeconds += Duration.between(beginLateNight, endWork).toSeconds();
            // 休憩の開始が深夜の前かつ休憩の終了が深夜間である場合(休憩の開始が深夜の開始と同じ場合はfalse)
            if(beginBreak.isBefore(beginLateNight) && !endBreak.isAfter(endLateNight))
            {
                // 深夜労働開始と休憩終了の時間分減算
                totalSeconds -= Duration.between(beginLateNight, endBreak).toSeconds();
                // 休暇計算
                for(ShiftListVacation shiftListVacation : vacations)
                {
                    // 休憩終了前に休暇開始、休憩終了後から労働終了の間に休暇終了の場合
                    if(!shiftListVacation.getVacationId().getBeginVacation().isAfter(endBreak) && !shiftListVacation.getVacationId().getEndVacation().isBefore(endBreak) && !shiftListVacation.getVacationId().getEndVacation().isAfter(endWork))
                    {
                        // 休憩終了から休暇終了の時間分減算
                        totalSeconds -= Duration.between(endBreak, shiftListVacation.getVacationId().getEndVacation()).toSeconds();
                    }
                    // 休暇の開始と終了が休憩終了と労働終了の間の場合
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isBefore(endBreak) && !shiftListVacation.getVacationId().getEndVacation().isAfter(endWork))
                    {
                        // 休暇の開始から終了の時間分減算
                        totalSeconds -= Duration.between(shiftListVacation.getVacationId().getBeginVacation(), shiftListVacation.getVacationId().getEndVacation()).toSeconds();
                    }
                }
            }
            // 休憩の開始と休憩の終了が深夜間である場合(休憩の終了が深夜の終了と同じ場合はtrue)
            else if(!beginBreak.isBefore(beginLateNight) && !endBreak.isBefore(beginLateNight))
            {
                // 休憩も必ず含まれるため減算
                totalSeconds -= Duration.between(beginBreak, endBreak).toSeconds();
                // 休暇計算
                for(ShiftListVacation shiftListVacation : vacations)
                {
                    // 深夜労働開始前に休暇開始、深夜労働開始から休暇開始の間に休暇の終了の場合
                    if(!shiftListVacation.getVacationId().getBeginVacation().isAfter(beginLateNight) && !shiftListVacation.getVacationId().getEndVacation().isAfter(beginLateNight) && !shiftListVacation.getVacationId().getEndVacation().isBefore(beginBreak))
                    {
                        // 深夜労働開始から休暇終了までの時間分減算
                        totalSeconds -= Duration.between(beginLateNight, shiftListVacation.getVacationId().getEndVacation()).toSeconds();
                    }
                    // 深夜労働開始前に休暇開始、休憩の間に休暇の終了の場合
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isBefore(beginLateNight) && !shiftListVacation.getVacationId().getEndVacation().isAfter(beginBreak) && !shiftListVacation.getVacationId().getEndVacation().isBefore(endBreak))
                    {
                        // 深夜労働開始から休憩開始までの時間分減算
                        totalSeconds -= Duration.between(beginLateNight, beginBreak).toSeconds();
                    }
                    // 深夜労働開始前に休暇開始、休憩終了後に休暇の終了の場合
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isBefore(beginLateNight) && !shiftListVacation.getVacationId().getEndVacation().isBefore(endBreak))
                    {
                        // 深夜労働開始から休憩開始までの時間分減算
                        totalSeconds -= Duration.between(beginLateNight, beginBreak).toSeconds();
                        // 休憩終了から休暇終了までの時間分減算
                        totalSeconds -= Duration.between(endBreak, shiftListVacation.getVacationId().getEndVacation()).toSeconds();
                    }
                    // 深夜労働開始から休憩開始の間に休暇の開始と終了の場合
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isBefore(beginLateNight) && !shiftListVacation.getVacationId().getEndVacation().isAfter(beginBreak))
                    {
                        // 休暇の時間分減算
                        totalSeconds -= Duration.between(shiftListVacation.getVacationId().getBeginVacation(), shiftListVacation.getVacationId().getEndVacation()).toSeconds();
                    }
                    // 深夜労働開始から休憩開始の間に休暇の開始、休憩の間に休暇の終了の場合
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isBefore(beginLateNight) && !shiftListVacation.getVacationId().getBeginVacation().isAfter(beginBreak) &&
                            !shiftListVacation.getVacationId().getEndVacation().isBefore(beginBreak) && !shiftListVacation.getVacationId().getEndVacation().isAfter(endBreak))
                    {
                        // 休暇の開始から休憩の開始までの時間分減算
                        totalSeconds -= Duration.between(shiftListVacation.getVacationId().getBeginVacation(), beginBreak).toSeconds();
                    }
                    // 深夜労働開始から休憩開始の間に休暇の開始、休憩終了後に休暇の終了の場合
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isBefore(beginLateNight) && !shiftListVacation.getVacationId().getBeginVacation().isAfter(beginBreak) && !shiftListVacation.getVacationId().getEndVacation().isBefore(endBreak))
                    {
                        // 休暇開始から休憩開始までの時間分減算
                        totalSeconds -= Duration.between(beginLateNight, endLateNight).toSeconds();
                        // 休憩終了から休暇終了までの時間分減算
                        totalSeconds -= Duration.between(beginLateNight, endLateNight).toSeconds();
                    }
                    // 休憩の間に休暇の開始、休憩終了後に休暇の終了の場合
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isAfter(beginBreak) && !shiftListVacation.getVacationId().getBeginVacation().isBefore(endBreak) && !shiftListVacation.getVacationId().getEndVacation().isBefore(endBreak))
                    {
                        // 休憩終了から休暇終了までの時間分減算
                        totalSeconds -= Duration.between(beginLateNight, endLateNight).toSeconds();
                    }
                    // 休憩終了後に休暇の場合
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isBefore(endBreak))
                    {
                        // 休暇の時間分減算
                        totalSeconds -= Duration.between(shiftListVacation.getVacationId().getBeginVacation(), shiftListVacation.getVacationId().getEndVacation()).toSeconds();
                    }
                }
            }
            // 休憩の開始と休憩の終了が深夜の前である場合
            else if(!beginBreak.isAfter(beginLateNight) && !endBreak.isAfter(beginLateNight))
            {
                // 深夜労働に休憩が含まれないため計算しない
                // 休暇計算
                for(ShiftListVacation shiftListVacation : vacations)
                {                    
                    // 深夜労働開始前に休暇の開始、深夜労働間に休暇の終了の場合
                    if(!shiftListVacation.getVacationId().getBeginVacation().isAfter(beginLateNight) && !shiftListVacation.getVacationId().getEndVacation().isBefore(beginLateNight))
                    {
                        // 深夜労働開始から休暇終了までの時間分減算
                        totalSeconds -= Duration.between(beginLateNight, shiftListVacation.getVacationId().getEndVacation()).toSeconds();
                    }
                    // 深夜労働間に休暇の場合
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isBefore(beginLateNight))
                    {
                        // 休暇の時間分減算
                        totalSeconds -= Duration.between(shiftListVacation.getVacationId().getBeginVacation(), shiftListVacation.getVacationId().getEndVacation()).toSeconds();
                    }
                }
            }
        }
        // 労働の開始が深夜間かつ労働の終了が深夜間である場合
        else if(!beginWork.isBefore(beginLateNight) && !endWork.isAfter(endLateNight))
        {
            // 労働の開始から終了までが深夜労働のため加算
            totalSeconds += Duration.between(beginWork, endWork).toSeconds();
            // 休憩も必ず含まれるため減算
            totalSeconds -= Duration.between(beginBreak, endBreak).toSeconds();
            // 休暇の計算
            for(ShiftListVacation shiftListVacation : vacations)
            {
                totalSeconds -= getVacation(shiftListVacation.getVacationId().getBeginVacation(), shiftListVacation.getVacationId().getEndVacation(), shiftListVacation.getShiftId().getBeginBreak(), shiftListVacation.getShiftId().getEndBreak()).toSeconds();
            }
        }
        // 労働の開始が深夜間かつ労働の終了が深夜後である場合(労働の終了が深夜の後と同じ場合はfalse)
        else if(!beginWork.isBefore(beginLateNight) && !beginWork.isAfter(endLateNight) && endWork.isAfter(endLateNight))
        {
            // 労働の開始から深夜労働の終了までが深夜労働時間のため加算
            totalSeconds += Duration.between(beginWork, endLateNight).toSeconds();
            // 休憩の終了が深夜間である場合
            if(!endBreak.isAfter(endLateNight))
            {
                // 休憩も必ず含まれるため減算
                totalSeconds -= Duration.between(beginBreak, endBreak).toSeconds();
                for(ShiftListVacation shiftListVacation : vacations)
                {
                    // 労働の開始から休憩の開始の間に休暇の場合
                    if(!shiftListVacation.getVacationId().getBeginVacation().isBefore(beginWork) && !shiftListVacation.getVacationId().getEndVacation().isAfter(beginBreak))
                    {
                        // 休暇の時間分減算
                        totalSeconds -= Duration.between(shiftListVacation.getVacationId().getBeginVacation(), shiftListVacation.getVacationId().getEndVacation()).toSeconds();
                    }
                    // 労働の開始から休憩の開始の間に休暇の開始、休憩の間に休暇の終了の場合
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isAfter(beginWork) && !shiftListVacation.getVacationId().getBeginVacation().isBefore(beginBreak) &&
                            !shiftListVacation.getVacationId().getEndVacation().isAfter(beginBreak) && !shiftListVacation.getVacationId().getEndVacation().isBefore(endBreak))
                    {
                        // 休暇の開始から休憩の開始の時間分減算
                        totalSeconds -= Duration.between(shiftListVacation.getVacationId().getBeginVacation(), beginBreak).toSeconds();
                    }
                    // 労働の開始から休憩の開始の間に休暇の開始、休憩の終了から深夜労働の終了の間に休暇の終了の場合
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isAfter(beginWork) && !shiftListVacation.getVacationId().getBeginVacation().isBefore(beginBreak) &&
                            !shiftListVacation.getVacationId().getEndVacation().isAfter(endBreak) && !shiftListVacation.getVacationId().getEndVacation().isBefore(endLateNight))
                    {
                        // 休暇の開始から休憩の開始までの時間分減算
                        totalSeconds -= Duration.between(shiftListVacation.getVacationId().getBeginVacation(), beginBreak).toSeconds();
                        // 休憩の終了から休暇の終了までの時間分減算
                        totalSeconds -= Duration.between(endBreak, shiftListVacation.getVacationId().getEndVacation()).toSeconds();
                    }
                    // 労働の開始から休憩の開始の間に休暇の開始、深夜労働の終了から労働の終了の間に休暇の終了の場合
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isAfter(beginWork) && !shiftListVacation.getVacationId().getBeginVacation().isBefore(beginBreak) &&
                            !shiftListVacation.getVacationId().getEndVacation().isAfter(endLateNight) && !shiftListVacation.getVacationId().getEndVacation().isBefore(endWork))
                    {
                        // 休暇の開始から休憩の開始までの時間分減算
                        totalSeconds -= Duration.between(shiftListVacation.getVacationId().getBeginVacation(), beginBreak).toSeconds();
                        // 休憩の終了から深夜労働の終了までの時間分減算
                        totalSeconds -= Duration.between(shiftListVacation.getVacationId().getBeginVacation(), endLateNight).toSeconds();
                    }
                    // 休憩の間に休暇の開始、休憩の終了から深夜労働の終了の間に休暇の終了
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isAfter(beginBreak) && !shiftListVacation.getVacationId().getBeginVacation().isBefore(endBreak) &&
                            !shiftListVacation.getVacationId().getEndVacation().isAfter(endBreak) && !shiftListVacation.getVacationId().getEndVacation().isBefore(endLateNight))
                    {
                        // 休憩の終了から休暇の終了までの時間分減算
                        totalSeconds -= Duration.between(endBreak, shiftListVacation.getVacationId().getEndVacation()).toSeconds();
                    }
                    // 休憩の間に休暇の開始、深夜労働終了後に休暇の終了
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isAfter(beginBreak) && !shiftListVacation.getVacationId().getBeginVacation().isBefore(endBreak) && !shiftListVacation.getVacationId().getEndVacation().isBefore(endLateNight))
                    {
                        // 休憩の終了から深夜労働終了までの時間分減算
                        totalSeconds -= Duration.between(shiftListVacation.getVacationId().getEndVacation(), endLateNight).toSeconds();
                    }
                    // 休憩の終了から深夜労働終了の間に休暇の場合
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isAfter(endBreak) && !shiftListVacation.getVacationId().getEndVacation().isBefore(endLateNight))
                    {
                        // 休暇の時間分減算
                        totalSeconds -= Duration.between(shiftListVacation.getVacationId().getBeginVacation(), shiftListVacation.getVacationId().getEndVacation()).toSeconds();
                    }
                    // 休憩の終了から深夜労働終了の間に休暇の開始、深夜労働終了後に休暇の終了
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isBefore(endBreak) && !shiftListVacation.getVacationId().getBeginVacation().isAfter(endLateNight) && !shiftListVacation.getVacationId().getEndVacation().isBefore(endLateNight))
                    {
                        // 休暇の開始から深夜労働終了までの時間分減算
                        totalSeconds -= Duration.between(shiftListVacation.getVacationId().getBeginVacation(), endLateNight).toSeconds();
                    }
                }
            }
            // 休憩の開始が深夜間かつ休憩の終了が深夜の後の場合(休憩の終了が深夜の後と同じ場合はfalse)
            else if(!beginBreak.isAfter(endLateNight) && endBreak.isAfter(endLateNight))
            {
                // 休憩開始と深夜労働終了の時間分減算
                totalSeconds -= Duration.between(beginBreak, endLateNight).toSeconds();
                for(ShiftListVacation shiftListVacation : vacations)
                {
                    // 労働の開始と休憩の開始の間に休暇
                    if(!shiftListVacation.getVacationId().getBeginVacation().isBefore(beginWork) && !shiftListVacation.getVacationId().getEndVacation().isAfter(beginBreak))
                    {
                        // 休暇の時間分減算
                        totalSeconds -= Duration.between(shiftListVacation.getVacationId().getBeginVacation(), shiftListVacation.getVacationId().getEndVacation()).toSeconds();
                    }
                    // 労働の開始と休憩の開始の間に休暇の開始、休憩の開始と深夜労働の終了の間に休暇の終了
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isBefore(beginWork) && !shiftListVacation.getVacationId().getBeginVacation().isAfter(beginBreak) &&
                            !shiftListVacation.getVacationId().getEndVacation().isBefore(beginBreak) && !shiftListVacation.getVacationId().getEndVacation().isAfter(endLateNight))
                    {
                        // 休憩の開始から休暇の終了までの時間分減算
                        totalSeconds -= Duration.between(beginBreak, shiftListVacation.getVacationId().getEndVacation()).toSeconds();
                    }
                    // 労働の開始と休憩の開始の間に休暇の開始、深夜労働の終了後に休暇の終了
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isBefore(beginWork) && !shiftListVacation.getVacationId().getBeginVacation().isAfter(beginBreak) && !shiftListVacation.getVacationId().getEndVacation().isBefore(endLateNight))
                    {
                        // 休憩の開始から休暇の終了までの時間分減算
                        totalSeconds -= Duration.between(beginBreak, shiftListVacation.getVacationId().getEndVacation()).toSeconds();
                    }
                }
            }
            // 休憩の開始と休憩の終了が深夜の後である場合
            else if(!beginBreak.isBefore(endLateNight) && !endBreak.isBefore(endLateNight))
            {
                // 深夜労働に休憩が含まれないため計算しない
                for(ShiftListVacation shiftListVacation : vacations)
                {
                    // 労働開始と深夜労働終了の間に休暇の場合
                    if(!shiftListVacation.getVacationId().getBeginVacation().isBefore(beginWork) && !shiftListVacation.getVacationId().getEndVacation().isAfter(endLateNight))
                    {
                        // 休暇の時間分減算
                        totalSeconds -= Duration.between(shiftListVacation.getVacationId().getBeginVacation(), shiftListVacation.getVacationId().getEndVacation()).toSeconds();
                    }
                    // 労働開始と深夜労働終了の間に休暇の開始、深夜労働終了後に休暇の終了の場合
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isBefore(beginWork) && !shiftListVacation.getVacationId().getBeginVacation().isAfter(endLateNight))
                    {
                        // 休暇の開始から深夜労働の終了までの時間分減算
                        totalSeconds -= Duration.between(shiftListVacation.getVacationId().getBeginVacation(), endLateNight).toSeconds();
                    }
                }
            }
        }
        // 労働の開始が深夜の前、労働の終了が深夜の後である場合
        else if(!beginWork.isAfter(beginLateNight) && !endWork.isBefore(endLateNight))
        {
            // 深夜労働の開始から終了までが深夜労働時間
            totalSeconds += Duration.between(beginLateNight, endLateNight).toSeconds();
            // 深夜労働の開始前に休憩が終了している場合
            if(!beginBreak.isAfter(beginLateNight))
            {
                // 計算しない
                for(ShiftListVacation shiftListVacation : vacations)
                {
                    // 深夜労働の開始前に休暇の開始、深夜労働の間に休暇の終了
                    if(!shiftListVacation.getVacationId().getBeginVacation().isAfter(beginLateNight) && !shiftListVacation.getVacationId().getEndVacation().isBefore(beginLateNight) && !shiftListVacation.getVacationId().getEndVacation().isAfter(endLateNight))
                    {
                        // 深夜労働の開始から休暇の終了までの時間分減算
                        totalSeconds -= Duration.between(beginLateNight, shiftListVacation.getVacationId().getEndVacation()).toSeconds();
                    }
                    // 深夜労働の開始前に休暇の開始、深夜労働の後に休暇の終了
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isAfter(beginLateNight) && !shiftListVacation.getVacationId().getEndVacation().isBefore(endLateNight))
                    {
                        // 深夜労働時間丸ごと休暇のため0にする
                        totalSeconds = 0L;
                    }
                    // 深夜労働の間に休暇
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isBefore(beginLateNight) && !shiftListVacation.getVacationId().getEndVacation().isAfter(endLateNight))
                    {
                        // 休暇時間分減算
                        totalSeconds -= Duration.between(shiftListVacation.getVacationId().getBeginVacation(), shiftListVacation.getVacationId().getEndVacation()).toSeconds();
                    }
                    // 深夜労働の間に休暇の開始、深夜労働の後に休暇の終了
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isBefore(beginLateNight) && !shiftListVacation.getVacationId().getBeginVacation().isAfter(endLateNight) && !shiftListVacation.getVacationId().getEndVacation().isBefore(endLateNight))
                    {
                        // 休暇の開始から深夜労働の終了までの時間分減算
                        totalSeconds -= Duration.between(beginLateNight, endLateNight).toSeconds();
                    }
                }
            }
            // 深夜労働の開始前に休憩が開始され深夜間に休憩が終了した場合
            else if(!beginWork.isAfter(beginLateNight) && !endWork.isBefore(beginLateNight) &&!endWork.isAfter(endLateNight))
            {
                // 深夜の開始から休憩の終了まで減算
                totalSeconds -= Duration.between(beginLateNight, endBreak).toSeconds();
                for(ShiftListVacation shiftListVacation : vacations)
                {
                    // 休憩の終了前に休暇の開始、休憩の終了後から深夜労働の終了の間に休暇の終了の場合
                    if(!shiftListVacation.getVacationId().getBeginVacation().isAfter(endBreak) && !shiftListVacation.getVacationId().getEndVacation().isBefore(endBreak) && !shiftListVacation.getVacationId().getEndVacation().isAfter(endLateNight))
                    {
                        // 休憩の終了から休暇の終了までの時間分減算
                        totalSeconds -= Duration.between(endBreak, shiftListVacation.getVacationId().getEndVacation()).toSeconds();
                    }
                    // 休憩の終了前に休暇の開始、深夜労働の終了後に休暇の終了の場合
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isAfter(endBreak) && !shiftListVacation.getVacationId().getEndVacation().isBefore(endLateNight))
                    {
                        // 休暇の時間分減算
                        totalSeconds -= Duration.between(shiftListVacation.getVacationId().getBeginVacation(), shiftListVacation.getVacationId().getEndVacation()).toSeconds();
                    }
                    // 休憩の終了後から深夜労働の終了の間に休暇の場合
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isBefore(endBreak) && !shiftListVacation.getVacationId().getEndVacation().isAfter(endLateNight))
                    {
                        // 休暇の時間分減算
                        totalSeconds -= Duration.between(shiftListVacation.getVacationId().getBeginVacation(), shiftListVacation.getVacationId().getEndVacation()).toSeconds();
                    }
                    // 休憩の終了後から深夜労働の終了の間に休暇の開始、深夜労働の終了後に休暇の終了の場合
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isBefore(endBreak) && !shiftListVacation.getVacationId().getBeginVacation().isAfter(endLateNight) && !shiftListVacation.getVacationId().getEndVacation().isBefore(endLateNight))
                    {
                        // 休暇の開始から深夜労働の終了までの時間分減算
                        totalSeconds -= Duration.between(shiftListVacation.getVacationId().getBeginVacation(), endLateNight).toSeconds();
                    }
                }
            }
            // 休憩の開始と終了が深夜間の場合
            else if(!beginBreak.isAfter(beginLateNight) && !endBreak.isBefore(endLateNight))
            {
                // 休憩時間分減算
                totalSeconds -= Duration.between(beginBreak, endBreak).toSeconds();
                for(ShiftListVacation shiftListVacation : vacations)
                {
                    // 深夜労働の開始前に休暇開始、深夜労働の開始から休憩の開始までに休暇の終了の場合
                    if(!shiftListVacation.getVacationId().getBeginVacation().isAfter(beginLateNight) && !shiftListVacation.getVacationId().getEndVacation().isBefore(beginLateNight) && !shiftListVacation.getVacationId().getEndVacation().isAfter(beginBreak))
                    {
                        // 深夜労働の開始から休暇の終了までの時間分減算
                        totalSeconds -= Duration.between(beginLateNight, shiftListVacation.getVacationId().getEndVacation()).toSeconds();
                    }
                    // 深夜労働の開始前に休暇開始、休憩の間に休暇の終了の場合
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isAfter(beginLateNight) && !shiftListVacation.getVacationId().getEndVacation().isBefore(beginBreak) && !shiftListVacation.getVacationId().getEndVacation().isAfter(endBreak))
                    {
                        // 深夜労働の開始から休憩の開始までの時間分減算
                        totalSeconds -= Duration.between(beginLateNight, beginBreak).toSeconds();
                    }
                    // 深夜労働の開始前に休暇開始、休憩終了後から深夜労働の終了までに休暇の終了の場合
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isAfter(beginLateNight) && !shiftListVacation.getVacationId().getEndVacation().isBefore(endBreak) && !shiftListVacation.getVacationId().getEndVacation().isAfter(endLateNight))
                    {
                        // 深夜労働の開始から休憩開始までの時間分減算
                        totalSeconds -= Duration.between(beginLateNight, beginBreak).toSeconds();
                        // 休憩終了から休暇の終了までの時間分減算
                        totalSeconds -= Duration.between(endBreak, shiftListVacation.getVacationId().getEndVacation()).toSeconds();
                    }
                    // 深夜労働の開始前に休暇開始、深夜労働後に休暇終了
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isAfter(beginLateNight) && !shiftListVacation.getVacationId().getEndVacation().isBefore(endLateNight))
                    {
                        // 深夜労働の開始から休憩開始までの時間分減算
                        totalSeconds -= Duration.between(beginLateNight, beginBreak).toSeconds();
                        // 休憩終了から深夜労働の終了までの時間分減算
                        totalSeconds -= Duration.between(endLateNight, endBreak).toSeconds();
                    }
                    // 深夜労働の開始から休憩の開始までに休暇の場合
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isBefore(beginLateNight) && !shiftListVacation.getVacationId().getEndVacation().isAfter(beginBreak))
                    {
                        // 休暇の時間分減算
                        totalSeconds -= Duration.between(shiftListVacation.getVacationId().getBeginVacation(), shiftListVacation.getVacationId().getEndVacation()).toSeconds();
                    }
                    // 深夜労働の開始から休憩の開始までに休暇の開始、休憩の間に休暇の終了の場合
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isBefore(beginLateNight) && !shiftListVacation.getVacationId().getBeginVacation().isAfter(beginBreak) &&
                            !shiftListVacation.getVacationId().getEndVacation().isBefore(beginBreak) && !shiftListVacation.getVacationId().getEndVacation().isAfter(endBreak))
                    {
                        // 休暇の開始から休憩の開始までの時間分減算
                        totalSeconds -= Duration.between(shiftListVacation.getVacationId().getBeginVacation(), endLateNight).toSeconds();
                    }
                    // 深夜労働の開始から休憩の開始までに休暇の開始、休憩終了後から深夜労働終了までに休暇の終了の場合
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isBefore(beginLateNight) && !shiftListVacation.getVacationId().getBeginVacation().isAfter(beginBreak) &&
                            !shiftListVacation.getVacationId().getEndVacation().isBefore(endBreak) && !shiftListVacation.getVacationId().getEndVacation().isAfter(endLateNight))
                    {
                        // 休暇の開始から休憩の開始までの時間分減算
                        totalSeconds -= Duration.between(shiftListVacation.getVacationId().getBeginVacation(), endLateNight).toSeconds();
                        // 休憩の終了から休暇の終了までの時間分減算
                        totalSeconds -= Duration.between(endBreak, shiftListVacation.getVacationId().getEndVacation()).toSeconds();
                    }
                    // 深夜労働の開始から休憩の開始までに休暇の開始、深夜労働終了後に休暇の終了の場合
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isBefore(beginLateNight) && !shiftListVacation.getVacationId().getBeginVacation().isAfter(beginBreak) && !shiftListVacation.getVacationId().getEndVacation().isBefore(endLateNight))
                    {
                        // 休暇の開始から休憩の開始までの時間分減算
                        totalSeconds -= Duration.between(shiftListVacation.getVacationId().getBeginVacation(), endBreak).toSeconds();
                        // 休憩の終了から深夜労働の終了までの時間分減算
                        totalSeconds -= Duration.between(endBreak, endLateNight).toSeconds();
                    }
                    // 休憩の間に休暇の開始、休憩の終了から深夜労働の終了までに休暇の終了の場合
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isBefore(beginBreak) && !shiftListVacation.getVacationId().getBeginVacation().isAfter(endBreak) &&
                            !shiftListVacation.getVacationId().getEndVacation().isBefore(endBreak) && !shiftListVacation.getCreatedDate().isAfter(endLateNight))
                    {
                        // 休憩の終了から休暇の終了までの時間分減算
                        totalSeconds -= Duration.between(endBreak, shiftListVacation.getVacationId().getEndVacation()).toSeconds();
                    }
                    // 休憩の間に休暇の開始、深夜労働の終了後休暇の終了の場合
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isBefore(beginBreak) && !shiftListVacation.getVacationId().getBeginVacation().isAfter(endBreak) &&
                            !shiftListVacation.getVacationId().getEndVacation().isBefore(endLateNight))
                    {
                        // 休憩終了から深夜労働の終了までの時間分減算
                        totalSeconds -= Duration.between(endBreak, endLateNight).toSeconds();
                    }
                    // 休憩の終了から深夜労働の終了までに休暇の場合
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isBefore(endBreak) && !shiftListVacation.getVacationId().getEndVacation().isAfter(endLateNight))
                    {
                        // 休暇の時間分減算
                        totalSeconds -= Duration.between(shiftListVacation.getVacationId().getBeginVacation(), shiftListVacation.getVacationId().getEndVacation()).toSeconds();
                    }
                    // 休憩の終了から深夜労働の終了までに休暇の開始、深夜労働の終了後に休暇の終了
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isBefore(endBreak) && !shiftListVacation.getVacationId().getBeginVacation().isAfter(endLateNight) && !shiftListVacation.getVacationId().getEndVacation().isBefore(endLateNight))
                    {
                        // 休暇の開始から深夜労働の終了までの時間分減算
                        totalSeconds -= Duration.between(beginLateNight, endLateNight).toSeconds();
                    }
                }
            }
            // 休憩の開始が深夜間かつ休憩の終了が深夜労働終了後の場合
            else if(!beginBreak.isBefore(beginLateNight) && !beginBreak.isAfter(endLateNight) && !endBreak.isBefore(endLateNight))
            {
                // 休憩の開始から深夜の終了まで減算
                totalSeconds -= Duration.between(beginBreak, endLateNight).toSeconds();
                for(ShiftListVacation shiftListVacation : vacations)
                {
                    // 深夜労働開始前に休暇の開始、深夜労働開始から休憩の開始までの間に休暇の終了の場合
                    if(!shiftListVacation.getVacationId().getBeginVacation().isAfter(beginLateNight) && !shiftListVacation.getVacationId().getEndVacation().isBefore(beginLateNight) && !shiftListVacation.getVacationId().getEndVacation().isAfter(beginBreak))
                    {
                        // 深夜労働開始から休暇の終了までの時間分減算
                        totalSeconds -= Duration.between(beginLateNight, shiftListVacation.getVacationId().getEndVacation()).toSeconds();
                    }
                    // 深夜労働開始前に休暇の開始、休憩開始後休暇の終了の場合
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isAfter(beginLateNight) && !shiftListVacation.getVacationId().getEndVacation().isBefore(beginBreak))
                    {
                        // 深夜労働の開始から休憩開始までの時間分減算
                        totalSeconds -= Duration.between(beginLateNight, endLateNight).toSeconds();
                    }
                    // 深夜労働開始から休憩開始までに休暇の場合
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isBefore(beginLateNight) && !shiftListVacation.getVacationId().getEndVacation().isAfter(beginBreak))
                    {
                        // 休暇の時間分減算
                        totalSeconds -= Duration.between(shiftListVacation.getVacationId().getBeginVacation(), shiftListVacation.getVacationId().getEndVacation()).toSeconds();
                    }
                    // 深夜労働開始から休憩開始までに休暇の開始、休憩開始後休暇の終了の場合
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isBefore(beginLateNight) && !shiftListVacation.getVacationId().getBeginVacation().isAfter(beginBreak) && !shiftListVacation.getVacationId().getEndVacation().isBefore(beginBreak))
                    {
                        // 休暇の開始から休憩開始までの時間分減算
                        totalSeconds -= Duration.between(shiftListVacation.getVacationId().getBeginVacation(), beginBreak).toSeconds();
                    }
                }
            }
            // 深夜労働の終了後に休憩が開始している場合
            else if(!endBreak.isBefore(endLateNight))
            {
                // 計算しない
                for(ShiftListVacation shiftListVacation : vacations)
                {
                    // 深夜労働の開始前に休暇の開始、深夜労働の間に休暇の終了
                    if(!shiftListVacation.getVacationId().getBeginVacation().isAfter(beginLateNight) && !shiftListVacation.getVacationId().getEndVacation().isBefore(beginLateNight) && !shiftListVacation.getVacationId().getEndVacation().isAfter(endLateNight))
                    {
                        // 深夜労働の開始から休暇の終了までの時間分減算
                        totalSeconds -= Duration.between(beginLateNight, shiftListVacation.getVacationId().getEndVacation()).toSeconds();
                    }
                    // 深夜労働の開始前に休暇の開始、深夜労働の後に休暇の終了
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isAfter(beginLateNight) && !shiftListVacation.getVacationId().getEndVacation().isBefore(endLateNight))
                    {
                        // 深夜労働時間丸ごと休暇のため0にする
                        totalSeconds = 0L;
                    }
                    // 深夜労働の間に休暇
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isBefore(beginLateNight) && !shiftListVacation.getVacationId().getEndVacation().isAfter(endLateNight))
                    {
                        // 休暇時間分減算
                        totalSeconds -= Duration.between(shiftListVacation.getVacationId().getBeginVacation(), shiftListVacation.getVacationId().getEndVacation()).toSeconds();
                    }
                    // 深夜労働の間に休暇の開始、深夜労働の後に休暇の終了
                    else if(!shiftListVacation.getVacationId().getBeginVacation().isBefore(beginLateNight) && !shiftListVacation.getVacationId().getBeginVacation().isAfter(endLateNight) && !shiftListVacation.getVacationId().getEndVacation().isBefore(endLateNight))
                    {
                        // 休暇の開始から深夜労働の終了までの時間分減算
                        totalSeconds -= Duration.between(beginLateNight, endLateNight).toSeconds();
                    }
                }
            }
            // 休憩の間に深夜労働が含まれる場合計算しない
        }

        LocalTime result = LocalTime.MIDNIGHT.plusSeconds(totalSeconds);
        return Time.valueOf(result);
    }

    public Duration getVacation(LocalDateTime beginVacation, LocalDateTime endVacation, LocalDateTime beginBreak, LocalDateTime endBreak)
    {
        Duration totalMinutes = Duration.ZERO;

        // 休暇が休憩の完全に外側(前または後)にある場合
        if (endVacation.isBefore(beginBreak) || beginVacation.isAfter(endBreak))
        {
            // 休憩とは一切重ならないため、休暇時間をそのまま加算
            totalMinutes = Duration.between(beginVacation, endVacation);
        }

        // 休暇が休憩を完全に含んでいる場合(休暇の開始と休憩の開始が等しく休暇の終了と休憩の終了が等しい場合計算が起きるが0が減算されるだけなので許容)
        else if (!beginVacation.isAfter(beginBreak) && !endVacation.isBefore(endBreak))
        {
            // 休暇全体から休憩時間を減算
            totalMinutes = Duration.between(beginVacation, endVacation).minus(Duration.between(beginBreak, endBreak));
        }

        // 休暇が完全に休憩の中に収まっている場合
        else if (!beginVacation.isBefore(beginBreak) && !endVacation.isAfter(endBreak))
        {
            // 休憩中の休暇なのでカウントしない
        }

        // 休暇の開始が休憩中、終了は休憩後の場合
        else if (!beginVacation.isBefore(beginBreak) && beginVacation.isBefore(endBreak))
        {
            // 休暇のうち、休憩後の部分のみを加算
            totalMinutes = Duration.between(endBreak, endVacation);
        }

        // 休暇の開始は休憩前、終了が休憩中の場合
        else if (beginVacation.isBefore(beginBreak) && !endVacation.isAfter(endBreak))
        {
            // 休暇のうち、休憩前の部分のみを加算
            totalMinutes = Duration.between(beginVacation, beginBreak);
        }

        return totalMinutes;
    }

}
