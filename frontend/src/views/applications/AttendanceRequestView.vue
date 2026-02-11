<script setup>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import axios from "axios";
import NavList from "../../components/NavList.vue";
import SelectShift from "../../components/SelectShift.vue";

// ルート取得
const route = useRoute();

// 遅刻・早退・外出の種類
const requests = ref([
  { id: 1, label: "遅刻" },
  { id: 2, label: "早退" },
  { id: 3, label: "外出" },
]);

// 選択値
const selectedRequest = ref(null);
const selectedShiftId = ref(null);

// 時刻
const beginWork = ref("");  // 始業
const endWork = ref("");    // 終業
const beginBreak = ref("01:00"); // 休憩開始
const endBreak = ref("01:00");   // 休憩終了

// 申請理由
const requestComment = ref("");

// シフト取得
const shifts = ref([]);
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

// yyyy/MM/ddTHH:mm:ss に変換
const normalizeDateTime = (time) => {
  if (!time) return null;
  const [hours, minutes] = time.split(":");
  const now = new Date();
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, "0");
  const day = String(now.getDate()).padStart(2, "0");
  return `${year}/${month}/${day}T${hours}:${minutes}:00`;
};

// 勤務例外申請 POST
const AttendanceRequestPost = async () => {
  if (!selectedShiftId.value || !selectedRequest.value) {
    alert("シフトと申請種類を選択してください");
    return;
  }

  const payload = {
    shiftId: selectedShiftId.value,
    otherType: selectedRequest.value.id,
    beginOtherTime: normalizeDateTime(beginWork.value),
    endOtherTime: normalizeDateTime(endWork.value),
    requestComment: requestComment.value,
    requestDate: normalizeDateTime(new Date().toTimeString().slice(0,5)),
  };

  console.log("送信データ:", payload);

  try {
    await axios.post("http://localhost:8080/api/send/othertime", payload, {
      withCredentials: true,
    });
    alert("申請を送信しました");
  } catch (error) {
    console.error("申請エラー:", error.response?.data || error);
    alert("申請に失敗しました。コンソールを確認してください。");
  }
};

onMounted(() => {
  getShiftList();
});
</script>

<template>
  <div class="flex h-screen">
    <!-- NavList はそのまま -->
    <NavList />

    <main class="flex-1 p-6 bg-gray-100 overflow-auto pt-25 lg:ml-64 lg:pt-7">
      <h1 class="text-xl font-bold mb-6 text-center">
        勤務例外申請（遅刻・早退・外出）
      </h1>

      <div class="bg-white p-6 rounded-lg shadow-md space-y-5">

        <!-- 対象シフト選択 -->
        <SelectShift
          :shifts="shifts?.shiftList || []"
          v-model:selectedShiftId="selectedShiftId"
          v-model:beginWork="beginWork"
          v-model:endWork="endWork"
          v-model:beginBreak="beginBreak"
          v-model:endBreak="endBreak"
        />

        <!-- 時刻直接編集 -->
        <div>
          <label class="block font-semibold mb-2">始業時刻</label>
          <input type="time" v-model="beginWork"
            class="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring focus:ring-green-200" />
        </div>

        <div>
          <label class="block font-semibold mb-2">終業時刻</label>
          <input type="time" v-model="endWork"
            class="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring focus:ring-green-200" />
        </div>

        <div v-if="route.path !== '/attendancerequest'">
          <label class="block font-semibold mb-2">休憩開始時刻</label>
          <input type="time" v-model="beginBreak"
            class="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring focus:ring-green-200" />
        </div>

        <div v-if="route.path !== '/attendancerequest'">
          <label class="block font-semibold mb-2">休憩終了時刻</label>
          <input type="time" v-model="endBreak"
            class="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring focus:ring-green-200" />
        </div>

        <!--申請種類選択-->
        <div>
          <label class="block font-semibold mb-2">申請種類</label>
          <select v-model="selectedRequest" class="w-full p-2 border border-gray-300 rounded">
            <option disabled :value="null">選択してください</option>
            <option v-for="req in requests" :key="req.id" :value="req">{{ req.label }}</option>
          </select>
        </div>

        <!--申請理由-->
        <div>
          <label class="block font-semibold mb-2">申請理由</label>
          <textarea v-model="requestComment"
            class="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring focus:ring-green-200"
            rows="3"
            placeholder="任意で理由を入力"></textarea>
        </div>

        <!--申請ボタン-->
        <div class="text-center">
          <button @click="AttendanceRequestPost"
            class="px-6 py-2 bg-green-500 text-white rounded hover:bg-green-600">申請</button>
        </div>

      </div>
    </main>
  </div>
</template>
