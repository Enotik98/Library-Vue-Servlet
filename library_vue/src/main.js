import {createApp} from 'vue/dist/vue.esm-bundler.js';
import {createWebHistory, createRouter} from "vue-router";
import Login from "@/components/user/LoginUser.vue";
import UserInfo from "@/components/user/UserInfo.vue";
import RegistrationUser from "@/components/user/RegistrationUser.vue";
import ListBook from "@/components/book/ListBook.vue";
import BookInfo from "@/components/book/BookInfo.vue";
import ListOrder from "@/components/order/ListOrder.vue";
import OrderInfo from "@/components/order/OrderInfo.vue";
import CreateBook from "@/components/book/CreateBook.vue";
import App from "@/App.vue";

import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.js'
import 'jquery/dist/jquery.js'
import Notiflix from 'notiflix'
import store from "@/store";


const routes = [
    {
        path: "/profile",
        name: "UserInfo",
        component: UserInfo
    },
    {
        path:'/login',
        name: "LoginUser",
        component: Login
    },
    {
        path: '/registration',
        name: 'RegistrationUser',
        component: RegistrationUser
    },
    {
        path: '/books',
        name: 'ListBook',
        component: ListBook
    },
    {
        path: '/book/:id',
        name: 'BookInfo',
        component: BookInfo
    },
    {
        path: '/orders',
        name: 'ListOrder',
        component: ListOrder
    },
    {
        path: '/order/:id',
        name: 'OrderInfo',
        component: OrderInfo,
    },
    {
        path: '/add-book',
        name: 'AddBook',
        component: CreateBook
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});
// export default router;
const app = createApp(App)
app.use(router)
app.use(store);
app.config.globalProperties.$Notiflix = Notiflix;
app.mount('#app')
