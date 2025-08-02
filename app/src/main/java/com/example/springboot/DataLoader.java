package com.example.springboot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.springboot.model.Department;
import com.example.springboot.model.Role;
import com.example.springboot.model.Account;
import com.example.springboot.model.Salt;
import com.example.springboot.model.Style;
import com.example.springboot.model.StylePlace;
import com.example.springboot.service.AccountService;
import com.example.springboot.service.DepartmentService;
import com.example.springboot.service.RoleService;
import com.example.springboot.service.SaltService;
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

    public void run(String... args) throws Exception
    {
        styleService.resetAllTables();
        accountService.resetAllTables();
        saltService.resetAllTables();
        roleService.resetAllTables();
        departmentService.resetAllTables();
        stylePlaceService.resetAllTables();
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
    }
}