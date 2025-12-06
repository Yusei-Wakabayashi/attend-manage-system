package com.example.springboot.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.springboot.dto.change.DurationToString;
import com.example.springboot.dto.change.StringToDuration;
import com.example.springboot.dto.change.StringToLocalDateTime;
import com.example.springboot.dto.input.MonthlyInput;
import com.example.springboot.dto.input.OtherTimeInput;
import com.example.springboot.dto.input.OverTimeInput;
import com.example.springboot.dto.input.ShiftChangeInput;
import com.example.springboot.dto.input.ShiftInput;
import com.example.springboot.dto.input.StampInput;
import com.example.springboot.dto.input.VacationInput;
import com.example.springboot.dto.response.RequestDetailShiftChangeResponse;
import com.example.springboot.dto.response.RequestDetailShiftResponse;
import com.example.springboot.dto.response.RequestDetailStampResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.Attend;
import com.example.springboot.model.AttendanceExceptionRequest;
import com.example.springboot.model.AttendanceListSource;
import com.example.springboot.model.LegalTime;
import com.example.springboot.model.MonthlyRequest;
import com.example.springboot.model.NewsList;
import com.example.springboot.model.OverTimeRequest;
import com.example.springboot.model.PaydHoliday;
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
        LegalTimeService legalTimeService
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
                if ((startTime.isAfter(shift.getBeginWork()) || startTime.isEqual(shift.getBeginWork())) &&
                (endTime.isBefore(shift.getEndWork()) || endTime.isEqual(shift.getEndWork())))
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

    public RequestDetailStampResponse getStampDetail(Account account, Long requestid)
    {
        StampRequest stampRequest = stampRequestService.findByAccountIdAndStampId(account, requestid);
        return stampRequestService.mapToDetailResponse(stampRequest);
    }

}
