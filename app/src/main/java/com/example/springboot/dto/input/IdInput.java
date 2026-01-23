package com.example.springboot.dto.input;

import com.fasterxml.jackson.annotation.JsonAnySetter;

public class IdInput
{
    private Long id;

    @JsonAnySetter
    public void setAny(String key, Object value) {
        if (value instanceof Number)
        {
            this.id = ((Number) value).longValue();
        }
        else if (value instanceof String)
        {
            try
            {
                this.id = Long.parseLong((String) value);
            }
            catch
            (NumberFormatException ignored) {}
        }
    }

    public Long getId()
    {
        return this.id;
    }
}
