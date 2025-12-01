package com.example.springboot.service;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.dto.change.StringToLocalDateTime;
import com.example.springboot.dto.input.ShiftInput;
import com.example.springboot.model.Account;
import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftRequest;
import com.example.springboot.repository.ShiftRequestRepository;

@Service
public class ShiftRequestService
{
    // 再代入不可で定義
    private final ShiftRequestRepository shiftRequestRepository;
    private final ShiftService shiftService;
    private final StringToLocalDateTime stringToLocalDateTime;
    // コンストラクタ定義(依存性注入)
    @Autowired // 念のため記載
    public ShiftRequestService
    (
        ShiftRequestRepository shiftRequestRepository,
        ShiftService shiftService,
        StringToLocalDateTime stringToLocalDateTime
    )
    {
        this.shiftRequestRepository = shiftRequestRepository;
        this.shiftService = shiftService;
        this.stringToLocalDateTime = stringToLocalDateTime;
    }

    public List<ShiftRequest> findAllShiftRequest()
    {
        return shiftRequestRepository.findAll();
    }

    public ShiftRequest findById(Long id)
    {
        return shiftRequestRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("シフト申請が見つかりません"));
    }

    public int createShiftRequest(Account account, ShiftInput shiftInput)
    {
        // 始業時間、終業時間、休憩開始時間、休憩終了時間が現在取得時間より後になっていることを確認
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime beginWork = stringToLocalDateTime.stringToLocalDateTime(shiftInput.getBeginWork());
        LocalDateTime endWork = stringToLocalDateTime.stringToLocalDateTime(shiftInput.getEndWork());
        LocalDateTime beginBreak = stringToLocalDateTime.stringToLocalDateTime(shiftInput.getBeginBreak());
        LocalDateTime endBreak = stringToLocalDateTime.stringToLocalDateTime(shiftInput.getEndBreak());

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
        // 承認されたシフトで同じ日に重複していないことを確認
        List<Shift> shifts = shiftService.findByAccountIdAndDayBeginWorkBetween(account, beginWork);
        // シフト申請で同じ日に申請が出ていないことを確認
        List<ShiftRequest> shiftRequests = findAccountIdAndBeginWorkBetweenDay(account, beginWork);
        // 0より大きかったら申請できない
        if(shiftRequests.size() > 0 || shifts.size() > 0)
        {
            return 3;
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
            return 3;
        }
        // シフト申請に登録(サービス層で行うべきこと？)
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
        ShiftRequest resultShiftRequest = save(shiftRequest);
        if(Objects.isNull(resultShiftRequest) || Objects.isNull(resultShiftRequest.getShiftRequestId()))
        {
            return 3;
        }
        return 1;
    }

    public List<ShiftRequest> findAccountIdAndBeginWorkBetween(Account accountId, LocalDateTime beginWork, LocalDateTime endWork)
    {
        return shiftRequestRepository.findByAccountIdAndBeginWorkBetween(accountId, beginWork, endWork);
    }

    public List<ShiftRequest> findAccountIdAndBeginWorkBetweenDay(Account accountId, LocalDateTime begin)
    {
        // 始業時間から1日の開始と終了を作成
        LocalDateTime beginWork = LocalDateTime.of(begin.toLocalDate(), LocalTime.MIN);
        LocalDateTime endWork = LocalDateTime.of(begin.toLocalDate(), LocalTime.MAX);
        return shiftRequestRepository.findByAccountIdAndBeginWorkBetween(accountId, beginWork, endWork);
    }

    public List<ShiftRequest> findAccountIdAndBeginWorkBetweenAndRequestStatusWaitWeek(Long id, LocalDateTime begin)
    {
        Account account = new Account();
        account.setId(id);
        LocalDateTime beginWork = begin.toLocalDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
        LocalDateTime endWork = begin.toLocalDate().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).atTime(LocalTime.MAX);
        // 承認済みステータスが存在してほしくない
        int requestStatus = 2;
        List<ShiftRequest> shiftRequests = shiftRequestRepository.findByAccountIdAndBeginWorkBetweenAndRequestStatus(account, beginWork, endWork, requestStatus);
        return shiftRequests;
    }

    public List<ShiftRequest> findAccountIdAndBeginWorkBetweenAndRequestStatusWaitWeek(Account account, LocalDateTime begin)
    {
        LocalDateTime beginWork = begin.toLocalDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
        LocalDateTime endWork = begin.toLocalDate().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).atTime(LocalTime.MAX);
        // 承認済みステータスが存在してほしくない
        int requestStatus = 2;
        List<ShiftRequest> shiftRequests = shiftRequestRepository.findByAccountIdAndBeginWorkBetweenAndRequestStatus(account, beginWork, endWork, requestStatus);
        return shiftRequests;
    }

    public ShiftRequest findByAccountIdAndShiftRequestId(Account account, Long id)
    {
        return shiftRequestRepository.findByAccountIdAndShiftRequestId(account, id)
            .orElseThrow(() -> new RuntimeException("シフト申請が見つかりません"));
    }

    public ShiftRequest findByAccountIdAndShiftRequestId(Long accountId, Long id)
    {
        Account account = new Account();
        account.setId(accountId);
        return shiftRequestRepository.findByAccountIdAndShiftRequestId(account, id)
            .orElseThrow(() -> new RuntimeException("シフト申請が見つかりません"));
    }

    public List<ShiftRequest> findByAccountId(Long accountId)
    {
        Account account = new Account();
        account.setId(accountId);
        return shiftRequestRepository.findByAccountId(account);
    }

    public List<ShiftRequest> findByAccountId(Account account)
    {
        return shiftRequestRepository.findByAccountId(account);
    }    

    public List<ShiftRequest> findByAccountIdIn(List<Account> accounts)
    {
        List<ShiftRequest> shiftRequests = shiftRequestRepository.findByAccountIdIn(accounts);
        return shiftRequests;
    }

    public List<ShiftRequest> findByAccountIdAndBeginWorkBetweenAndRequestStatusWait(Long accountId, int year, int month)
    {
        Account account = new Account();
        account.setId(accountId);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        int wait = 1;
        List<ShiftRequest> shiftRequests = shiftRequestRepository.findByAccountIdAndBeginWorkBetweenAndRequestStatus(account, startPeriod, endPeriod, wait);
        return shiftRequests;
    }

    public List<ShiftRequest> findByAccountIdAndBeginWorkBetweenAndRequestStatusWait(Account account, int year, int month)
    {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDateTime startPeriod = firstDay.atStartOfDay();
        LocalDateTime endPeriod = lastDay.atTime(23,59,59);
        int wait = 1;
        List<ShiftRequest> shiftRequests = shiftRequestRepository.findByAccountIdAndBeginWorkBetweenAndRequestStatus(account, startPeriod, endPeriod, wait);
        return shiftRequests;
    }

    // modelに関するものは入力値で考える
    public Shift shiftRequestToShift(ShiftRequest shiftRequest)
    {
        Shift shift = new Shift();
        shift.setAccountId(shiftRequest.getAccountId());
        shift.setBeginWork(shiftRequest.getBeginWork());
        shift.setEndWork(shiftRequest.getEndWork());
        shift.setBeginBreak(shiftRequest.getBeginBreak());
        shift.setEndBreak(shiftRequest.getEndBreak());
        shift.setLateness(Time.valueOf("00:00:00"));
        shift.setLeaveEarly(Time.valueOf("00:00:00"));
        shift.setOuting(Time.valueOf("00:00:00"));
        shift.setOverWork(Time.valueOf("00:00:00"));
        return shift;
    }

    public ShiftRequest save(ShiftRequest shiftRequest)
    {
        return shiftRequestRepository.save(shiftRequest);
    }

    @Transactional
    public void resetAllTables()
    {
        shiftRequestRepository.deleteAll();
        shiftRequestRepository.resetAutoIncrement();
    }
}