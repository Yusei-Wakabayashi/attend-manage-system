package com.example.springboot.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

import com.example.springboot.dto.LoginPostData;
import com.example.springboot.dto.change.StringToLocalDateTime;
import com.example.springboot.dto.response.Response;
import com.example.springboot.dto.input.ShiftChangeInput;
import com.example.springboot.dto.input.ShiftInput;
import com.example.springboot.dto.input.StampInput;
import com.example.springboot.dto.IdData;
import com.example.springboot.model.Account;
import com.example.springboot.model.AccountApprover;
import com.example.springboot.model.LegalTime;
import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftChangeRequest;
import com.example.springboot.model.ShiftListShiftRequest;
import com.example.springboot.model.ShiftRequest;
import com.example.springboot.model.StampRequest;
import com.example.springboot.model.Style;
import com.example.springboot.model.StylePlace;
import com.example.springboot.model.Vacation;
import com.example.springboot.service.AccountApproverService;
// import com.example.springboot.model.Salt;
import com.example.springboot.service.AccountService;
import com.example.springboot.service.LegalTimeService;
import com.example.springboot.service.ShiftChangeRequestService;
import com.example.springboot.service.ShiftListShiftRequestService;
import com.example.springboot.service.ShiftRequestService;
import com.example.springboot.service.ShiftService;
import com.example.springboot.service.StampRequestService;
import com.example.springboot.service.StylePlaceService;
import com.example.springboot.service.StyleService;
import com.example.springboot.service.VacationService;
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

    @Autowired
    private ShiftChangeRequestService shiftChangeRequestService;

    @Autowired
    private VacationService vacationService;

    @Autowired
    private ShiftListShiftRequestService shiftListShiftRequestService;

    @Autowired
    private StampRequestService stampRequestService;
    
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
        StringToLocalDateTime stringToLocalDateTime = new StringToLocalDateTime();
        int status = 0;
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.getAccountByUsername(username);
        if(Objects.isNull(account))
        {
            status = 4;
            return new Response(status);
        }
        // 始業時間、終業時間、休憩開始時間、休憩終了時間が現在取得時間より後になっていることを確認
        // 2025/09/02追記シフト申請の出し忘れも考慮して現在時間より前でも申請可能に
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime beginWork = stringToLocalDateTime.stringToLocalDateTime(shiftInput.getBeginWork());
        LocalDateTime endWork = stringToLocalDateTime.stringToLocalDateTime(shiftInput.getEndWork());
        LocalDateTime beginBreak = stringToLocalDateTime.stringToLocalDateTime(shiftInput.getBeginBreak());
        LocalDateTime endBreak = stringToLocalDateTime.stringToLocalDateTime(shiftInput.getEndBreak());
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

        // 始業時間が1年先までは許容する
        LocalDateTime nextYear = nowTime.plusYears(1L);
        // 現在時刻より後かつ1年後(nextYear)より前
        if(beginWork.isAfter(nowTime) && beginWork.isBefore(nextYear))
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
        // 法定休憩時間で初期化
        Duration standardBreakTime = Duration.ofHours(legalTime.getScheduleBreakTime().toLocalTime().getHour()).plusMinutes(legalTime.getScheduleBreakTime().toLocalTime().getMinute()).plusSeconds(legalTime.getScheduleBreakTime().toLocalTime().getSecond());
        // 週の法定労働時間で初期化
        Duration standardWeekWorkTime = Duration.ofHours(legalTime.getWeeklyWorkTime().toLocalTime().getHour()).plusMinutes(legalTime.getWeeklyWorkTime().toLocalTime().getMinute()).plusSeconds(legalTime.getWeeklyWorkTime().toLocalTime().getSecond());
        // 申請の労働時間のコピーで初期化
        Duration weekWorkTime = workTime.abs();
        // 週のシフト
        List<Shift> weekShifts = shiftService.findByAccountIdAndBeginWorkBetweenWeek(account, beginWork);
        // すでにあるシフトと今回申請される1件を足した件数が一週間(7)から法定休日の日数を引いた数より大きければエラー
        // 法定休日を超えないようにしたい
        if((weekShifts.size() + 1) > (7 - legalTime.getWeeklyHoliday()))
        {
            status = 3;
            return new Response(status);
        }

        // シフトに関連する申請を取得したい
        List<ShiftListShiftRequest> shiftListShiftRequests = shiftListShiftRequestService.findByShiftIdIn(weekShifts);
        // 週シフト申請
        List<ShiftRequest> weekShiftRequests = new ArrayList<ShiftRequest>();
        // シフト時間変更申請が出されているシフトを取得したい
        List<ShiftChangeRequest> shiftChangeRequests = new ArrayList<ShiftChangeRequest>();

        for(ShiftListShiftRequest shiftListShiftRequest : shiftListShiftRequests)
        {
            // シフト申請がなければシフト時間変更申請を追加
            if(Objects.isNull(shiftListShiftRequest.getShiftRequestId()))
            {
                shiftChangeRequests.add(shiftListShiftRequest.getShiftChangeRequestId());
            }
            // シフト時間変更申請がなければシフト申請を追加
            else if(Objects.isNull(shiftListShiftRequest.getShiftChangeRequestId()))
            {
                weekShiftRequests.add(shiftListShiftRequest.getShiftRequestId());
            }
            // どちらでもなければエラー
            else
            {
                status = 3;
                return new Response(status);
            }
        }
        // シフトでの計算だと遅刻や早退、残業などでずれた時間で計算することになる
        // それを避けるため反映する申請から取得し計算している
        // 週の休暇
        List<Vacation> weekVacations = vacationService.findByAccountIdAndBeginVacationBetweenWeek(account, beginWork);

        // 申請待ち状態の時間を加算
        for(ShiftRequest weekShiftRequest : weekShiftRequests)
        {
            Duration weekShiftRequestWorkTimeMorning = Duration.between(weekShiftRequest.getBeginWork(), weekShiftRequest.getBeginBreak());
            Duration weekShiftRequestWorkTimeAfternoon = Duration.between(weekShiftRequest.getEndBreak(), weekShiftRequest.getBeginWork());
            weekWorkTime.plus(weekShiftRequestWorkTimeMorning);
            weekWorkTime.plus(weekShiftRequestWorkTimeAfternoon);
        }
        // 時間変更申請の申請待ち状態の時間を加算
        for(ShiftChangeRequest weekShiftChangeRequest : shiftChangeRequests)
        {
            Duration weekShiftChangeRequestWorkTimeMorning = Duration.between(weekShiftChangeRequest.getBeginWork(), weekShiftChangeRequest.getBeginBreak());
            Duration weekShiftChangeRequestWorkTimeAfternoon = Duration.between(weekShiftChangeRequest.getEndBreak(), weekShiftChangeRequest.getBeginWork());
            weekWorkTime.plus(weekShiftChangeRequestWorkTimeMorning);
            weekWorkTime.plus(weekShiftChangeRequestWorkTimeAfternoon);
        }
        // 休暇の時間分減算
        for(Vacation weekVacation : weekVacations)
        {
            weekWorkTime.minus(Duration.between(weekVacation.getBeginVacation(), weekVacation.getEndVacation()));
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
        shiftRequest.setRequestDate(stringToLocalDateTime.stringToLocalDateTime(shiftInput.getRequestDate()));
        shiftRequest.setRequestStatus(1);
        shiftRequest.setApprover(null);
        shiftRequest.setApprovalTime(null);
        shiftRequest.setApproverComment(null);
        shiftRequestService.save(shiftRequest);
        return new Response(status);
    }
    @PostMapping("/send/changetime")
    public Response shiftChangeSet(@RequestBody ShiftChangeInput shiftChangeInput, HttpSession session)
    {
        StringToLocalDateTime stringToLocalDateTime = new StringToLocalDateTime();
        int status = 0;
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.getAccountByUsername(username);
        // accountの情報があるか
        if(Objects.isNull(account))
        {
            status = 4;
            return new Response(status);
        }
        // アカウントの情報とシフトの情報を基に検索
        Shift shift = shiftService.findByAccountIdAndShiftId(account, shiftChangeInput.getShiftId());
        if(Objects.isNull(shift))
        {
            status = 4;
            return new Response(status);
        }
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime beginWork = stringToLocalDateTime.stringToLocalDateTime(shiftChangeInput.getBeginWork());
        LocalDateTime endWork = stringToLocalDateTime.stringToLocalDateTime(shiftChangeInput.getEndWork());
        LocalDateTime beginBreak = stringToLocalDateTime.stringToLocalDateTime(shiftChangeInput.getBeginBreak());
        LocalDateTime endBreak = stringToLocalDateTime.stringToLocalDateTime(shiftChangeInput.getEndBreak());
        // シフト時間変更申請で同じシフトに承認待ちの申請がないことを確認
        List<ShiftChangeRequest> shiftChangeRequestWaits = shiftChangeRequestService.findByAccountIdAndShiftIdAndRequestStatusWait(account, shift.getShiftId());
        if(shiftChangeRequestWaits.size() > 0)
        {
            status = 3;
            return new Response(status);
        }        
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

        // 始業時間が1年先までは許容する
        LocalDateTime nextYear = nowTime.plusYears(1L);
        // 現在時刻より後かつ1年後(nextYear)より前
        if(beginWork.isAfter(nowTime) && beginWork.isBefore(nextYear))
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
        // 法定時間で初期化
        Duration standardWeekWorkTime = Duration.ofHours(legalTime.getWeeklyWorkTime().toLocalTime().getHour()).plusMinutes(legalTime.getWeeklyWorkTime().toLocalTime().getMinute()).plusSeconds(legalTime.getWeeklyWorkTime().toLocalTime().getSecond());
        // 申請の労働時間のコピーで初期化
        Duration weekWorkTime = workTime.abs();
        // 週のシフト
        List<Shift> weekShifts = shiftService.findByAccountIdAndBeginWorkBetweenWeek(account, beginWork);
    
        // 始業日が同じか確認
        if(shift.getBeginWork().getDayOfMonth() == beginWork.getDayOfMonth())
        {
            // おなじならなにもしない
        }
        else
        {
            status = 3;
            return new Response(status);
        }

        // すでにあるシフトと今回申請される1件を足した件数が一週間(7)から法定休日の日数を引いた数より大きければエラー
        // 法定休日を超えないようにしたい
        if((weekShifts.size() + 1) > (7 - legalTime.getWeeklyHoliday()))
        {
            status = 3;
            return new Response(status);
        }

        // シフトに関連する申請を取得したい
        List<ShiftListShiftRequest> shiftListShiftRequests = shiftListShiftRequestService.findByShiftIdIn(weekShifts);
        // 週シフト申請
        List<ShiftRequest> weekShiftRequests = new ArrayList<ShiftRequest>();
        // シフト時間変更申請が出されているシフトを取得したい
        List<ShiftChangeRequest> shiftChangeRequests = new ArrayList<ShiftChangeRequest>();

        for(ShiftListShiftRequest shiftListShiftRequest : shiftListShiftRequests)
        {
            // シフト申請がなければシフト時間変更申請を追加
            if(Objects.isNull(shiftListShiftRequest.getShiftRequestId()))
            {
                shiftChangeRequests.add(shiftListShiftRequest.getShiftChangeRequestId());
            }
            // シフト時間変更申請がなければシフト申請を追加
            else if(Objects.isNull(shiftListShiftRequest.getShiftChangeRequestId()))
            {
                weekShiftRequests.add(shiftListShiftRequest.getShiftRequestId());
            }
            // どちらでもなければエラー
            else
            {
                status = 3;
                return new Response(status);
            }
        }
        // シフトでの計算だと遅刻や早退、残業などでずれた時間で計算することになる
        // それを避けるため反映する申請から取得し計算している
        // 週の休暇
        List<Vacation> weekVacations = vacationService.findByAccountIdAndBeginVacationBetweenWeek(account, beginWork);

        // シフトで予定されている週の労働時間を加算
        for(Shift weekShift : weekShifts)
        {
            Duration weekWorkTimeMorning = Duration.between(weekShift.getBeginWork(), weekShift.getBeginBreak());
            Duration weekWorkTimeAfternoon = Duration.between(weekShift.getEndBreak(), weekShift.getBeginWork());
            weekWorkTime.plus(weekWorkTimeMorning);
            weekWorkTime.plus(weekWorkTimeAfternoon);
        }
        // 申請待ち状態の時間を加算
        for(ShiftRequest weekShiftRequest : weekShiftRequests)
        {
            Duration weekShiftRequestWorkTimeMorning = Duration.between(weekShiftRequest.getBeginWork(), weekShiftRequest.getBeginBreak());
            Duration weekShiftRequestWorkTimeAfternoon = Duration.between(weekShiftRequest.getEndBreak(), weekShiftRequest.getBeginWork());
            weekWorkTime.plus(weekShiftRequestWorkTimeMorning);
            weekWorkTime.plus(weekShiftRequestWorkTimeAfternoon);
        }
        // 時間変更申請の申請待ち状態の時間を加算
        for(ShiftChangeRequest weekShiftChangeRequest : shiftChangeRequests)
        {
            Duration weekShiftChangeRequestWorkTimeMorning = Duration.between(weekShiftChangeRequest.getBeginWork(), weekShiftChangeRequest.getBeginBreak());
            Duration weekShiftChangeRequestWorkTimeAfternoon = Duration.between(weekShiftChangeRequest.getEndBreak(), weekShiftChangeRequest.getBeginWork());
            weekWorkTime.plus(weekShiftChangeRequestWorkTimeMorning);
            weekWorkTime.plus(weekShiftChangeRequestWorkTimeAfternoon);
        }
        // 休暇の時間分減算
        for(Vacation weekVacation : weekVacations)
        {
            weekWorkTime.minus(Duration.between(weekVacation.getBeginVacation(), weekVacation.getEndVacation()));
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

        ShiftChangeRequest shiftChangeRequest = new ShiftChangeRequest();
        shiftChangeRequest.setAccountId(account);
        shiftChangeRequest.setBeginWork(beginWork);
        shiftChangeRequest.setEndWork(endWork);
        shiftChangeRequest.setBeginBreak(beginBreak);
        shiftChangeRequest.setEndBreak(endBreak);
        shiftChangeRequest.setRequestComment(shiftChangeInput.getRequestComment());
        shiftChangeRequest.setRequestDate(stringToLocalDateTime.stringToLocalDateTime(shiftChangeInput.getRequestDate()));
        shiftChangeRequest.setRequestStatus(1);
        shiftChangeRequest.setApprover(null);
        shiftChangeRequest.setApprovalTime(null);
        shiftChangeRequest.setApproverComment(null);
        shiftChangeRequest.setShiftId(shift);
        shiftChangeRequestService.save(shiftChangeRequest);
        return new Response(status);
    }
    @PostMapping("/send/stamp")
    public Response stampSet(@RequestBody StampInput stampInput, HttpSession session)
    {
        StringToLocalDateTime stringToLocalDateTime = new StringToLocalDateTime();
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.getAccountByUsername(username);
        int status = 0;
        // accountの情報があるか
        if(Objects.isNull(account))
        {
            status = 4;
            return new Response(status);
        }
        // アカウントの情報とシフトの情報を基に検索
        Shift shift = shiftService.findByAccountIdAndShiftId(account, stampInput.getShiftId());
        if(Objects.isNull(shift))
        {
            status = 4;
            return new Response(status);
        }
        // 打刻漏れ申請で同じシフトで既に承認済みのものがないか確認
        List<StampRequest> stampRequests = stampRequestService.findByShiftIdAndRequestStatusWait(shift);
        if(stampRequests.size() > 0)
        {
            status = 3;
            return new Response(status);
        }
        LocalDateTime nowTime = LocalDateTime.now();
        // シフトの終業時刻が現在時刻より前であること
        if(shift.getEndWork().isBefore(nowTime))
        {
            // 条件通りなら何もしない
        }
        else
        {
            status = 3;
            return new Response(status);
        }
        LocalDateTime beginWork = stringToLocalDateTime.stringToLocalDateTime(stampInput.getBeginWork());
        LocalDateTime endWork = stringToLocalDateTime.stringToLocalDateTime(stampInput.getEndWork());
        LocalDateTime beginBreak = stringToLocalDateTime.stringToLocalDateTime(stampInput.getBeginBreak());
        LocalDateTime endBreak = stringToLocalDateTime.stringToLocalDateTime(stampInput.getEndBreak());
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
        // 始業日が同じか確認
        if(shift.getBeginWork().getDayOfMonth() == beginWork.getDayOfMonth())
        {
            // おなじならなにもしない
        }
        else
        {
            status = 3;
            return new Response(status);
        }
        // 始業と終業、休憩の開始と終了がシフトの時間と一致していること
        if(shift.getBeginWork().compareTo(beginWork) == 0 && shift.getEndWork().compareTo(endWork) == 0 && shift.getBeginBreak().compareTo(beginBreak) == 0 && shift.getEndBreak().compareTo(endBreak) == 0)
        {
            // 一致していたら何もしない
        }
        else
        {
            status = 3;
            return new Response(status);
        }
        status = 1;
        int requestStatus = 1;
        StampRequest stampRequest = new StampRequest();
        stampRequest.setAccountId(account);
        stampRequest.setBeginWork(beginWork);
        stampRequest.setEndWork(endWork);
        stampRequest.setBeginBreak(beginBreak);
        stampRequest.setEndBreak(endBreak);
        stampRequest.setRequestComment(stampInput.getRequestComment());
        stampRequest.setRequestDate(Objects.isNull(stampInput.getRequestDate()) ? null : stringToLocalDateTime.stringToLocalDateTime(stampInput.getRequestDate()));
        stampRequest.setRequestStatus(requestStatus);
        stampRequest.setApprover(null);
        stampRequest.setApprovalTime(null);
        stampRequest.setApproverComment(null);
        stampRequest.setShiftId(shift);
        stampRequestService.save(stampRequest);
        return new Response(status);
    }

}