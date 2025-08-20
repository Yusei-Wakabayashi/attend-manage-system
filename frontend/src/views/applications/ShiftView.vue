<script setup>
import { ref } from "vue";
import NavList from "../../components/NavList.vue";
import WorkControlPanel from "../../components/WorkControlPanel.vue";
import RequestReason from "../../components/RequestReason.vue";
import ReqestTime from "../../components/ReqestTime.vue";
import ApplyBtn from "../../components/ApplyBtn.vue";
import { formatDay, formatTime } from '../../utils/datetime';

const day = ref("");
const beginWork = ref(""); // 始業時刻
const endWork = ref(""); // 就業時刻
const beginBreak = ref(""); // 休憩時間
const endBreak = ref(""); // 休憩時間
const reasonText = ref(""); //申請理由テキスト

//シフト申請関数
const shiftPost = () => {
  console.log(`${day.value}　出勤時間(${formatTime(beginWork.value)}-${formatTime(endWork.value)})休憩：(${formatTime(beginBreak.value)}-${formatTime(endBreak.value)}申請理由${reasonText.value}`);
};
</script>

<template>
  <div class="flex h-screen text-base">
    <NavList />
    <main class="flex-1 p-6 bg-gray-100 overflow-auto pt-25 lg:ml-64 lg:pt-7">
      <h1 class="text-xl font-bold mb-3 md:mb-6 text-center">シフト登録</h1>

      <div class="bg-white p-4 md:p-6 rounded-lg shadow-md space-y-5">
        <!--申請日付-->
        <div>
          <label class="block font-semibold md:mb-2">日付</label>
          <input
            type="date"
            v-model="day"
            class="border rounded px-3 py-2 w-full max-w-xs"
          />
        </div>

        <!-- 始業,就業,休憩時刻-->
        <WorkControlPanel
          v-model:beginWork="beginWork"
          v-model:endWork="endWork"
          v-model:beginBreak="beginBreak"
          v-model:endBreak="endBreak"
        />

        <!-- 申請理由 -->
        <RequestReason v-model:reasonText="reasonText" />

        <!-- 申請時刻 -->
        <ReqestTime
          :day="day"
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
