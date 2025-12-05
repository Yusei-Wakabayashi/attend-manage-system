package com.example.springboot.dto.change;

import java.time.Duration;

import org.springframework.stereotype.Component;

@Component
public class DurationToString
{
    public String durationToString(Duration duration)
    {
        long totalSeconds = duration.getSeconds();
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        return String.format("%03d:%02d:%02d", hours, minutes, seconds);
    }
}