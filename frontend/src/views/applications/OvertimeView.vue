<script setup>
import { ref, watch, onMounted } from "vue";
import {
  getCurrentDateTime,
  getCurrentDate,
  convertToApiDate,
} from "../../utils/datetime";
import axios from "axios";
import ApplyBtn from "../../components/ApplyBtn.vue";
import NavList from "../../components/NavList.vue";
import RequestReason from "../../components/RequestReason.vue";
import SelectShift from "../../components/SelectShift.vue";
import ReqestTime from "../../components/ReqestTime.vue";
import ShiftComplete from "../../components/ShiftComplete.vue";
import LoadingScreen from "../../components/LoadingScreen.vue";

const isLoading = ref(false);
const errorMsg = ref("");
const isCompleteModal = ref(false); //モーダル表示用
const reasonText = ref(""); //申請理由テキスト
const selectedShiftId = ref(null);
const beginOvertime = ref(""); //残業開始
const endOvertime = ref(""); //残業終了
const beginWork = ref(""); //始業時刻
const endWork = ref(""); //就業時刻
const beginBreak = ref(""); //休憩時間
const endBreak = ref(""); //休憩時間

//シフトデータ
const shifts = ref([]);

//シフトリスト取得
const getShiftList = async () => {
  try {
    const now = new Date();
    const year = now.getFullYear();
    const month = now.getMonth() + 1;

    const response = await axios.get(
      "http://localhost:8080/api/reach/shiftlist",
      {
        params: { year, month },
        withCredentials: true,
      }
    );

    shifts.value = response.data;
  } catch (error) {
    console.error("シフトリスト取得エラー:", error);
  }
};

const beginOverTimeForApi = ref("");//API送信用残業開始
const endOverTimeForApi = ref("");//API送信用残業終了

//selectedShiftId, beginOvertime, endOvertimeが変わったときにAPI送信用に変換
watch([selectedShiftId, beginOvertime, endOvertime], () => {
    const selected = shifts.value?.shiftList?.find((s) => s.id === selectedShiftId.value);

    if (!selected || !beginOvertime.value || !endOvertime.value) {
      beginOverTimeForApi.value = "";
      endOverTimeForApi.value = "";
      return;
    }

    //YYYY-MM-DD → YYYY/MM/DD
    const date = selected.beginWork
      .split("T")[0]
      .replaceAll("-", "/");


    beginOverTimeForApi.value = `${date}T${beginOvertime.value}:00`;
    endOverTimeForApi.value = `${date}T${endOvertime.value}:00`;
  }
);


const requestDate = ref(getCurrentDateTime()); //API送信時間

///api/send/overtime
//残業申請関数
const overTimePost = async () => {

  //時間の入力チェック
  if (selectedShiftId.value === null) {
    errorMsg.value = "対象シフトを選択してください";
    return;
  }
  if (!beginOvertime.value || !endOvertime.value) {
    errorMsg.value = "残業時間を入力してください";
    return;
  }

  isLoading.value = true;
  errorMsg.value = "";

  try {
    await axios.post(
      "http://localhost:8080/api/send/overtime",
      {
        shiftId: selectedShiftId.value,
        beginOverTime: beginOverTimeForApi.value,
        endOverTime: endOverTimeForApi.value,
        requestComment: reasonText.value,
        requestDate: requestDate.value,
      },
      { withCredentials: true }
    );
    //申請成功
    isCompleteModal.value = true;
  } catch (error) {
    console.error("残業申請エラー", error);
  } finally {
    isLoading.value = false;
  }
};

onMounted(() => {
  getShiftList();
});
</script>

<!--残業申請ページ-->
<template>
  <div class="flex h-screen">
    <NavList />
    <main class="flex-1 p-6 bg-gray-100 overflow-auto pt-25 lg:ml-64 lg:pt-7">
      <h1 class="text-xl font-bold mb-3 md:mb-6 text-center">残業申請</h1>

      <div class="bg-white p-4 md:p-6 rounded-lg shadow-md space-y-5">
        <!-- 対象シフト選択 -->
        <SelectShift
          :shifts="shifts?.shiftList || []"
          v-model:selectedShiftId="selectedShiftId"
          v-model:beginWork="beginWork"
          v-model:endWork="endWork"
          v-model:beginBreak="beginBreak"
          v-model:endBreak="endBreak"
        />
        <div v-if="selectedShiftId">
          <!--残業開始時刻-->
          <div>
            <label class="block font-semibold md:mb-2">残業開始時刻</label>
            <input
              type="time"
              v-model="beginOvertime"
              class="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring focus:ring-green-200 hover:ring hover:ring-green-200"
            />
          </div>
          <!--残業終了時刻-->
          <div>
            <label class="block font-semibold md:mb-2">残業終了時刻</label>
            <input
              type="time"
              v-model="endOvertime"
              class="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring focus:ring-green-200 hover:ring hover:ring-green-200"
            />
          </div>

          <!--申請理由-->
          <RequestReason v-model:reasonText="reasonText" />

          <ReqestTime
            :beginWork="beginWork"
            :beginOvertime="beginOvertime"
            :endOvertime="endOvertime"
          />
        </div>

        <!--申請ボタン-->
        <ApplyBtn :overTimePost="overTimePost" :errorMsg="errorMsg" />
      </div>
    </main>
  </div>

  <!--ローディング画面-->
  <LoadingScreen :isLoading="isLoading" />
  <!--申請完了-->
  <ShiftComplete v-model:isCompleteModal="isCompleteModal" />
</template>