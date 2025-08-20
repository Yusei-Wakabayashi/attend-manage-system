<script setup>
import { useRoute } from "vue-router";

const route = useRoute();

const props = defineProps({
  shiftPost: Function,
  TimeChangePost: Function,
  MissingStampingPost:Function,
  vacationPost: Function,
  overTimePost: Function,
  AttendanceRequestPost: Function
})

// ルートによって押されたときの動作を切り替え
const handleClick = () => {
  switch (route.path) {
    case "/shift":
      props.shiftPost && props.shiftPost();
      break;
    case "/timechange":
      props.TimeChangePost && props.TimeChangePost();
      break;
    case "/missingstamping":
      props.MissingStampingPost && props.MissingStampingPost();
      break;
    case "/vacation":
      props.vacationPost && props.vacationPost();
      break;
    case "/overtime":
      props.overTimePost && props.overTimePost();
      break;
    case "/attendancerequest":
      props.AttendanceRequestPost && props.AttendanceRequestPost();
      break;
    default:
      console.log("不明なページです");
      break;
  }
};

</script>

<template>
  <!-- 登録ボタン -->
  <div class="text-right space-x-5">
    <router-link to="/application">
      <button
        class="bg-gray-500 hover:bg-gray-600 text-white font-bold py-2 px-4 rounded w-25 cursor-pointer"
      >
        戻る
      </button>
    </router-link>
    <button
      class="bg-green-500 hover:bg-green-600 text-white font-bold py-2 px-4 rounded w-25 cursor-pointer"
      @click="handleClick"
    >
      登録
    </button>
  </div>
</template>