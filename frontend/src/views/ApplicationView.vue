<script setup>
import NavList from "../components/NavList.vue";

import { useRoute } from "vue-router";

const route = useRoute();

const buttonGroups = [
  {
    title: "🛠 業務設定",
    items: [
      { label: "勤務形態設定", color: "blue", path: "/workingstyle" },
      { label: "承認者設定", color: "blue", path: "/approver" },
    ],
  },
  {
    title: "📝 各種申請",
    items: [
      { label: "シフト申請", color: "green", path: "/shift" },
      { label: "時間変更申請", color: "green", path: "/timechange" },
      { label: "打刻漏れ申請", color: "green", path: "/missingstamping" },
      { label: "休暇申請", color: "green", path: "/vacation" },
      { label: "残業申請", color: "green", path: "/overtime" },
      { label: "遅刻・早退・外出申請", color: "green", path: "/attendancerequest" },
      { label: "月次申請", color: "green", path: "/monthly" },
    ],
  },
];

const getColorClass = (color) => {
  const map = {
    green: "bg-green-500 hover:bg-green-600 border-green-600",
    blue: "bg-blue-500 hover:bg-blue-600 border-blue-600",
  };
  return map[color];
};
</script>

<template>
  <div class="flex h-screen">
    <NavList />
    <main class="flex-1 p-6 bg-gray-100 overflow-auto pt-25 lg:ml-64 lg:pt-7">
      <h1
        class="text-lg font-bold mb-6 bg-blue-200 text-blue-900 rounded-md p-5 text-center md:text-xl lg:text-2xl"
      >
        お知らせ（打刻漏れ申請をしていません）
      </h1>

      <div
        v-for="(group, gIndex) in buttonGroups"
        :key="gIndex"
        class="mb-10 bg-gray-200 p-5"
      >
        <h2 class="text-lg font-semibold mb-4">{{ group.title }}</h2>
        <div class="grid md:grid-cols-2 xl:grid-cols-3 gap-4">
          <router-link
            v-for="(item, index) in group.items"
            :key="index"
            :to="item.path"
          >
            <button
              :class="[
                'w-full py-4 rounded-md border-3 shadow-md font-bold text-white text-lg lg:text-xl cursor-pointer',
                getColorClass(item.color),
              ]"
            >
              {{ item.label }}
            </button>
          </router-link>
        </div>
      </div>
    </main>
  </div>
</template>
