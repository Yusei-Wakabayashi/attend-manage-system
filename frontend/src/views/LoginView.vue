<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";

const router = useRouter();

const loginId = ref("");
const password = ref("");
const error = ref("");

//仮ID wakabayashi@karaage.com
//仮パスワード password
const login = async () => {
  // try {
  //   await axios.post("http://localhost:8080/api/send/login", {
  //     id: loginId.value,
  //     password: password.value,
  //   });
  //   console.log(loginId.value, password.value)
  //   router.push("/application");
  // } catch (error) {
  //   console.log(`ログインエラー${error}`);
  // }
  router.push("/application");
};
</script>

<template>
  <div class="flex items-center justify-center min-h-screen bg-gray-200">
    <div
      class="bg-white w-full max-w-70 rounded-md p-5 shadow-md md:max-w-md lg:max-w-lg"
    >
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
