<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();

const loginId = ref("");
const password = ref("");
const error = ref("");

const login = () => {
  if (loginId.value === "yokoji" && password.value === "123") {
    router.push("/application");
  } else {
    error.value = "ログインIDまたはパスワードが違います";
  }
}
</script>

<template>
  <div class="flex items-center justify-center min-h-screen bg-gray-200">
    <div class="bg-white w-full max-w-70 rounded-md p-5 shadow-md md:max-w-md lg:max-w-lg">
      <h2 class="text-center font-medium md:text-lg lg:text-xl">ログイン</h2>
      <form @submit.prevent="login">
        <div class="pt-5 pb-2.5">
          <input
            v-model="loginId"
            type="text"
            class="w-full border-2 border-gray-200 rounded-md focus:outline-none focus:ring focus:ring-green-200 hover:ring hover:ring-green-200 p-1 text-base md:text-lg lg:text-xl"
            placeholder="ログインID"
          />
        </div>
        <div class="pt-2.5 pb-2.5">
          <input
            v-model="password"
            type="password"
            class="w-full border-2 border-gray-200 rounded-md focus:outline-none focus:ring focus:ring-green-200 hover:ring hover:ring-green-200 p-1 text-base md:text-lg lg:text-xl"
            placeholder="パスワード"
          />
        </div>
        <div v-if="error" class="text-red-500 text-center text-xs md:text-base">
          {{ error }}
        </div>
        <div class="pt-2.5">
          <button
            type="submit"
            class="w-full bg-green-500 hover:bg-green-600 text-white rounded-md transition cursor-pointer p-1 font-medium text-base md:text-lg lg:text-xl"
          >
            ログイン
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
