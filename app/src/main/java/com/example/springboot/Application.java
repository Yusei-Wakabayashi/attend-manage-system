package com.example.springboot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.model.Attend;
import com.example.springboot.model.Shift;
import com.example.springboot.dto.ArrayResponse;
import com.example.springboot.dto.AttendListResponse;
import com.example.springboot.dto.ShiftListResponse;
import com.example.springboot.dto.ShiftRequest;

@EnableJpaAuditing
@RestController
@SpringBootApplication
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:80","http://localhost:5173"})
public class Application
{
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/data")
    public Map<String, String> getData() {
        Map<String, String> map = Map.of("message", "Hello World!");
        return map;
    }

    @GetMapping("/form")
    public int StringResponse(@ModelAttribute ShiftRequest request)
    {
        return request.getYear();
    }

    @GetMapping("/reach/shiftlist")
    public ArrayResponse<ShiftListResponse> returnShifts(@ModelAttribute  ShiftRequest request) throws Exception
    {
        int month = request.getMonth();
        String path = "csv/";
        String filename = switch (month)
        {
            case 5 -> "ShiftListMay.csv";
            case 6 -> "ShiftListJune.csv";
            case 7 -> "ShiftListJuly.csv";
            case 8 -> "ShiftListAugust.csv";
            default -> null;
        };

        if (filename == null) {
            return new ArrayResponse<>(0, new ArrayList<>(), "shiftlist"); // 異常値
        }

        ArrayList<ShiftListResponse> shiftlist = new ArrayList<>();

        // resources/csv/フォルダに配置されている前提
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path + filename);

        if (inputStream == null) {
            throw new FileNotFoundException("ファイルが見つかりません: " + filename);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            ShiftListResponse shiftListResponse = new ShiftListResponse();
            while ((line = br.readLine()) != null)
            {
                if (line.startsWith("id,")) continue; // ヘッダー
                shiftListResponse.setId(new Shift().processLine(line).getShiftId());
                shiftListResponse.setBeginWork(new Shift().processLine(line).getBeginWork().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + new Shift().processLine(line).getBeginWork().format(DateTimeFormatter.ofPattern("HH/mm/ss")));
                shiftListResponse.setEndWork(new Shift().processLine(line).getEndWork().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + new Shift().processLine(line).getEndWork().format(DateTimeFormatter.ofPattern("HH/mm/ss")));
                shiftListResponse.setBeginBreak(new Shift().processLine(line).getBeginBreak().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + new Shift().processLine(line).getBeginBreak().format(DateTimeFormatter.ofPattern("HH/mm/ss")));
                shiftListResponse.setEndBreak(new Shift().processLine(line).getEndBreak().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + new Shift().processLine(line).getEndBreak().format(DateTimeFormatter.ofPattern("HH/mm/ss")));
                shiftListResponse.setLateness(new Shift().processLine(line).getLateness());
                shiftListResponse.setLeaveEarly(new Shift().processLine(line).getLeaveEarly());
                shiftListResponse.setOuting(new Shift().processLine(line).getOuting());
                shiftListResponse.setOverWork(new Shift().processLine(line).getOverWork());
                shiftlist.add(shiftListResponse);
            }
        }

        return new ArrayResponse<>(1, shiftlist, "shiftlist");
    }

    @GetMapping("/reach/attendlist")
    public ArrayResponse<AttendListResponse> returnAttends(@ModelAttribute ShiftRequest request) throws Exception
    {
        int month = request.getMonth();
        String path = "csv/";
        String filename = switch (month) {
            case 5 -> "AttendListMay.csv";
            case 6 -> "AttendListJune.csv";
            case 7 -> "AttendListJuly.csv";
            case 8 -> "AttendListAugust.csv";
            default -> null;
        };

        if (filename == null) {
            return new ArrayResponse<>(0, new ArrayList<>(), "attendlist"); // 異常値
        }

        ArrayList<AttendListResponse> attendlist = new ArrayList<>();

        // resources/csv/ フォルダに配置されている前提
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path + filename);

        if (inputStream == null) {
            throw new FileNotFoundException("ファイルが見つかりません: " + filename);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            AttendListResponse attendListResponse = new AttendListResponse();

            while ((line = br.readLine()) != null) {
                if (line.startsWith("id,")) continue; // ヘッダー
                attendListResponse.setId(new Attend().processLine(line).getAttendanceId());
                attendListResponse.setBeginWork(new Attend().processLine(line).getBeginWork().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + new Attend().processLine(line).getBeginWork().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                attendListResponse.setEndWork(new Attend().processLine(line).getEndWork().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + new Attend().processLine(line).getEndWork().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                attendListResponse.setBeginBreak(new Attend().processLine(line).getBeginBreak().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + new Attend().processLine(line).getBeginBreak().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                attendListResponse.setEndBreak(new Attend().processLine(line).getEndBreak().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "T" + new Attend().processLine(line).getEndBreak().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                attendListResponse.setWorkTime(new Attend().processLine(line).getWorkTime());
                attendListResponse.setBreakTime(new Attend().processLine(line).getBreakTime());
                attendListResponse.setLateness(new Attend().processLine(line).getLateness());
                attendListResponse.setLeaveEarly(new Attend().processLine(line).getLeaveEarly());
                attendListResponse.setOuting(new Attend().processLine(line).getOuting());
                attendListResponse.setOverWork(new Attend().processLine(line).getOverWork());
                attendListResponse.setHolidayTime(new Attend().processLine(line).getHolidayWork());
                attendListResponse.setLateNightWork(new Attend().processLine(line).getLateNightWork());
                attendlist.add(attendListResponse);
            }
        }

        return new ArrayResponse<>(1, attendlist, "shiftlist");
    }
}