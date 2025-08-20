<script setup>
import { ref } from "vue";
import { useRoute } from "vue-router";

const navList = ref([
  { label: "各種申請", path: "/application" },
  { label: "出勤簿", path: "/work" },
  { label: "申請履歴", path: "/history" },
]);

const route = useRoute();
const isOpen = ref(false);
</script>

<template>
  <div class="flex h-screen text-base z-999">
    <div class="fixed w-full h-18 z-1000 bg-green-700 lg:hidden">
      <button
        class="absolute top-2.5 left-3 bg-green-500 text-white py-2.5 px-4 rounded text-2xl"
        @click="isOpen = !isOpen"
      >
        ☰
      </button>
    </div>

    <nav
      :class="[
        'fixed h-full w-full bg-green-500 text-white flex flex-col justify-between pt-20 pb-2 lg:py-4 lg:w-64 duration-300',
        isOpen ? 'translate-x-0' : '-translate-x-full',
        'lg:translate-x-0', // デスクトップでは常に表示
      ]"
    >
      <!--上部メニュー-->
      <div>
        <ul class="px-2 space-y-2">
          <li
            class="md:text-lg lg:text-xl font-bold"
            v-for="(item, index) in navList"
            :key="index"
          >
            <router-link
              :to="item.path"
              :class="[
                'block rounded px-3 py-4',
                route.path === item.path
                  ? 'bg-green-700'
                  : 'hover:bg-green-600',
              ]"
            >
              {{ item.label }}
            </router-link>
          </li>
        </ul>
      </div>

      <!--下部メニュー-->
      <div class="space-y-2 px-2 md:text-lg lg:text-xl font-bold">
        <router-link
          to="/support"
          class="block bg-blue-500 hover:bg-blue-600 rounded px-3 py-2"
        >
          サポート
        </router-link>
        <div class="block bg-gray-500 rounded px-3 py-2">
          アカウント名: よこじ
        </div>
      </div>
    </nav>
  </div>
</template>
