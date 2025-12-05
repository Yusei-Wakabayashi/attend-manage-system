package com.example.springboot.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import com.example.springboot.Config;
import com.example.springboot.dto.change.StringToLocalDateTime;
import com.example.springboot.service.ShiftChangeRequestService;
import com.example.springboot.service.ShiftService;

@ContextConfiguration(classes = Config.class)
@ExtendWith(MockitoExtension.class)
public class ShiftChangeRequestServiceTest
{
    @InjectMocks
    @Spy
    ShiftChangeRequestService shiftChangeRequestService;
    
    @Mock
    ShiftService shiftService;

    @Mock
    StringToLocalDateTime stringToLocalDateTime;
}
