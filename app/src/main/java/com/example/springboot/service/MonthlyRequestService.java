package com.example.springboot.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.dto.change.DurationToString;
import com.example.springboot.dto.change.StringToLocalDateTime;
import com.example.springboot.dto.input.MonthlyInput;
import com.example.springboot.model.Account;
import com.example.springboot.model.Attend;
import com.example.springboot.model.AttendanceExceptionRequest;
import com.example.springboot.model.AttendanceListSource;
import com.example.springboot.model.MonthlyRequest;
import com.example.springboot.model.NewsList;
import com.example.springboot.model.OverTimeRequest;
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
import com.example.springboot.repository.MonthlyRequestRepository;

@Service
public class MonthlyRequestService
{
    private final MonthlyRequestRepository monthlyRequestRepository;
    private final ShiftRequestService shiftRequestService;
    private final ShiftChangeRequestService shiftChangeRequestService;
    private final StampRequestService stampRequestService;
    private final VacationRequestService vacationRequestService;
    private final AttendanceExceptionRequestService attendanceExceptionRequestService;
    private final OverTimeRequestService overTimeRequestService;
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

    @Autowired
    public MonthlyRequestService
    (
        MonthlyRequestRepository monthlyRequestRepository,
        ShiftRequestService shiftRequestService,
        ShiftChangeRequestService shiftChangeRequestService,
        StampRequestService stampRequestService,
        VacationRequestService vacationRequestService,
        AttendanceExceptionRequestService attendanceExceptionRequestService,
        OverTimeRequestService overTimeRequestService,
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
        StringToLocalDateTime stringToLocalDateTime
    )
    {
        this.monthlyRequestRepository = monthlyRequestRepository;
        this.shiftRequestService = shiftRequestService;
        this.shiftChangeRequestService = shiftChangeRequestService;
        this.stampRequestService = stampRequestService;
        this.vacationRequestService = vacationRequestService;
        this.attendanceExceptionRequestService = attendanceExceptionRequestService;
        this.overTimeRequestService = overTimeRequestService;
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
            // シフト時間変更申請がなければシフト申請を
            if(Objects.isNull(shiftListShiftRequest.getShiftChangeRequestId()))
            {
                ShiftRequest shiftRequest = shiftListShiftRequest.getShiftRequestId();
                shiftRequest.setRequestStatus(monthlyRequestStatus);
                shiftRequestService.save(shiftRequest);
            }
            // シフト申請がなければシフト時間変更申請を
            else if(Objects.isNull(shiftListShiftRequest.getShiftRequestId()))
            {
                ShiftChangeRequest shiftChangeRequest = shiftListShiftRequest.getShiftChangeRequestId();
                shiftChangeRequest.setRequestStatus(monthlyRequestStatus);
                shiftChangeRequestService.save(shiftChangeRequest);
            }
            // どちらも存在すればエラー
            else
            {
                return 3;
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
        MonthlyRequest resultMonthlyRequest = save(monthlyRequest);
        if(Objects.isNull(resultMonthlyRequest) || Objects.isNull(resultMonthlyRequest.getMonthRequestId()))
        {
            return 3;
        }
        return 1;
    }

    public MonthlyRequest findById(Long id)
    {
        return monthlyRequestRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("月次申請が見つかりません"));
    }

    public MonthlyRequest findByAccountIdAndMothlyRequestId(Long accountId, Long id)
    {
        Account account = new Account();
        account.setId(accountId);
        return monthlyRequestRepository.findByAccountIdAndMonthRequestId(account, id)
            .orElseThrow(() -> new RuntimeException("月次申請が見つかりません"));
    }

    public MonthlyRequest findByAccountIdAndMothlyRequestId(Account account, Long id)
    {
        return monthlyRequestRepository.findByAccountIdAndMonthRequestId(account, id)
            .orElseThrow(() -> new RuntimeException("月次申請が見つかりません"));
    }

    public List<MonthlyRequest> findByAccountId(Long accountId)
    {
        Account account = new Account();
        account.setId(accountId);
        return monthlyRequestRepository.findByAccountId(account);
    }

    public List<MonthlyRequest> findByAccountIdAndYearAndMonth(Long accountId, int year, int month)
    {
        Account account = new Account();
        account.setId(accountId);
        List<MonthlyRequest> monthlyRequests = monthlyRequestRepository.findByAccountIdAndYearAndMonth(account, year, month);
        return monthlyRequests;
    }

    public List<MonthlyRequest> findByAccountIdAndYearAndMonth(Account account, int year, int month)
    {
        List<MonthlyRequest> monthlyRequests = monthlyRequestRepository.findByAccountIdAndYearAndMonth(account, year, month);
        return monthlyRequests;
    }

    public List<MonthlyRequest> findByAccountId(Account account)
    {
        return monthlyRequestRepository.findByAccountId(account);
    }

    public List<MonthlyRequest> findByAccountIdIn(List<Account> accounts)
    {
        List<MonthlyRequest> monthlyRequests = monthlyRequestRepository.findByAccountIdIn(accounts);
        return monthlyRequests;
    }

    public List<MonthlyRequest> findAll()
    {
        return monthlyRequestRepository.findAll();
    }

    public MonthlyRequest save(MonthlyRequest monthlyRequest)
    {
        return monthlyRequestRepository.save(monthlyRequest);
    }
}
