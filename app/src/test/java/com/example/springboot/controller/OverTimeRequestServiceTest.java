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
import com.example.springboot.service.LegalTimeService;
import com.example.springboot.service.OverTimeRequestService;
import com.example.springboot.service.ShiftListOtherTimeService;
import com.example.springboot.service.ShiftService;

@ContextConfiguration(classes = Config.class)
@ExtendWith(MockitoExtension.class)
public class OverTimeRequestServiceTest
{
    @InjectMocks
    @Spy
    OverTimeRequestService overTimeRequestService;

    @Mock
    StringToLocalDateTime stringToLocalDateTime;

    @Mock
    StringToDuration stringToDuration;

    @Mock
    ShiftService shiftService;

    @Mock
    ShiftListOtherTimeService shiftListOtherTimeService;

    @Mock
    LegalTimeService legalTimeService;
 
}
