<script setup>
import axios from "axios";
import {
  getCurrentDateTime,
  getCurrentDate,
  convertToApiDate,
} from "../../utils/datetime";
import { ref, watch, onMounted, computed } from "vue";
import ApplyBtn from "../../components/ApplyBtn.vue";
import NavList from "../../components/NavList.vue";
import ReqestTime from "../../components/ReqestTime.vue";
import RequestReason from "../../components/RequestReason.vue";
import SelectShift from "../../components/SelectShift.vue";
import WorkControlPanel from "../../components/WorkControlPanel.vue";
import ShiftComplete from "../../components/ShiftComplete.vue";
import LoadingScreen from "../../components/LoadingScreen.vue";

const isLoading = ref(false);
const isCompleteModal = ref(false); //モーダル表示用
const selectedShiftId = ref(null); // シフト選択変数
const beginWork = ref(""); // 始業時刻
const endWork = ref(""); // 就業時刻
const beginBreak = ref(""); // 休憩時間
const endBreak = ref(""); // 休憩時間
const reasonText = ref(""); //申請理由テキスト
const errorMsg = ref("");

// beginBreak: "2025/10/11T12:29:00";
// beginWork: "2025/10/11T09:50:00";
// endBreak: "2025/10/11T13:29:00";
// endWork: "2025/10/11T18:50:00";
// id: 16;
// lateness: "00:00:00";
// leaveEarly: "00:00:00";
// outing: "00:12:00";
// overWork: "00:00:00";

const shifts = ref([]);
const getShiftList = async () => {
  try {
    const now = new Date();
    const year = now.getFullYear();
    const month = now.getMonth() + 1;

    const response = await axios.get(
      "http://localhost:8080/api/reach/shiftlist",
      {
        params: { year, month },
        withCredentials: true,
      }
    );

    shifts.value = response.data;
    console.log(shifts.value)
  } catch (error) {
    console.error("シフトリスト取得エラー:", error);
  }
};

// watch(selectedShiftId, (newId) => {
//   const selected = shifts.value?.shiftList?.find((s) => s.id === newId);
//   if (selected) {
//     beginWork.value = selected.beginWork;
//     endWork.value = selected.endWork;
//     beginBreak.value = selected.beginBreak;
//     endBreak.value = selected.endBreak;
//   } else {
//     beginWork.value = "";
//     endWork.value = "";
//     beginBreak.value = "";
//     endBreak.value = "";
//   }
// });


// selectedShiftIdが変わったときに自動で反映
watch(selectedShiftId, (newId) => {
  const selected = shifts.value?.shiftList?.find((s) => s.id === newId);
  if (selected) {
    //時刻コピー
    beginWork.value = selected.beginWork;
    endWork.value = selected.endWork;
    beginBreak.value = selected.beginBreak;
    endBreak.value = selected.endBreak;

    //日付コピー
    selectedDate.value = selected.beginWork.split("T")[0];
    //console.log(selected);
  } else {
    beginWork.value = "";
    endWork.value = "";
    beginBreak.value = "";
    endBreak.value = "";
  }
});

const requestDate = ref(getCurrentDateTime()); //API送信時間
const selectedDate = ref(getCurrentDate()); //日付選択用(この形じゃないと日付表示できない)

//勤務時間変更申請関数
const TimeChangePost = async () => {

  //時間の入力チェック
  if (selectedShiftId.value === null) {
    errorMsg.value = "対象シフトを選択してください";
    return;
  }
  if (!beginWork.value || !endWork.value || !beginBreak.value || !endBreak.value) {
    errorMsg.value = "すべての時間を入力してください";
    return;
  }

  errorMsg.value = "";
  isLoading.value = true;
  try {
    await axios.post(
      "http://localhost:8080/api/send/changetime",
      {
        shiftId: selectedShiftId.value,
        beginWork: beginWork.value,
        endWork: endWork.value,
        beginBreak: beginBreak.value,
        endBreak: endBreak.value,
        requestComment: reasonText.value,
        requestDate: requestDate.value,
      },
      { withCredentials: true }
    );
    //申請成功
    isCompleteModal.value = true;
  } catch (error) {
    console.error("シフト申請エラー", error);
  } finally {
    isLoading.value = false;
  }
};

onMounted(() => {
  getShiftList();
});
</script>


<!--時間変更申請ページ-->
<template>
  <div class="flex h-screen text-base">
    <NavList />
    <main class="flex-1 p-6 bg-gray-100 overflow-auto pt-25 lg:ml-64 lg:pt-7">
      <h1 class="text-xl font-bold mb-3 md:mb-6 text-center">
        勤務時間変更申請
      </h1>

      <div class="bg-white p-4 md:p-6 rounded-lg shadow-md space-y-5">
        <!-- 対象シフト選択 -->
        <SelectShift
          :shifts="shifts?.shiftList || []"
          v-model:selectedShiftId="selectedShiftId"
          v-model:beginWork="beginWork"
          v-model:endWork="endWork"
          v-model:beginBreak="beginBreak"
          v-model:endBreak="endBreak"
        />

        <div v-if="selectedShiftId">
          <!-- 始業,就業,休憩時間-->
          <WorkControlPanel
            v-model:selectedDate="selectedDate"
            v-model:beginWork="beginWork"
            v-model:endWork="endWork"
            v-model:beginBreak="beginBreak"
            v-model:endBreak="endBreak"
          />

          <!-- 申請理由 -->
          <RequestReason v-model:reasonText="reasonText" />

          <!-- 申請時間 -->
          <ReqestTime
            :day="selectedDate"
            :beginWork="beginWork"
            :endWork="endWork"
            :beginBreak="beginBreak"
            :endBreak="endBreak"
          />
        </div>

        <!-- 申請ボタン -->
        <ApplyBtn :TimeChangePost="TimeChangePost" :errorMsg="errorMsg" />
      </div>
    </main>
  </div>
  <!--ローディング画面-->
  <LoadingScreen :isLoading="isLoading" />
  <!--申請完了-->
  <ShiftComplete v-model:isCompleteModal="isCompleteModal" />
</template>
