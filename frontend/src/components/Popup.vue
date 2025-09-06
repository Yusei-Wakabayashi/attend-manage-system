<script setup>
import { ref } from 'vue';

const shiftList = ref([
  {
    id: 1,
    beginWork: '2025-09-04T09:00:00',
    endWork: '2025-09-04T18:00:00',
    beginBreak: '2025-09-04T12:00:00',
    endBreak: '2025-09-04T13:00:00',
    lateness: 5,
    leaveEarly: 10,
    outing: 30,
    overWork: 60,
  },
])
</script>

<template>
  <div class="fixed inset-0 flex items-center justify-center">
    <div
      class="bg-white border-2 border-green-500 w-50 h-auto p-4"
    >
          <div class="" v-for="(shift, index) in shiftList" :key="index">
            <p><strong>id:</strong> {{ shift.id }}</p>
            <p><strong>beginWork:</strong> {{ shift.beginWork }}</p>
            <p><strong>endWork:</strong> {{ shift.endWork }}</p>
            <p><strong>beginBreak:</strong> {{ shift.beginBreak }}</p>
            <p><strong>endBreak:</strong> {{ shift.endBreak }}</p>
            <p><strong>lateness:</strong> {{ shift.lateness }} 分</p>
            <p><strong>leaveEarly:</strong> {{ shift.leaveEarly }} 分</p>
            <p><strong>outing:</strong> {{ shift.outing }} 分</p>
            <p><strong>overWork:</strong> {{ shift.overWork }} 分</p>
          </div>
    </div>
  </div>
</template>

