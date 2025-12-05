package com.example.springboot.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.dto.change.StringToLocalDateTime;
import com.example.springboot.dto.input.OtherTimeInput;
import com.example.springboot.model.Account;
import com.example.springboot.model.AttendanceExceptionRequest;
import com.example.springboot.model.AttendanceExceptionType;
import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftChangeRequest;
import com.example.springboot.model.ShiftListOverTime;
import com.example.springboot.model.ShiftListShiftRequest;
import com.example.springboot.model.ShiftRequest;
import com.example.springboot.repository.AttendanceExceptionRequestRepsitory;

@Service
public class AttendanceExceptionRequestService
{
    private final AttendanceExceptionRequestRepsitory attendanceExceptionRequestRepsitory;
    private final ShiftService shiftService;
    private final StringToLocalDateTime stringToLocalDateTime;
    private final ShiftListOverTimeService shiftListOverTimeService;
    private final ShiftListShiftRequestService shiftListShiftRequestService;
    private final AttendanceExceptionTypeService attendanceExceptionTypeService;

    @Autowired
    public AttendanceExceptionRequestService
    (
        AttendanceExceptionRequestRepsitory attendanceExceptionRequestRepsitory,
        ShiftService shiftService,
        StringToLocalDateTime stringToLocalDateTime,
        ShiftListOverTimeService shiftListOverTimeService,
        ShiftListShiftRequestService shiftListShiftRequestService,
        AttendanceExceptionTypeService attendanceExceptionTypeService
    )
    {
        this.attendanceExceptionRequestRepsitory = attendanceExceptionRequestRepsitory;
        this.shiftService = shiftService;
        this.stringToLocalDateTime = stringToLocalDateTime;
        this.shiftListOverTimeService = shiftListOverTimeService;
        this.shiftListShiftRequestService = shiftListShiftRequestService;
        this.attendanceExceptionTypeService = attendanceExceptionTypeService;
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
                List<AttendanceExceptionRequest> attendanceExceptionRequests = findByAccountIdAndShiftIdAndOutingAndBeginTimeBetweenOrEndTimeBetweenAndRequestStatusWaitOrRequestStatusApproved(account, shift, startTime, endTime);
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
                else if(Objects.isNull(shiftListShiftRequest.getShiftChangeRequestId()))
                {
                    // シフト,時間変更申請請がなければシフト申請で
                    ShiftRequest shiftRequest = shiftListShiftRequest.getShiftRequestId();
                    beginTime = shiftRequest.getBeginWork();
                }
                else
                {
                    // シフト申請がなければシフト時間変更申請を利用
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
                else if(Objects.isNull(shiftListShiftRequest.getShiftChangeRequestId()))
                {
                    ShiftRequest shiftRequest = shiftListShiftRequest.getShiftRequestId();
                    closeTime = shiftRequest.getEndWork();
                }
                // シフト申請がなければシフト時間変更申請で
                else
                {
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
        AttendanceExceptionRequest resultAttendanceExceptionRequest = save(attendanceExceptionRequest);
        if(Objects.isNull(resultAttendanceExceptionRequest) || Objects.isNull(resultAttendanceExceptionRequest.getAttendanceExceptionId()))
        {
            return 3;
        }
        return 1;
    }
    public AttendanceExceptionRequest findById(Long id)
    {
        return attendanceExceptionRequestRepsitory.findById(id)
            .orElseThrow(() -> new RuntimeException("勤怠例外申請が見つかりません"));
    }

    public AttendanceExceptionRequest findByAccountIdAndAttendanceExceptionId(Long accountId, Long id)
    {
        Account account = new Account();
        account.setId(accountId);
        return attendanceExceptionRequestRepsitory.findByAccountIdAndAttendanceExceptionId(account, id)
            .orElseThrow(() -> new RuntimeException("勤怠例外申請が見つかりません"));
    }

    public AttendanceExceptionRequest findByAccountIdAndAttendanceExceptionId(Account account, Long id)
    {
        return attendanceExceptionRequestRepsitory.findByAccountIdAndAttendanceExceptionId(account, id)
            .orElseThrow(() -> new RuntimeException("勤怠例外申請が見つかりません"));
    }

    public List<AttendanceExceptionRequest> findByAccountId(Long accountId)
    {
        Account account = new Account();
        account.setId(accountId);
        return attendanceExceptionRequestRepsitory.findByAccountId(account);
    }

    public List<AttendanceExceptionRequest> findByAccountIdIn(List<Account> accounts)
    {
        List<AttendanceExceptionRequest> attendanceExceptionRequests = attendanceExceptionRequestRepsitory.findByAccountIdIn(accounts);
        return attendanceExceptionRequests;
    }

    public List<AttendanceExceptionRequest> findByAccountId(Account account)
    {
        return attendanceExceptionRequestRepsitory.findByAccountId(account);
    }

    public List<AttendanceExceptionRequest> findByAccountIdAndShiftIdAndOutingAndBeginTimeBetweenOrEndTimeBetweenAndRequestStatusWaitOrRequestStatusApproved(Long accountId, Long shiftId, LocalDateTime startPeriod, LocalDateTime endPeriod)
    {
        Account account = new Account();
        account.setId(accountId);
        Shift shift = new Shift();
        shift.setShiftId(shiftId);
        // 検索する際申請のリクエストが9時開始10時終了で既に10時開始11時終了があった際10時を含みたくないため1秒ずらしている
        startPeriod.plusSeconds(1);
        endPeriod.minusSeconds(1);
        AttendanceExceptionType outing = new AttendanceExceptionType();
        outing.setAttendanceExceptionTypeId(1L);
        int requestStatusWait = 1;
        int requestStatusApproved = 2;
        List<AttendanceExceptionRequest> beginTimeListWait = attendanceExceptionRequestRepsitory.findByAccountIdAndShiftIdAndAttendanceExceptionTypeIdAndBeginTimeBetweenAndRequestStatus(account, shift, outing, startPeriod, endPeriod, requestStatusWait);
        List<AttendanceExceptionRequest> endTimeListWait = attendanceExceptionRequestRepsitory.findByAccountIdAndShiftIdAndAttendanceExceptionTypeIdAndEndTimeBetweenAndRequestStatus(account, shift, outing, startPeriod, endPeriod, requestStatusWait);
        List<AttendanceExceptionRequest> beginTimeListApproved = attendanceExceptionRequestRepsitory.findByAccountIdAndShiftIdAndAttendanceExceptionTypeIdAndBeginTimeBetweenAndRequestStatus(account, shift, outing, startPeriod, endPeriod, requestStatusApproved);
        List<AttendanceExceptionRequest> endTimeListApproved = attendanceExceptionRequestRepsitory.findByAccountIdAndShiftIdAndAttendanceExceptionTypeIdAndEndTimeBetweenAndRequestStatus(account, shift, outing, startPeriod, endPeriod, requestStatusApproved);
        List<AttendanceExceptionRequest> allTimeListWait = Stream.concat(beginTimeListWait.stream(), endTimeListWait.stream()).distinct().collect(Collectors.toList());
        List<AttendanceExceptionRequest> allTimeListApproved = Stream.concat(beginTimeListApproved.stream(), endTimeListApproved.stream()).distinct().collect(Collectors.toList());
        List<AttendanceExceptionRequest> allTimeList = Stream.concat(allTimeListWait.stream(), allTimeListApproved.stream()).distinct().collect(Collectors.toList());
        return allTimeList;
    }

    public List<AttendanceExceptionRequest> findByAccountIdAndShiftIdAndOutingAndBeginTimeBetweenOrEndTimeBetweenAndRequestStatusWaitOrRequestStatusApproved(Account account, Long shiftId, LocalDateTime startPeriod, LocalDateTime endPeriod)
    {
        Shift shift = new Shift();
        shift.setShiftId(shiftId);
        // 検索する際申請のリクエストが9時開始10時終了で既に10時開始11時終了があった際10時を含みたくないため1秒ずらしている
        startPeriod.plusSeconds(1);
        endPeriod.minusSeconds(1);
        AttendanceExceptionType outing = new AttendanceExceptionType();
        outing.setAttendanceExceptionTypeId(1L);
        int requestStatusWait = 1;
        int requestStatusApproved = 2;
        List<AttendanceExceptionRequest> beginTimeListWait = attendanceExceptionRequestRepsitory.findByAccountIdAndShiftIdAndAttendanceExceptionTypeIdAndBeginTimeBetweenAndRequestStatus(account, shift, outing, startPeriod, endPeriod, requestStatusWait);
        List<AttendanceExceptionRequest> endTimeListWait = attendanceExceptionRequestRepsitory.findByAccountIdAndShiftIdAndAttendanceExceptionTypeIdAndEndTimeBetweenAndRequestStatus(account, shift, outing, startPeriod, endPeriod, requestStatusWait);
        List<AttendanceExceptionRequest> beginTimeListApproved = attendanceExceptionRequestRepsitory.findByAccountIdAndShiftIdAndAttendanceExceptionTypeIdAndBeginTimeBetweenAndRequestStatus(account, shift, outing, startPeriod, endPeriod, requestStatusApproved);
        List<AttendanceExceptionRequest> endTimeListApproved = attendanceExceptionRequestRepsitory.findByAccountIdAndShiftIdAndAttendanceExceptionTypeIdAndEndTimeBetweenAndRequestStatus(account, shift, outing, startPeriod, endPeriod, requestStatusApproved);
        List<AttendanceExceptionRequest> allTimeListWait = Stream.concat(beginTimeListWait.stream(), endTimeListWait.stream()).distinct().collect(Collectors.toList());
        List<AttendanceExceptionRequest> allTimeListApproved = Stream.concat(beginTimeListApproved.stream(), endTimeListApproved.stream()).distinct().collect(Collectors.toList());
        List<AttendanceExceptionRequest> allTimeList = Stream.concat(allTimeListWait.stream(), allTimeListApproved.stream()).distinct().collect(Collectors.toList());
        return allTimeList;
    }

    public List<AttendanceExceptionRequest> findByAccountIdAndShiftIdAndOutingAndBeginTimeBetweenOrEndTimeBetweenAndRequestStatusWaitOrRequestStatusApproved(Long accountId, Shift shift, LocalDateTime startPeriod, LocalDateTime endPeriod)
    {
        Account account = new Account();
        account.setId(accountId);
        // 検索する際申請のリクエストが9時開始10時終了で既に10時開始11時終了があった際10時を含みたくないため1秒ずらしている
        startPeriod.plusSeconds(1);
        endPeriod.minusSeconds(1);
        AttendanceExceptionType outing = new AttendanceExceptionType();
        outing.setAttendanceExceptionTypeId(1L);
        int requestStatusWait = 1;
        int requestStatusApproved = 2;
        List<AttendanceExceptionRequest> beginTimeListWait = attendanceExceptionRequestRepsitory.findByAccountIdAndShiftIdAndAttendanceExceptionTypeIdAndBeginTimeBetweenAndRequestStatus(account, shift, outing, startPeriod, endPeriod, requestStatusWait);
        List<AttendanceExceptionRequest> endTimeListWait = attendanceExceptionRequestRepsitory.findByAccountIdAndShiftIdAndAttendanceExceptionTypeIdAndEndTimeBetweenAndRequestStatus(account, shift, outing, startPeriod, endPeriod, requestStatusWait);
        List<AttendanceExceptionRequest> beginTimeListApproved = attendanceExceptionRequestRepsitory.findByAccountIdAndShiftIdAndAttendanceExceptionTypeIdAndBeginTimeBetweenAndRequestStatus(account, shift, outing, startPeriod, endPeriod, requestStatusApproved);
        List<AttendanceExceptionRequest> endTimeListApproved = attendanceExceptionRequestRepsitory.findByAccountIdAndShiftIdAndAttendanceExceptionTypeIdAndEndTimeBetweenAndRequestStatus(account, shift, outing, startPeriod, endPeriod, requestStatusApproved);
        List<AttendanceExceptionRequest> allTimeListWait = Stream.concat(beginTimeListWait.stream(), endTimeListWait.stream()).distinct().collect(Collectors.toList());
        List<AttendanceExceptionRequest> allTimeListApproved = Stream.concat(beginTimeListApproved.stream(), endTimeListApproved.stream()).distinct().collect(Collectors.toList());
        List<AttendanceExceptionRequest> allTimeList = Stream.concat(allTimeListWait.stream(), allTimeListApproved.stream()).distinct().collect(Collectors.toList());
        return allTimeList;
    }

    public List<AttendanceExceptionRequest> findByAccountIdAndShiftIdAndOutingAndBeginTimeBetweenOrEndTimeBetweenAndRequestStatusWaitOrRequestStatusApproved(Account account, Shift shift, LocalDateTime startPeriod, LocalDateTime endPeriod)
    {
        // 検索する際申請のリクエストが9時開始10時終了で既に10時開始11時終了があった際10時を含みたくないため1秒ずらしている
        startPeriod.plusSeconds(1);
        endPeriod.minusSeconds(1);
        AttendanceExceptionType outing = new AttendanceExceptionType();
        outing.setAttendanceExceptionTypeId(1L);
        int requestStatusWait = 1;
        int requestStatusApproved = 2;
        List<AttendanceExceptionRequest> beginTimeListWait = attendanceExceptionRequestRepsitory.findByAccountIdAndShiftIdAndAttendanceExceptionTypeIdAndBeginTimeBetweenAndRequestStatus(account, shift, outing, startPeriod, endPeriod, requestStatusWait);
        List<AttendanceExceptionRequest> endTimeListWait = attendanceExceptionRequestRepsitory.findByAccountIdAndShiftIdAndAttendanceExceptionTypeIdAndEndTimeBetweenAndRequestStatus(account, shift, outing, startPeriod, endPeriod, requestStatusWait);
        List<AttendanceExceptionRequest> beginTimeListApproved = attendanceExceptionRequestRepsitory.findByAccountIdAndShiftIdAndAttendanceExceptionTypeIdAndBeginTimeBetweenAndRequestStatus(account, shift, outing, startPeriod, endPeriod, requestStatusApproved);
        List<AttendanceExceptionRequest> endTimeListApproved = attendanceExceptionRequestRepsitory.findByAccountIdAndShiftIdAndAttendanceExceptionTypeIdAndEndTimeBetweenAndRequestStatus(account, shift, outing, startPeriod, endPeriod, requestStatusApproved);
        List<AttendanceExceptionRequest> allTimeListWait = Stream.concat(beginTimeListWait.stream(), endTimeListWait.stream()).distinct().collect(Collectors.toList());
        List<AttendanceExceptionRequest> allTimeListApproved = Stream.concat(beginTimeListApproved.stream(), endTimeListApproved.stream()).distinct().collect(Collectors.toList());
        List<AttendanceExceptionRequest> allTimeList = Stream.concat(allTimeListWait.stream(), allTimeListApproved.stream()).distinct().collect(Collectors.toList());
        return allTimeList;
    }
    
    public List<AttendanceExceptionRequest> findByAccountIdAndBeginTimeBetweenAndRequestStatusWait(Long accountId, int year, int month)
    {
        Account account = new Account();
        account.setId(accountId);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        int wait = 1;
        List<AttendanceExceptionRequest> attendanceExceptionRequests = attendanceExceptionRequestRepsitory.findByAccountIdAndRequestStatusAndBeginTimeBetween(account, wait, startPeriod, endPeriod);
        return attendanceExceptionRequests;
    }

    public List<AttendanceExceptionRequest> findByAccountIdAndBeginTimeBetweenAndRequestStatusWait(Account account, int year, int month)
    {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        int wait = 1;
        List<AttendanceExceptionRequest> attendanceExceptionRequests = attendanceExceptionRequestRepsitory.findByAccountIdAndRequestStatusAndBeginTimeBetween(account, wait, startPeriod, endPeriod);
        return attendanceExceptionRequests;
    }

    public AttendanceExceptionRequest save(AttendanceExceptionRequest attendanceExceptionRequest)
    {
        return attendanceExceptionRequestRepsitory.save(attendanceExceptionRequest);
    }
}