import { createApp } from 'vue'
import { createPinia } from "pinia";
import App from './App.vue'
import './style.css'
import router from './router/index'

const app = createApp(App);

//Piniaの作成と登録
const pinia = createPinia();
app.use(pinia);

//Routerの登録
app.use(router);

//アプリをマウント
app.mount('#app');
