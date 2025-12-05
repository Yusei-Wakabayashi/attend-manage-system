<script setup>
import { ref } from "vue";
import { computed } from "vue";

const props = defineProps({
  shiftDataPopup: Array,
  selectedDay: Number,
});

const selectedShift = computed(() => {
  if (!props.shiftDataPopup || !props.selectedDay) return null;

  return props.shiftDataPopup.find((item) => {
    const day = Number(item.beginWork.split("T")[0].split("/")[2]);
    return day === props.selectedDay;
  });
});

const buttonGroups = [
  {
    items: [
      { label: "勤務時間変更申請", color: "green", path: "/timechange" },
      { label: "打刻漏れ申請", color: "green", path: "/missingstamping" },
    ],
  },
];

const getColorClass = (color) => {
  const map = {
    green: "bg-green-500 hover:bg-green-600 border-green-600",
    blue: "bg-blue-500 hover:bg-blue-600 border-blue-600",
  };
  return map[color];
};
</script>

<template>
  <div class="fixed inset-0 flex items-center justify-center bg-black/40">
    <div
      class="bg-white border-2 border-green-500 w-72 h-auto p-4 rounded shadow md:w-96 md:p-6 md:text-lg"
    >
      <h2 class="font-bold mb-2 text-lg md:text-xl">
        {{ selectedDay }} 日のシフト
      </h2>

      <div v-if="selectedShift">
        <p class="text-green-600">出勤: {{ selectedShift.beginWork.slice(11, 16) }}</p>
        <p class="text-blue-600">退勤: {{ selectedShift.endWork.slice(11, 16) }}</p>
        <p class="text-red-600">
          休憩:
          {{ selectedShift.beginBreak.slice(11, 16) }}
          ~
          {{ selectedShift.endBreak.slice(11, 16) }}
        </p>
      </div>

      <div v-else>
        <p class="text-red-500">この日はシフトがありません</p>
      </div>

      <div v-for="(group, gIndex) in buttonGroups" :key="gIndex">
        <div v-if="selectedShift">
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

      <button
        class="mt-3 bg-gray-500 hover:bg-gray-600 text-white px-3 py-1 rounded md:px-4 md:py-2 md:text-lg"
        @click="$emit('close')"
      >
        閉じる
      </button>
    </div>
  </div>
</template>


