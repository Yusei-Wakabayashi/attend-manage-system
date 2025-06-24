package com.example.springboot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.dao.Attend;
import com.example.springboot.dao.Shift;
import com.example.springboot.dto.ArrayResponse;
import com.example.springboot.dto.ShiftRequest;

@RestController
@SpringBootApplication
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:80","http://localhost:5173"})
public class Application {

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
    public ArrayResponse<Shift> returnShifts(@ModelAttribute  ShiftRequest request) throws Exception {
        int month = request.getMonth();
        String path = "shifts/";
        String filename = switch (month) {
            case 5 -> "ShiftListMay.csv";
            case 6 -> "ShiftListJune.csv";
            case 7 -> "ShiftListJuly.csv";
            case 8 -> "ShiftListAugust.csv";
            default -> null;
        };

        if (filename == null) {
            return new ArrayResponse<>(0, new ArrayList<>(), "shiftlist"); // 異常値
        }

        ArrayList<Shift> shiftlist = new ArrayList<>();

        // resources/shift/ フォルダに配置されている前提
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path + filename);

        if (inputStream == null) {
            throw new FileNotFoundException("ファイルが見つかりません: " + filename);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("id,")) continue; // ヘッダー
                shiftlist.add(new Shift().processLine(line));
            }
        }

        return new ArrayResponse<>(1, shiftlist, "shiftlist");
    }

    @GetMapping("/reach/attendlist")
    public ArrayResponse<Attend> returnAttends(@ModelAttribute ShiftRequest request) throws Exception
    {
        int month = request.getMonth();
        String path = "shifts/";
        String filename = switch (month) {
            case 5 -> "ShiftListMay.csv";
            case 6 -> "ShiftListJune.csv";
            case 7 -> "ShiftListJuly.csv";
            case 8 -> "ShiftListAugust.csv";
            default -> null;
        };

        if (filename == null) {
            return new ArrayResponse<>(0, new ArrayList<>(), "attendlist"); // 異常値
        }

        ArrayList<Attend> attendlist = new ArrayList<>();

        // resources/shift/ フォルダに配置されている前提
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path + filename);

        if (inputStream == null) {
            throw new FileNotFoundException("ファイルが見つかりません: " + filename);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("id,")) continue; // ヘッダー
                attendlist.add(new Attend().processLine(line));
            }
        }

        return new ArrayResponse<>(1, attendlist, "shiftlist");
    }
}

