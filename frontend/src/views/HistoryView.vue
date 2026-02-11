<script setup>
import NavList from "../components/NavList.vue";
import axios from "axios";
import { ref, computed, onMounted } from "vue";

const tabs = ["未対応", "対応済"];
const selectedTab = ref("未対応");

///api/reach/requestlist
const historyData = ref([]); //申請一覧

//申請一覧取得
const getHistoryData = async () => {
  try {
    const response = await axios.get(
      "http://localhost:8080/api/reach/requestlist",
      {
        withCredentials: true,
      }
    );
    historyData.value = response.data.requestList;
    console.log(response.data);
  } catch (error) {
    console.error(error);
  }
};

//履歴タイプ変換
const typeLabel = (type) => {
  switch (type) {
    case 1:
      return "シフト申請";
    case 2:
      return "シフト変更申請";
    case 3:
      return "打刻漏れ申請";
    case 4:
      return "休暇申請";
    case 5:
      return "残業申請";
    case 6:
      return "遅刻・早退・外出申請";
    case 7:
      return "月次申請";
    default:
      return "不明";
  }
};

//statusに応じた背景色取得
const statusClass = (status) => {
  switch (status) {
    case 1: // 承認待ち
      return "bg-yellow-500";
    case 2: // 承認
      return "bg-green-500";
    case 3: // 却下
      return "bg-red-600";
    case 4: // 承認取り消し
      return "bg-gray-500";
    case 5: // 申請取り下げ
      return "bg-indigo-500";
    case 6: // 月次申請済み
      return "bg-blue-500";
    default:
      return "bg-gray-900";
  }
};

//履歴ステータス変換
const statusLabel = (status) => {
  switch (status) {
    case 1:
      return "承認待ち";
    case 2:
      return "承認";
    case 3:
      return "却下";
    case 4:
      return "承認取り消し";
    case 5:
      return "申請取り下げ";
    case 6:
      return "月次申請済み";
    default:
      return "不明";
  }
};

// const history = ref([
//   { type: "有給休暇", date: "2025/05/10", status: "許可待ち", tab: "未対応" },
//   { type: "出張", date: "2025/05/13", status: "許可待ち", tab: "未対応" },
//   { type: "遅刻", date: "2025/05/15", status: "許可待ち", tab: "未対応" },
//   { type: "遅刻", date: "2025/05/15", status: "許可待ち", tab: "未対応" },
//   { type: "遅刻", date: "2025/05/15", status: "許可待ち", tab: "未対応" },

//   { type: "有給休暇", date: "2025/04/30", status: "許可済み", tab: "対応済" },
//   { type: "出張", date: "2025/04/28", status: "許可済み", tab: "対応済" },
// ]);

//対応、未対応フィルタリング(申請日降順)
const filteredData = computed(() => {
  return historyData.value.filter((item) => {
    // 未対応 = 承認待ち(1)のみ
    if (selectedTab.value === "未対応") {
      return item.requestStatus === 1;
    }
    // 対応済 = 承認待ち以外すべて
    if (selectedTab.value === "対応済") {
      return item.requestStatus !== 1;
    }
  });
});

onMounted(() => {
  getHistoryData();
});

//1	シフト申請
//2	シフト変更申請
//3	打刻漏れ申請
//4	休暇申請
//5	残業申請
//6	遅刻、早退、外出申請
//7	月次申請

// 1	承認待ち
// 2	承認
// 3	却下
// 4	承認取り消し
// 5	申請取り下げ
// 6	月次申請済み
</script>

<template>
  <div class="flex h-screen">
    <NavList />
    <main class="flex-1 p-6 bg-gray-100 overflow-auto pt-25 lg:ml-64 lg:pt-7">
      <h1 class="text-xl lg:text-2xl font-bold mb-4">申請履歴</h1>

      <!-- タブ -->
      <div class="flex text-base">
        <button
          v-for="(tab, index) in tabs"
          :key="index"
          @click="selectedTab = tab"
          :class="[
            'px-4 py-2 font-semibold border-t border-l border-r cursor-pointer',
            selectedTab === tab ? 'bg-green-500 text-white' : 'text-green-500',
          ]"
        >
          {{ tab }}
        </button>
      </div>

      <div
        v-if="filteredData.length === 0"
        class="text-center text-lg lg:text-xl p-0 text-gray-500 mt-0 border border-green-500"
      >
        申請履歴はありません
      </div>

      <!-- テーブル -->
      <div class="w-full overflow-x-auto">
        <div class="min-w-[450px] sm:w-full bg-white">
          <table class="w-full text-left">
            <thead
              class="bg-gray-300 text-base sm:text-lg"
              v-if="filteredData.length > 0"
            >
              <tr>
                <th class="px-1 py-1 border sm:px-2 sm:py-2">No.</th>
                <th class="px-1 py-1 border sm:px-2 sm:py-2">履歴の種類</th>
                <th class="px-1 py-1 border sm:px-2 sm:py-2">申請日</th>
                <th class="px-1 py-1 border sm:px-2 sm:py-2">状態</th>
              </tr>
            </thead>
            <tbody>
              <tr
                v-for="(item, index) in filteredData"
                :key="index"
                class="hover:bg-green-100 text-sm sm:text-base  odd:bg-white even:bg-gray-100"
              >
                <td class="px-1 py-1 border sm:px-2 sm:py-2">
                  {{ index + 1 }}
                </td>
                <td class="px-1 py-1 border sm:px-2 sm:py-2">
                  {{ typeLabel(item.requestType) }}
                </td>
                <td class="px-1 py-1 border sm:px-2 sm:py-2">
                  {{ item.requestDate }}
                </td>
                <td class="px-1 py-1 border sm:px-2 sm:py-2">
                  <div class="flex justify-between items-center">
                    <span
                      class="px-0.5 py-0.5 sm:px-1 sm:py-1 text-white rounded"
                      :class="statusClass(item.requestStatus)"
                    >
                      {{ statusLabel(item.requestStatus) }}
                    </span>

                    <span class="text-blue-600 cursor-pointer hover:underline">
                      詳細
                    </span>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </main>
  </div>
</template>
