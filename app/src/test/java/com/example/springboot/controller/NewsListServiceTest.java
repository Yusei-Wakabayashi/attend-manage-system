package com.example.springboot.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import com.example.springboot.Config;
import com.example.springboot.dto.response.NewsListResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.NewsList;
import com.example.springboot.service.NewsListService;

@ContextConfiguration(classes = Config.class)
@ExtendWith(MockitoExtension.class)
public class NewsListServiceTest
{
    @InjectMocks
    @Spy
    NewsListService newsListService;

    @Test
    void returnNewsListSuccess()
    {
        Account generalAccount = new Account();
        Long generalAccountId = 3L;
        String generalAccountUserName = "testuser";
        generalAccount.setId(generalAccountId);
        generalAccount.setUsername(generalAccountUserName);

        List<NewsList> newsLists = new ArrayList<NewsList>();
        NewsList newsList = new NewsList();
        Long newsId = 35L;
        String newsDate = "2025/07/08T22:33:33";
        String newsDetil = "your stampRequest is not approved";
        newsList.setNewsId(newsId);
        newsList.setAccountId(generalAccount);
        newsList.setDate(LocalDateTime.parse(LocalDateTime.parse(newsDate ,DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        newsList.setNewsDetil(newsDetil);
        newsLists.add(newsList);

        NewsListResponse newsListResponse = new NewsListResponse();
        newsListResponse.setDate(newsDate);
        newsListResponse.setMessageDetil(newsDetil);

        doReturn(newsLists).when(newsListService).findByAccountId(any(Account.class));
        doReturn(newsListResponse).when(newsListService).newsListToNewsListResponse(any(NewsList.class));

        List<NewsListResponse> newsListResponses = newsListService.returnNewsList(generalAccount);
        assertEquals(1, newsListResponses.size());
    }
}
