<template>
  <div class="min-h-screen bg-blue-500 flex items-center justify-center">
    <h1 class="text-white text-4xl font-bold">
      Tailwind CSS は動作しています！
    </h1>
    <h1>VueとSpring連携</h1>
    <p>{{ message }}</p>
  </div>
</template>


<script setup>
import { ref, onMounted } from 'vue'

const message = ref('読み込み中…')

onMounted(async () => {
  const res = await fetch(import.meta.env.VITE_API_URL + "/message")
  message.value = await res.text()
})
</script>
