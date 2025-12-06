<script setup>
import { computed } from "vue";

const props = defineProps({
  shiftDataPopup: Array,
  attendanceDataPopup: Array,
  selectedDay: Number,
});

// 選択された日のシフト・勤怠データ取得
const selectedShift = computed(() => {
  if (!props.shiftDataPopup || !props.selectedDay) return null;

  return props.shiftDataPopup.find((item) => {
    const day = Number(item.beginWork.split("T")[0].split("/")[2]);
    return day === props.selectedDay;
  });
});

// 選択された日の勤怠データ取得
const selectedAttendance = computed(() => {
  if (!props.attendanceDataPopup || !props.selectedDay) return null;

  return props.attendanceDataPopup.find((item) => {
    const day = Number(item.beginWork.split("T")[0].split("/")[2]);
    return day === props.selectedDay;
  });
});

// シフトと出勤簿の表示(シフトデータ優先表示)
const selectedData = computed(() => {
  return selectedShift.value
    ? selectedShift.value // シフトがあればシフト
    : selectedAttendance.value; // なければ勤怠
});

// ボタングループデータ
const buttonGroups = [
  {
    items: [
      { label: "勤務時間変更申請", color: "green", path: "/timechange" },
      { label: "打刻漏れ申請", color: "green", path: "/missingstamping" },
      { label: "休暇申請", color: "green", path: "/vacation" },
      { label: "残業申請", color: "green", path: "/overtime" },
      {
        label: "遅刻・早退・外出申請",
        color: "green",
        path: "/attendancerequest",
      },
      { label: "月次申請", color: "green", path: "/monthly" },
    ],
  },
];

//シフト側

//出勤簿側

// ボタンの色クラス取得関数
const getColorClass = (color) => {
  const map = {
    green: "bg-green-500 hover:bg-green-600 border-green-600",
    blue: "bg-blue-500 hover:bg-blue-600 border-blue-600",
  };
  return map[color];
};

const closeButtonClass = computed(() => {
  if (selectedShift.value) {
    return "bottom-32 md:bottom-18"; // シフト用
  } else if (selectedAttendance.value) {
    return "bottom-32 md:bottom-18"; // 出勤簿（勤怠）用
  } else {
    return "bottom-70 md:bottom-67"; // データなし
  }
});
</script>

<template>
  <div class="fixed inset-0 flex items-center justify-center bg-black/40">
    <div
      class="bg-white border-t-2 border-r-2 border-l-2 border-green-500 w-72 h-auto p-4 rounded shadow md:w-96 md:p-6 md:text-lg max-h-[400px] md:max-h-[500px] overflow-y-auto"
    >
      <!-- 中身はそのままでOK -->

      <h2 class="font-bold mb-2 text-lg md:text-xl">
        {{ selectedDay }} 日のシフト
      </h2>

      <!--シフト・出勤簿データ-->
      <div v-if="selectedData" class="mt-4">
        <table class="min-w-full border border-gray-300 rounded-lg">
          <tbody class="divide-y divide-gray-200">
            <!-- 出勤 -->
            <tr class="bg-gray-100">
              <td
                class="px-0 py-2 w-28 text-center align-middle border-r border-gray-300"
              >
                <span class="px-2 py-1 rounded font-semibold text-gray-700"
                  >出勤</span
                >
              </td>
              <td class="px-0 py-2 text-green-600 text-center align-middle">
                {{ selectedData.beginWork.slice(11, 16) }}
              </td>
            </tr>

            <!-- 退勤 -->
            <tr>
              <td
                class="px-0 py-2 text-center align-middle border-r border-gray-300"
              >
                <span class="px-2 py-1 rounded font-semibold text-gray-700"
                  >退勤</span
                >
              </td>
              <td class="px-0 py-2 text-blue-600 text-center align-middle">
                {{ selectedData.endWork.slice(11, 16) }}
              </td>
            </tr>

            <!-- 休憩 -->
            <tr class="bg-gray-100">
              <td
                class="px-0 py-2 text-center align-middle border-r border-gray-300"
              >
                <span class="px-2 py-1 rounded font-semibold text-gray-700"
                  >休憩</span
                >
              </td>
              <td class="px-0 py-2 text-red-600 text-center align-middle">
                {{ selectedData.beginBreak.slice(11, 16) }} ~
                {{ selectedData.endBreak.slice(11, 16) }}
              </td>
            </tr>

            <!-- 以下は attendance がある場合のみ -->

            <!-- 遅刻 -->
            <tr v-if="selectedAttendance">
              <td
                class="px-0 py-2 text-center align-middle border-r border-gray-300"
              >
                <span class="px-2 py-1 rounded font-semibold text-gray-700"
                  >遅刻</span
                >
              </td>
              <td class="px-0 py-2 text-center align-middle">
                {{ selectedAttendance.lateness.slice(0, 5) }}
              </td>
            </tr>

            <!-- 早退 -->
            <tr v-if="selectedAttendance" class="bg-gray-100">
              <td
                class="px-0 py-2 text-center align-middle border-r border-gray-300"
              >
                <span class="px-2 py-1 rounded font-semibold text-gray-700"
                  >早退</span
                >
              </td>
              <td class="px-0 py-2 text-center align-middle">
                {{ selectedAttendance.leaveEarly.slice(0, 5) }}
              </td>
            </tr>

            <!-- 外出 -->
            <tr v-if="selectedAttendance">
              <td
                class="px-0 py-2 text-center align-middle border-r border-gray-300"
              >
                <span class="px-2 py-1 rounded font-semibold text-gray-700"
                  >外出</span
                >
              </td>
              <td class="px-0 py-2 text-center align-middle">
                {{ selectedAttendance.outing.slice(0, 5) }}
              </td>
            </tr>

            <!-- 残業 -->
            <tr v-if="selectedAttendance" class="bg-gray-100">
              <td
                class="px-0 py-2 text-center align-middle border-r border-gray-300"
              >
                <span class="px-2 py-1 rounded font-semibold text-gray-700"
                  >残業</span
                >
              </td>
              <td class="px-0 py-2 text-center align-middle">
                {{ selectedAttendance.overWork.slice(0, 5) }}
              </td>
            </tr>

            <!-- 勤務時間 -->
            <tr v-if="selectedAttendance">
              <td
                class="px-0 py-2 text-center align-middle border-r border-gray-300"
              >
                <span class="px-2 py-1 rounded font-semibold text-gray-700"
                  >勤務時間</span
                >
              </td>
              <td class="px-0 py-2 text-center align-middle">
                {{ selectedAttendance.workTime.slice(0, 5) }}
              </td>
            </tr>

            <!-- 休暇時間 -->
            <tr v-if="selectedAttendance" class="bg-gray-100">
              <td
                class="px-0 py-2 text-center align-middle border-r border-gray-300"
              >
                <span class="px-2 py-1 rounded font-semibold text-gray-700"
                  >休暇</span
                >
              </td>
              <td class="px-0 py-2 text-center align-middle">
                {{ selectedAttendance.vacationTime.slice(0, 5) }}
              </td>
            </tr>

            <!-- 欠勤時間 -->
            <tr v-if="selectedAttendance">
              <td
                class="px-0 py-2 text-center align-middle border-r border-gray-300"
              >
                <span class="px-2 py-1 rounded font-semibold text-gray-700"
                  >欠勤</span
                >
              </td>
              <td class="px-0 py-2 text-center align-middle">
                {{ selectedAttendance.absenceTime.slice(0, 5) }}
              </td>
            </tr>

            <!-- 休日労働 -->
            <tr v-if="selectedAttendance" class="bg-gray-100">
              <td
                class="px-0 py-2 text-center align-middle border-r border-gray-300"
              >
                <span class="px-2 py-1 rounded font-semibold text-gray-700"
                  >休日労働</span
                >
              </td>
              <td class="px-0 py-2 text-center align-middle">
                {{ selectedAttendance.holidayWork.slice(0, 5) }}
              </td>
            </tr>

            <!-- 深夜労働 -->
            <tr v-if="selectedAttendance">
              <td
                class="px-0 py-0 text-center align-middle border-r border-gray-300"
              >
                <span class="px-0 py-0 rounded font-semibold text-gray-700"
                  >深夜</span
                >
              </td>
              <td class="px-0 py-2 text-center align-middle">
                {{ selectedAttendance.lateNightWork.slice(0, 5) }}
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <div v-else>
        <p class="text-red-500">この日はシフトがありません</p>
      </div>

      <!--各種申請ボタン-->
      <div v-if="selectedShift" class="mt-3">
        <div v-for="(group, gIndex) in buttonGroups" :key="gIndex">
          <router-link
            v-for="(item, index) in group.items"
            :key="index"
            :to="item.path"
          >
            <button
              :class="[
                'w-full mt-1 py-1 rounded-md border-3 shadow-md font-bold text-white text-base cursor-pointer',
                'md:py-2 md:text-lg',
                getColorClass(item.color),
              ]"
            >
              {{ item.label }}
            </button>
          </router-link>
        </div>
      </div>

      <div
        :class="[
          'z-0 absolute left-0 right-0 flex justify-center mt-4 transition-all',
          closeButtonClass,
        ]"
      >
        <button
          class="bg-gray-500 hover:bg-gray-600 text-white px-4 py-2 rounded-b w-72 md:w-96 border-r-2 border-b-2 border-l-2 border-green-500"
          @click="$emit('close')"
        >
          閉じる
        </button>
      </div>
    </div>
  </div>
</template>


