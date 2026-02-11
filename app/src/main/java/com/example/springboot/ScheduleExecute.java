package com.example.springboot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class ScheduleExecute
{
    // 左から秒、分、時、日、月、年に対応
    @Scheduled(cron = "0 0 4 * * *", zone = "Asia/Tokyo")
    public void checkAttendance()
    {
        // 打刻漏れ確認処理呼び出し
    }

    @Scheduled(cron = "0 0 4 * * *", zone = "Asia/Tokyo")
    public void checkPaydHoliay()
    {
        // 有給付与
    }
}
