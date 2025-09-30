<script setup>
import { ref } from "vue";
import NavList from "../../components/NavList.vue";
import WorkControlPanel from "../../components/WorkControlPanel.vue";
import RequestReason from "../../components/RequestReason.vue";
import ReqestTime from "../../components/ReqestTime.vue";
import ApplyBtn from "../../components/ApplyBtn.vue";
import axios from "axios";
import { formatDay, formatTime } from "../../utils/datetime";

const isLoading = ref(false);

const today = new Date();
const year = today.getFullYear();
const month = String(today.getMonth() + 1).padStart(2, "0");
const day = String(today.getDate()).padStart(2, "0");
const hours = String(today.getHours()).padStart(2, "0");
const minutes = String(today.getMinutes()).padStart(2, "0");
const seconds = String(today.getSeconds()).padStart(2, "0");

const requestDate = ref(
  `${year}/${month}/${day}T${hours}:${minutes}:${seconds}`
); //API送信時間
const selectedDate = ref(`${year}-${month}-${day}`); //日付選択用(この形じゃないと日付表示できない)
const apiDate = ref(`${year}/${month}/${day}`); //API送信用(yyyy/MM/ddの形でおくるから)

const beginWork = ref(""); // 始業時刻
const endWork = ref(""); // 就業時刻
const beginBreak = ref(""); // 休憩時間
const endBreak = ref(""); // 休憩時間
const reasonText = ref(""); //申請理由テキスト

//シフト申請関数

/**
 * 
 *"beginWork": "2025/09/30T09:00:00",
  "endWork": "2025/09/30T18:00:00",
  "beginBreak": "12:00",
  "endBreak": "13:00",
  "requestComment": "テスト",
  "requestDate": "2025/09/30T18:45:12"
 */
const shiftPost = async () => {
  isLoading.value = true;
  // try {
  //   await axios.post("http://localhost:8080/api/send/login", {
  //     beginWork: beginWork.value,
  //     endWork: endWork.value,
  //     beginBreak: beginBreak.value,
  //     endBreak: endBreak.value,
  //     requestComment: reasonText.value,
  //     requestDate: requestDate.value,
  //   });
  // } catch (error) {
  //   console.error("シフト申請エラー", error);
  // } finally {
  //   isLoading.value = false;
  // }
  try {
    await axios.post("http://localhost:8080/api/send/shift", {
      beginWork: "2025/09/30T09:00:00",
      endWork: "2025/09/30T18:00:00",
      beginBreak: "12:00",
      endBreak: "13:00",
      requestComment: "テスト",
      requestDate: "2025/09/30T18:45:12",
    });
  } catch (error) {
    console.error("シフト申請エラー", error);
  } finally {
    isLoading.value = false;
  }
};
</script>

<template>
  <div
    v-if="isLoading"
    class="fixed inset-0 w-full h-full bg-gray-400/50 flex justify-center items-center z-[9999]"
  >
    <div
      class="w-12 h-12 border-4 border-gray-200 border-t-green-500 rounded-full animate-spin"
    ></div>
  </div>
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
          :day="selectedDate"
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
