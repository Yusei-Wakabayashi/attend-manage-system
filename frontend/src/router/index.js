// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import LoginView from "../view/LoginView.vue"
import ManagementView from '../view/ManagementView.vue'

const routes = [
  { path: '/', component: LoginView },
  { path: '/management', component: ManagementView },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router