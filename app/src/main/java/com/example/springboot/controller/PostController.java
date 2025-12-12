package com.example.springboot.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.dto.change.StringToDuration;
import com.example.springboot.dto.change.StringToLocalDateTime;
import com.example.springboot.dto.response.Response;
import com.example.springboot.dto.input.LegalCheckShiftChangeInput;
import com.example.springboot.dto.input.LegalCheckShiftInput;
import com.example.springboot.dto.input.LoginInput;
import com.example.springboot.dto.input.MonthlyInput;
import com.example.springboot.dto.input.OtherTimeInput;
import com.example.springboot.dto.input.OverTimeInput;
import com.example.springboot.dto.input.RequestJudgmentInput;
import com.example.springboot.dto.input.ShiftChangeInput;
import com.example.springboot.dto.input.ShiftInput;
import com.example.springboot.dto.input.StampInput;
import com.example.springboot.dto.input.VacationInput;
import com.example.springboot.dto.input.WithDrowInput;
import com.example.springboot.dto.IdData;
import com.example.springboot.model.Account;
import com.example.springboot.model.LegalTime;
import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftChangeRequest;
import com.example.springboot.model.ShiftListShiftRequest;
import com.example.springboot.model.ShiftRequest;
import com.example.springboot.model.Vacation;
import com.example.springboot.service.AccountApproverService;
import com.example.springboot.service.AccountService;
import com.example.springboot.service.LegalTimeService;
import com.example.springboot.service.RequestService;
import com.example.springboot.service.ShiftListShiftRequestService;
import com.example.springboot.service.ShiftService;
import com.example.springboot.service.StyleService;
import com.example.springboot.service.VacationService;
import com.example.springboot.util.SecurityUtil;

@RequestMapping("/api")
@RestController
@CrossOrigin(origins = {"http://localhost:5173"})
public class PostController
{
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountApproverService accountApproverService;

    @Autowired
    private StyleService styleService;

    @Autowired
    private LegalTimeService legalTimeService;

    @Autowired
    private ShiftService shiftService;

    @Autowired
    private VacationService vacationService;

    @Autowired
    private ShiftListShiftRequestService shiftListShiftRequestService;

    @Autowired
    private RequestService requestService;
    
    @CrossOrigin
    @PostMapping("/send/login")
    public Response login(@RequestBody LoginInput data)
    {
        String username = data.getId();
        String rawPassword = data.getPassword();
        Account account = accountService.findAccountByUsername(username);
        if(Objects.isNull(account))
        {
            return new Response(4);
        }

        // PasswordEncoder でチェック
        boolean valid = passwordEncoder.matches(rawPassword, account.getPassword());
        if (!valid)
        {
            return new Response(4); // 認証失敗
        }

        // 認証成功 → SecurityContext に詰める
        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken
            (
                account.getUsername(),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );
        SecurityContextHolder.getContext().setAuthentication(authToken);

        return new Response(1);
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
    public Response approverSet(@RequestBody IdData idData)
    {
        Account account = accountService.findCurrentAccount();
        if(Objects.isNull(account))
        {
            return new Response(4);
        }
        int result = accountApproverService.updateApprover(account, idData.getId());
        return new Response(result);
    }
    
    @PostMapping("/send/style")
    public Response styleSet(@RequestBody IdData idData)
    {
        Account account = accountService.findCurrentAccount();
        if(Objects.isNull(account))
        {
            return new Response(4);
        }
        int result = styleService.updateStyle(account, idData.getId());
        return new Response(result);
    }

    @PostMapping("/send/shift")
    public Response shiftSet(@RequestBody ShiftInput shiftInput)
    {
        Account account = accountService.findCurrentAccount();
        if(Objects.isNull(account))
        {
            return new Response(4);
        }
        int result = requestService.createShiftRequest(account, shiftInput);
        return new Response(result);
    }

    @PostMapping("/send/changetime")
    public Response shiftChangeSet(@RequestBody ShiftChangeInput shiftChangeInput)
    {
        Account account = accountService.findCurrentAccount();
        // accountの情報があるか
        if(Objects.isNull(account))
        {
            return new Response(4);
        }
        int result = requestService.createShiftChangeRequest(account, shiftChangeInput);
        return new Response(result);
    }

    @PostMapping("/send/stamp")
    public Response stampSet(@RequestBody StampInput stampInput, HttpSession session)
    {
        Account account = accountService.findCurrentAccount();
        // accountの情報があるか
        if(Objects.isNull(account))
        {
            return new Response(4);
        }
        int result = requestService.createStampRequest(account, stampInput);
        return new Response(result);
    }

    @PostMapping("/send/vacation")
    public Response vacationSet(@RequestBody VacationInput vacationInput, HttpSession session)
    {
        Account account = accountService.findCurrentAccount();
        if(Objects.isNull(account))
        {
            return new Response(4);
        }
        int result = requestService.createVacationRequest(account, vacationInput);
        return new Response(result);
    }

    @PostMapping("/send/othertime")
    public Response otherTimeSet(@RequestBody OtherTimeInput otherTimeInput, HttpSession session)
    {
        Account account = accountService.findCurrentAccount();
        if(Objects.isNull(account))
        {
            return new Response(3);
        }
        int result = requestService.createAttendanceExceptionRequest(account, otherTimeInput);
        return new Response(result);
    }

    @PostMapping("/send/overtime")
    public Response overTimeSet(@RequestBody OverTimeInput overTimeInput, HttpSession session)
    {
        Account account = accountService.findCurrentAccount();
        int status = 0;
        if(Objects.isNull(account))
        {
            status = 3;
            return new Response(status);
        }
        int result = requestService.createOverTimeRequest(account, overTimeInput);
        return new Response(result);
    }

    @PostMapping("/send/monthly")
    public Response monthlySet(@RequestBody MonthlyInput monthlyInput, HttpSession session)
    {
        Account account = accountService.findCurrentAccount();
        if(Objects.isNull(account))
        {
            return new Response(3);
        }
        int result = requestService.createMonthlyRequest(account, monthlyInput);
        return new Response(result);
    }

    @PostMapping("/send/legalcheck/shift")
    public Response shiftLegalCheck(@RequestBody LegalCheckShiftInput legalCheckShiftInput, HttpSession session)
    {
        StringToLocalDateTime stringToLocalDateTime = new StringToLocalDateTime();
        StringToDuration stringToDuration = new StringToDuration();
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.findAccountByUsername(username);
        int status = 0;

        LocalDateTime beginWork = stringToLocalDateTime.stringToLocalDateTime(legalCheckShiftInput.getBeginWork());
        LocalDateTime endWork = stringToLocalDateTime.stringToLocalDateTime(legalCheckShiftInput.getEndWork());
        LocalDateTime beginBreak = stringToLocalDateTime.stringToLocalDateTime(legalCheckShiftInput.getBeginBreak());
        LocalDateTime endBreak = stringToLocalDateTime.stringToLocalDateTime(legalCheckShiftInput.getEndBreak());

        LegalTime legalTime = legalTimeService.findFirstByOrderByBeginDesc();
        // 労働時間が8時間(所定労働時間)になっているか確認
        // 休憩が1時間を超えているか確認
        Duration standardWorkTime = stringToDuration.stringToDuration(legalTime.getScheduleWorkTime());
        Duration morning = Duration.between(beginWork, beginBreak);
        Duration afternoon = Duration.between(endBreak, endWork);
        Duration workTime = morning.plus(afternoon);

        Duration breakTime = Duration.between(beginBreak, endBreak);
        // 法定休憩時間で初期化
        Duration standardBreakTime = stringToDuration.stringToDuration(legalTime.getScheduleBreakTime());
        // 週の法定労働時間で初期化
        Duration standardWeekWorkTime = stringToDuration.stringToDuration(legalTime.getWeeklyWorkTime());
        // 申請の労働時間のコピーで初期化
        Duration weekWorkTime = workTime.abs();
        // 週のシフト
        List<Shift> weekShifts = shiftService.findByAccountIdAndBeginWorkBetweenWeek(account, beginWork);
        // すでにあるシフトと今回申請される1件を足した件数が一週間(7)から法定休日の日数を引いた数より大きければエラー
        // 法定休日を超えないようにしたい
        if((weekShifts.size() + 1) > (7 - legalTime.getWeeklyHoliday()))
        {
            status = 7;
        }

        // シフトに関連する申請を取得したい
        List<ShiftListShiftRequest> shiftListShiftRequests = shiftListShiftRequestService.findByShiftIdIn(weekShifts);
        // 週シフト申請
        List<ShiftRequest> weekShiftRequests = new ArrayList<ShiftRequest>();
        // シフト時間変更申請が出されているシフトを取得したい
        List<ShiftChangeRequest> shiftChangeRequests = new ArrayList<ShiftChangeRequest>();

        for(ShiftListShiftRequest shiftListShiftRequest : shiftListShiftRequests)
        {
            if(Objects.isNull(shiftListShiftRequest.getShiftChangeRequestId()) && Objects.isNull(shiftListShiftRequest.getShiftRequestId()))
            {
                // どちらもなければエラー
                status = 3;
            }
            else if(Objects.isNull(shiftListShiftRequest.getShiftRequestId()))
            {
                // シフト申請がなければエラー
                status = 3;
            }
            else if(Objects.isNull(shiftListShiftRequest.getShiftChangeRequestId()))
            {
                // シフト時間変更申請がなければシフト申請を利用
                weekShiftRequests.add(shiftListShiftRequest.getShiftRequestId());
            }
            else
            {
                // シフト時間変更申請がないということはないので利用
                shiftChangeRequests.add(shiftListShiftRequest.getShiftChangeRequestId());
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
            status = 7;
        }
        else if(Objects.equals(workTime, standardWorkTime))
        {
            // 労働時間が所定労働時間と等しい
            status = 1;
        }
        else
        {
            // 所定労働時間より短い
            status = 1;
        }
        return new Response(status);
    }

    @PostMapping("/send/legalcheck/shiftchange")
    public Response shiftChangeLegalCheck(@RequestBody LegalCheckShiftChangeInput legalCheckShiftChangeInput, HttpSession session)
    {
        StringToLocalDateTime stringToLocalDateTime = new StringToLocalDateTime();
        StringToDuration stringToDuration = new StringToDuration();

        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.findAccountByUsername(username);

        int status = 0;

        Shift shift = shiftService.findByAccountIdAndShiftId(account, legalCheckShiftChangeInput.getShiftId());
        if(Objects.isNull(shift))
        {
            status = 3;
            return new Response(status);
        }

        LocalDateTime beginWork = stringToLocalDateTime.stringToLocalDateTime(legalCheckShiftChangeInput.getBeginWork());
        LocalDateTime endWork = stringToLocalDateTime.stringToLocalDateTime(legalCheckShiftChangeInput.getEndWork());
        LocalDateTime beginBreak = stringToLocalDateTime.stringToLocalDateTime(legalCheckShiftChangeInput.getBeginBreak());
        LocalDateTime endBreak = stringToLocalDateTime.stringToLocalDateTime(legalCheckShiftChangeInput.getEndBreak());

        LegalTime legalTime = legalTimeService.findFirstByOrderByBeginDesc();
        // 労働時間が8時間(所定労働時間)になっているか確認
        // 休憩が1時間を超えているか確認
        Duration standardWorkTime = stringToDuration.stringToDuration(legalTime.getScheduleWorkTime());
        Duration morning = Duration.between(beginWork, beginBreak);
        Duration afternoon = Duration.between(endBreak, endWork);
        Duration workTime = morning.plus(afternoon);

        Duration breakTime = Duration.between(beginBreak, endBreak);
        Duration standardBreakTime = stringToDuration.stringToDuration(legalTime.getScheduleBreakTime());
        // 法定時間で初期化
        Duration standardWeekWorkTime = stringToDuration.stringToDuration(legalTime.getWeeklyWorkTime());
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
            status = 7;
        }

        // シフトに関連する申請を取得したい
        List<ShiftListShiftRequest> shiftListShiftRequests = shiftListShiftRequestService.findByShiftIdIn(weekShifts);
        // 週シフト申請
        List<ShiftRequest> weekShiftRequests = new ArrayList<ShiftRequest>();
        // シフト時間変更申請が出されているシフトを取得したい
        List<ShiftChangeRequest> shiftChangeRequests = new ArrayList<ShiftChangeRequest>();

        for(ShiftListShiftRequest shiftListShiftRequest : shiftListShiftRequests)
        {
            // シフト申請、シフト時間変更申請どちらもなければエラー
            if(Objects.isNull(shiftListShiftRequest.getShiftRequestId()) && Objects.isNull(shiftListShiftRequest.getShiftChangeRequestId()))
            {
                status = 3;
            }
            else if(Objects.isNull(shiftListShiftRequest.getShiftRequestId()))
            {
                status = 3;
            }
            else if(Objects.isNull(shiftListShiftRequest.getShiftChangeRequestId()))
            {
                // シフト時間変更申請がなければシフト申請を追加
                weekShiftRequests.add(shiftListShiftRequest.getShiftRequestId());
            }
            else
            {
                // シフト時間変更申請がないということはないので利用
                shiftChangeRequests.add(shiftListShiftRequest.getShiftChangeRequestId());
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
            status = 7;
        }
        else if(Objects.equals(workTime, standardWorkTime))
        {
            // 労働時間が所定労働時間と等しい
            status = 1;
        }
        else
        {
            // 所定労働時間より短い
            status = 1;
        }

        return new Response(status);
    }

    @PostMapping("/send/withdrow")
    public Response withDrow(@RequestBody WithDrowInput withDrowInput, HttpSession session)
    {
        Account account = accountService.findCurrentAccount();
        if(Objects.isNull(account))
        {
            return new Response(3);
        }
        int result = requestService.withdrow(account, withDrowInput);
        return new Response(result);
    }
    
    @PostMapping("/send/approval")
    public Response approval(@RequestBody RequestJudgmentInput requestJudgmentInput, HttpSession session)
    {
        Account account = accountService.findCurrentAccount();
        if(Objects.isNull(account))
        {
            return new Response(3);
        }
        int result = requestService.approval(account, requestJudgmentInput);
        return new Response(result);
    }

    @PostMapping("/send/reject")
    public Response reject(@RequestBody RequestJudgmentInput requestJudgmentInput, HttpSession session)
    {
        Account account = accountService.findCurrentAccount();
        if(Objects.isNull(account))
        {
            return new Response(3);
        }
        int result = requestService.reject(account, requestJudgmentInput);
        return new Response(result);
    }

    @PostMapping("/send/approvalcancel")
    public Response approvalcancel(@RequestBody RequestJudgmentInput requestJudgmentInput, HttpSession session)
    {
        Account account = accountService.findCurrentAccount();
        if(Objects.isNull(account))
        {
            return new Response(3);
        }
        int result = requestService.approvalCancel(account, requestJudgmentInput);
        return new Response(result);
    }

}