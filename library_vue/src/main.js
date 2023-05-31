import {createApp} from 'vue/dist/vue.esm-bundler.js';
import {createWebHistory, createRouter} from "vue-router";
import {createAuth0} from '@auth0/auth0-vue'


import Login from "@/components/user/LoginUser.vue";
import UserInfo from "@/components/user/UserInfo.vue";
import RegistrationUser from "@/components/user/RegistrationUser.vue";
import ListBook from "@/components/book/ListBook.vue";
import BookInfo from "@/components/book/BookInfo.vue";
import ListOrder from "@/components/order/ListOrder.vue";
import OrderInfo from "@/components/order/OrderInfo.vue";
import CreateBook from "@/components/book/CreateBook.vue";
import App from "@/App.vue";

import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap";
import 'jquery/dist/jquery.js'
import Notiflix from 'notiflix'
import store from "@/script/store";
import Callback from "@/components/Callback.vue";
import RegistrationAuth0 from "@/components/RegistrationAuth0.vue";


const routes = [
    {
        path: '/',
        name: 'ListBook',
        component: ListBook
    },
    {
        path: '/book/:id',
        name: 'BookInfo',
        component: BookInfo,
    },
    {
        path: "/profile",
        name: "UserInfo",
        component: UserInfo
    },
    {
        path: '/login',
        name: "LoginUser",
        component: Login
    },
    {
        path: '/registration',
        name: 'RegistrationUser',
        component: RegistrationUser
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
    },
    {
        path: '/callback',
        name: "CallBack",
        component: Callback
    },
    {
        path: '/registration-auth0',
        name: "RegistrationAuth0",
        component: RegistrationAuth0
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});
const app = createApp(App)
app.use(router)
app.use(store);
app.use(
    createAuth0({
        domain: "dev-sx5kw23vuznwkzbt.us.auth0.com",
        clientId: "HRd9v3JZfa3K3uQbiburrp5mJkjQlWHr",
        // clientId: "bYU6GyFjlhuNHkhol97Z9BA1E2ZMQ5Jl",
        authorizationParams: {
            audience: "http://localhost:8081",
            redirect_uri: "http://localhost:8081/callback"
            // redirect_uri: window.location.origin
        },
    })
);

app.config.globalProperties.$Notiflix = Notiflix;
app.mount('#app')
