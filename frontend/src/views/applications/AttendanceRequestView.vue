<script setup>
import { ref } from "vue";
import ApplyBtn from "../../components/ApplyBtn.vue";
import NavList from "../../components/NavList.vue";
import SelectRequest from "../../components/SelectRequest.vue";
import WorkControlPanel from "../../components/WorkControlPanel.vue";
import ReqestTime from "../../components/ReqestTime.vue";
const requests = ref(["遅刻", "早退", "外出"]);

const selectedRequest = ref(null);
const startTime = ref(""); // 始業時刻
const endTime = ref(""); // 就業時刻

//遅刻、早退、外出申請関数
const AttendanceRequestPost = () => {
  console.log(`遅刻早退外出申請から押した。 始業時刻: ${startTime.value},就業時刻: ${endTime.value} ${selectedRequest.value} `);
};
</script>

<!--遅刻・早退・外出申請ページ-->
<template>
  <div class="flex h-screen">
    <NavList />
    <main class="flex-1 p-6 bg-gray-100 overflow-auto pt-25 lg:ml-64 lg:pt-7">
      <h1 class="text-xl font-bold mb-3 md:mb-6 text-center">
        遅刻・早退・外出申請ページ
      </h1>

      <div class="bg-white p-4 md:p-6 rounded-lg shadow-md space-y-5">
        <!--遅刻・早退・外出選択-->
        <SelectRequest
          :requests="requests"
          v-model:selectedRequest="selectedRequest"
        />

        <WorkControlPanel
          v-model:startTime="startTime"
          v-model:endTime="endTime"
        />

        <ReqestTime
          :startTime="startTime"
          :endTime="endTime"
        />

        <!--申請ボタン-->
        <ApplyBtn :AttendanceRequestPost="AttendanceRequestPost" />
      </div>
    </main>
  </div>
</template>
