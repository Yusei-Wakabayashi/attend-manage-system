package com.example.springboot.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.dto.LoginPostData;
import com.example.springboot.dto.change.StringToDuration;
import com.example.springboot.dto.change.StringToLocalDateTime;
import com.example.springboot.dto.response.Response;
import com.example.springboot.dto.input.LegalCheckShiftChangeInput;
import com.example.springboot.dto.input.LegalCheckShiftInput;
import com.example.springboot.dto.input.MonthlyInput;
import com.example.springboot.dto.input.OtherTimeInput;
import com.example.springboot.dto.input.OverTimeInput;
import com.example.springboot.dto.input.ShiftChangeInput;
import com.example.springboot.dto.input.ShiftInput;
import com.example.springboot.dto.input.StampInput;
import com.example.springboot.dto.input.VacationInput;
import com.example.springboot.dto.input.WithDrowInput;
import com.example.springboot.dto.IdData;
import com.example.springboot.model.Account;
import com.example.springboot.model.AccountApprover;
import com.example.springboot.model.AttendanceExceptionRequest;
import com.example.springboot.model.LegalTime;
import com.example.springboot.model.MonthlyRequest;
import com.example.springboot.model.OverTimeRequest;
import com.example.springboot.model.PaydHoliday;
import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftChangeRequest;
import com.example.springboot.model.ShiftListOtherTime;
import com.example.springboot.model.ShiftListOverTime;
import com.example.springboot.model.ShiftListShiftRequest;
import com.example.springboot.model.ShiftRequest;
import com.example.springboot.model.StampRequest;
import com.example.springboot.model.Style;
import com.example.springboot.model.StylePlace;
import com.example.springboot.model.Vacation;
import com.example.springboot.model.VacationRequest;
import com.example.springboot.service.AccountApproverService;
// import com.example.springboot.model.Salt;
import com.example.springboot.service.AccountService;
import com.example.springboot.service.AttendanceExceptionRequestService;
import com.example.springboot.service.AttendanceExceptionTypeService;
import com.example.springboot.service.LegalTimeService;
import com.example.springboot.service.MonthlyRequestService;
import com.example.springboot.service.OverTimeRequestService;
import com.example.springboot.service.PaydHolidayService;
import com.example.springboot.service.ShiftChangeRequestService;
import com.example.springboot.service.ShiftListOtherTimeService;
import com.example.springboot.service.ShiftListOverTimeService;
import com.example.springboot.service.ShiftListShiftRequestService;
import com.example.springboot.service.ShiftRequestService;
import com.example.springboot.service.ShiftService;
import com.example.springboot.service.StampRequestService;
import com.example.springboot.service.StylePlaceService;
import com.example.springboot.service.StyleService;
import com.example.springboot.service.VacationRequestService;
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

    @Autowired
    private VacationRequestService vacationRequestService;

    @Autowired
    private ShiftListOverTimeService shiftListOverTimeService;

    @Autowired
    private PaydHolidayService paydHolidayService;

    @Autowired
    private OverTimeRequestService overTimeRequestService;

    @Autowired
    private MonthlyRequestService monthlyRequestService;

    @Autowired
    private AttendanceExceptionRequestService attendanceExceptionRequestService;

    @Autowired
    private AttendanceExceptionTypeService attendanceExceptionTypeService;

    @Autowired
    private ShiftListOtherTimeService shiftListOtherTimeService;
    
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
                    new UsernamePasswordAuthenticationToken(account.getUsername(), null, List.of(new SimpleGrantedAuthority("ROLE_USER"))); // 認証が完了しているためpasswordなどの情報は保存していない
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
            // 1年後に収まっていない始業時間ならエラー
            status = 3;
            return new Response(status);
        }
        status = 1;
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

        status = 1;
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

        // 申請するシフトの年月で月次申請が申請されていればエラー
        int searchYear = shift.getBeginWork().getYear();
        int searchMonth = shift.getBeginWork().getMonthValue();
        List<MonthlyRequest> monthlyRequests = monthlyRequestService.findByAccountIdAndYearAndMonth(account, searchYear, searchMonth);
        if(monthlyRequests.size() > 0)
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

    @PostMapping("/send/vacation")
    public Response vacationSet(@RequestBody VacationInput vacationInput, HttpSession session)
    {
        StringToLocalDateTime stringToLocalDateTime = new StringToLocalDateTime();
        StringToDuration stringToDuration = new StringToDuration();
        int status = 0;
        // アカウントの取得
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.getAccountByUsername(username);
        // 休暇の時刻が形式に沿っているか
        LocalDateTime beginVacation = stringToLocalDateTime.stringToLocalDateTime(vacationInput.getBeginVacation());
        LocalDateTime endVacation = stringToLocalDateTime.stringToLocalDateTime(vacationInput.getEndVacation());

        // 休暇開始の後に休暇終了の形になっているか
        if(endVacation.isAfter(beginVacation))
        {
            // 形式通りなら何もしない
        }
        else
        {
            // それ以外ならエラー
            status = 3;
            return new Response(status);
        }
        // 現在時刻より後なら正常
        LocalDateTime nowTime = LocalDateTime.now();
        if(beginVacation.isAfter(nowTime))
        {
            // 正常
        }
        else
        {
            status = 4;
            return new Response(status);
        }
        // シフトの時間内に収まっているのか
        Shift shift = shiftService.findByAccountIdAndShiftId(account, vacationInput.getShiftId());
        // 始業時間より休暇の開始または終了が前ならエラー、終業時間より休暇の開始または終了が後ならエラー
        if(beginVacation.isBefore(shift.getBeginWork()) || endVacation.isBefore(shift.getBeginWork()) || beginVacation.isAfter(shift.getEndWork()) || endVacation.isAfter(shift.getEndWork()))
        {
            status = 3;
            return new Response(status);
        }
        // すでに申請されている休暇と重複していないか
        List<VacationRequest> vacationRequests = vacationRequestService.findByAccountIdAndShiftId(account, shift);
        for(VacationRequest vacationRequest : vacationRequests)
        {
            // 申請済みの休暇の休暇開始と休暇終了の間に、今回申請する休暇開始、休暇終了のどちらかが存在していればエラー
            if((vacationRequest.getBeginVacation().isBefore(beginVacation) && vacationRequest.getEndVacation().isAfter(beginVacation)) || (vacationRequest.getBeginVacation().isBefore(endVacation) && vacationRequest.getEndVacation().isAfter(endVacation)))
            {
                status = 3;
                return new Response(status);
            }
        }
        // 残業の前後(始業前の残業直後、終業後の残業直前)でないこと
        List<ShiftListOverTime> shiftListOverTimes = shiftListOverTimeService.findByShiftId(shift);
        for(ShiftListOverTime shiftListOverTime : shiftListOverTimes)
        {
            LocalDateTime beginOverTime = shiftListOverTime.getOverTimeId().getBeginWork();
            LocalDateTime endOverTime = shiftListOverTime.getOverTimeId().getEndWork();
            // 残業の終了の後に休暇の開始(同じ時刻ならfalse)、残業の開始の前に休暇の終了(同じ時刻ならfalse)
            if(beginVacation.isAfter(endOverTime) || endVacation.isBefore(beginOverTime))
            {
                // 正常動作
            }
            else
            {
                // 残業終了の前に休暇の開始、または休暇の終了の後に終業後の残業開始が存在していればエラー
                status = 3;
                return new Response(status);     
            }
        }

        // 残業申請の承認待ちでも行う
        List<OverTimeRequest> overTimeRequests = overTimeRequestService.findByAccountIdAndShiftIdAndRequestStatusWait(account, shift);
        for(OverTimeRequest overTimeRequest : overTimeRequests)
        {
            LocalDateTime requestBeginOverTime = overTimeRequest.getBeginWork();
            LocalDateTime requestEndOverTime = overTimeRequest.getEndWork();
            // 残業の終了の後に休暇の開始(同じ時刻ならfalse)、残業の開始の前に休暇の終了(同じ時刻ならfalse)
            if(beginVacation.isAfter(requestEndOverTime) || endVacation.isBefore(requestBeginOverTime))
            {
                // 正常動作
            }
            else
            {
                // 残業終了の前に休暇の開始、または休暇の終了の後に終業後の残業開始が存在していればエラー
                status = 3;
                return new Response(status);            
            }
        }
        // 有給の場合承認待ちの有給申請の時間も含め足りるかを計算
        switch (vacationInput.getVacationType())
        {
            // 有給の場合
            case 1:
                // 使用可能な有給を取得
                List<PaydHoliday> paydHolidays = paydHolidayService.findByAccountIdAndLimitAfter(account);
                Duration availableHoliday = Duration.ZERO;
                for(PaydHoliday paydHoliday : paydHolidays)
                {
                    availableHoliday = availableHoliday.plus(stringToDuration.stringToDuration(paydHoliday.getTime()));
                }
                // 申請によって予約済みの有給を取得
                List<VacationRequest> vacationRequestPaydHolidays = vacationRequestService.findByAccountIdAndRequestStatusWaitAndVacationTypePaydHoiday(account);
                Duration usedHoliday = Duration.ZERO;
                for(VacationRequest vacationRequest : vacationRequestPaydHolidays)
                {
                    usedHoliday = usedHoliday.plus(Duration.between(vacationRequest.getBeginVacation(), vacationRequest.getEndVacation()));
                }
                // 使用可能な有給から予約済みの有給を減算
                availableHoliday = availableHoliday.minus(usedHoliday);
                // 申請された有給と使用可能な有給を比較し等しいか、大きければtrue
                Duration requestHoliday = Duration.between(beginVacation, endVacation);
                if(availableHoliday.compareTo(requestHoliday) >= 0)
                {
                    // 正常
                }
                else
                {
                    status = 8;
                    return new Response(status);
                }
                break;
            
            // 代休の場合
            case 2:
                break;

            // 欠勤の場合
            case 3:
                break;

            // 忌引きの場合
            case 4:
                break;
            
            // 特別休暇の場合
            case 5:
                break;
            
            // 子供休暇
            case 6:
                break;
            // 介護休暇
            case 7:
                break;
            // 保存休暇
            case 8:
                break;

            default:

                break;
        }
        status = 1;
        Response response = new Response(status);
        return response;
    }

    @PostMapping("/send/othertime")
    public Response otherTimeSet(@RequestBody OtherTimeInput otherTimeInput, HttpSession session)
    {
        StringToLocalDateTime stringToLocalDateTime = new StringToLocalDateTime();
        // アカウントの取得
        int status = 0;
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.getAccountByUsername(username);
        if(Objects.isNull(account))
        {
            status = 3;
            return new Response(status);
        }
        Shift shift = shiftService.findByAccountIdAndShiftId(account, otherTimeInput.getShiftId());
        if(Objects.isNull(shift))
        {
            status = 3;
            return new Response(status);
        }
        // 開始の後に終了が存在すること
        LocalDateTime startTime = stringToLocalDateTime.stringToLocalDateTime(otherTimeInput.getBeginOtherTime());
        LocalDateTime endTime = stringToLocalDateTime.stringToLocalDateTime(otherTimeInput.getEndOtherTime());
        if(endTime.isAfter(startTime))
        {
            // 条件通りなら何もしない
        }
        else
        {
            status = 3;
            return new Response(status);
        }
        // shiftidを基に反映されている残業申請の一覧を取得
        List<ShiftListOverTime> shiftListOverTimes = shiftListOverTimeService.findByShiftId(shift);
        // shiftidを基にシフトに関する申請の取得
        ShiftListShiftRequest shiftListShiftRequest = shiftListShiftRequestService.findByShiftId(shift);
        switch (otherTimeInput.getOtherType().intValue())
        {
            // 外出申請の場合
            // シフト内か確認
            // 重複NG(承認待ち、承認済み問わず)
            // すでに申請されている申請をシフトidとステータス(申請済み、承認済み)で検索
            // shiftIdと申請したい時間帯で検索時間帯の中にその他の外出申請の開始または、終了があればエラー
            case 1:
                // 重なる時間を許容したいのでequalで等しい場合にもtrueになるように
                if ((startTime.isAfter(shift.getBeginWork()) || startTime.isEqual(shift.getBeginWork())) &&
                (endTime.isBefore(shift.getEndWork()) || endTime.isEqual(shift.getEndWork())))
                {
                    // 条件通りなら何もしない
                }
                else
                {
                    status = 3;
                    return new Response(status);
                }
                // 重複する外出申請の取得
                List<AttendanceExceptionRequest> attendanceExceptionRequests = attendanceExceptionRequestService.findByAccountIdAndShiftIdAndOutingAndBeginTimeBetweenOrEndTimeBetweenAndRequestStatusWaitOrRequestStatusApproved(account, shift, startTime, endTime);
                if(attendanceExceptionRequests.size() >0)
                {
                    // 0より大きければ重複する内容があったことになるのでエラー
                    status = 3;
                    return new Response(status);
                }
                break;
            // 遅刻申請の場合
            // 始業時間は元となるシフト申請の始業時間
            // 始業時間と一致すること
            // 始業前の残業が存在する場合申請NG
            // 休暇と被る場合も申請NG
            case 2:
                // 始業前の残業が存在しないか確認
                for(ShiftListOverTime shiftListOverTime : shiftListOverTimes)
                {
                    // 遅刻の開始の前に残業の終了が存在すればエラー
                    if(shiftListOverTime.getOverTimeId().getEndWork().isBefore(startTime))
                    {
                        status = 3;
                        return new Response(status);
                    }
                }
                LocalDateTime beginTime;
                // シフト時間変更申請がなければシフト申請で
                if(Objects.isNull(shiftListShiftRequest.getShiftChangeRequestId()))
                {
                    ShiftRequest shiftRequest = shiftRequestService.findById(shiftListShiftRequest.getShiftRequestId().getShiftRequestId());
                    beginTime = shiftRequest.getBeginWork();
                }
                // シフト申請がなければシフト時間変更申請で
                else if(Objects.isNull(shiftListShiftRequest.getShiftRequestId()))
                {
                    ShiftChangeRequest shiftChangeRequest = shiftChangeRequestService.findById(shiftListShiftRequest.getShiftChangeRequestId().getShiftChangeId());
                    beginTime = shiftChangeRequest.getBeginWork();
                }
                else
                {
                    // どちらでもなければエラー
                    status = 3;
                    return new Response(status);
                }
                // 元となる申請の取得をしたら条件と合致するかシフトの就業まで(終業時間調度は許容)に終了するか
                if(startTime.isEqual(beginTime) && (startTime.isBefore(shift.getEndWork()) || startTime.isEqual(shift.getEndWork())))
                {
                    // 一致すれば何もしない
                }
                else
                {
                    status = 3;
                    return new Response(status);
                }
                break;
            // 早退申請の場合
            // 終業時間は元となるシフト申請の終業時間
            // 終業時間と一致すること
            // 始業より後に早退が存在すること
            // 終業後の残業が存在する場合申請NG
            // 休暇と被る場合も申請NG
            case 3:
                // 始業前の残業が存在しないか確認
                for(ShiftListOverTime shiftListOverTime : shiftListOverTimes)
                {
                    // 早退の終了後に残業が存在すればエラー
                    if(shiftListOverTime.getOverTimeId().getBeginWork().isAfter(endTime))
                    {
                        status = 3;
                        return new Response(status);
                    }
                }

                LocalDateTime closeTime;
                // シフト時間変更申請がなければシフト申請で
                if(Objects.isNull(shiftListShiftRequest.getShiftChangeRequestId()))
                {
                    ShiftRequest shiftRequest = shiftRequestService.findById(shiftListShiftRequest.getShiftRequestId().getShiftRequestId());
                    closeTime = shiftRequest.getEndWork();
                }
                // シフト申請がなければシフト時間変更申請で
                else if(Objects.isNull(shiftListShiftRequest.getShiftRequestId()))
                {
                    ShiftChangeRequest shiftChangeRequest = shiftChangeRequestService.findById(shiftListShiftRequest.getShiftChangeRequestId().getShiftChangeId());
                    closeTime = shiftChangeRequest.getEndWork();
                }
                else
                {
                    // どちらでもなければエラー
                    status = 3;
                    return new Response(status);
                }
                // 元となる申請の取得をしたら条件と合致するかシフトの開始(シフト開始直後は許容)の後始まるか
                if(endTime.isEqual(closeTime) && (startTime.isAfter(shift.getBeginWork()) || startTime.isEqual(shift.getBeginWork())))
                {
                    // 一致すれば何もしない
                }
                else
                {
                    status = 3;
                    return new Response(status);
                }
                break;
            default:
                break;
        }
        AttendanceExceptionRequest attendanceExceptionRequest = new AttendanceExceptionRequest();
        attendanceExceptionRequest.setAccountId(account);
        attendanceExceptionRequest.setBeginTime(startTime);
        attendanceExceptionRequest.setEndTime(endTime);
        attendanceExceptionRequest.setAttendanceExceptionTypeId(attendanceExceptionTypeService.findByAttendanceExceptionTypeId(otherTimeInput.getOtherType()));
        attendanceExceptionRequest.setRequestComment(otherTimeInput.getRequestComment());
        attendanceExceptionRequest.setRequestDate(otherTimeInput.getRequestDate() == null ? null : stringToLocalDateTime.stringToLocalDateTime(otherTimeInput.getRequestDate()));
        attendanceExceptionRequest.setRequestStatus(1);
        attendanceExceptionRequest.setShiftId(shift);
        if(attendanceExceptionRequestService.save(attendanceExceptionRequest) == "ok");
        {
            status = 1;
        }
        return new Response(status);
    }

    @PostMapping("/send/overtime")
    public Response overTimeSet(@RequestBody OverTimeInput overTimeInput, HttpSession session)
    {
        StringToDuration stringToDuration = new StringToDuration();
        StringToLocalDateTime stringToLocalDateTime = new StringToLocalDateTime();
        // アカウントの取得
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.getAccountByUsername(username);
        int status = 0;
        if(Objects.isNull(account))
        {
            status = 3;
            return new Response(status);
        }

        Shift shift = shiftService.findByAccountIdAndShiftId(account, overTimeInput.getShiftId());
        if(Objects.isNull(shift))
        {
            status = 3;
            return new Response(status);
        }
        // 残業の開始の後に残業の終了があること
        LocalDateTime startTimeOverWork = stringToLocalDateTime.stringToLocalDateTime(overTimeInput.getBeginOverTime());
        LocalDateTime endTimeOverWork = stringToLocalDateTime.stringToLocalDateTime(overTimeInput.getEndOverTime());
        if(endTimeOverWork.isAfter(startTimeOverWork))
        {
            // 条件通りなら何もしない
        }
        else
        {
            status = 3;
            return new Response(status);
        }
        // 既に存在する残業と重複する場合申請NG
        List<OverTimeRequest> overTimeRequests = overTimeRequestService.findByAccounIdAndRequestStatusWaitOrApprovedAndBeginWorkOrEndWorkBetween(account, startTimeOverWork, endTimeOverWork);
        if(overTimeRequests.size() > 0)
        {
            // 1件でも存在すれば重複があったということなのでエラー
            status = 3;
            return new Response(status);
        }
        // シフトと時間が重なっていないこと
        List<Shift> shifts = shiftService.shiftOverLapping(account, startTimeOverWork, endTimeOverWork);
        if(shifts.size() > 0)
        {
            // 1件でも存在すれば重複があったということなのでエラー
            status = 3;
            return new Response(status);
        }
        // 遅刻早退との重複NG
        List<ShiftListOtherTime> shiftListOtherTimes = shiftListOtherTimeService.findByShiftId(shift);
        List<ShiftListOtherTime> shiftListOtherTimesLateness = new ArrayList<ShiftListOtherTime>();
        List<ShiftListOtherTime> shiftListOtherTimesLeaveEarly = new ArrayList<ShiftListOtherTime>();
        // 勤怠例外のうち遅刻と早退を取得
        for(ShiftListOtherTime shiftListOtherTime : shiftListOtherTimes)
        {
            if(shiftListOtherTime.getAttendanceExceptionId().getAttendanceExceptionTypeId().getAttendanceExceptionTypeId() == 2L)
            {
                shiftListOtherTimesLateness.add(shiftListOtherTime);
            }
            else if(shiftListOtherTime.getAttendanceExceptionId().getAttendanceExceptionTypeId().getAttendanceExceptionTypeId() == 3L)
            {
                shiftListOtherTimesLeaveEarly.add(shiftListOtherTime);
            }
        }
        // 申請する残業が始業前で遅刻が存在する場合
        if(endTimeOverWork.isEqual(shift.getBeginWork()) && shiftListOtherTimesLateness.size() > 0)
        {
            status = 3;
            return new Response(status);
        }
        // 申請する残業が終業後で早退が存在する場合
        if(startTimeOverWork.isEqual(shift.getEndWork()) && shiftListOtherTimesLeaveEarly.size() > 0)
        {
            status = 3;
            return new Response(status);
        }
        // 法定時間を超える申請(承認待ち、承認済み)はNG
        // 申請する月でそのアカウントの残業申請(承認待ち、承認済み)を取得法定時間を超えないことを確認
        List<OverTimeRequest> overTimeRequestMonthList = overTimeRequestService.findByAccountIdAndRequestStatusWaitOrApprovedAndBeginWorkBetweenMonth(account, startTimeOverWork.getYear(), startTimeOverWork.getMonthValue());
        Duration monthOverWorkTime = Duration.between(startTimeOverWork, endTimeOverWork);
        for(OverTimeRequest overTimeRequest : overTimeRequestMonthList)
        {
            Duration overTime = Duration.between(overTimeRequest.getBeginWork(), overTimeRequest.getEndWork());
            monthOverWorkTime = monthOverWorkTime.plus(overTime);
        }
        LegalTime legalTime = legalTimeService.getFirstByOrderByBeginDesc();
        // メソッド内の値より後(大きい)ならエラー
        if(monthOverWorkTime.compareTo(stringToDuration.stringToDuration(legalTime.getMonthlyOverWorkTime())) > 0)
        {
            status = 3;
            return new Response(status);
        }
        OverTimeRequest overTimeRequest = new OverTimeRequest();
        overTimeRequest.setAccountId(account);
        overTimeRequest.setBeginWork(startTimeOverWork);
        overTimeRequest.setEndWork(endTimeOverWork);
        overTimeRequest.setRequestComment(overTimeInput.getRequestComment());
        overTimeRequest.setRequestDate(overTimeInput.getRequestDate() == null ? null : stringToLocalDateTime.stringToLocalDateTime(overTimeInput.getRequestDate()));
        overTimeRequest.setRequestStatus(1);
        overTimeRequest.setShiftId(shift);
        if(overTimeRequestService.save(overTimeRequest) == "ok")
        {
            status = 1;
        }
        return new Response(status);
    }

    @PostMapping("/send/monthly")
    public Response monthlySet(@RequestBody MonthlyInput monthlyInput, HttpSession session)
    {
        // お知らせから月次申請を行う年月の情報が不足している場合申請不可
        Response response = new Response();
        return response;
    }

    @PostMapping("/send/legalcheck/shift")
    public Response shiftLegalCheck(@RequestBody LegalCheckShiftInput legalCheckShiftInput, HttpSession session)
    {
        StringToLocalDateTime stringToLocalDateTime = new StringToLocalDateTime();
        StringToDuration stringToDuration = new StringToDuration();
        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.getAccountByUsername(username);
        int status = 0;

        LocalDateTime beginWork = stringToLocalDateTime.stringToLocalDateTime(legalCheckShiftInput.getBeginWork());
        LocalDateTime endWork = stringToLocalDateTime.stringToLocalDateTime(legalCheckShiftInput.getEndWork());
        LocalDateTime beginBreak = stringToLocalDateTime.stringToLocalDateTime(legalCheckShiftInput.getBeginBreak());
        LocalDateTime endBreak = stringToLocalDateTime.stringToLocalDateTime(legalCheckShiftInput.getEndBreak());

        LegalTime legalTime = legalTimeService.getFirstByOrderByBeginDesc();
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
            if(Objects.isNull(shiftListShiftRequest.getShiftChangeRequestId()))
            {
                // シフト時間変更申請がなければシフト申請を追加
                weekShiftRequests.add(shiftListShiftRequest.getShiftRequestId());
            }
            else if(Optional.ofNullable(shiftListShiftRequest.getShiftChangeRequestId()).isPresent())
            {
                // シフト時間変更申請があればシフト時間変更申請を追加
                shiftChangeRequests.add(shiftListShiftRequest.getShiftChangeRequestId());
            }
            else
            {
                // どちらでもなければエラー
                status = 7;
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
        return new Response(status);
    }

    @PostMapping("/send/legalcheck/shiftchange")
    public Response shiftChangeLegalCheck(@RequestBody LegalCheckShiftChangeInput legalCheckShiftChangeInput, HttpSession session)
    {
        StringToLocalDateTime stringToLocalDateTime = new StringToLocalDateTime();
        StringToDuration stringToDuration = new StringToDuration();

        String username = SecurityUtil.getCurrentUsername();
        Account account = accountService.getAccountByUsername(username);

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

        LegalTime legalTime = legalTimeService.getFirstByOrderByBeginDesc();
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
                status = 7;
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

        return new Response(status);
    }
    @PostMapping("/send/withdrow")
    public Response withDrow(@RequestBody WithDrowInput withDrowInput, HttpSession session)
    {
        return new Response();
    }
    
    @PostMapping("/send/approval")
    public Response approval(@RequestBody WithDrowInput withDrowInput, HttpSession session)
    {
        return new Response();
    }

}