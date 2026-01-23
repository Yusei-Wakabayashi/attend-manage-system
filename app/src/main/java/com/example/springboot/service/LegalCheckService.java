package com.example.springboot.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.example.springboot.dto.change.StringToDuration;
import com.example.springboot.dto.change.StringToLocalDateTime;
import com.example.springboot.dto.input.LegalCheckShiftChangeInput;
import com.example.springboot.dto.input.LegalCheckShiftInput;
import com.example.springboot.model.Account;
import com.example.springboot.model.LegalTime;
import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftChangeRequest;
import com.example.springboot.model.ShiftListShiftRequest;
import com.example.springboot.model.ShiftRequest;
import com.example.springboot.model.Vacation;

@Service
public class LegalCheckService
{
    private final StringToLocalDateTime stringToLocalDateTime;
    private final StringToDuration stringToDuration;
    private final ShiftService shiftService;
    private final LegalTimeService legalTimeService;
    private final ShiftListShiftRequestService shiftListShiftRequestService;
    private final VacationService vacationService;

    public LegalCheckService
    (
        StringToLocalDateTime stringToLocalDateTime,
        StringToDuration stringToDuration,
        ShiftService shiftService,
        LegalTimeService legalTimeService,
        ShiftListShiftRequestService shiftListShiftRequestService,
        VacationService vacationService
    )
    {
        this.stringToLocalDateTime = stringToLocalDateTime;
        this.stringToDuration = stringToDuration;
        this.shiftService = shiftService;
        this.legalTimeService = legalTimeService;
        this.shiftListShiftRequestService = shiftListShiftRequestService;
        this.vacationService = vacationService;
    }

    public int shiftLegalCheck(Account account, LegalCheckShiftInput legalCheckShiftInput)
    {
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
            return 7;
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
                return 3;
            }
            else if(Objects.isNull(shiftListShiftRequest.getShiftRequestId()))
            {
                // シフト申請がなければエラー
                return 3;
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
            return 7;
        }
        else if(Objects.equals(workTime, standardWorkTime))
        {
            // 労働時間が所定労働時間と等しい
            return 1;
        }
        else
        {
            // 所定労働時間より短い
            return 1;
        }
    }

    public int shiftChangeLegalCheck(Account account, LegalCheckShiftChangeInput legalCheckShiftChangeInput)
    {
        Shift shift = shiftService.findByAccountIdAndShiftId(account, legalCheckShiftChangeInput.getShiftId());
        if(Objects.isNull(shift))
        {
            return 3;
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
            return 3;
        }

        // すでにあるシフトと今回申請される1件を足した件数が一週間(7)から法定休日の日数を引いた数より大きければエラー
        // 法定休日を超えないようにしたい
        if((weekShifts.size() + 1) > (7 - legalTime.getWeeklyHoliday()))
        {
            return 7;
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
                return 3;
            }
            else if(Objects.isNull(shiftListShiftRequest.getShiftRequestId()))
            {
                return 3;
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
            return 7;
        }
        else if(Objects.equals(workTime, standardWorkTime))
        {
            // 労働時間が所定労働時間と等しい
            return  1;
        }
        else
        {
            // 所定労働時間より短い
            return  1;
        }
    }
}
