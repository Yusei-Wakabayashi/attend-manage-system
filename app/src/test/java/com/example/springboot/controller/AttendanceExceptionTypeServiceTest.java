package com.example.springboot.controller;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import com.example.springboot.Config;
import com.example.springboot.dto.response.OtherTypeListResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.AttendanceExceptionType;
import com.example.springboot.service.AttendanceExceptionTypeService;

@ContextConfiguration(classes = Config.class)
@ExtendWith(MockitoExtension.class)
public class AttendanceExceptionTypeServiceTest
{
    @InjectMocks
    @Spy
    AttendanceExceptionTypeService attendanceExceptionTypeService;

    @Test
    void returnOtherTypeListResponsesSuccess()
    {
        Account generalAccount = new Account();
        String generalAccountUsername = "testuser";
        generalAccount.setUsername(generalAccountUsername);

        List<AttendanceExceptionType> attendanceExceptionTypes = new ArrayList<AttendanceExceptionType>();
        AttendanceExceptionType attendanceExceptionType = new AttendanceExceptionType();
        Long attendanceExceptionTypeId = 2L;
        String attendanceExceptionTypeName = "遅刻";
        attendanceExceptionType.setAttendanceExceptionTypeId(attendanceExceptionTypeId);
        attendanceExceptionType.setAttednaceExceptionTypeName(attendanceExceptionTypeName);
        attendanceExceptionTypes.add(attendanceExceptionType);

        doReturn(attendanceExceptionTypes).when(attendanceExceptionTypeService).findAll();

        List<OtherTypeListResponse> result = attendanceExceptionTypeService.returnOtherTypeListResponses();
        assertEquals(1, result.size());
    }

}
