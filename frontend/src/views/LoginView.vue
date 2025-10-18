<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";

const router = useRouter();

const loginId = ref("wakabayashi@karaage.com");
const password = ref("password");
const error = ref("");

//ユーザー情報切り替え関数
const setUser = (type) => {
  if (type === "user") {
    loginId.value = "wakabayashi@karaage.com";
    password.value = "password";
  } else if (type === "admin") {
    loginId.value = "nanasinogonbei@karaage.com";
    password.value = "2kcowoc8ec";
  }
};

//一般
//wakabayashi@karaage.com
//password

//管理者
//nanasinogonbei@karaage.com
//2kcowoc8ec
const login = async () => {
  try {
    await axios.post(
      "http://localhost:8080/api/send/login",
      {
        id: loginId.value,
        password: password.value,
      },
      { withCredentials: true }
    );
    router.push("/application");
  } catch (error) {
    console.log(`ログインエラー${error}`);
  }
  router.push("/application");
};
</script>

<template>
  <div class="flex items-center justify-center min-h-screen bg-gray-200">
    <div
      class="relative bg-white w-full max-w-70 rounded-md p-5 shadow-md md:max-w-md lg:max-w-lg"
    >
      <div class="flex items-center justify-center space-x-4 pb-4">
        <!-- 左画像 -->
        <img src="../img/bikkuri.png" alt="左画像" class="w-10 h-10" />

        <h2 class="text-center font-medium md:text-lg lg:text-xl">ログイン</h2>


        <!-- 右画像 -->
        <img src="../img/karaage.png" alt="右画像" class="w-10 h-10" />
      </div>

      <!-- 切り替えボタン -->
      <div class="flex justify-center space-x-2 pb-4">
        <button
          class="bg-blue-500 hover:bg-blue-600 text-white px-4 py-1 rounded"
          @click="setUser('user')"
        >
          一般
        </button>
        <button
          class="bg-red-500 hover:bg-red-600 text-white px-4 py-1 rounded"
          @click="setUser('admin')"
        >
          管理者
        </button>
      </div>

      <form @submit.prevent="login">
        <div class="pt-2 pb-2">
          <input
            v-model="loginId"
            type="text"
            class="w-full border-2 border-gray-200 rounded-md focus:outline-none focus:ring focus:ring-green-200 hover:ring hover:ring-green-200 p-1 text-base md:text-lg lg:text-xl"
            placeholder="ログインID"
          />
        </div>
        <div class="pt-2 pb-2">
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
        <div class="pt-2">
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
