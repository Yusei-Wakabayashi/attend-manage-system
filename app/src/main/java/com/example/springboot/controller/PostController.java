package com.example.springboot.controller;

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

import com.example.springboot.dto.response.Response;
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
import com.example.springboot.service.AccountApproverService;
import com.example.springboot.service.AccountService;
import com.example.springboot.service.RequestService;
import com.example.springboot.service.StyleService;

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