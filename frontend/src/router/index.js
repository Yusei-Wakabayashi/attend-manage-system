// src/router/index.ts
import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/store/userStore'

import LoginView from '../views/LoginView.vue'
import ApplicationView from '../views/ApplicationView.vue'
import WorkView from '../views/WorkView.vue'
import HistoryView from '../views/HistoryView.vue'
import SupportView from '../views/SupportView.vue'
import ShiftView from '../views/applications/ShiftView.vue'
import TimechangeView from '../views/applications/TimechangeView.vue'
import MissingstampingView from '../views/applications/MissingstampingView.vue'
import VacationView from '../views/applications/VacationView.vue'
import OvertimeView from '../views/applications/OvertimeView.vue'
import AttendanceRequestView from '../views/applications/AttendanceRequestView.vue'
import MonthlyView from '../views/applications/MonthlyView.vue'
import WorkingstyleView from '../views/setting/WorkingstyleView.vue'
import ApproverView from '../views/setting/ApproverView.vue'

const routes = [
  { path: '/', component: LoginView },
  { path: '/application', component: ApplicationView },
  { path: '/work', component: WorkView },
  { path: '/history', component: HistoryView },
  { path: '/support', component: SupportView },
  { path: '/shift', component: ShiftView },
  { path: '/timechange', component: TimechangeView },
  { path: '/missingstamping', component: MissingstampingView },
  { path: '/vacation', component: VacationView },
  { path: '/overtime', component: OvertimeView },
  { path: '/attendancerequest', component: AttendanceRequestView },
  { path: '/monthly', component: MonthlyView },
  // 管理者専用ルート
  { path: '/workingstyle', component: WorkingstyleView, meta: { requiresAdmin: true } },
  { path: '/approver', component: ApproverView, meta: { requiresAdmin: true } },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, next) => {
  const userStore = useUserStore()

  if (to.meta.requiresAdmin && !userStore.admin) {
    //管理者でなければアプリケーションのページに戻す
    return next('/application')
  }

  next()
})

export default router
