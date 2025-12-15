<script setup>
import { ref, watch } from 'vue';
import { useRouter } from "vue-router";
import { useRoute } from "vue-router";

const route = useRoute();
const router = useRouter();
const props = defineProps({
  isCompleteModal: Boolean
});

const emit = defineEmits(["update:isCompleteModal"]);

const closeModal = () => {
    emit("update:isCompleteModal", false);
    router.push("/application");
};

const changeText = () => {
  switch (route.path) {
    case "/shift":
      return "シフト申請";
    case "/timechange":
      return "勤務時間変更申請";
    case "/missingstamping":
      return "打刻漏れ申請";
    case "/vacation":
      //今後追加
    case "/overtime":
      return "残業申請";
    case "/attendancerequest":
      //今後追加
    default:
      console.log("不明なページです");
  }
}
</script>


<!--申請完了モーダル-->
<template>
  <div
    v-if="isCompleteModal"
    class="fixed inset-0 flex items-center justify-center bg-black/50 z-50"
  >
    <div class="bg-white rounded-lg shadow-lg p-8 max-w-sm w-full text-center">
      <h2 class="text-xl font-bold mb-4">{{changeText()}}が正常に送信されました。</h2>
      <button
        @click="closeModal"
        class="px-6 py-2 bg-green-500 text-white rounded hover:bg-green-600 cursor-pointer"
      >
        閉じる
      </button>
    </div>
  </div>
</template>
