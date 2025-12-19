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
import com.example.springboot.dto.response.VacationTypeListResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.VacationType;
import com.example.springboot.service.VacationTypeService;

@ContextConfiguration(classes = Config.class)
@ExtendWith(MockitoExtension.class)
public class VacationTypeServiceTest
{
    @InjectMocks
    @Spy
    VacationTypeService vacationTypeService;

    @Test
    void returnAllVacationTypeListResponsesSuccess()
    {
        Account generalAccount = new Account();
        String generalAccountUsername = "testuser";
        generalAccount.setUsername(generalAccountUsername);

        List<VacationType> vacationTypes = new ArrayList<VacationType>();
        VacationType vacationType = new VacationType();
        Long vacationTypeId = 3L;
        String vacationTypeName = "代休";
        vacationType.setVacationTypeId(vacationTypeId);
        vacationType.setVacationName(vacationTypeName);
        vacationTypes.add(vacationType);

        doReturn(vacationTypes).when(vacationTypeService).findAll();

        List<VacationTypeListResponse> result = vacationTypeService.returnAllVacationTypeListResponses();
        assertEquals(1, result.size());
    }

}
