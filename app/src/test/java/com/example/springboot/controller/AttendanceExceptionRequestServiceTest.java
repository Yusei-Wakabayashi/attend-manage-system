package com.example.springboot.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import com.example.springboot.Config;
import com.example.springboot.dto.change.StringToLocalDateTime;
import com.example.springboot.dto.input.OtherTimeInput;
import com.example.springboot.model.Account;
import com.example.springboot.model.AttendanceExceptionRequest;
import com.example.springboot.model.AttendanceExceptionType;
import com.example.springboot.model.OverTimeRequest;
import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftChangeRequest;
import com.example.springboot.model.ShiftListOverTime;
import com.example.springboot.model.ShiftListShiftRequest;
import com.example.springboot.model.ShiftRequest;
import com.example.springboot.service.AttendanceExceptionRequestService;
import com.example.springboot.service.AttendanceExceptionTypeService;
import com.example.springboot.service.ShiftListOverTimeService;
import com.example.springboot.service.ShiftListShiftRequestService;
import com.example.springboot.service.ShiftService;

@ContextConfiguration(classes = Config.class)
@ExtendWith(MockitoExtension.class)
public class AttendanceExceptionRequestServiceTest
{
    @InjectMocks
    @Spy
    AttendanceExceptionRequestService attendanceExceptionRequestService;

    @Mock
    ShiftService shiftService;

    @Mock
    ShiftListOverTimeService shiftListOverTimeService;

    @Mock
    ShiftListShiftRequestService shiftListShiftRequestService;

    @Mock
    StringToLocalDateTime stringToLocalDateTime;

    @Mock
    AttendanceExceptionTypeService attendanceExceptionTypeService;

    @Test
    void attendanceExceptionRequestOutingSuccess()
    {
        Long requestShiftId = 1L;
        Long requestOtherType = 1L;
        String requestBeginOtherTime = "2025/12/23T09:00:00";
        String requestEndOtherTime = "2025/12/23T10:00:00";
        String requestComment = "";
        String requestDate = "2025/09/02T10:00:00";

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        Shift generalShift = new Shift();
        Long generalShiftId = 5L;
        String generalShiftBeginWork = "2025/12/23T09:00:00";
        String generalShiftEndWork = "2025/12/23T18:00:00";
        generalShift.setShiftId(generalShiftId);
        generalShift.setBeginWork(LocalDateTime.parse(LocalDateTime.parse(generalShiftBeginWork,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        generalShift.setEndWork(LocalDateTime.parse(LocalDateTime.parse(generalShiftEndWork,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

        List<ShiftListOverTime> shiftListOverTimes = new ArrayList<ShiftListOverTime>();

        ShiftListShiftRequest shiftListShiftRequest = new ShiftListShiftRequest();
        ShiftRequest shiftRequest = new ShiftRequest();
        Long shiftRequestId = 51L;
        String shiftRequestBegin = "2025/12/23T09:00:00";
        String shiftRequestEnd = "2025/12/23T18:00:00";
        shiftRequest.setShiftRequestId(shiftRequestId);
        shiftRequest.setBeginWork(LocalDateTime.parse(LocalDateTime.parse(shiftRequestBegin,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shiftRequest.setEndWork(LocalDateTime.parse(LocalDateTime.parse(shiftRequestEnd,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shiftListShiftRequest.setShiftRequestId(shiftRequest);

        List<AttendanceExceptionRequest> attendanceExceptionRequests = new ArrayList<AttendanceExceptionRequest>();
        AttendanceExceptionRequest attendanceExceptionRequest = new AttendanceExceptionRequest();
        Long attendanceExceptionRequestId = 34L;
        attendanceExceptionRequest.setAttendanceExceptionId(attendanceExceptionRequestId);

        AttendanceExceptionType attendanceExceptionType = new AttendanceExceptionType();
        attendanceExceptionType.setAttendanceExceptionTypeId(requestOtherType);

        OtherTimeInput otherTimeInput = new OtherTimeInput();
        otherTimeInput.setShiftId(requestShiftId);
        otherTimeInput.setOtherType(requestOtherType);
        otherTimeInput.setBeginOtherTime(requestBeginOtherTime);
        otherTimeInput.setEndOtherTime(requestEndOtherTime);
        otherTimeInput.setRequestComment(requestComment);
        otherTimeInput.setRequestDate(requestDate);

        when(shiftService.findByAccountIdAndShiftId(any(Account.class), anyLong())).thenReturn(generalShift);
        when(stringToLocalDateTime.stringToLocalDateTime(requestBeginOtherTime)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestBeginOtherTime,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(stringToLocalDateTime.stringToLocalDateTime(requestEndOtherTime)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestEndOtherTime,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(shiftListOverTimeService.findByShiftId(any(Shift.class))).thenReturn(shiftListOverTimes);
        when(shiftListShiftRequestService.findByShiftId(any(Shift.class))).thenReturn(shiftListShiftRequest);
        when(attendanceExceptionTypeService.findByAttendanceExceptionTypeId(anyLong())).thenReturn(attendanceExceptionType);
        when(stringToLocalDateTime.stringToLocalDateTime(requestDate)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestDate,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

        doReturn(attendanceExceptionRequests).when(attendanceExceptionRequestService).findByAccountIdAndShiftIdAndOutingAndBeginTimeBetweenOrEndTimeBetweenAndRequestStatusWaitOrRequestStatusApproved(any(Account.class), any(Shift.class), any(LocalDateTime.class), any(LocalDateTime.class));
        doReturn(attendanceExceptionRequest).when(attendanceExceptionRequestService).save(any(AttendanceExceptionRequest.class));

        int result = attendanceExceptionRequestService.createAttendanceExceptionRequest(generalAccount, otherTimeInput);
        assertEquals(1, result);
    }

    @Test
    void attendanceExceptionRequestLatenessSuccess()
    {
        Long requestShiftId = 1L;
        Long requestOtherType = 2L;
        String requestBeginOtherTime = "2025/12/23T09:00:00";
        String requestEndOtherTime = "2025/12/23T10:00:00";
        String requestComment = "";
        String requestDate = "2025/09/02T10:00:00";

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        Shift generalShift = new Shift();
        Long generalShiftId = 5L;
        String generalShiftBeginWork = "2025/12/23T09:00:00";
        String generalShiftEndWork = "2025/12/23T18:00:00";
        generalShift.setShiftId(generalShiftId);
        generalShift.setBeginWork(LocalDateTime.parse(LocalDateTime.parse(generalShiftBeginWork,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        generalShift.setEndWork(LocalDateTime.parse(LocalDateTime.parse(generalShiftEndWork,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

        List<ShiftListOverTime> shiftListOverTimes = new ArrayList<ShiftListOverTime>();

        OverTimeRequest overTimeRequest = new OverTimeRequest();
        String shiftListOverTimeBeginOverTime = "2025/12/23T18:00:00";
        String shiftListOverTimeEndOverTime = "2025/12/23T19:00:00";
        overTimeRequest.setBeginWork(LocalDateTime.parse(LocalDateTime.parse(shiftListOverTimeBeginOverTime,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        overTimeRequest.setEndWork(LocalDateTime.parse(LocalDateTime.parse(shiftListOverTimeEndOverTime,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

        ShiftListShiftRequest shiftListShiftRequest = new ShiftListShiftRequest();
        ShiftChangeRequest shiftChangeRequest = new ShiftChangeRequest();
        Long shiftChangeRequestId = 51L;
        String shiftChangeRequestBegin = "2025/12/23T09:00:00";
        String shiftChangeRequestEnd = "2025/12/23T18:00:00";
        shiftChangeRequest.setShiftChangeId(shiftChangeRequestId);
        shiftChangeRequest.setBeginWork(LocalDateTime.parse(LocalDateTime.parse(shiftChangeRequestBegin,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shiftChangeRequest.setEndWork(LocalDateTime.parse(LocalDateTime.parse(shiftChangeRequestEnd,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shiftListShiftRequest.setShiftChangeRequestId(shiftChangeRequest);

        AttendanceExceptionRequest attendanceExceptionRequest = new AttendanceExceptionRequest();
        Long attendanceExceptionRequestId = 34L;
        attendanceExceptionRequest.setAttendanceExceptionId(attendanceExceptionRequestId);

        AttendanceExceptionType attendanceExceptionType = new AttendanceExceptionType();
        attendanceExceptionType.setAttendanceExceptionTypeId(requestOtherType);

        OtherTimeInput otherTimeInput = new OtherTimeInput();
        otherTimeInput.setShiftId(requestShiftId);
        otherTimeInput.setOtherType(requestOtherType);
        otherTimeInput.setBeginOtherTime(requestBeginOtherTime);
        otherTimeInput.setEndOtherTime(requestEndOtherTime);
        otherTimeInput.setRequestComment(requestComment);
        otherTimeInput.setRequestDate(requestDate);

        when(shiftService.findByAccountIdAndShiftId(any(Account.class), anyLong())).thenReturn(generalShift);
        when(stringToLocalDateTime.stringToLocalDateTime(requestBeginOtherTime)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestBeginOtherTime,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(stringToLocalDateTime.stringToLocalDateTime(requestEndOtherTime)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestEndOtherTime,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(shiftListOverTimeService.findByShiftId(any(Shift.class))).thenReturn(shiftListOverTimes);
        when(shiftListShiftRequestService.findByShiftId(any(Shift.class))).thenReturn(shiftListShiftRequest);
        when(attendanceExceptionTypeService.findByAttendanceExceptionTypeId(anyLong())).thenReturn(attendanceExceptionType);
        when(stringToLocalDateTime.stringToLocalDateTime(requestDate)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestDate,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

        doReturn(attendanceExceptionRequest).when(attendanceExceptionRequestService).save(any(AttendanceExceptionRequest.class));

        int result = attendanceExceptionRequestService.createAttendanceExceptionRequest(generalAccount, otherTimeInput);
        assertEquals(1, result);
    }

    @Test
    void attendanceExceptionRequestLeaveEarlySuccess()
    {
        Long requestShiftId = 1L;
        Long requestOtherType = 3L;
        String requestBeginOtherTime = "2025/12/23T17:00:00";
        String requestEndOtherTime = "2025/12/23T18:00:00";
        String requestComment = "";
        String requestDate = "2025/09/02T10:00:00";

        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        Shift generalShift = new Shift();
        Long generalShiftId = 5L;
        String generalShiftBeginWork = "2025/12/23T09:00:00";
        String generalShiftEndWork = "2025/12/23T18:00:00";
        generalShift.setShiftId(generalShiftId);
        generalShift.setBeginWork(LocalDateTime.parse(LocalDateTime.parse(generalShiftBeginWork,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        generalShift.setEndWork(LocalDateTime.parse(LocalDateTime.parse(generalShiftEndWork,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

        List<ShiftListOverTime> shiftListOverTimes = new ArrayList<ShiftListOverTime>();

        OverTimeRequest overTimeRequest = new OverTimeRequest();
        String shiftListOverTimeBeginOverTime = "2025/12/23T08:00:00";
        String shiftListOverTimeEndOverTime = "2025/12/23T09:00:00";
        overTimeRequest.setBeginWork(LocalDateTime.parse(LocalDateTime.parse(shiftListOverTimeBeginOverTime,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        overTimeRequest.setEndWork(LocalDateTime.parse(LocalDateTime.parse(shiftListOverTimeEndOverTime,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

        ShiftListShiftRequest shiftListShiftRequest = new ShiftListShiftRequest();
        ShiftChangeRequest shiftChangeRequest = new ShiftChangeRequest();
        Long shiftChangeRequestId = 51L;
        String shiftChangeRequestBegin = "2025/12/23T09:00:00";
        String shiftChangeRequestEnd = "2025/12/23T18:00:00";
        shiftChangeRequest.setShiftChangeId(shiftChangeRequestId);
        shiftChangeRequest.setBeginWork(LocalDateTime.parse(LocalDateTime.parse(shiftChangeRequestBegin,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shiftChangeRequest.setEndWork(LocalDateTime.parse(LocalDateTime.parse(shiftChangeRequestEnd,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shiftListShiftRequest.setShiftChangeRequestId(shiftChangeRequest);

        AttendanceExceptionRequest attendanceExceptionRequest = new AttendanceExceptionRequest();
        Long attendanceExceptionRequestId = 43L;
        attendanceExceptionRequest.setAttendanceExceptionId(attendanceExceptionRequestId);

        AttendanceExceptionType attendanceExceptionType = new AttendanceExceptionType();
        attendanceExceptionType.setAttendanceExceptionTypeId(requestOtherType);

        OtherTimeInput otherTimeInput = new OtherTimeInput();
        otherTimeInput.setShiftId(requestShiftId);
        otherTimeInput.setOtherType(requestOtherType);
        otherTimeInput.setBeginOtherTime(requestBeginOtherTime);
        otherTimeInput.setEndOtherTime(requestEndOtherTime);
        otherTimeInput.setRequestComment(requestComment);
        otherTimeInput.setRequestDate(requestDate);

        when(shiftService.findByAccountIdAndShiftId(any(Account.class), anyLong())).thenReturn(generalShift);
        when(stringToLocalDateTime.stringToLocalDateTime(requestBeginOtherTime)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestBeginOtherTime,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(stringToLocalDateTime.stringToLocalDateTime(requestEndOtherTime)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestEndOtherTime,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        when(shiftListOverTimeService.findByShiftId(any(Shift.class))).thenReturn(shiftListOverTimes);
        when(shiftListShiftRequestService.findByShiftId(any(Shift.class))).thenReturn(shiftListShiftRequest);
        when(attendanceExceptionTypeService.findByAttendanceExceptionTypeId(anyLong())).thenReturn(attendanceExceptionType);
        when(stringToLocalDateTime.stringToLocalDateTime(requestDate)).thenReturn(LocalDateTime.parse(LocalDateTime.parse(requestDate,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));

        doReturn(attendanceExceptionRequest).when(attendanceExceptionRequestService).save(any(AttendanceExceptionRequest.class));

        int result = attendanceExceptionRequestService.createAttendanceExceptionRequest(generalAccount, otherTimeInput);
        assertEquals(1, result);
    }
}
