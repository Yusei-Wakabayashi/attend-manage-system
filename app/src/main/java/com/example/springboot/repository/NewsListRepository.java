package com.example.springboot.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.springboot.model.Account;
import com.example.springboot.model.NewsList;

public interface NewsListRepository extends JpaRepository<NewsList, Long>
{
    List<NewsList> findByAccountId(Account accountid);
    List<NewsList> findByAccountIdAndDateBetween(Account accountId, LocalDateTime startPeriod, LocalDateTime endPeriod);
    @Modifying
    @Query(value = "ALTER TABLE news_list AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}
