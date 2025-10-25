package com.example.springboot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.springboot.model.Department;
import com.example.springboot.model.LegalTime;
import com.example.springboot.model.Role;
import com.example.springboot.dto.change.StringToLocalDateTime;
import com.example.springboot.model.Account;
import com.example.springboot.model.ApprovalSetting;
import com.example.springboot.model.Attend;
import com.example.springboot.model.AttendanceExceptionType;
import com.example.springboot.model.Salt;
import com.example.springboot.model.Shift;
import com.example.springboot.model.Style;
import com.example.springboot.model.StylePlace;
import com.example.springboot.service.AccountService;
import com.example.springboot.service.ApprovalSettingService;
import com.example.springboot.service.AttendService;
import com.example.springboot.service.AttendanceExceptionTypeService;
import com.example.springboot.service.DepartmentService;
import com.example.springboot.service.LegalTimeService;
import com.example.springboot.service.OverTimeRequestService;
import com.example.springboot.service.RoleService;
import com.example.springboot.service.SaltService;
import com.example.springboot.service.ShiftChangeRequestService;
import com.example.springboot.service.ShiftRequestService;
import com.example.springboot.service.ShiftService;
import com.example.springboot.service.StampRequestService;
import com.example.springboot.service.StylePlaceService;
import com.example.springboot.service.StyleService;

@Component
public class DataLoader implements CommandLineRunner
{
    @Autowired
    SaltService saltService;

    @Autowired
    RoleService roleService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    AccountService accountService;

    @Autowired
    StylePlaceService stylePlaceService;

    @Autowired
    StyleService styleService;

    @Autowired
    ShiftRequestService shiftRequestService;

    @Autowired
    LegalTimeService legalTimeService;

    @Autowired
    ShiftService shiftService;

    @Autowired
    AttendService attendService;

    @Autowired
    ShiftChangeRequestService shiftChangeRequestService;

    @Autowired
    StampRequestService stampRequestService;

    @Autowired
    AttendanceExceptionTypeService attendanceExceptionTypeService;

    @Autowired
    ApprovalSettingService approvalSettingService;

    @Autowired
    OverTimeRequestService overTimeRequestService;
    
    public void run(String... args) throws Exception
    {
        overTimeRequestService.resetAllTables();
        approvalSettingService.resetAllTables();
        attendService.resetAllTables();
        shiftService.resetAllTables();
        shiftRequestService.resetAllTables();
        shiftChangeRequestService.resetAllTables();
        stampRequestService.resetAllTables();
        styleService.resetAllTables();
        accountService.resetAllTables();
        saltService.resetAllTables();
        roleService.resetAllTables();
        departmentService.resetAllTables();
        stylePlaceService.resetAllTables();
        legalTimeService.resetAllTables();

        String attendanceExceptionTypePath = "csv/AttendanceExceptionTypeList.csv";
        InputStream attendanceExceptionTypeInputStream = getClass().getClassLoader().getResourceAsStream(attendanceExceptionTypePath);
        if(attendanceExceptionTypeInputStream == null)
        {
            throw new FileNotFoundException("ファイルが見つかりません" + attendanceExceptionTypePath);
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(attendanceExceptionTypeInputStream)))
        {
            String attendanceExceptionTypeLine;
            while ((attendanceExceptionTypeLine = br.readLine()) != null)
            {
                if (attendanceExceptionTypeLine.startsWith("id,")) continue;
                AttendanceExceptionType attendanceExceptionType = new AttendanceExceptionType();
                String[] attendanceExceptionTypeList = attendanceExceptionTypeLine.split(",");
                attendanceExceptionType.setAttednaceExceptionTypeName(attendanceExceptionTypeList[1]);
                attendanceExceptionTypeService.save(attendanceExceptionType);
            }
        }

        StringToLocalDateTime stringToLocalDateTime = new StringToLocalDateTime();
        String saltPath = "csv/SaltList.csv";
        InputStream saltInputStream = getClass().getClassLoader().getResourceAsStream(saltPath);
        if (saltInputStream == null)
        {
            throw new FileNotFoundException("ファイルが見つかりません: " + saltPath);
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(saltInputStream)))
        {
            String saltLine;
            while ((saltLine = br.readLine()) != null)
            {
                if (saltLine.startsWith("id,")) continue; // ヘッダー
                Salt salt = new Salt();
                String[] saltList = saltLine.split(",");
                salt.setText(String.valueOf(saltList[1]));
                saltService.save(salt);
            }
        }

        String departmentPath = "csv/DepartmentList.csv";
        InputStream departmentInputStream = getClass().getClassLoader().getResourceAsStream(departmentPath);
        if (departmentInputStream == null)
        {
            throw new FileNotFoundException("ファイルが見つかりません: " + departmentPath);
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(departmentInputStream)))
        {
            String departmentLine;
            while ((departmentLine = br.readLine()) != null)
            {
                if (departmentLine.startsWith("id,")) continue;
                Department department = new Department();
                String[] departmentList = departmentLine.split(",");
                department.setName(String.valueOf(departmentList[1]));
                departmentService.save(department);
            }
        }

        String rolePath = "csv/RoleList.csv";
        InputStream roleInputStream = getClass().getClassLoader().getResourceAsStream(rolePath);
        if (roleInputStream == null)
        {
            throw new FileNotFoundException("ファイルが見つかりません: " + rolePath);
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(roleInputStream)))
        {
            String roleLine;
            while ((roleLine = br.readLine()) != null)
            {
                if (roleLine.startsWith("id,")) continue;
                Role role = new Role();
                String[] roleList = roleLine.split(",");
                role.setName(String.valueOf(roleList[1]));
                role.setPower(Integer.valueOf(roleList[2]));
                roleService.save(role);
            }
        }
        
        String accountPath = "csv/AccountList.csv";
        InputStream accountInputStream = getClass().getClassLoader().getResourceAsStream(accountPath);
        if (accountInputStream == null)
        {
            throw new FileNotFoundException("ファイルが見つかりません: " + accountPath);
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(accountInputStream)))
        {
            MessageDigest md = MessageDigest.getInstance("SHA256");
            String accountLine;
            while ((accountLine = br.readLine()) != null)
            {
                if (accountLine.startsWith("id,")) continue;
                Account account = new Account();
                String[] accountList = accountLine.split(",");
                account.setUsername(accountList[1]);
                account.setSaltId(saltService.getSaltById(Long.valueOf(accountList[2])));
                account.setPassword(md.digest(accountList[3].getBytes()));
                account.setName(accountList[4]);
                account.setGender(accountList[5]);
                account.setAge(Integer.valueOf(accountList[6]));
                account.setRoleId(roleService.getRoleById(Long.valueOf(accountList[7])));
                account.setDepartmentId(departmentService.getDepartmentById(Long.valueOf(accountList[8])));
                account.setJoinDate(LocalDateTime.parse(accountList[9]));
                accountService.save(account);
            }
        }
        String stylePlacePath = "csv/StylePlace.csv";
        InputStream stylePlaceStream = getClass().getClassLoader().getResourceAsStream(stylePlacePath);
        if (stylePlaceStream == null)
        {
            throw new FileNotFoundException("ファイルが見つかりません: " + stylePlacePath);
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stylePlaceStream)))
        {
            String stylePlaceLine;
            while ((stylePlaceLine = br.readLine()) != null)
            {
                if (stylePlaceLine.startsWith("id,")) continue;
                StylePlace stylePlace = new StylePlace();
                String[] stylePlaceList = stylePlaceLine.split(",");
                stylePlace.setName(stylePlaceList[1]);
                stylePlaceService.save(stylePlace);
            }
        }

        String stylePath = "csv/Style.csv";
        InputStream styleStream = getClass().getClassLoader().getResourceAsStream(stylePath);
        if(styleStream == null)
        {
            throw new FileNotFoundException("ファイルが見つかりません:" + stylePath);
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(styleStream)))
        {
            String styleLine;
            while ((styleLine = br.readLine()) != null)
            {
                if (styleLine.startsWith("id,")) continue;
                Style style = new Style();
                String[] styleList = styleLine.split(",");
                style.setAccountId(accountService.getAccountByAccountId(Long.valueOf(styleList[1])));
                styleService.save(style);
            }
        }

        String approvalSettingPath = "csv/ApproverSettingList.csv";
        InputStream approvalSettingInputStream = getClass().getClassLoader().getResourceAsStream(approvalSettingPath);
        if(approvalSettingInputStream == null)
        {
            throw new FileNotFoundException("ファイルが見つかりません:" + approvalSettingPath);
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(approvalSettingInputStream)))
        {
            String approvalSettingLine;
            while ((approvalSettingLine = br.readLine()) != null)
            {
                if (approvalSettingLine.startsWith("id,")) continue;
                ApprovalSetting approvalSetting = new ApprovalSetting();
                String[] approvalSettingList = approvalSettingLine.split(",");
                approvalSetting.setRoleId(roleService.getRoleById(Long.valueOf(approvalSettingList[1])));
                approvalSetting.setApprovalId(roleService.getRoleById(Long.valueOf(approvalSettingList[2])));
                approvalSettingService.save(approvalSetting);
            }
        }

        LegalTime legalTime = new LegalTime();
        legalTime.setBegin(stringToLocalDateTime.stringToLocalDateTime("2025/09/30T00:00:00"));
        legalTime.setScheduleWorkTime("08:00:00");
        legalTime.setWeeklyWorkTime("40:00:00");
        legalTime.setMonthlyOverWorkTime("45:00:00");
        legalTime.setYearOverWorkTime("360:00:00");
        legalTime.setMaxOverWorkTime("100:00:00");
        legalTime.setMonthlyOverWorkAverage("80:00:00");
        legalTime.setLateNightWorkBegin("22:00:00");
        legalTime.setLateNightWorkEnd("05:00:00");
        legalTime.setScheduleBreakTime("01:00:00");
        legalTime.setWeeklyHoliday(1);
        legalTimeService.save(legalTime);

        String[] shiftPathList = 
        {
            "ShiftListMay.csv",
            "ShiftListJune.csv",
            "ShiftListJuly.csv",
            "ShiftListAugust.csv",
            "ShiftListSep.csv",
            "ShiftListOct.csv",
            "ShiftListNove.csv",
            "ShiftListDece.csv",
            "ShiftListJanu.csv",
            "ShiftListFeb.csv",
            "ShiftListMarch.csv",
            "ShiftListApril.csv"
        };
        for(String shiftPath : shiftPathList)
        {
            shiftPath = "csv/" + shiftPath;
            InputStream shiftStream = getClass().getClassLoader().getResourceAsStream(shiftPath);
            if(shiftStream == null)
            {
                throw new FileNotFoundException("ファイルが見つかりません:" + shiftPath);
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(shiftStream)))
            {
            String shiftLine;
                while ((shiftLine = br.readLine()) != null)
                {
                    if (shiftLine.startsWith("id,")) continue;
                    Shift shift = new Shift();
                    String[] shiftList = shiftLine.split(",");
                    shift.setAccountId(accountService.getAccountByAccountId(1L));
                    shift.setBeginWork(LocalDateTime.parse(shiftList[1], DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
                    shift.setEndWork(LocalDateTime.parse(shiftList[2], DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
                    shift.setBeginBreak(LocalDateTime.parse(shiftList[3], DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
                    shift.setEndBreak(LocalDateTime.parse(shiftList[4], DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
                    shift.setLateness(Time.valueOf(shiftList[5]));
                    shift.setLeaveEarly(Time.valueOf(shiftList[6]));
                    shift.setOuting(Time.valueOf(shiftList[7]));
                    shift.setOverWork(Time.valueOf(shiftList[8]));
                    shiftService.save(shift);
                }
            }
        }

        String[] attendPathList = 
        {
            "AttendListMay.csv",
            "AttendListJune.csv",
            "AttendListJuly.csv",
            "AttendListAugust.csv",
            "AttendListSep.csv",
            "AttendListOct.csv",
            "AttendListNove.csv",
            "AttendListDece.csv",
            "AttendListJanu.csv",
            "AttendListFeb.csv",
            "AttendListMarch.csv",
            "AttendListApril.csv"
        };
        for(String attendPath : attendPathList)
        {
            attendPath = "csv/" + attendPath;
            InputStream attendStream = getClass().getClassLoader().getResourceAsStream(attendPath);
            if(attendStream == null)
            {
                throw new FileNotFoundException("ファイルが見つかりません:" + attendPath);
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(attendStream)))
            {
                String attendLine;
                while ((attendLine = br.readLine()) != null)
                {
                    if (attendLine.startsWith("id,")) continue;
                    Attend attend = new Attend();
                    String[] attendList = attendLine.split(",");
                    attend.setAccountId(accountService.getAccountByAccountId(1L));
                    attend.setBeginWork(LocalDateTime.parse(attendList[1], DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
                    attend.setEndWork(LocalDateTime.parse(attendList[2], DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
                    attend.setBeginBreak(LocalDateTime.parse(attendList[3], DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
                    attend.setEndBreak(LocalDateTime.parse(attendList[4], DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
                    attend.setWorkTime(Time.valueOf(attendList[5]));
                    attend.setBreakTime(Time.valueOf(attendList[6]));
                    attend.setLateness(Time.valueOf(attendList[7]));
                    attend.setLeaveEarly(Time.valueOf(attendList[8]));
                    attend.setOuting(Time.valueOf(attendList[9]));
                    attend.setOverWork(Time.valueOf(attendList[10]));
                    attend.setHolidayWork(Time.valueOf(attendList[11]));
                    attend.setLateNightWork(Time.valueOf(attendList[12]));
                    attend.setVacationTime(Time.valueOf(attendList[13]));
                    attend.setAbsenceTime(Time.valueOf(attendList[14]));
                    attendService.save(attend);
                }
            }
        }
    }
}