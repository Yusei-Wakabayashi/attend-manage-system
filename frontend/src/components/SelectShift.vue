<script setup>
import { ref, watch } from "vue";
import { formatDay, formatTime } from '../utils/datetime';

const props = defineProps({
  shifts: Array,
  selectedShiftId: Number,
});
const emit = defineEmits(["update:selectedShiftId"]);

const localSelectedShiftId = ref(props.selectedShiftId);
const isOpen = ref(false);

watch(
  () => props.selectedShiftId,
  (val) => (localSelectedShiftId.value = val)
);
watch(localSelectedShiftId, (val) => emit("update:selectedShiftId", val));


// 選択処理
const selectShift = (id) => {
  localSelectedShiftId.value = id;
  isOpen.value = false;
};

// 選択中のシフト
const getShiftLabel = (id) => {
  const shift = props.shifts.find((s) => s.id === id);
  return shift ? `${formatDay(shift.beginWork)}　出勤時間(${formatTime(shift.beginWork)}~${formatTime(shift.endWork)})　休憩時間(${formatTime(shift.beginBreak)}~${formatTime(shift.endBreak)})` : null;
};
</script>

<template>
  <div>
    <label class="block font-semibold md:mb-2">対象シフト選択</label>

    <!-- モーダルを開くトリガーボタン -->
    <button
      @click="isOpen = true"
      class="w-full p-2 border border-gray-300 rounded text-left bg-white hover:ring hover:ring-green-200"
    >
      {{ getShiftLabel(localSelectedShiftId) || "選択してください" }}
    </button>

    <!-- モーダル本体 -->
    <div
      v-if="isOpen"
      class="fixed inset-0 z-50 flex items-center justify-center backdrop-blur-sm lg:ml-64"
      @click.self="isOpen = false"
    >
     <div class="bg-white mt-[70px] border-2 border-green-500 max-h-[70vh] w-90 overflow-y-auto shadow-xl">

        <div class="p-4 border-b text-lg font-semibold">シフトを選択</div>
        <ul>
          <li
            v-for="shift in shifts"
            :key="shift.id"
            class="p-3 cursor-pointer border-b"
            :class="{
              'bg-green-100': shift.id === localSelectedShiftId,
              'hover:bg-green-50': shift.id !== localSelectedShiftId,
            }"
            @click="selectShift(shift.id)"
          >
            <div class="text-gray-800">
              {{ formatDay(shift.beginWork) }}
            </div>
            <div class="text-sm text-gray-500">
              <span class="text-gray-800">出勤時間</span>{{ formatTime(shift.beginWork) }}〜{{ formatTime(shift.endWork) }}
            </div>
            <div class="text-sm text-gray-500">
              <span class="text-gray-800">休憩時間</span>{{ formatTime(shift.beginBreak) }}〜{{ formatTime(shift.endBreak) }}
            </div>
          </li>
        </ul>

        <button
          class="w-full text-center py-4 text-gray-500"
          @click="isOpen = false"
        >
          キャンセル
        </button>
      </div>
    </div>
  </div>
</template>
