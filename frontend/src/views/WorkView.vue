<script setup>
import { ref, computed, onMounted, watch } from "vue";
import axios from "axios";
import NavList from "../components/NavList.vue";
import Popup from "../components/Popup.vue";

const viewType = ref("shift");
const showPopup = ref(false);
const togglePopup = () => {
  showPopup.value = !showPopup.value;
  console.log(showPopup.value);
};

const today = new Date();
const currentYear = today.getFullYear();
const currentMonth = today.getMonth();
const currentDate = today.getDate();

const year = ref(currentYear);
const month = ref(currentMonth);
const isCurrentMonth = computed(() => year.value === currentYear && month.value === currentMonth);
const prevMonth = () => { month.value === 0 ? (year.value--, month.value = 11) : month.value--; };
const nextMonth = () => { month.value === 11 ? (year.value++, month.value = 0) : month.value++; };

const monthKey = computed(() => `${year.value}-${String(month.value + 1).padStart(2, "0")}`);
const firstDate = computed(() => new Date(year.value, month.value, 1));
const daysInMonth = computed(() => new Date(year.value, month.value + 1, 0).getDate());
const firstDayOfWeek = computed(() => firstDate.value.getDay());
const emptyCells = computed(() => Array.from({ length: firstDayOfWeek.value }));
const calendarDays = computed(() => Array.from({ length: daysInMonth.value }, (_, i) => i + 1));
const monthLabel = computed(() => `${year.value}年${month.value + 1}月`);

const shiftData = ref({});

const attendanceData = {
  "2025-06": {
    5: { start: "09:02", end: "18:10", breakStart: "12:05", breakEnd: "13:00" },
    20: { start: "10:01", end: "17:45", breakStart: "12:00", breakEnd: "13:00" },
  },
  "2025-07": {
    3: { start: "09:33", end: "17:32", breakStart: "12:00", breakEnd: "13:00" },
    10: { start: "10:05", end: "18:05", breakStart: "13:00", breakEnd: "14:00" },
  },
};

const getShiftData = async () => {
  try {
    const res = await axios.get(
      `http://localhost:8080/api/reach/shiftlist?year=${year.value}&month=${month.value + 1}`
    );

    const rawList = res.data.shiftlist;

    const mapped = {};
    rawList.forEach(item => {
      const date = new Date(item.workStart).getDate();
      mapped[date] = {
        start: item.workStart.slice(11, 16),       
        end: item.workEnd.slice(11, 16),           
        breakStart: item.breakStart.slice(11, 16),
        breakEnd: item.breakEnd.slice(11, 16)     
      };
    });

    shiftData.value[monthKey.value] = mapped;
    console.log("shiftData:", mapped);
  } catch (error) {
    console.error("エラーが発生しました:", error);
  }
};



const label = (day) => {
  const data =
    viewType.value === "shift"
      ? shiftData.value[monthKey.value]
      : attendanceData[monthKey.value];
  return data?.[day] ?? null;
};

onMounted(() => {
  getShiftData();
});

// 月変更でデータ取得
watch([year, month], () => {
  getShiftData();
});
</script>


<template>
  <div class="flex h-screen">
    <NavList />

    <main class="flex-1 p-0 bg-gray-100 overflow-auto pt-25 lg:ml-64 lg:p-6">
      <!-- 月ヘッダー -->
      <div class="flex justify-between items-center mb-4 px-4">
        <button
          @click="prevMonth"
          class="text-xl font-bold bg-white border px-4 py-1 rounded hover:bg-gray-200"
        >
          ←
        </button>
        <h1 class="text-xl lg:text-2xl font-bold text-center">
          📅{{ monthLabel }}
        </h1>
        <button
          @click="nextMonth"
          class="text-xl font-bold bg-white border px-4 py-1 rounded hover:bg-gray-200"
        >
          →
        </button>
      </div>

      <!-- タブ切替 -->
      <div class="m-4 flex space-x-4 text-base lg:text-xl">
        <button
          @click="viewType = 'shift'"
          class="w-1/2 cursor-pointer font-semibold px-4 py-2"
          :class="
            viewType === 'shift'
              ? 'bg-green-500 text-white rounded'
              : 'bg-white border rounded'
          "
        >
          シフト
        </button>
        <button
          @click="viewType = 'attendance'"
          class="w-1/2 cursor-pointer font-semibold px-4 py-2"
          :class="
            viewType === 'attendance'
              ? 'bg-blue-500 text-white rounded'
              : 'bg-white border rounded'
          "
        >
          出勤簿
        </button>
      </div>

      <!-- カレンダー -->
      <div class="grid grid-cols-7 text-base lg:text-xl">
        <!-- 曜日 -->
        <div
          v-for="(label, i) in ['日', '月', '火', '水', '木', '金', '土']"
          :key="i"
          class="text-center font-semibold bg-green-200 border-t border-b border-r border-gray-500"
          :class="{
            'text-red-500': i === 0,
            'text-blue-500': i === 6,
          }"
        >
          {{ label }}
        </div>

        <!-- 空白セル -->
        <div
          v-for="(_, i) in emptyCells"
          :key="'empty-' + i"
          class="h-28 bg-gray-200 border-r border-b"
        ></div>

        <!-- 日付セル -->
        <div
          v-for="day in calendarDays"
          :key="day"
          @click="togglePopup"
          class="h-28 cursor-pointer border-r border-b border-gray-500 bg-white p-1 flex flex-col text-xs relative hover:bg-green-100 hover:border-green-500"
          :class="{
            'bg-yellow-100 border-yellow-500':
              isCurrentMonth && day === currentDate,
          }"
        >
          <div class="font-bold text-right text-gray-800 whitespace-nowrap">
            {{ day }}
          </div>

          <template v-if="label(day)">
            <div class="text-green-600 whitespace-nowrap">
              始: {{ label(day).start }}
            </div>
            <div class="text-blue-600 whitespace-nowrap">
              終: {{ label(day).end }}
            </div>
            <div class="text-orange-600 whitespace-nowrap">
              休: {{ label(day).breakStart }}~{{ label(day).breakEnd }}
            </div>
          </template>
        </div>
      </div>

      <!-- 出勤簿 合計（固定表示） -->
      <div
        v-if="viewType === 'attendance'"
        class="mt-4 p-4 bg-white rounded shadow border border-green-500"
      >
        <h2 class="text-xl lg:text-2xl font-semibold mb-2">{{ monthLabel }}</h2>
        <p class="text-gray-800">出勤日数: 20日</p>
        <p class="text-gray-800">労働時間: 160時間</p>
        <p class="text-gray-800">残業時間: 12時間</p>
      </div>

      <Popup v-if="showPopup" />
    </main>
  </div>
</template>
