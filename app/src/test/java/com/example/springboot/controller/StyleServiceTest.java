package com.example.springboot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import com.example.springboot.Config;
import com.example.springboot.dto.response.StyleResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.Style;
import com.example.springboot.model.StylePlace;
import com.example.springboot.repository.StyleRepository;
import com.example.springboot.service.AccountService;
import com.example.springboot.service.StylePlaceService;
import com.example.springboot.service.StyleService;

@ContextConfiguration(classes = Config.class)
@ExtendWith(MockitoExtension.class)
public class StyleServiceTest
{

    @InjectMocks
    @Spy
    StyleService styleService;

    @Mock
    AccountService accountService;

    @Mock
    StylePlaceService stylePlaceService;

    @Mock
    StyleRepository styleRepository;

    @Test
    void updateStyleSuccess()
    {
        // 必要なデータを設定
        Account account = new Account();
        String accountUsername = "testuser";
        Long accountId = 49L;
        account.setId(accountId);
        account.setUsername(accountUsername);

        // style
        Style style = new Style();
        Long styleId = 43L;
        style.setStyleId(styleId);
        // 勤怠形態種類設定
        StylePlace stylePlace = new StylePlace();
        Long stylePlaceId = 934L;
        stylePlace.setId(stylePlaceId);
        // 新しいstyle設定
        Style newStyle = new Style();
        newStyle.setStyleId(styleId);
        newStyle.setStylePlaceId(stylePlace);

        when(stylePlaceService.findStylePlaceById(anyLong())).thenReturn(stylePlace);

        doReturn(style).when(styleService).findStyleByAccountId(any(Account.class));
        doReturn(newStyle).when(styleService).save(any(Style.class));

        int result = styleService.updateStyle(account, stylePlaceId);

        assertEquals(1, result);
    }

    @Test
    void returnStyle()
    {
        Account generalAccount = new Account();
        Long generalAccountId = 1L;
        String generalAccountUsername = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUsername);

        Style generalStyle = new Style();
        Long generalStyleId = 2L;
        StylePlace generalStylePlace = new StylePlace();
        String generalStylePlaceName = "出勤";
        generalStylePlace.setName(generalStylePlaceName);
        generalStyle.setStyleId(generalStyleId);
        generalStyle.setStylePlaceId(generalStylePlace);

        doReturn(generalStyle).when(styleService).findStyleByAccountId(any(Account.class));

        StyleResponse result = styleService.returnStyle(generalAccount);
        assertEquals(1, result.getStatus());
    }

}
