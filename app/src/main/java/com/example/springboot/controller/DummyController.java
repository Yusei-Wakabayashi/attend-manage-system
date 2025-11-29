package com.example.springboot.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.dto.ArrayResponse;
import com.example.springboot.dto.response.AttendListResponse;
import com.example.springboot.dto.response.RequestListResponse;
import com.example.springboot.dto.YearMonthParam;
import com.example.springboot.dto.change.LocalDateTimeToString;
import com.example.springboot.dto.response.ShiftListResponse;
import com.example.springboot.model.Attend;
import com.example.springboot.model.Shift;

@RequestMapping("/dummy")
@RestController
@CrossOrigin(origins = {"http://localhost:5173"})
public class DummyController
{
    @GetMapping("/reach/shiftlist")
    public ArrayResponse<ShiftListResponse> returnShifts(@ModelAttribute  YearMonthParam request) throws Exception
    {
        LocalDateTimeToString localDateTimeToString = new LocalDateTimeToString();
        int month = request.getMonth();
        String path = "csv/";
        String filename = switch (month)
        {
            case 1 -> "ShiftListJanu.csv";
            case 2 -> "ShiftListFeb.csv";
            case 3 -> "ShiftListMarch.csv";
            case 4 -> "ShiftListApril.csv";
            case 5 -> "ShiftListMay.csv";
            case 6 -> "ShiftListJune.csv";
            case 7 -> "ShiftListJuly.csv";
            case 8 -> "ShiftListAugust.csv";
            case 9 -> "ShiftListSep.csv";
            case 10 -> "ShiftListOct.csv";
            case 11 -> "ShiftListNove.csv";
            case 12 -> "ShiftListDece.csv";
            default -> null;
        };

        if (filename == null)
        {
            return new ArrayResponse<>(0, new ArrayList<>(), "shiftlist"); // 異常値
        }

        ArrayList<ShiftListResponse> shiftlist = new ArrayList<>();

        // resources/csv/フォルダに配置されている前提
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path + filename);

        if (inputStream == null)
        {
            throw new FileNotFoundException("ファイルが見つかりません: " + filename);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream)))
        {
            String line;
            ShiftListResponse shiftListResponse = new ShiftListResponse();
            while ((line = br.readLine()) != null)
            {
                if (line.startsWith("id,")) continue; // ヘッダー
                shiftListResponse.setId(new Shift().processLine(line).getShiftId());
                shiftListResponse.setBeginWork(localDateTimeToString.localDateTimeToString(new Shift().processLine(line).getBeginWork()));
                shiftListResponse.setEndWork(localDateTimeToString.localDateTimeToString(new Shift().processLine(line).getEndWork()));
                shiftListResponse.setBeginBreak(localDateTimeToString.localDateTimeToString(new Shift().processLine(line).getBeginBreak()));
                shiftListResponse.setEndBreak(localDateTimeToString.localDateTimeToString(new Shift().processLine(line).getEndBreak()));
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
    public ArrayResponse<AttendListResponse> returnAttends(@ModelAttribute YearMonthParam request) throws Exception
    {
        LocalDateTimeToString localDateTimeToString = new LocalDateTimeToString();
        int month = request.getMonth();
        String path = "csv/";
        String filename = switch (month)
        {
            case 1 -> "AttendListJanu.csv";
            case 2 -> "AttendListFeb.csv";
            case 3 -> "AttendListMarch.csv";
            case 4 -> "AttendListApril.csv";
            case 5 -> "AttendListMay.csv";
            case 6 -> "AttendListJune.csv";
            case 7 -> "AttendListJuly.csv";
            case 8 -> "AttendListAugust.csv";
            case 9 -> "AttendListSep.csv";
            case 10 -> "AttendListOct.csv";
            case 11 -> "AttendListNove.csv";
            case 12 -> "AttendListDece.csv";
            default -> null;
        };

        if (filename == null)
        {
            return new ArrayResponse<>(0, new ArrayList<>(), "attendlist"); // 異常値
        }

        ArrayList<AttendListResponse> attendlist = new ArrayList<>();

        // resources/csv/ フォルダに配置されている前提
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path + filename);

        if (inputStream == null)
        {
            throw new FileNotFoundException("ファイルが見つかりません: " + filename);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream)))
        {
            String line;
            AttendListResponse attendListResponse = new AttendListResponse();

            while ((line = br.readLine()) != null) {
                if (line.startsWith("id,")) continue; // ヘッダー
                attendListResponse.setId(new Attend().processLine(line).getAttendanceId());
                attendListResponse.setBeginWork(localDateTimeToString.localDateTimeToString(new Attend().processLine(line).getBeginWork()));
                attendListResponse.setEndWork(localDateTimeToString.localDateTimeToString(new Attend().processLine(line).getEndWork()));
                attendListResponse.setBeginBreak(localDateTimeToString.localDateTimeToString(new Attend().processLine(line).getBeginBreak()));
                attendListResponse.setEndBreak(localDateTimeToString.localDateTimeToString(new Attend().processLine(line).getEndBreak()));
                attendListResponse.setWorkTime(new Attend().processLine(line).getWorkTime());
                attendListResponse.setBreakTime(new Attend().processLine(line).getBreakTime());
                attendListResponse.setLateness(new Attend().processLine(line).getLateness());
                attendListResponse.setLeaveEarly(new Attend().processLine(line).getLeaveEarly());
                attendListResponse.setOuting(new Attend().processLine(line).getOuting());
                attendListResponse.setOverWork(new Attend().processLine(line).getOverWork());
                attendListResponse.setHolidayWork(new Attend().processLine(line).getHolidayWork());
                attendListResponse.setLateNightWork(new Attend().processLine(line).getLateNightWork());
                attendListResponse.setVacationTime(new Attend().processLine(line).getVacationTime());
                attendListResponse.setAbsenceTime(new Attend().processLine(line).getAbsenceTime());
                attendlist.add(attendListResponse);
            }
        }

        return new ArrayResponse<>(1, attendlist, "attendlist");
    }

    @GetMapping("/reach/requestlist")
    public ArrayResponse<RequestListResponse> returnRequests()
    {
        RequestListResponse waitRequestRequestListResponse = new RequestListResponse(1,1,"2025/12/12T11:22:03",1);
        RequestListResponse approvalRequestListResponse = new RequestListResponse(2,2,"2025/12/12T11:22:03",2);
        RequestListResponse rejectRequestListResponse = new RequestListResponse(4,3,"2025/12/12T11:22:03",3);
        RequestListResponse approvalCancelRequestListResponse = new RequestListResponse(6,4,"2025/12/12T11:22:03",4);
        RequestListResponse withdrowRequestListResponse = new RequestListResponse(3,5,"2025/12/12T11:22:03",5);
        RequestListResponse monthlyRequestListResponse = new RequestListResponse(5,6,"2025/12/12T11:22:03",6);
        List<RequestListResponse> requestResponses = List.of(approvalRequestListResponse, rejectRequestListResponse, approvalCancelRequestListResponse, withdrowRequestListResponse, monthlyRequestListResponse, waitRequestRequestListResponse);
        return new ArrayResponse<RequestListResponse>(1, requestResponses, "requestList");
    }
}
