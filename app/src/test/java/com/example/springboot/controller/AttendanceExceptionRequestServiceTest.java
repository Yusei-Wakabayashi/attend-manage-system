package com.example.springboot.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import com.example.springboot.Config;
import com.example.springboot.dto.change.StringToLocalDateTime;
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
}
