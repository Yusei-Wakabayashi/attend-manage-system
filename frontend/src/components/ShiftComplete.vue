<script setup>
import { ref, watch } from 'vue';
import { useRouter } from "vue-router";

const router = useRouter();
const props = defineProps({
  isCompleteModal: Boolean
});

const emit = defineEmits(["update:isCompleteModal"]);

const closeModal = () => {
    emit("update:isCompleteModal", false);
    router.push("/application");
};

//確認用
watch(
  () => props.isCompleteModal,
  (newVal) => {
    console.log("更新された isCompleteModal:", newVal);
  }
);
</script>


<!--申請完了モーダル-->
<template>
  <div
    v-if="isCompleteModal"
    class="fixed inset-0 flex items-center justify-center bg-black/50 z-50"
  >
    <div class="bg-white rounded-lg shadow-lg p-8 max-w-sm w-full text-center">
      <h2 class="text-xl font-bold mb-4">シフト申請が正常に送信されました。</h2>
      <button
        @click="closeModal"
        class="px-6 py-2 bg-green-500 text-white rounded hover:bg-green-600"
      >
        閉じる
      </button>
    </div>
  </div>
</template>
