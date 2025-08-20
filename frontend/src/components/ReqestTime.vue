<script setup>
import { useRoute } from 'vue-router';
import { computed } from 'vue';
import { formatDay, formatTime } from '../utils/datetime';

const route = useRoute();
const props = defineProps({
  day: String,
  beginWork: String,
  endWork: String,
  beginBreak: String,
  endBreak: String,
  beginOvertime: String,
  endOvertime: String
});

const isShift = route.path === '/shift';
const isOvertime = route.path === '/overtime';
const isAttendanceRequest = route.path === '/attendancerequest';

//リアルタイムで変更
const dayPart = computed(() =>
  !isShift ? formatDay(props.beginWork) : props.day
);
const workPart = computed(() =>
  !isOvertime ? '出勤時間' : '残業時間'
);
const timePart = computed(() =>
  !isOvertime ? `${formatTime(props.beginWork)}-${formatTime(props.endWork)}` : `${props.beginOvertime}-${props.endOvertime}`
);
const breakPart = computed(() =>
  !isOvertime && !isAttendanceRequest
    ? ` 休憩時間(${formatTime(props.beginBreak)}-${formatTime(props.endBreak)})`
    : ''
);

const displayText = computed(() =>
  `${dayPart.value}　${workPart.value}(${timePart.value})${breakPart.value}`
);
</script>

<template>
  <div>
    <label class="block font-semibold md:mb-2">申請時間</label>
    <input
      type="text"
      class="w-full p-2 border border-gray-200 rounded bg-gray-100 focus:outline-none focus:ring focus:ring-green-200 hover:ring hover:ring-green-200"
      :value="displayText"
      readonly
    />
  </div>
</template>
