package com.example.springboot.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import com.example.springboot.Config;

import com.example.springboot.service.MonthlyRequestService;

@ContextConfiguration(classes = Config.class)
@ExtendWith(MockitoExtension.class)
public class MonthlyRequestServiceTest
{
    @InjectMocks
    @Spy
    MonthlyRequestService monthlyRequestService;
}
