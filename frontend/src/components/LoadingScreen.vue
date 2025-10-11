<script setup>
const props = defineProps({
  isLoading: Boolean
});
</script>

<template>
  <div
    v-if="isLoading"
    class="fixed inset-0 w-full h-full bg-gray-400/50 flex justify-center items-center z-[9999]"
  >
    <div
      class="w-12 h-12 border-4 border-gray-200 border-t-green-500 rounded-full animate-spin"
    ></div>
  </div>
</template>