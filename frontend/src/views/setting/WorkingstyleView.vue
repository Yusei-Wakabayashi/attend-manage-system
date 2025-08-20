<script setup>
import ApplyBtn from "../../components/ApplyBtn.vue";
import NavList from "../../components/NavList.vue";
</script>

<!--勤務形態設定ページ-->
<template>
  <div class="flex h-screen">
    <NavList />
    <main class="flex-1 p-6 bg-gray-100 overflow-auto pt-25 lg:ml-64 lg:pt-7">

       <h1 class="text-xl font-bold mb-3 md:mb-6 text-center">勤務形態設定</h1>

      <div class="bg-white p-4 md:p-6 rounded-lg shadow-md space-y-5">

       <ApplyBtn />
      </div>
    </main>
  </div>
</template>
