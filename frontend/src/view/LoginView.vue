<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const test = () => {
  router.push('/management')
}
</script>

<template>
  <div>
    <p>login画面</p>
    <button @click="test" class="bg-red-500 text-white">
      管理画面へ飛ぶ
    </button>
  </div>
</template>
