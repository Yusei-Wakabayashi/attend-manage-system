package com.example.springboot.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import com.example.springboot.Config;
import com.example.springboot.dto.change.StringToDuration;
import com.example.springboot.dto.change.StringToLocalDateTime;
import com.example.springboot.service.OverTimeRequestService;
import com.example.springboot.service.PaydHolidayService;
import com.example.springboot.service.ShiftListOverTimeService;
import com.example.springboot.service.ShiftService;
import com.example.springboot.service.VacationRequestService;
import com.example.springboot.service.VacationTypeService;

@ContextConfiguration(classes = Config.class)
@ExtendWith(MockitoExtension.class)
public class VacationRequestServiceTest
{
    @InjectMocks
    @Spy
    VacationRequestService vacationRequestService;

    @Mock
    ShiftService shiftService;

    @Mock
    ShiftListOverTimeService shiftListOverTimeService;

    @Mock
    OverTimeRequestService overTimeRequestService;
    
    @Mock
    PaydHolidayService paydHolidayService;

    @Mock
    VacationTypeService vacationTypeService;

    @Mock
    StringToLocalDateTime stringToLocalDateTime;

    @Mock
    StringToDuration stringToDuration;

}
