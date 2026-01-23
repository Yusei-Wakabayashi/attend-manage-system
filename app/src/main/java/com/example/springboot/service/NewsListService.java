package com.example.springboot.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.dto.change.LocalDateTimeToString;
import com.example.springboot.dto.response.NewsListResponse;
import com.example.springboot.model.Account;
import com.example.springboot.model.NewsList;
import com.example.springboot.repository.NewsListRepository;

@Service
public class NewsListService
{
    @Autowired
    private NewsListRepository newsListRepository;

    public List<NewsListResponse> returnNewsList(Account account)
    {
        List<NewsList> newsLists = findByAccountId(account);
        List<NewsListResponse> newsListResponses = new ArrayList<NewsListResponse>();
        for(NewsList newsList : newsLists)
        {
            NewsListResponse newsListResponse = newsListToNewsListResponse(newsList);
            newsListResponses.add(newsListResponse);
        }
        return newsListResponses;
    }

    public List<NewsList> findByAccountId(Long accountId)
    {
        Account account = new Account();
        account.setId(accountId);
        List<NewsList> newsList = newsListRepository.findByAccountId(account);
        return newsList;
    }

    public List<NewsList> findByAccountId(Account account)
    {
        List<NewsList> newsList = newsListRepository.findByAccountId(account);
        return newsList;
    }

    public List<NewsList> findByAccountIdAndDateBetweenMonthly(Account account, int year, int month)
    {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        List<NewsList> newsList = newsListRepository.findByAccountIdAndDateBetween(account, startPeriod, endPeriod);
        return newsList;
    }

    public List<NewsList> findByAccountIdAndDateBetweenMonthly(Long accountId, int year, int month)
    {
        Account account = new Account();
        account.setId(accountId);;
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        List<NewsList> newsList = newsListRepository.findByAccountIdAndDateBetween(account, startPeriod, endPeriod);
        return newsList;
    }

    public NewsListResponse newsListToNewsListResponse(NewsList newsList)
    {
        LocalDateTimeToString localDateTimeToString = new LocalDateTimeToString();
        NewsListResponse newsListResponse  = new NewsListResponse();
        newsListResponse.setDate(localDateTimeToString.localDateTimeToString(newsList.getDate()));
        newsListResponse.setMessageDetil(newsList.getNewsDetil());
        return newsListResponse;
    }

    @Transactional
    public void resetAllTables()
    {
        newsListRepository.deleteAll();
        newsListRepository.resetAutoIncrement();
    }
}
