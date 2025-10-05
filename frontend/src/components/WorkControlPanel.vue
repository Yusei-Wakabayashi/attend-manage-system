<script setup>
import { ref, watch } from "vue";
import { useRoute } from "vue-router";
import { formatDay, formatTime } from "../utils/datetime";

const route = useRoute();

const props = defineProps({
  selectedDate: String,
  beginWork: String,
  endWork: String,
  beginBreak: String,
  endBreak: String,
});

const emit = defineEmits([
  "update:selectedDate",
  "update:beginWork",
  "update:endWork",
  "update:beginBreak",
  "update:endBreak",
]);

const localBeginWork = ref(formatTime(props.beginWork));
const localEndWork = ref(formatTime(props.endWork));
const localBeginBreak = ref(formatTime(props.beginBreak));
const localEndBreak = ref(formatTime(props.endBreak));

//親 → 子 の更新("T" を含んでいたら → formatTime(val) を実行)
watch(
  () => props.beginWork,
  (val) => {
    localBeginWork.value = val?.includes("T") ? formatTime(val) : val;
  }
);
watch(
  () => props.endWork,
  (val) => {
    localEndWork.value = val?.includes("T") ? formatTime(val) : val;
  }
);
watch(
  () => props.beginBreak,
  (val) => {
    localBeginBreak.value = val?.includes("T") ? formatTime(val) : val;
  }
);
watch(
  () => props.endBreak,
  (val) => {
    localEndBreak.value = val?.includes("T") ? formatTime(val) : val;
  }
);

// watch(localBeginWork, (val) => {
//   emit("update:beginWork", `${props.selectedDate}T${val}:00`);
// });
// watch(localEndWork, (val) => {
//   const datePart = props.endWork ? props.endWork.split("T")[0] : props.selectedDate;
//   emit("update:endWork", `${datePart}T${val}:00`);
// });
// watch(localBeginBreak, (val) => {
//   const datePart = props.beginBreak ? props.beginBreak.split("T")[0] : props.selectedDate;
//   emit("update:beginBreak", `${datePart}T${val}:00`);
// });
// watch(localEndBreak, (val) => {
//   const datePart = props.endBreak ? props.endBreak.split("T")[0] : props.selectedDate;
//   emit("update:endBreak", `${datePart}T${val}:00`);
// });


//子コンポーネント内の値が変わったときに親に変更通知（子 → 親）
//time が空でない場合のみ親に更新を通知
watch([localBeginWork, () => props.selectedDate], ([time, date]) => {
  if (time) emit("update:beginWork", `${date}T${time}:00`);
});

watch([localEndWork, () => props.selectedDate], ([time, date]) => {
  if (time) emit("update:endWork", `${date}T${time}:00`);
});

watch([localBeginBreak, () => props.selectedDate], ([time, date]) => {
  if (time) emit("update:beginBreak", `${date}T${time}:00`);
});

watch([localEndBreak, () => props.selectedDate], ([time, date]) => {
  if (time) emit("update:endBreak", `${date}T${time}:00`);
});

// watch(() => props.selectedDate, (newDate) => {
//   console.log(newDate)
//   if(localBeginWork.value) emit("update:beginWork", `${newDate}T${localBeginWork.value}:00`);
//   if(localEndWork.value) emit("update:endWork", `${newDate}T${localEndWork.value}:00`);
//   if(localBeginBreak.value) emit("update:beginBreak", `${newDate}T${localBeginBreak.value}:00`);
//   if(localEndBreak.value) emit("update:endBreak", `${newDate}T${localEndBreak.value}:00`);
// });
</script>

<template>
  <!--始業時刻-->
  <div>
    <label class="block font-semibold md:mb-2">始業時刻</label
    ><!--開始時刻-->
    <input
      type="time"
      v-model="localBeginWork"
      class="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring focus:ring-green-200 hover:ring hover:ring-green-200"
    />
  </div>
  <!--終業時刻-->
  <div>
    <label class="block font-semibold md:mb-2">終業時刻</label
    ><!--終業時刻-->
    <input
      v-model="localEndWork"
      type="time"
      class="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring focus:ring-green-200 hover:ring hover:ring-green-200"
    />
  </div>

  <!-- 休憩開始時刻-->
  <div v-if="route.path !== '/attendancerequest'">
    <label class="block font-semibold md:mb-2">休憩開始時刻</label>
    <input
      v-model="localBeginBreak"
      type="time"
      value="01:00"
      class="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring focus:ring-green-200 hover:ring hover:ring-green-200"
    />
  </div>

  <!--休憩終了時刻-->
  <div v-if="route.path !== '/attendancerequest'">
    <label class="block font-semibold md:mb-2">休憩終了時刻</label>
    <input
      v-model="localEndBreak"
      type="time"
      value="01:00"
      class="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring focus:ring-green-200 hover:ring hover:ring-green-200"
    />
  </div>
</template>
