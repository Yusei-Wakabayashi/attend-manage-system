<script setup>
import axios from "axios";
import { getCurrentDateTime, getCurrentDate, convertToApiDate } from "../../utils/datetime";
import { ref, computed } from "vue";
import NavList from "../../components/NavList.vue";
import WorkControlPanel from "../../components/WorkControlPanel.vue";
import RequestReason from "../../components/RequestReason.vue";
import ReqestTime from "../../components/ReqestTime.vue";
import ApplyBtn from "../../components/ApplyBtn.vue";
import ShiftComplete from "../../components/ShiftComplete.vue";
import LoadingScreen from "../../components/LoadingScreen.vue";

const isLoading = ref(false);
const isCompleteModal = ref(false); //モーダル表示用
const beginWork = ref(""); //始業時刻
const endWork = ref(""); //就業時刻
const beginBreak = ref(""); //休憩時間
const endBreak = ref(""); //休憩時間
const reasonText = ref(""); //申請理由テキスト

const requestDate = ref(getCurrentDateTime()); //API送信時間
const selectedDate = ref(getCurrentDate()); //日付選択用(この形じゃないと日付表示できない)
//API送信用(yyyy/MM/ddの形でおくるから)
const apiDate = computed(() => {
  return convertToApiDate(selectedDate.value);
})

//シフト申請関数
const shiftPost = async () => {
  isLoading.value = true;
  try {
    await axios.post(
      "http://localhost:8080/api/send/shift",
      {
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
</script>

<template>
  <!--ローディング画面-->
  <LoadingScreen :isLoading="isLoading" />
  <!--申請完了-->
  <ShiftComplete v-model:isCompleteModal="isCompleteModal" />

  <div class="flex h-screen text-base">
    <NavList />
    <main class="flex-1 p-6 bg-gray-100 overflow-auto pt-25 lg:ml-64 lg:pt-7">
      <h1 class="text-xl font-bold mb-3 md:mb-6 text-center">シフト申請</h1>

      <div class="bg-white p-4 md:p-6 rounded-lg shadow-md space-y-5">
        <!--申請日付-->
        <div>
          <label class="block font-semibold md:mb-2">日付</label>
          <input
            type="date"
            v-model="selectedDate"
            class="border rounded px-3 py-2 w-full max-w-xs"
          />
        </div>

        <!-- 始業,就業,休憩時刻-->
        <WorkControlPanel
          v-model:selectedDate="apiDate"
          v-model:beginWork="beginWork"
          v-model:endWork="endWork"
          v-model:beginBreak="beginBreak"
          v-model:endBreak="endBreak"
        />

        <!-- 申請理由 -->
        <RequestReason v-model:reasonText="reasonText" />

        <!-- 申請時刻 -->
        <ReqestTime
          :day="apiDate"
          :beginWork="beginWork"
          :endWork="endWork"
          :beginBreak="beginBreak"
          :endBreak="endBreak"
        />

        <!--申請,戻るボタン-->
        <ApplyBtn :shiftPost="shiftPost" />
      </div>
    </main>
  </div>
</template>
