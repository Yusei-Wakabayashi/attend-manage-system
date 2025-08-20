<script setup>
import NavList from "../components/NavList.vue";
import { ref, computed } from "vue";

const tabs = ["未対応", "対応済"];
const selectedTab = ref("未対応");

const history = ref([
  { type: "有給休暇", date: "2025/05/10", status: "許可待ち", tab: "未対応" },
  { type: "出張", date: "2025/05/13", status: "許可待ち", tab: "未対応" },
  { type: "遅刻", date: "2025/05/15", status: "許可待ち", tab: "未対応" },
  { type: "遅刻", date: "2025/05/15", status: "許可待ち", tab: "未対応" },
  { type: "遅刻", date: "2025/05/15", status: "許可待ち", tab: "未対応" },

  { type: "有給休暇", date: "2025/04/30", status: "許可済み", tab: "対応済" },
  { type: "出張", date: "2025/04/28", status: "許可済み", tab: "対応済" },
]);

//対応、未対応フィルタリング(申請日降順)
const filteredData = computed(() =>
  history.value
    .filter((item) => item.tab === selectedTab.value)
    .sort((a, b) => b.date.localeCompare(a.date))
);

//statusの色変える(APIだと多分trueかfalseにすると思う)
const statusClass = (status) => {
  switch (status) {
    case "許可待ち":
      return "bg-red-500";
    case "許可済み":
      return "bg-green-500";
    default:
      //スペルミスあったとき用に色変えとく
      return "bg-gray-900";
  }
};
</script>

<template>
  <div class="flex h-screen">
    <NavList />
    <main
      class="flex-1 p-6 bg-gray-100 overflow-auto pt-25 lg:ml-64 lg:pt-7"
    >
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

      <!-- テーブル -->
      <div class="w-full overflow-x-auto text-base">
        <div
          class="min-w-[760px] md:w-full bg-white"
        >
          <table class="w-full text-left">
            <thead class="bg-gray-300">
              <tr>
                <th class="px-2 py-2 border">No.</th>
                <th class="px-2 py-2 border">履歴の種類</th>
                <th class="px-2 py-2 border">申請日</th>
                <th class="px-2 py-2 border">状態</th>
              </tr>
            </thead>
            <tbody>
              <tr
                v-for="(item, index) in filteredData"
                :key="index"
                class="hover:bg-green-100"
              >
                <td class="px-2 py-2 border">{{ index + 1 }}</td>
                <td class="px-2 py-2 border">{{ item.type }}</td>
                <td class="px-2 py-2 border">{{ item.date }}</td>
                <td class="px-2 py-2 border">
                  <span
                    class="px-2 py-1 text-white rounded"
                    :class="statusClass(item.status)"
                  >
                    {{ item.status }}
                  </span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </main>
  </div>
</template>
