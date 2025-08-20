<script setup>
import { ref, watch } from "vue";
import ApplyBtn from "../../components/ApplyBtn.vue";
import NavList from "../../components/NavList.vue";
import ReqestTime from "../../components/ReqestTime.vue";
import RequestReason from "../../components/RequestReason.vue";
import SelectShift from "../../components/SelectShift.vue";
import WorkControlPanel from "../../components/WorkControlPanel.vue";
import { formatDay, formatTime } from "../../utils/datetime";

// シフト(仮)データ
const shifts = ref([
  {
    id: 1,
    beginWork: "2025-07-01T09:00:00",
    endWork: "2025-07-01T18:00:00",
    beginBreak: "2025-07-01T12:00:00",
    endBreak: "2025-07-01T13:00:00",
    lateness: 0,
    leaveEarly: 0,
    outing: 0,
    overWork: 30,
  },
  {
    id: 2,
    beginWork: "2025-07-02T09:15:00",
    endWork: "2025-07-02T18:15:00",
    beginBreak: "2025-07-02T12:15:00",
    endBreak: "2025-07-02T13:15:00",
    lateness: 1,
    leaveEarly: 0,
    outing: 0,
    overWork: 45,
  },
  {
    id: 3,
    beginWork: "2025-07-03T09:00:00",
    endWork: "2025-07-03T18:00:00",
    beginBreak: "2025-07-03T12:00:00",
    endBreak: "2025-07-03T13:00:00",
    lateness: 0,
    leaveEarly: 0,
    outing: 1,
    overWork: 60,
  },
  {
    id: 4,
    beginWork: "2025-07-04T09:10:00",
    endWork: "2025-07-04T18:00:00",
    beginBreak: "2025-07-04T12:00:00",
    endBreak: "2025-07-04T13:00:00",
    lateness: 1,
    leaveEarly: 0,
    outing: 0,
    overWork: 20,
  },
  {
    id: 5,
    beginWork: "2025-07-05T09:00:00",
    endWork: "2025-07-05T17:45:00",
    beginBreak: "2025-07-05T12:15:00",
    endBreak: "2025-07-05T13:15:00",
    lateness: 0,
    leaveEarly: 1,
    outing: 0,
    overWork: 15,
  },
  {
    id: 6,
    beginWork: "2025-07-06T09:00:00",
    endWork: "2025-07-06T18:30:00",
    beginBreak: "2025-07-06T12:00:00",
    endBreak: "2025-07-06T13:00:00",
    lateness: 0,
    leaveEarly: 0,
    outing: 0,
    overWork: 90,
  },
  {
    id: 7,
    beginWork: "2025-07-07T08:45:00",
    endWork: "2025-07-07T18:00:00",
    beginBreak: "2025-07-07T12:00:00",
    endBreak: "2025-07-07T13:00:00",
    lateness: 0,
    leaveEarly: 0,
    outing: 0,
    overWork: 30,
  },
  {
    id: 8,
    beginWork: "2025-07-08T09:30:00",
    endWork: "2025-07-08T18:00:00",
    beginBreak: "2025-07-08T12:30:00",
    endBreak: "2025-07-08T13:30:00",
    lateness: 1,
    leaveEarly: 0,
    outing: 1,
    overWork: 60,
  },
  {
    id: 9,
    beginWork: "2025-07-09T09:00:00",
    endWork: "2025-07-09T18:00:00",
    beginBreak: "2025-07-09T12:00:00",
    endBreak: "2025-07-09T13:00:00",
    lateness: 0,
    leaveEarly: 0,
    outing: 0,
    overWork: 0,
  },
  {
    id: 10,
    beginWork: "2025-07-10T09:00:00",
    endWork: "2025-07-10T17:30:00",
    beginBreak: "2025-07-10T12:00:00",
    endBreak: "2025-07-10T13:00:00",
    lateness: 0,
    leaveEarly: 1,
    outing: 0,
    overWork: 0,
  }
]);
const selectedShiftId = ref(null); // シフト選択変数
const beginWork = ref(""); // 始業時刻
const endWork = ref(""); // 就業時刻
const beginBreak = ref(""); // 休憩時間
const endBreak = ref(""); // 休憩時間
const reasonText = ref(""); //申請理由テキスト

// selectedShiftIdが変わったときに自動で反映
watch(selectedShiftId, (newId) => {
  const selected = shifts.value.find((s) => s.id === newId);
  if (selected) {
    beginWork.value = selected.beginWork;
    endWork.value = selected.endWork;
    beginBreak.value = selected.beginBreak;
    endBreak.value = selected.endBreak;
  } else {
    beginWork.value = "";
    endWork.value = "";
    beginBreak.value = "";
    endBreak.value = "";
  }
});

//打刻漏れ申請関数
const MissingStampingPost = () => {
  console.log(`${formatDay(beginWork.value)}　出勤時間(${formatTime(beginWork.value)}-${formatTime(endWork.value)})休憩：(${formatTime(beginBreak.value)}-${formatTime(endBreak.value)}申請理由${reasonText.value}`);
};
</script>

<!--打刻漏れ申請ページ-->
<template>
  <div class="flex h-screen">
    <NavList />
    <main class="flex-1 p-6 bg-gray-100 overflow-auto pt-25 lg:ml-64 lg:pt-7">
      <h1 class="text-xl font-bold mb-3 md:mb-6 text-center">打刻漏れ申請</h1>

      <div class="bg-white p-4 md:p-6 rounded-lg shadow-md space-y-5">
        <!-- 対象シフト選択 -->
        <SelectShift
          :shifts="shifts"
          v-model:selectedShiftId="selectedShiftId"
        />

        <!-- 始業,就業,休憩時間-->
        <WorkControlPanel
          v-if="selectedShiftId"
          v-model:beginWork="beginWork"
          v-model:endWork="endWork"
          v-model:beginBreak="beginBreak"
          v-model:endBreak="endBreak"
        />

        <!-- 申請理由 -->
        <RequestReason v-model:reasonText="reasonText" />

        <!-- 申請時間 -->
        <ReqestTime
          v-if="selectedShiftId"
          :beginWork="beginWork"
          :endWork="endWork"
          :beginBreak="beginBreak"
          :endBreak="endBreak"
        />

        <!-- 申請ボタン -->
        <ApplyBtn :MissingStampingPost="MissingStampingPost" />
      </div>
    </main>
  </div>
</template>
