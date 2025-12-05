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

import com.example.springboot.dto.change.DurationToString;
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
import com.example.springboot.model.AccountApprover;
import com.example.springboot.model.Attend;
import com.example.springboot.model.AttendanceExceptionRequest;
import com.example.springboot.model.AttendanceListSource;
import com.example.springboot.model.LegalTime;
import com.example.springboot.model.MonthlyRequest;
import com.example.springboot.model.NewsList;
import com.example.springboot.model.OverTimeRequest;
import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftChangeRequest;
import com.example.springboot.model.ShiftListOtherTime;
import com.example.springboot.model.ShiftListOverTime;
import com.example.springboot.model.ShiftListShiftRequest;
import com.example.springboot.model.ShiftListVacation;
import com.example.springboot.model.ShiftRequest;
import com.example.springboot.model.StampRequest;
import com.example.springboot.model.Vacation;
import com.example.springboot.model.VacationRequest;
import com.example.springboot.service.AccountApproverService;
import com.example.springboot.service.AccountService;
import com.example.springboot.service.AttendService;
import com.example.springboot.service.AttendanceExceptionRequestService;
import com.example.springboot.service.AttendanceExceptionTypeService;
import com.example.springboot.service.AttendanceListSourceService;
import com.example.springboot.service.LegalTimeService;
import com.example.springboot.service.MonthlyRequestService;
import com.example.springboot.service.NewsListService;
import com.example.springboot.service.OverTimeRequestService;
import com.example.springboot.service.PaydHolidayService;
import com.example.springboot.service.RequestService;
import com.example.springboot.service.ShiftChangeRequestService;
import com.example.springboot.service.ShiftListOtherTimeService;
import com.example.springboot.service.ShiftListOverTimeService;
import com.example.springboot.service.ShiftListShiftRequestService;
import com.example.springboot.service.ShiftListVacationService;
import com.example.springboot.service.ShiftRequestService;
import com.example.springboot.service.ShiftService;
import com.example.springboot.service.StampRequestService;
import com.example.springboot.service.StylePlaceService;
import com.example.springboot.service.StyleService;
import com.example.springboot.service.VacationRequestService;
import com.example.springboot.service.VacationService;
import com.example.springboot.service.VacationTypeService;
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

    @Autowired
    private VacationRequestService vacationRequestService;

    @Autowired
    private ShiftListOverTimeService shiftListOverTimeService;

    @Autowired
    private OverTimeRequestService overTimeRequestService;

    @Autowired
    private MonthlyRequestService monthlyRequestService;

    @Autowired
    private AttendanceExceptionRequestService attendanceExceptionRequestService;

    @Autowired
    private ShiftListOtherTimeService shiftListOtherTimeService;

    @Autowired
    private ShiftListVacationService shiftListVacationService;

    @Autowired
    private AttendService attendService;

    @Autowired
    private AttendanceListSourceService attendanceListSourceService;

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
        int status = 0;
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.findAccountByUsername(username);
        if(Objects.isNull(account))
        {
            status = 3;
            return new Response(status);
        }

        int requestType = withDrowInput.getRequestType();
        Long requestId = withDrowInput.getRequestId();

        // 月次申請以外はその申請のステータスを3の却下に変更する
        // 月次申請はその範囲の月次申請済みの申請を承認に戻す作業が必要
        switch (requestType)
        {
            // シフト申請
            case 1:
                ShiftRequest shiftRequest = shiftRequestService.findByAccountIdAndShiftRequestId(account, requestId);
                // 申請がない場合
                if(Objects.isNull(shiftRequest))
                {
                    status = 3;
                    return new Response(status);
                }
                // 申請の状態チェック
                if(shiftRequest.getRequestStatus() == 1)
                {
                    shiftRequest.setRequestStatus(5);
                    // 正常に更新できたか
                    ShiftRequest resultShiftRequest = shiftRequestService.save(shiftRequest);
                    if(Objects.isNull(resultShiftRequest))
                    {
                        status = 3;
                        return new Response(status);
                    }
                    status = 1;
                }
                else
                {
                    status = 3;
                    return new Response(status);
                }
                break;
            // シフト時間変更申請
            case 2:
                ShiftChangeRequest shiftChangeRequest = shiftChangeRequestService.findByAccountIdAndShiftChangeRequestId(account, requestId);
                // 申請がない場合
                if(Objects.isNull(shiftChangeRequest))
                {
                    status = 3;
                    return new Response(status);
                }
                // 申請の状態チェック
                if(shiftChangeRequest.getRequestStatus() == 1)
                {
                    shiftChangeRequest.setRequestStatus(5);
                    // 正常に更新できたか
                    ShiftChangeRequest resultShiftChangeRequest = shiftChangeRequestService.save(shiftChangeRequest);
                    if(Objects.isNull(resultShiftChangeRequest))
                    {
                        status = 3;
                        return new Response(status);
                    }
                    status = 1;
                }
                else
                {
                    status = 3;
                    return new Response(status);
                }
                break;
            // 打刻漏れ申請
            case 3:
                StampRequest stampRequest = stampRequestService.findByAccountIdAndStampId(account, requestId);
                // 申請がない場合
                if(Objects.isNull(stampRequest))
                {
                    status = 3;
                    return new Response(status);
                }
                // 申請の状態チェック
                if(stampRequest.getRequestStatus() == 1)
                {
                    stampRequest.setRequestStatus(5);
                    // 正常に更新できたか
                    StampRequest resultStampRequest = stampRequestService.save(stampRequest);
                    if(Objects.isNull(resultStampRequest))
                    {
                        status = 3;
                        return new Response(status);
                    }
                    status = 1;
                }
                else
                {
                    status = 3;
                    return new Response(status);
                }
                break;
            // 休暇申請
            case 4:
                VacationRequest vacationRequest = vacationRequestService.findByAccountIdAndVacationId(account, requestId);
                // 申請がない場合
                if(Objects.isNull(vacationRequest))
                {
                    status = 3;
                    return new Response(status);
                }
                // 申請の状態チェック
                if(vacationRequest.getRequestStatus() == 1)
                {
                    vacationRequest.setRequestStatus(5);
                    // 正常に更新できたか
                    VacationRequest resultVacationRequest = vacationRequestService.save(vacationRequest);
                    if(Objects.isNull(resultVacationRequest))
                    {
                        status = 3;
                        return new Response(status);
                    }
                    status = 1;
                }
                else
                {
                    status = 3;
                    return new Response(status);
                }
                break;
            // 残業申請
            case 5:
                OverTimeRequest overTimeRequest = overTimeRequestService.findByAccountIdAndOverTimeRequestId(account, requestId);
                // 申請がない場合
                if(Objects.isNull(overTimeRequest))
                {
                    status = 3;
                    return new Response(status);
                }
                // 申請の状態チェック
                if(overTimeRequest.getRequestStatus() == 1)
                {
                    overTimeRequest.setRequestStatus(5);
                    OverTimeRequest resultOverTimeRequest = overTimeRequestService.save(overTimeRequest);
                    if(Objects.isNull(resultOverTimeRequest))
                    {
                        status = 3;
                        return new Response(status);
                    }
                    status = 1;
                }
                else
                {
                    status = 3;
                    return new Response(status);
                }
                break;
            // 勤怠例外申請
            case 6:
                AttendanceExceptionRequest attendanceExceptionRequest = attendanceExceptionRequestService.findByAccountIdAndAttendanceExceptionId(account, requestId);
                // 申請がない場合
                if(Objects.isNull(attendanceExceptionRequest))
                {
                    status = 3;
                    return new Response(status);
                }
                // 申請の状態チェック
                if(attendanceExceptionRequest.getRequestStatus() == 1)
                {
                    attendanceExceptionRequest.setRequestStatus(5);
                    // 正常に更新できたか
                    AttendanceExceptionRequest resultAttendanceExceptionRequest = attendanceExceptionRequestService.save(attendanceExceptionRequest);
                    if(Objects.isNull(resultAttendanceExceptionRequest))
                    {
                        status = 3;
                        return new Response(status);
                    }
                    status = 1;
                }
                else
                {
                    status = 3;
                    return new Response(status);
                }
                break;
            // 月次申請
            case 7:
                MonthlyRequest monthlyRequest = monthlyRequestService.findByAccountIdAndMothlyRequestId(account, requestId);
                // 申請がない場合
                if(Objects.isNull(monthlyRequest))
                {
                    status = 3;
                    return new Response(status);
                }
                // 申請の状態チェック
                if(monthlyRequest.getRequestStatus() == 1)
                {
                    // 正常
                }
                else
                {
                    status = 3;
                    return new Response(status);
                }
                // 月次申請の期間内のシフト取得
                List<Shift> shifts = shiftService.findByAccountIdAndBeginWorkBetween(account, monthlyRequest.getYear(), monthlyRequest.getMonth());
                // シフトから関連テーブルを検索
                List<ShiftListOtherTime> shiftListOtherTimes = shiftListOtherTimeService.findByShiftIdIn(shifts);
                List<ShiftListOverTime> shiftListOverTimes = shiftListOverTimeService.findByShiftIdIn(shifts);
                List<ShiftListShiftRequest> shiftListShiftRequests = shiftListShiftRequestService.findByShiftIdIn(shifts);
                List<ShiftListVacation> shiftListVacations = shiftListVacationService.findByShiftIdIn(shifts);
                // 最終的に取得した申請のステータスを月次申請済みから承認に変更
                int monthlyRequestStatus = 2;
                for(ShiftListOtherTime shiftListOtherTime : shiftListOtherTimes)
                {
                    AttendanceExceptionRequest attendanceExceptionRequestMonthly = shiftListOtherTime.getAttendanceExceptionId();
                    attendanceExceptionRequestMonthly.setRequestStatus(monthlyRequestStatus);
                    attendanceExceptionRequestService.save(attendanceExceptionRequestMonthly);
                }
                for(ShiftListOverTime shiftListOverTime : shiftListOverTimes)
                {
                    OverTimeRequest overTimeRequestMonthly = shiftListOverTime.getOverTimeId();
                    overTimeRequestMonthly.setRequestStatus(monthlyRequestStatus);
                    overTimeRequestService.save(overTimeRequestMonthly);
                }
                for(ShiftListShiftRequest shiftListShiftRequest : shiftListShiftRequests)
                {
                    // どちらも存在しなければエラー
                    if(Objects.isNull(shiftListShiftRequest.getShiftChangeRequestId()) && Objects.isNull(shiftListShiftRequest.getShiftRequestId()))
                    {
                        status = 3;
                        return new Response(status);
                    }
                    else if(Objects.isNull(shiftListShiftRequest.getShiftRequestId()))
                    {
                        // シフト申請がなければエラー
                        status = 3;
                        return new Response(status);
                    }
                    // シフト時間変更申請がなければシフト申請を
                    else if(Objects.isNull(shiftListShiftRequest.getShiftChangeRequestId()))
                    {
                        ShiftRequest shiftRequestMonthly = shiftListShiftRequest.getShiftRequestId();
                        shiftRequestMonthly.setRequestStatus(monthlyRequestStatus);
                        shiftRequestService.save(shiftRequestMonthly);
                    }
                    // どちらも存在すればエラー
                    else
                    {
                        ShiftChangeRequest shiftChangeRequestMonthly = shiftListShiftRequest.getShiftChangeRequestId();
                        shiftChangeRequestMonthly.setRequestStatus(monthlyRequestStatus);
                        shiftChangeRequestService.save(shiftChangeRequestMonthly);
                    }
                }
                for(ShiftListVacation shiftListVacation : shiftListVacations)
                {
                    VacationRequest vacationRequestMonthly = shiftListVacation.getVacationId();
                    vacationRequestMonthly.setRequestStatus(monthlyRequestStatus);
                    vacationRequestService.save(vacationRequestMonthly);
                }
                // 月次申請の期間内の勤怠情報取得
                List<Attend> attends = attendService.findByAccountIdAndBeginWorkBetween(account, monthlyRequest.getYear(), monthlyRequest.getMonth());
                // 勤怠情報から関連テーブルを検索
                List<AttendanceListSource> attendanceListSources = attendanceListSourceService.findByAttendIdIn(attends);
                // 最終的に取得した申請のステータスを月次申請済みから承認に変更
                for(AttendanceListSource attendanceListSource : attendanceListSources)
                {
                    StampRequest stampRequestMonthly = attendanceListSource.getStampRequestId();
                    stampRequestMonthly.setRequestStatus(monthlyRequestStatus);
                    stampRequestService.save(stampRequestMonthly);
                }
                // 正常に更新できたか
                monthlyRequest.setRequestStatus(5);
                MonthlyRequest resultMonthlyRequest = monthlyRequestService.save(monthlyRequest);
                if(Objects.isNull(resultMonthlyRequest))
                {
                    status = 3;
                    return new Response(status);
                }
                status = 1;
                break;
            default:
                break;
        }
        return new Response(status);
    }
    
    @PostMapping("/send/approval")
    public Response approval(@RequestBody RequestJudgmentInput requestJudgmentInput, HttpSession session)
    {
        int status = 0;
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.findAccountByUsername(username);
        if(Objects.isNull(account))
        {
            status = 3;
            return new Response(status);
        }

        // 申請を取得
        // 申請者の承認者であることを確認
        // 申請の状態が申請待ち状態であることを確認

        int request = requestJudgmentInput.getRequestType();
        switch (request)
        {
            // シフト申請
            case 1:
                ShiftRequest shiftRequest = shiftRequestService.findById(requestJudgmentInput.getRequestId());
                Account shiftGeneralAccount = shiftRequest.getAccountId();
                AccountApprover shiftAccountApprover = accountApproverService.findAccountAndApprover(shiftGeneralAccount, account);
                // 申請者の承認者の情報がなければエラー
                if(Objects.isNull(shiftAccountApprover))
                {
                    status = 3;
                    return new Response(status);
                }
                // 申請の状態確認
                if(shiftRequest.getRequestStatus() == 1)
                {
                    // 承認に変更
                    shiftRequest.setRequestStatus(2);
                    shiftRequestService.save(shiftRequest);
                }
                else
                {
                    status = 3;
                    return new Response(status);
                }
                // シフトに記録
                Shift shiftRequestShift = shiftRequestService.shiftRequestToShift(shiftRequest);
                Shift resultShiftRequestShift = shiftService.save(shiftRequestShift);
                if(Objects.isNull(resultShiftRequestShift))
                {
                    status = 3;
                    return new Response(status);
                }
                // シフトとシフトに関する申請テーブル登録
                ShiftListShiftRequest shiftShiftListShiftRequest = new ShiftListShiftRequest();
                // シフト設定
                shiftShiftListShiftRequest.setShiftId(resultShiftRequestShift);
                // シフト申請設定
                shiftShiftListShiftRequest.setShiftRequestId(shiftRequest);
                ShiftListShiftRequest resultShiftShiftListShiftRequest = shiftListShiftRequestService.save(shiftShiftListShiftRequest);
                // 結果をチェック
                if(Objects.isNull(resultShiftShiftListShiftRequest))
                {
                    status = 3;
                    return new Response(status);
                }
                status = 1;
                break;
            // シフト時間変更申請
            case 2:
                ShiftChangeRequest shiftChangeRequest = shiftChangeRequestService.findById(requestJudgmentInput.getRequestId());
                Account shiftChangeGeneralAccount = shiftChangeRequest.getAccountId();
                AccountApprover shiftChangeAccountApprover = accountApproverService.findAccountAndApprover(shiftChangeGeneralAccount, account);
                if(Objects.isNull(shiftChangeAccountApprover))
                {
                    status = 3;
                    return new Response(status);
                }
                // シフトで既に勤怠情報が確定していればエラー
                AttendanceListSource shiftChangeAttendanceListSource = attendanceListSourceService.findByShiftId(shiftChangeRequest.getShiftId());
                if(Objects.isNull(shiftChangeAttendanceListSource))
                {
                    // nullが想定通り
                }
                else
                {
                    status = 3;
                    return new Response(status);
                }
                // 申請の状態確認
                if(shiftChangeRequest.getRequestStatus() == 1)
                {
                    // 承認に変更
                    shiftChangeRequest.setRequestStatus(2);
                    shiftChangeRequestService.save(shiftChangeRequest);
                }
                else
                {
                    status = 3;
                    return new Response(status);
                }
                // シフトに記録
                Shift shiftChangeRequestShift = shiftChangeRequestService.shiftChangeRequestToShift(shiftChangeRequest);
                Shift resultShiftChangeRequestShift = shiftService.save(shiftChangeRequestShift);
                if(Objects.isNull(resultShiftChangeRequestShift))
                {
                    status = 3;
                    return new Response(status);
                }
                // シフトとシフトに関する申請テーブル
                ShiftListShiftRequest shiftChangeShiftListShiftRequest = shiftListShiftRequestService.findByShiftId(resultShiftChangeRequestShift);
                // 時間変更申請を登録
                shiftChangeShiftListShiftRequest.setShiftChangeRequestId(shiftChangeRequest);
                ShiftListShiftRequest resultShiftChangeShiftListShiftRequest = shiftListShiftRequestService.save(shiftChangeShiftListShiftRequest);
                // 結果をチェック
                if(Objects.isNull(resultShiftChangeShiftListShiftRequest))
                {
                    status = 3;
                    return new Response(status);
                }
                // シフトに関連する申請で反映状態のものがあれば却下し関連テーブルは削除する
                List<ShiftListOtherTime> shiftChangeShiftListOtherTimes = shiftListOtherTimeService.findByShiftId(resultShiftChangeRequestShift);
                for(ShiftListOtherTime shiftChangeShiftListOtherTime : shiftChangeShiftListOtherTimes)
                {
                    AttendanceExceptionRequest shiftChangeAttendanceExceptionRequest = shiftChangeShiftListOtherTime.getAttendanceExceptionId();
                    shiftChangeAttendanceExceptionRequest.setRequestStatus(3);
                    shiftListOtherTimeService.deleteByShiftListOtherTime(shiftChangeShiftListOtherTime);
                }
                List<ShiftListOverTime> shiftChangeShiftListOverTimes = shiftListOverTimeService.findByShiftId(resultShiftChangeRequestShift);
                for(ShiftListOverTime shiftChangeShiftListOverTime : shiftChangeShiftListOverTimes)
                {
                    OverTimeRequest shiftChangeOverTimeRequest = shiftChangeShiftListOverTime.getOverTimeId();
                    shiftChangeOverTimeRequest.setRequestStatus(3);
                    shiftListOverTimeService.deleteByShiftListOverTime(shiftChangeShiftListOverTime);
                }
                List<ShiftListVacation> shiftChangeShiftListVacations = shiftListVacationService.findByShiftId(resultShiftChangeRequestShift);
                for(ShiftListVacation shiftChangeShiftListVacation : shiftChangeShiftListVacations)
                {
                    VacationRequest shiftChangeVacationRequest = shiftChangeShiftListVacation.getVacationId();
                    shiftChangeVacationRequest.setRequestStatus(3);
                    shiftListVacationService.deleteByShiftListVacationId(shiftChangeShiftListVacation);
                }
                status = 1;
                break;
            // 打刻漏れ申請
            case 3:
                StampRequest stampRequest = stampRequestService.findById(requestJudgmentInput.getRequestId());

                // 勤怠テーブルに記録
                // 遅刻時間、早退時間、外出時間、労働時間、休憩時間、残業時間、休日労働時間(法定時間より)、深夜労働時間(法定時間より取得した内容から計算)、休暇時間()、欠勤時間()
                // 勤怠と勤怠に関する情報源テーブル
                break;
            // 休暇申請
            case 4:
                VacationRequest vacationRequest = vacationRequestService.findById(requestJudgmentInput.getRequestId());
                // シフトの休暇時間
                // シフトと休暇テーブル
                // 休暇テーブル(計算の際に間に休憩が挟まっているもしくは休暇の前後に休憩が入っている場合休憩を除いて計算する必要がある)
                // 有給休暇消費テーブル
                break;
            // 残業申請
            case 5:
                OverTimeRequest overTimeRequest = overTimeRequestService.findById(requestJudgmentInput.getRequestId());
                // シフトの残業時間
                // シフトと残業テーブル
                break;
            // 遅刻、早退、残業申請
            case 6:
                AttendanceExceptionRequest attendanceExceptionRequest = attendanceExceptionRequestService.findById(requestJudgmentInput.getRequestId());
                // シフトの遅刻時間、早退時間、外出時間
                // シフト内で労働時間内にどれだけ外出しているか計算
                // シフトと勤怠例外テーブル
                break;
            // 月次申請
            case 7:
                MonthlyRequest monthlyRequest = monthlyRequestService.findById(requestJudgmentInput.getRequestId());
                // 申請者の承認者か
                Account monthGeneralAccount = monthlyRequest.getAccountId();
                if(Objects.isNull(monthGeneralAccount))
                {
                    status = 3;
                    return new Response(status);
                }
                // 申請の状態は?
                if(monthlyRequest.getRequestStatus() == 1)
                {
                    monthlyRequest.setRequestStatus(2);
                    monthlyRequestService.save(monthlyRequest);
                }
                else
                {
                    status = 3;
                    return new Response(status);
                }
                status = 1;
                break;

            default:
                status = 3;
                break;
        }
        return new Response(status);
    }

    @PostMapping("/send/reject")
    public Response reject(@RequestBody RequestJudgmentInput requestJudgmentInput, HttpSession session)
    {
       int status = 0;
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.findAccountByUsername(username);
        if(Objects.isNull(account))
        {
            status = 3;
            return new Response(status);
        }

        // 申請を取得
        // 申請者の承認者であることを確認
        // 申請の状態が申請待ち状態であることを確認

        int request = requestJudgmentInput.getRequestType();
        switch (request)
        {
            // シフト申請
            case 1:
                ShiftRequest shiftRequest = shiftRequestService.findById(requestJudgmentInput.getRequestId());
                Account generalAccount = shiftRequest.getAccountId();
                AccountApprover accountApprover = accountApproverService.findAccountAndApprover(generalAccount, account);
                if(Objects.isNull(accountApprover))
                {
                    status = 3;
                    return new Response(status);
                }
                // 申請が反映状態にあるか確認
                // 反映状態でなければよし
                // 反映状態ならを反映を解除し
                // 申請の状態を変更
                break;
            // シフト時間変更申請
            case 2:
                ShiftChangeRequest shiftChangeRequest = shiftChangeRequestService.findById(requestJudgmentInput.getRequestId());
                // 申請の状態を変更

                break;
            // 打刻漏れ申請
            case 3:
                StampRequest stampRequest = stampRequestService.findById(requestJudgmentInput.getRequestId());
                // 申請の状態を変更

                break;
            // 休暇申請
            case 4:
                VacationRequest vacationRequest = vacationRequestService.findById(requestJudgmentInput.getRequestId());
                // 申請の状態を変更

                break;
            // 残業申請
            case 5:
                OverTimeRequest overTimeRequest = overTimeRequestService.findById(requestJudgmentInput.getRequestId());
                // 申請の状態を変更

                break;
            // 遅刻、早退、残業申請
            case 6:
                AttendanceExceptionRequest attendanceExceptionRequest = attendanceExceptionRequestService.findById(requestJudgmentInput.getRequestId());
                // 申請の状態を変更

                break;
            // 月次申請
            case 7:
                MonthlyRequest monthlyRequest = monthlyRequestService.findById(requestJudgmentInput.getRequestId());
                // 申請の状態を変更
                // 範囲内の月次申請済みの申請の状態を承認に戻す

                break;

            default:
                break;
        }

        return new Response(status);
    }

    @PostMapping("/send/approvalcancel")
    public Response approvalcancel(@RequestBody RequestJudgmentInput requestJudgmentInput, HttpSession session)
    {
       int status = 0;
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.findAccountByUsername(username);
        if(Objects.isNull(account))
        {
            status = 3;
            return new Response(status);
        }

        // 申請を取得
        // 申請者の承認者であることを確認
        // 申請の状態が申請待ち状態であることを確認

        int request = requestJudgmentInput.getRequestType();
        switch (request)
        {
            // シフト申請
            case 1:
                ShiftRequest shiftRequest = shiftRequestService.findById(requestJudgmentInput.getRequestId());
                Account generalAccount = shiftRequest.getAccountId();
                AccountApprover accountApprover = accountApproverService.findAccountAndApprover(generalAccount, account);
                if(Objects.isNull(accountApprover))
                {
                    status = 3;
                    return new Response(status);
                }
                // シフトの削除
                // シフトとシフトに関する申請テーブル
                break;
            // シフト時間変更申請
            case 2:
                ShiftChangeRequest shiftChangeRequest = shiftChangeRequestService.findById(requestJudgmentInput.getRequestId());
                // 同じ日の承認済みの時間変更申請で更新日時が最も最近のものに変更
                // 上記がなければ同じ日のシフトを取得、これもない場合シフトを削除
                // シフトとシフトに関する申請テーブル

                break;
            // 打刻漏れ申請
            case 3:
                StampRequest stampRequest = stampRequestService.findById(requestJudgmentInput.getRequestId());

                // 勤怠と勤怠に関する情報源テーブルを基に勤怠テーブルから削除
                // 勤怠と勤怠に関する情報源テーブル
                break;
            // 休暇申請
            case 4:
                VacationRequest vacationRequest = vacationRequestService.findById(requestJudgmentInput.getRequestId());
                // シフトの休暇時間
                // シフトと休暇テーブル
                // 休暇テーブル
                // 有給休暇消費テーブルから対象を削除
                break;
            // 残業申請
            case 5:
                OverTimeRequest overTimeRequest = overTimeRequestService.findById(requestJudgmentInput.getRequestId());
                // シフトの残業時間
                // シフトと残業テーブル
                break;
            // 遅刻、早退、残業申請
            case 6:
                AttendanceExceptionRequest attendanceExceptionRequest = attendanceExceptionRequestService.findById(requestJudgmentInput.getRequestId());
                // シフトの遅刻時間、早退時間、外出時間
                // シフトと勤怠例外テーブル
                break;
            // 月次申請
            case 7:
                MonthlyRequest monthlyRequest = monthlyRequestService.findById(requestJudgmentInput.getRequestId());
                // 月次申請の範囲の申請を承認に変更
                break;

            default:
                break;
        }

        return new Response(status);

    }

}