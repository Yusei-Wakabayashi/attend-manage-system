<script setup>
import { onMounted, ref } from "vue";
import axios from "axios";

import ApplyBtn from "../../components/ApplyBtn.vue";
import NavList from "../../components/NavList.vue";
import RequestReason from "../../components/RequestReason.vue";
import SelectReqest from "../../components/SelectRequest.vue";
import SelectShift from "../../components/SelectShift.vue";
import ShiftComplete from "../../components/ShiftComplete.vue";

// 休暇種類（IDとラベルを持つ）
// const requests = ref([
//   { id: 1, label: "有給" },
//   { id: 2, label: "代休" },
//   { id: 3, label: "欠勤" },
//   { id: 4, label: "忌引き" },
//   { id: 5, label: "特別休暇" },
//   { id: 6, label: "子供" },
//   { id: 7, label: "介護" },
//   { id: 8, label: "保存" },
// ]);
const isCompleteModal = ref(false); //モーダル表示用
const vacationTypes = ref([]);

const getVacationTypeList = async () => {
  try {
    const { data } = await axios.get(
      "http://localhost:8080/api/reach/allvacationtypelist",
      { withCredentials: true }
    );
    // ここで data は {status:1, vacationTypes:[...]} なので
    vacationTypes.value = data;
    console.log(vacationTypes.value.vacationTypes); // 配列を確認
  } catch (error) {
    console.error(error);
  }
};

// 選択値
const selectedRequest = ref(null);
const reasonText = ref("");

// シフト関連
const shifts = ref([]);
const selectedShiftId = ref(null);
const beginWork = ref(null);
const endWork = ref(null);
const beginBreak = ref(null);
const endBreak = ref(null);

// シフト取得
const getShiftList = async () => {
  try {
    const now = new Date();
    const year = now.getFullYear();
    const month = now.getMonth() + 1;

    const response = await axios.get(
      "http://localhost:8080/api/reach/shiftlist",
      { params: { year, month }, withCredentials: true }
    );

    shifts.value = response.data;
    console.log("シフトリスト:", shifts.value);
  } catch (error) {
    console.error("シフトリスト取得エラー:", error);
  }
};

// 送信前に LocalDateTime 形式に変換
// 送信前に yyyy/MM/ddTHH:mm:ss 形式に統一
const normalizeDateTime = (value) => {
  if (!value) return null;

  if (value instanceof Date) {
    const year = value.getFullYear();
    const month = String(value.getMonth() + 1).padStart(2, "0");
    const day = String(value.getDate()).padStart(2, "0");
    const hours = String(value.getHours()).padStart(2, "0");
    const minutes = String(value.getMinutes()).padStart(2, "0");
    const seconds = String(value.getSeconds()).padStart(2, "0");
    return `${year}/${month}/${day}T${hours}:${minutes}:${seconds}`;
  }

  // 文字列の場合も / を統一
  return value.replace(/-/g, "/").slice(0, 19);
};

// 休暇申請
const vacationPost = async () => {
  try {
    if (!selectedShiftId.value || !selectedRequest.value) {
      alert("シフトと休暇種類を選択してください");
      return;
    }

    const payload = {
      shiftId: selectedShiftId.value,
      vacationType: selectedRequest.value?.vacationTypeId,
      beginVacation: normalizeDateTime(beginWork.value),
      endVacation: normalizeDateTime(endWork.value),
      requestComment: reasonText.value,
      requestDate: normalizeDateTime(new Date()), // ここも同じ関数で正規化
    };

    await axios.post("http://localhost:8080/api/send/vacation", payload, {
      withCredentials: true,
    });

    isCompleteModal.value = true;
  } catch (error) {
    console.log(error)
  }
};

onMounted(() => {
  getShiftList();
  getVacationTypeList();
});
</script>

<template>
  <div class="flex h-screen">
    <NavList />
    <main class="flex-1 p-6 bg-gray-100 overflow-auto pt-25 lg:ml-64 lg:pt-7">
      <h1 class="text-xl font-bold mb-3 md:mb-6 text-center">休暇申請</h1>

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

        <!--休暇種類選択-->
        <!-- VacationView.vue -->
        <SelectReqest
          :requests="vacationTypes.vacationTypes || []"
          v-model:selectedRequest="selectedRequest"
        />

        <!--申請理由-->
        <RequestReason v-model:reasonText="reasonText" />

        <!--申請ボタン-->
        <ApplyBtn :vacationPost="vacationPost" />
      </div>
    </main>
  </div>
  <ShiftComplete v-model:isCompleteModal="isCompleteModal" />
</template>
