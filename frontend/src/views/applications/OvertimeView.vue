<script setup>
import { ref, watch } from "vue";
import ApplyBtn from "../../components/ApplyBtn.vue";
import NavList from "../../components/NavList.vue";
import RequestReason from "../../components/RequestReason.vue";
import SelectShift from "../../components/SelectShift.vue";
import ReqestTime from "../../components/ReqestTime.vue";

const reasonText = ref(""); //申請理由テキスト
const beginWork = ref("") //選択したシフトの日付をとるための変数
const selectedShiftId = ref(null);
const beginOvertime = ref(""); //残業開始
const endOvertime = ref(""); //残業終了

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

watch(selectedShiftId, (newId) => {
  const selected = shifts.value.find((s) => s.id === newId);
  if (selected) {
    beginWork.value = selected.beginWork;
  } else {
    beginWork.value = "";
  }
});

//残業申請関数
const overTimePost = () => {
  console.log(`残業開始時間${beginOvertime.value}、残業終了時間${endOvertime.value}`)
};
</script>

<!--残業申請ページ-->
<template>
  <div class="flex h-screen">
    <NavList />
    <main class="flex-1 p-6 bg-gray-100 overflow-auto pt-25 lg:ml-64 lg:pt-7">
      <h1 class="text-xl font-bold mb-3 md:mb-6 text-center">残業申請</h1>

      <div class="bg-white p-4 md:p-6 rounded-lg shadow-md space-y-5">
        <SelectShift
          :shifts="shifts"
          v-model:selectedShiftId="selectedShiftId"
        />
        <div v-if="selectedShiftId">
          <!--残業開始時刻-->
        <div>
          <label class="block font-semibold md:mb-2">残業開始時刻</label>
          <input
            type="time"
            v-model="beginOvertime"
            class="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring focus:ring-green-200 hover:ring hover:ring-green-200"
          />
        </div>
        <!--残業終了時刻-->
        <div>
          <label class="block font-semibold md:mb-2">残業終了時刻</label>
          <input
            type="time"
            v-model="endOvertime"
            class="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring focus:ring-green-200 hover:ring hover:ring-green-200"
          />
        </div>

        <!--申請理由-->
        <RequestReason v-model:reasonText="reasonText" />

        <ReqestTime  :beginWork="beginWork" :beginOvertime="beginOvertime" :endOvertime="endOvertime" />
        </div>

        <!--申請ボタン-->
        <ApplyBtn :overTimePost="overTimePost" />
      </div>
    </main>
  </div>
</template>