<template>
  <div>
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
