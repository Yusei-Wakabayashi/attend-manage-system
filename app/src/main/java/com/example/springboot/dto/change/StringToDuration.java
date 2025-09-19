package com.example.springboot.dto.change;

import java.time.Duration;

public class StringToDuration
{
    public Duration stringToDuration(String str)
    {
        String[] parts = str.split(":");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid time format, expected HHH:mm:ss: " + str);
        }
        try
        {
            long hours = Long.parseLong(parts[0]);
            long minutes = Long.parseLong(parts[1]);
            long seconds = Long.parseLong(parts[2]);

            return Duration.ofHours(hours).plusMinutes(minutes).plusSeconds(seconds);
        }
        catch(ClassCastException e)
        {
            throw new IllegalArgumentException("Invalid time format, expected HHH:mm:ss: " + str);
        }

    }
}
