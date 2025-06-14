// src/main.js
import { createApp } from 'vue'
import App from './App.vue'
import './style.css' // ← これを追加

import router from './router/index' // ← 追加

createApp(App).use(router).mount('#app') // ← routerをuseする