package com.example.springboot.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.dto.InputPostData;
import com.example.springboot.dto.LoginPostData;
import com.example.springboot.dto.Response;
import com.example.springboot.dto.input.ShiftInput;
import com.example.springboot.dto.IdData;
import com.example.springboot.model.Account;
import com.example.springboot.model.AccountApprover;
import com.example.springboot.model.ShiftRequest;
import com.example.springboot.model.Style;
import com.example.springboot.model.StylePlace;
import com.example.springboot.service.AccountApproverService;
// import com.example.springboot.model.Salt;
import com.example.springboot.service.AccountService;
import com.example.springboot.service.ShiftRequestService;
import com.example.springboot.service.StylePlaceService;
import com.example.springboot.service.StyleService;
// import com.example.springboot.service.SaltService;
import com.example.springboot.util.SecurityUtil;

@RequestMapping("/api")
@RestController
// @CrossOrigin(origins = {"http://localhost:5173"})
public class PostController
{
    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountApproverService accountApproverService;

    @Autowired
    private StyleService styleService;

    @Autowired
    private StylePlaceService stylePlaceService;

    @Autowired
    private ShiftRequestService shiftRequestService;
    
    @PostMapping("/request")
    public String returns(@RequestBody InputPostData data)
    {
        return data.getText();
    }

    @CrossOrigin
    @PostMapping("/send/login")
    public Response login(@RequestBody LoginPostData data, HttpSession session)
    {
        try{
            MessageDigest md = MessageDigest.getInstance("SHA256");
            String username = data.getId();
            String password = data.getPassword();
            Account account = accountService.getAccountByUsername(username);
            byte[] perfectPassword = md.digest((password + account.getSaltId().getText()).getBytes());
            if (Arrays.equals(perfectPassword, account.getPassword()))
            {
                // 保存する認証情報を作成
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(account.getUsername(), null); // 認証が完了しているためpasswordなどの情報は保存していない
                // 認証情報を保存
                SecurityContextHolder.getContext().setAuthentication(authToken);
                return new Response(1);
            }
            else
            {
                return new Response(6);
            }
        }
        catch(NoSuchAlgorithmException ex1)
        {
            return new Response(4);
        }
    }

    @PostMapping("/send/logout")
    public Response logout(HttpServletRequest request)
    {
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null)
        {
            session.invalidate(); // セッション破棄
        }
        return new Response(1);
    }

    @PostMapping("/send/approverset")
    public Response approverSet(@RequestBody IdData idData, HttpSession session)
    {
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.getAccountByUsername(username);
        AccountApprover accountApprover = accountApproverService.getAccountApproverByAccount(account);
        Account newAccount = accountService.getAccountByAccountId(idData.getId());
        accountApprover.setApproverId(newAccount);
        String response = accountApproverService.save(accountApprover);
        if (response.equals("ok"))
        {
            return new Response(1);
        }
        return new Response(4);
    }
    
    @PostMapping("/send/style")
    public Response styleSet(@RequestBody IdData idData, HttpSession session)
    {
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.getAccountByUsername(username);
        Style style = styleService.getStyleByAccountId(account.getId());
        StylePlace newStylePlace = stylePlaceService.getStylePlaceById(idData.getId());
        style.setStylePlaceId(newStylePlace);
        String response = styleService.save(style);
        if (response.equals("ok"))
        {
            return new Response(1);
        }
        return new Response(4);
    }

    @PostMapping("/send/shift")
    public Response shiftSet(@RequestBody ShiftInput shiftInput, HttpSession session)
    {
        // 始業時間、終業時間、休憩開始時間、休憩終了時間が現在取得時間より後になっていることを確認
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime beginWork = LocalDateTime.parse(shiftInput.getBeginWork(),DateTimeFormatter.ofPattern("yyyy/mm/ddTHH:MM:ss"));
        LocalDateTime endWork = LocalDateTime.parse(shiftInput.getEndWork(),DateTimeFormatter.ofPattern("yyyy/mm/ddTHH:MM:ss"));
        LocalDateTime beginBreak = LocalDateTime.parse(shiftInput.getBeginBreak(),DateTimeFormatter.ofPattern("yyyy/mm/ddTHH:MM:ss"));
        LocalDateTime endBreak = LocalDateTime.parse(shiftInput.getEndBreak(),DateTimeFormatter.ofPattern("yyyy/mm/ddTHH:MM:ss"));
        if(beginWork.isBefore(nowTime) || endWork.isBefore(nowTime) || beginBreak.isBefore(nowTime) || endBreak.isBefore(nowTime))
        {
            // 現在取得時間より前の時の処理
            return new Response(3);
        }
        // 始業時間より終業時間が後になっていること、休憩開始時間より休憩終了時間が後になっていること、始業時間より休憩開始時間が後になっていること、終業時間より休憩開始時間が前になっていること
        // 同じアカウントで同じ日に登録していないか確認
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.getAccountByUsername(username);
        if(account.equals(null))
        {
            return new Response(4);
        }
        List<ShiftRequest> shiftRequests = shiftRequestService.getAccountIdAndBeginWorkBetween(account, beginWork, endWork);
        if(shiftRequests.size() == 0)
        {
            return new Response(3);
        }
        // 申請する日を含む一週(月曜日から日曜日)の合計労働時間が40時間を超えないように(勤怠とシフトで予定されている労働時間)
        
        // 申請日時は10年先までは許容する
        // 時間が9時間になっているか確認
        // 休憩が1時間になっているか確認
        return new Response(1);
    }
}