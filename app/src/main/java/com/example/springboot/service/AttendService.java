package com.example.springboot.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.dto.change.DurationToString;
import com.example.springboot.dto.change.LocalDateTimeToString;
import com.example.springboot.dto.input.UserAttendInput;
import com.example.springboot.dto.input.UserMonthWorkInfoInput;
import com.example.springboot.dto.input.YearMonthInput;
import com.example.springboot.dto.response.AttendListResponse;
import com.example.springboot.dto.response.MonthWorkInfoResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.AccountApprover;
import com.example.springboot.model.Attend;
import com.example.springboot.model.Vacation;
import com.example.springboot.repository.AttendRepository;

@Service
public class AttendService
{
    private final AttendRepository attendRepository;
    private final LocalDateTimeToString localDateTimeToString;
    private final VacationService vacationService;
    private final DurationToString durationToString;

    @Autowired
    public AttendService
    (
        AttendRepository attendRepository,
        LocalDateTimeToString localDateTimeToString,
        VacationService vacationService,
        DurationToString durationToString
    )
    {
        this.attendRepository = attendRepository;
        this.localDateTimeToString = localDateTimeToString;
        this.vacationService = vacationService;
        this.durationToString = durationToString;
    }

    public Attend findAttendById(Long id)
    {
        return attendRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("出勤簿が見つかりません"));
    }

    // 一月ごとの取得
    public List<Attend> findByAccountIdAndBeginWorkBetween(Long id, int year, int month)
    {
        Account accountId = new Account();
        accountId.setId(id);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        List<Attend> attendList = attendRepository.findByAccountIdAndBeginWorkBetween(accountId, startPeriod, endPeriod);
        return attendList;
    }

    public List<Attend> findByAccountIdAndBeginWorkBetween(Account id, int year, int month)
    {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        List<Attend> attendList = attendRepository.findByAccountIdAndBeginWorkBetween(id, startPeriod, endPeriod);
        return attendList;
    }

    public List<AttendListResponse> findAttendListFor(Account account, YearMonthInput request)
    {
        List<AttendListResponse> attendListResponses = new ArrayList<AttendListResponse>();
        List<Attend> attendList = findByAccountIdAndBeginWorkBetween(account.getId(), request.getYear(), request.getMonth());
        for(Attend attend : attendList)
        {
            attendListResponses.add(attendToAttendListResponse(attend));
        }
        return attendListResponses;
    }

    public MonthWorkInfoResponse monthWorkInfoResponse(Account account, YearMonthInput request)
    {
        MonthWorkInfoResponse monthWorkInfoResponse = new MonthWorkInfoResponse();
        int status = 0;
        if(Objects.isNull(account))
        {
            status = 4;
            monthWorkInfoResponse.setStatus(status);
            return monthWorkInfoResponse;
        }
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
        List<Attend> attends = findByAccountIdAndBeginWorkBetween(account, request.getYear(), request.getMonth());
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
        List<Vacation> vacations = vacationService.findByAccountIdAndBeginVacationBetweenMonthAndPaydHoliday(account, request.getYear(), request.getMonth());
        for(Vacation vacation : vacations)
        {
            monthPaydHolidayTime = monthPaydHolidayTime.plus(Duration.between(vacation.getBeginVacation(), vacation.getEndVacation()));
        }
        monthSpecialTime.minus(monthPaydHolidayTime);
        status = 1;
        monthWorkInfoResponse.setStatus(status);
        monthWorkInfoResponse.setWorkTime(durationToString.durationToString(monthWorkTime));
        monthWorkInfoResponse.setLateness(durationToString.durationToString(monthLateness));
        monthWorkInfoResponse.setLeaveEarly(durationToString.durationToString(monthLeaveEarly));
        monthWorkInfoResponse.setOuting(durationToString.durationToString(monthOuting));
        monthWorkInfoResponse.setOverWork(durationToString.durationToString(monthOverWork));
        monthWorkInfoResponse.setAbsenceTime(durationToString.durationToString(monthAbsenceTime));
        monthWorkInfoResponse.setSpecialTime(durationToString.durationToString(monthSpecialTime));
        monthWorkInfoResponse.setLateNightWorkTime(durationToString.durationToString(monthLateNightWorkTime));
        monthWorkInfoResponse.setPaydHolidayTime(durationToString.durationToString(monthPaydHolidayTime));
        return monthWorkInfoResponse;
    }

    public List<AttendListResponse> getAttendListResponses(Account account, UserAttendInput request)
    {
        List<AttendListResponse> attendListResponses = new ArrayList<AttendListResponse>();
        List<Attend> attends = findByAccountIdAndBeginWorkBetween(request.getAccountId(), request.getYear(), request.getMonth());
        for(Attend attend : attends)
        {
            attendListResponses.add(attendToAttendListResponse(attend));
        }
        return attendListResponses;
    }

    public MonthWorkInfoResponse getMonthWorkInfoResponse(Account account, AccountApprover accountApprover, UserMonthWorkInfoInput request)
    {
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
        List<Attend> attends = findByAccountIdAndBeginWorkBetween(accountApprover.getAccountId(), request.getYear(), request.getMonth());
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
        List<Vacation> vacations = vacationService.findByAccountIdAndBeginVacationBetweenMonthAndPaydHoliday(accountApprover.getAccountId(), request.getYear(), request.getMonth());
        for(Vacation vacation : vacations)
        {
            monthPaydHolidayTime = monthPaydHolidayTime.plus(Duration.between(vacation.getBeginVacation(), vacation.getEndVacation()));
        }

        monthSpecialTime.minus(monthPaydHolidayTime);
        MonthWorkInfoResponse monthWorkInfoResponse = new MonthWorkInfoResponse();
        monthWorkInfoResponse.setStatus(1);
        monthWorkInfoResponse.setWorkTime(durationToString.durationToString(monthWorkTime));
        monthWorkInfoResponse.setLateness(durationToString.durationToString(monthLateness));
        monthWorkInfoResponse.setLeaveEarly(durationToString.durationToString(monthLeaveEarly));
        monthWorkInfoResponse.setOuting(durationToString.durationToString(monthOuting));
        monthWorkInfoResponse.setOverWork(durationToString.durationToString(monthOverWork));
        monthWorkInfoResponse.setAbsenceTime(durationToString.durationToString(monthAbsenceTime));
        monthWorkInfoResponse.setSpecialTime(durationToString.durationToString(monthSpecialTime));
        monthWorkInfoResponse.setLateNightWorkTime(durationToString.durationToString(monthLateNightWorkTime));
        monthWorkInfoResponse.setPaydHolidayTime(durationToString.durationToString(monthPaydHolidayTime));
        return monthWorkInfoResponse;
    }

    public AttendListResponse attendToAttendListResponse(Attend attend)
    {
        AttendListResponse attendListResponse = new AttendListResponse();
        attendListResponse.setId(attend.getAttendanceId());
        attendListResponse.setBeginWork(localDateTimeToString.localDateTimeToString(attend.getBeginWork()));
        attendListResponse.setEndWork(localDateTimeToString.localDateTimeToString(attend.getEndWork()));
        attendListResponse.setBeginBreak(localDateTimeToString.localDateTimeToString(attend.getBeginBreak()));
        attendListResponse.setEndBreak(localDateTimeToString.localDateTimeToString(attend.getEndBreak()));
        attendListResponse.setWorkTime(attend.getWorkTime());
        attendListResponse.setBreakTime(attend.getBreakTime());
        attendListResponse.setLateness(attend.getLateness());
        attendListResponse.setLeaveEarly(attend.getLeaveEarly());
        attendListResponse.setOuting(attend.getOuting());
        attendListResponse.setOverWork(attend.getOverWork());
        attendListResponse.setHolidayWork(attend.getHolidayWork());
        attendListResponse.setLateNightWork(attend.getLateNightWork());
        attendListResponse.setVacationTime(attend.getVacationTime());
        attendListResponse.setAbsenceTime(attend.getAbsenceTime());
        return attendListResponse;
    }

    @Transactional
    public Attend save(Attend attend)
    {
        return attendRepository.save(attend);
    }

    @Transactional
    public void resetAllTables()
    {
        attendRepository.deleteAll();
        attendRepository.resetAutoIncrement();
    }

    @Transactional
    public void delete(Attend attend)
    {
        attendRepository.delete(attend);
    }
}
