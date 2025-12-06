<script setup>
import { ref, computed, onMounted, watch } from "vue";
import axios from "axios";
import NavList from "../components/NavList.vue";
import Popup from "../components/Popup.vue";

const viewType = ref("shift");// "shift" または "attendance"
const showPopup = ref(false);// ポップアップ表示制御

// const togglePopup = (day) => {
//   selectedDay.value = day;
//   showPopup.value = !showPopup.value;
// };

// ポップアップ表示関数
const openPopup = (day) => {
  selectedDay.value = day;
  showPopup.value = true;
};

const today = new Date();// 今日の日付情報
const currentYear = today.getFullYear();// 現在の年
const currentMonth = today.getMonth();// 現在の月(0-11)
const currentDate = today.getDate();// 現在の日(1-31)
const year = ref(currentYear);// 表示中の年
const month = ref(currentMonth);// 表示中の月(0-11)

// 今月表示判定
const isCurrentMonth = computed(
  () => year.value === currentYear && month.value === currentMonth
);
// 次の月移動関数
const prevMonth = () => {
  month.value === 0 ? (year.value--, (month.value = 11)) : month.value--;
};
// 前の月移動関数
const nextMonth = () => {
  month.value === 11 ? (year.value++, (month.value = 0)) : month.value++;
};

// const monthKey = computed(
//   () => `${year.value}-${String(month.value + 1).padStart(2, "0")}`
// );

// カレンダー関連計算
const firstDate = computed(() => new Date(year.value, month.value, 1));
// 月の日数取得
const daysInMonth = computed(() =>
  new Date(year.value, month.value + 1, 0).getDate()
);
// 月初の曜日取得(0:日曜〜6:土曜)
const firstDayOfWeek = computed(() => firstDate.value.getDay());
// 空白セル配列
const emptyCells = computed(() => Array.from({ length: firstDayOfWeek.value }));
// カレンダー日付配列
const calendarDays = computed(() =>
  Array.from({ length: daysInMonth.value }, (_, i) => i + 1)
);
// 月ラベル表示
const monthLabel = computed(() => `${year.value}年${month.value + 1}月`);
// 時間文字列を分に変換
const timeToMinutes = (time) => {
  if (!time) return 0;
  const [h, m] = time.split(":").map(Number);
  return h * 60 + m;
};

// 1日の労働時間計算
const calcWorkMinutes = (data) => {
  const start = timeToMinutes(data.start);
  const end = timeToMinutes(data.end);
  const breakStart = timeToMinutes(data.breakStart);
  const breakEnd = timeToMinutes(data.breakEnd);

  const work = end - start;
  const rest = breakEnd - breakStart;

  return work - rest; // 実労働時間（分）
};

// 出勤簿合計計算
const attendanceDays = computed(() => {
  return Object.keys(attendanceData.value).length;
});

// 総労働時間計算
const totalWorkMinutes = computed(() => {
  let total = 0;

  for (const day in attendanceData.value) {
    total += calcWorkMinutes(attendanceData.value[day]);
  }

  return total;
});

// 労働時間表示
const totalWorkHours = computed(() => {
  const h = Math.floor(totalWorkMinutes.value / 60);
  const m = totalWorkMinutes.value % 60;
  return `${h}時間${m}分`;
});

// 残業時間計算
const overTimeMinutes = computed(() => {
  let overtime = 0;
  const perDay = 8 * 60; // 8時間

  for (const day in attendanceData.value) {
    const work = calcWorkMinutes(attendanceData.value[day]);
    if (work > perDay) {
      overtime += work - perDay;
    }
  }

  return overtime;
});

// 残業時間表示
const overTimeHours = computed(() => {
  const h = Math.floor(overTimeMinutes.value / 60);
  const m = overTimeMinutes.value % 60;
  return `${h}時間${m}分`;
});


const shiftData = ref({});// シフトデータ格納
const attendanceData = ref({});// 出勤簿データ格納
const shiftDataPopup = ref({});// ポップアップ用シフトデータ
const attendanceDataPopup = ref({});// ポップアップ用出勤簿データ
const selectedDay = ref(null);

// シフトデータ取得関数
const getShiftData = async () => {
  try {
    const response = await axios.get(
      `http://localhost:8080/api/reach/shiftlist?year=${year.value}&month=${
        month.value + 1
      }`,
      { withCredentials: true }
    );

    shiftDataPopup.value = response.data;
    const rawList = response.data.shiftList;
    const mapped = {};

    rawList.forEach((item) => {
      const dateStr = item.beginWork.split("T")[0]; 
      const day = Number(dateStr.split("/")[2]); 

      mapped[day] = {
        start: item.beginWork.slice(11, 16),
        end: item.endWork.slice(11, 16),
        breakStart: item.beginBreak.slice(11, 16),
        breakEnd: item.endBreak.slice(11, 16),
      };
    });

    shiftData.value = mapped;
    console.log("整形前 shiftData:", response.data);
    console.log("整形後 shiftData:", shiftData.value);
  } catch (error) {
    console.error("エラーが発生しました:", error);
  }
};

// 出勤簿データ取得関数
const getAttendanceData = async () => {
  try {
    const response = await axios.get(
      `http://localhost:8080/api/reach/attendlist?year=${year.value}&month=${
        month.value + 1
      }`,
      { withCredentials: true }
    );

    attendanceDataPopup.value = response.data;
    const rawList = response.data.attendList; 
    const mapped = {};

    rawList.forEach((item) => {

      const dateStr = item.beginWork.split("T")[0];
      const day = Number(dateStr.split("/")[2]);

      mapped[day] = {
        start: item.beginWork.slice(11, 16),
        end: item.endWork.slice(11, 16),
        breakStart: item.beginBreak.slice(11, 16),
        breakEnd: item.endBreak.slice(11, 16),
      };
    });

    attendanceData.value = mapped;

    console.log("整形前 attendlistData:", response.data);
    console.log("整形後 attendanceData:", attendanceData.value);
  } catch (error) {
    console.error("出勤簿取得エラー:", error);
  }
};

// 日付ラベル取得関数
const label = (day) => {
  if (viewType.value === "shift") {
    return shiftData.value?.[day] ?? null;
  } else {
    return attendanceData.value?.[day] ?? null; // ← .value 必須
  }
};

// 初回データ取得
onMounted(() => {
  if (viewType.value === "shift") {
    getShiftData();
  } else {
    getAttendanceData();
  }
});

// 月変更でデータ取得
watch(viewType, () => {
  if (viewType.value === "shift") {
    getShiftData();
  } else {
    getAttendanceData();
  }
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
          @click="openPopup(day)"
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
              出勤: {{ label(day).start }}
            </div>
            <div class="text-blue-600 whitespace-nowrap">
              退勤: {{ label(day).end }}
            </div>
            <div class="text-orange-600 whitespace-nowrap">
              休憩: {{ label(day).breakStart }}~{{ label(day).breakEnd }}
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
        <p class="text-gray-800">出勤日数: {{ attendanceDays }}日</p>
        <p class="text-gray-800">労働時間: {{ totalWorkHours }}</p>
        <p class="text-gray-800">残業時間: {{ overTimeHours }}</p>
      </div>

      <Popup
        v-if="showPopup"
        :shiftDataPopup="shiftDataPopup.shiftList"
        :attendanceDataPopup="attendanceDataPopup.attendList"
        :selectedDay="selectedDay"
        @close="showPopup = false"
      />
    </main>
  </div>
</template>
