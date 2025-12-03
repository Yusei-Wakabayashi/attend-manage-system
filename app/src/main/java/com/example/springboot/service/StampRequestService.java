package com.example.springboot.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.dto.change.StringToLocalDateTime;
import com.example.springboot.dto.input.StampInput;
import com.example.springboot.model.Account;
import com.example.springboot.model.MonthlyRequest;
import com.example.springboot.model.Shift;
import com.example.springboot.model.StampRequest;
import com.example.springboot.repository.StampRequestRepository;

@Service
public class StampRequestService
{
    private final StampRequestRepository stampRequestRepository;
    private final ShiftService shiftService;
    private final StringToLocalDateTime stringToLocalDateTime;
    private final MonthlyRequestService monthlyRequestService;

    @Autowired
    public StampRequestService
    (
        StampRequestRepository stampRequestRepository,
        ShiftService shiftService,
        StringToLocalDateTime stringToLocalDateTime,
        MonthlyRequestService monthlyRequestService
    )
    {
        this.stampRequestRepository = stampRequestRepository;
        this.shiftService = shiftService;
        this.stringToLocalDateTime = stringToLocalDateTime;
        this.monthlyRequestService = monthlyRequestService;
    }

    public int createStampRequest(Account account, StampInput stampInput)
    {
        // アカウントの情報とシフトの情報を基に検索
        Shift shift = shiftService.findByAccountIdAndShiftId(account, stampInput.getShiftId());
        if(Objects.isNull(shift))
        {
            return 4;
        }
        // 打刻漏れ申請で同じシフトで既に承認済みのものがないか確認
        List<StampRequest> stampRequests = findByShiftIdAndRequestStatusWait(shift);
        if(stampRequests.size() > 0)
        {
            return 3;
        }
        LocalDateTime nowTime = LocalDateTime.now();
        // シフトの終業時刻が現在時刻より前であること
        if(shift.getEndWork().isBefore(nowTime))
        {
            // 条件通りなら何もしない
        }
        else
        {
            return 3;
        }

        // 申請するシフトの年月で月次申請が申請されていればエラー
        int searchYear = shift.getBeginWork().getYear();
        int searchMonth = shift.getBeginWork().getMonthValue();
        List<MonthlyRequest> monthlyRequests = monthlyRequestService.findByAccountIdAndYearAndMonth(account, searchYear, searchMonth);
        if(monthlyRequests.size() > 0)
        {
            return 3;
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
            return 3;
        }

        // 始業と終業、休憩の開始と終了がシフトの時間と一致していること
        if(shift.getBeginWork().compareTo(beginWork) == 0 && shift.getEndWork().compareTo(endWork) == 0 && shift.getBeginBreak().compareTo(beginBreak) == 0 && shift.getEndBreak().compareTo(endBreak) == 0)
        {
            // 一致していたら何もしない
        }
        else
        {
            return 3;
        }
        int requestStatus = 1;
        // 打刻漏れ申請登録(サービス層で行うべき？)
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
        StampRequest resultStampRequest = save(stampRequest);
        if(Objects.isNull(resultStampRequest) || Objects.isNull(resultStampRequest.getStampId()))
        {
            return 3;
        }
        return 1;
    }

    public StampRequest findById(Long id)
    {
        return stampRequestRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("打刻漏れ申請がありません"));
    }

    public StampRequest findByAccountIdAndStampId(Account account, Long id)
    {
        return stampRequestRepository.findByAccountIdAndStampId(account, id)
            .orElseThrow(() -> new RuntimeException("打刻漏れ申請がありません"));
    }

    public StampRequest findByAccountIdAndStampId(Long accountId, Long id)
    {
        Account account = new Account();
        account.setId(accountId);
        return stampRequestRepository.findByAccountIdAndStampId(account, id)
            .orElseThrow(() -> new RuntimeException("打刻漏れ申請がありません"));
    }

    public List<StampRequest> findByShiftIdAndRequestStatusWait(Shift shift)
    {
        int status = 1;
        List<StampRequest> stampRequests = stampRequestRepository.findByShiftIdAndRequestStatus(shift, status);
        return stampRequests;
    }
    public List<StampRequest> findByShiftIdAndRequestStatusWait(Long shiftId)
    {
        int status = 1;
        Shift shift = new Shift();
        shift.setShiftId(shiftId);
        List<StampRequest> stampRequests = stampRequestRepository.findByShiftIdAndRequestStatus(shift, status);
        return stampRequests;
    }

    public List<StampRequest> findByAccountId(Long accountId)
    {
        Account account = new Account();
        account.setId(accountId);
        return stampRequestRepository.findByAccountId(account);
    }

    public List<StampRequest> findByAccountIdIn(List<Account> accounts)
    {
        List<StampRequest> stampRequests = stampRequestRepository.findByAccountIdIn(accounts);
        return stampRequests;
    }

    public List<StampRequest> findByAccountId(Account account)
    {
        return stampRequestRepository.findByAccountId(account);
    }

    public List<StampRequest> findByAccountIdAndBeginWorkBetweenAndRequestStatusWait(Long accountId, int year, int month)
    {
        Account account = new Account();
        account.setId(accountId);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        int wait = 1;
        List<StampRequest> stampRequests = stampRequestRepository.findByAccountIdAndBeginWorkBetweenAndRequestStatus(account, startPeriod, endPeriod, wait);
        return stampRequests;
    }

    public List<StampRequest> findByAccountIdAndBeginWorkBetweenAndRequestStatusWait(Account account, int year, int month)
    {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        int wait = 1;
        List<StampRequest> stampRequests = stampRequestRepository.findByAccountIdAndBeginWorkBetweenAndRequestStatus(account, startPeriod, endPeriod, wait);
        return stampRequests;
    }

    public StampRequest save(StampRequest stampRequest)
    {
        return stampRequestRepository.save(stampRequest);
    }

    @Transactional
    public void resetAllTables()
    {
        stampRequestRepository.deleteAll();
        stampRequestRepository.resetAutoIncrement();
    }

    public void init()
    {
        
    }
}
