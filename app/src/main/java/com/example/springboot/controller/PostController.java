package com.example.springboot.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
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
import com.example.springboot.model.LegalTime;
import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftRequest;
import com.example.springboot.model.Style;
import com.example.springboot.model.StylePlace;
import com.example.springboot.service.AccountApproverService;
// import com.example.springboot.model.Salt;
import com.example.springboot.service.AccountService;
import com.example.springboot.service.LegalTimeService;
import com.example.springboot.service.ShiftRequestService;
import com.example.springboot.service.ShiftService;
import com.example.springboot.service.StylePlaceService;
import com.example.springboot.service.StyleService;
// import com.example.springboot.service.SaltService;
import com.example.springboot.util.SecurityUtil;

@RequestMapping("/api")
@RestController
@CrossOrigin(origins = {"http://localhost:5173"})
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

    @Autowired
    private LegalTimeService legalTimeService;

    @Autowired
    private ShiftService shiftService;
    
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
        int status = 0;
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.getAccountByUsername(username);
        if(account.equals(null))
        {
            status = 4;
            return new Response(status);
        }
        // 始業時間、終業時間、休憩開始時間、休憩終了時間が現在取得時間より後になっていることを確認
        // 2025/09/02追記シフト申請の出し忘れも考慮して現在時間より前でも申請可能に
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime beginWork = LocalDateTime.parse(LocalDateTime.parse(shiftInput.getBeginWork(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime endWork = LocalDateTime.parse(LocalDateTime.parse(shiftInput.getEndWork(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime beginBreak = LocalDateTime.parse(LocalDateTime.parse(shiftInput.getBeginBreak(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        LocalDateTime endBreak = LocalDateTime.parse(LocalDateTime.parse(shiftInput.getEndBreak(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"));
        // if(beginWork.isBefore(nowTime) || endWork.isBefore(nowTime) || beginBreak.isBefore(nowTime) || endBreak.isBefore(nowTime))
        // {
        //     // 現在取得時間より前の時の処理
        //     return new Response(3);
        // }
        // 始業時間より終業時間が後になっていること、休憩開始時間より休憩終了時間が後になっていること、始業時間より休憩開始時間が後になっていること、休憩終了時間より終業時間が後になっていること
        if(endWork.isAfter(beginWork) && endBreak.isAfter(beginBreak) && beginBreak.isAfter(beginWork) && endWork.isAfter(endBreak))
        {
            // 条件通りなら何もしない
        }
        else
        {
            // 条件に沿っていなかったらエラー
            status = 3;
            return new Response(status);
        }
        // 承認されたシフトで同じ日に重複していないことを確認
        List<Shift> shifts = shiftService.findByAccountIdAndDayBeginWorkBetween(account, beginWork);
        // シフト申請で同じ日に申請が出ていないことを確認
        List<ShiftRequest> shiftRequests = shiftRequestService.getAccountIdAndBeginWorkBetweenDay(account, beginWork);
        // 0より大きかったら申請できない
        if(shiftRequests.size() > 0 || shifts.size() > 0)
        {
            status = 3;
            return new Response(status);
        }
                
        // 始業時間が前後1年までは許容する
        LocalDateTime nextYear = nowTime.plusYears(1L);
        LocalDateTime prevYear = nowTime.minusYears(1L);
        // 1年前(prevYear)より後かつ1年後(nextYear)より前
        if(beginWork.isAfter(prevYear) && beginWork.isBefore(nextYear))
        {
            // 条件に従っていれば何もしない
        }
        else
        {
            // 1年前後に収まっていない始業時間ならエラー
            status = 3;
            return new Response(status);
        }
        LegalTime legalTime = legalTimeService.getFirstByOrderByBeginDesc();
        // 労働時間が8時間(所定労働時間)になっているか確認
        // 休憩が1時間を超えているか確認
        Duration standardWorkTime = Duration.ofHours(legalTime.getScheduleWorkTime().toLocalTime().getHour()).plusMinutes(legalTime.getScheduleWorkTime().toLocalTime().getMinute()).plusSeconds(legalTime.getScheduleWorkTime().toLocalTime().getSecond());
        Duration morning = Duration.between(beginWork, beginBreak);
        Duration afternoon = Duration.between(endBreak, endWork);
        Duration workTime = morning.plus(afternoon);

        Duration breakTime = Duration.between(beginBreak, endBreak);
        Duration standardBreakTime = Duration.ofHours(legalTime.getScheduleBreakTime().toLocalTime().getHour()).plusMinutes(legalTime.getScheduleBreakTime().toLocalTime().getMinute()).plusSeconds(legalTime.getScheduleBreakTime().toLocalTime().getSecond());
        List<Shift> weekShifts = shiftService.findByAccountIdAndBeginWorkBetweenWeek(account, beginWork);
        Duration standardWeekWorkTime = Duration.ofHours(legalTime.getWeeklyWorkTime().toLocalTime().getHour()).plusMinutes(legalTime.getWeeklyWorkTime().toLocalTime().getMinute()).plusSeconds(legalTime.getWeeklyWorkTime().toLocalTime().getSecond());
        // 申請の労働時間のコピーで初期化
        Duration weekWorkTime = workTime.abs();
        for(Shift weekShift : weekShifts)
        {
            Duration weekWorkTimeMorning = Duration.between(weekShift.getBeginWork(), weekShift.getBeginBreak());
            Duration weekWorkTimeAfternoon = Duration.between(weekShift.getEndBreak(), weekShift.getBeginWork());
            weekWorkTime.plus(weekWorkTimeMorning);
            weekWorkTime.plus(weekWorkTimeAfternoon);
        }
        // compareToで辞書的に前か後か(短いか長いか)を条件にしている
        if(workTime.compareTo(standardWorkTime) > 0 || breakTime.compareTo(standardBreakTime) < 0 || weekWorkTime.compareTo(standardWeekWorkTime) > 0)
        {
            // 所定労働時間より長いもしくは休憩時間が所定のものより短い
            // 申請する日を含む一週(月曜日から日曜日)の合計労働時間が法定時間を超える(シフトで予定されている労働時間)
            status = 3;
            return new Response(status);
        }
        else if(workTime.equals(standardWorkTime))
        {
            // 労働時間が所定労働時間と等しい
            status = 1;
        }
        else
        {
            // 所定労働時間より短い
            status = 1;
        }
        // シフト申請に登録
        ShiftRequest shiftRequest = new ShiftRequest();
        shiftRequest.setAccountId(account);
        shiftRequest.setBeginWork(beginWork);
        shiftRequest.setBeginBreak(beginBreak);
        shiftRequest.setEndBreak(endBreak);
        shiftRequest.setEndWork(endWork);
        shiftRequest.setRequestComment(shiftInput.getRequestComment());
        shiftRequest.setRequestDate(LocalDateTime.parse(LocalDateTime.parse(shiftInput.getRequestDate(),DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")),DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss")));
        shiftRequest.setRequestStatus(1);
        shiftRequest.setApprover(null);
        shiftRequest.setApprovalTime(null);
        shiftRequest.setApproverComment(null);
        shiftRequestService.save(shiftRequest);
        return new Response(status);
    }
}