import {createApp} from 'vue/dist/vue.esm-bundler.js';
// import App from "@/App.vue";
// import VueRouter from "vue-router";
import {createWebHistory, createRouter} from "vue-router";
import ListUser from "@/ListUser.vue";
import Login from "@/Login.vue";
// import App from "@/App.vue";

// Vue.use(VueRouter);

const routes = [
    {
        path: "/list",
        name: "ListUser",
        component: ListUser
    },
    {
        path:'/login',
        name: "LoginUser",
        component: Login
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});
// export default router;

const app = createApp({})
app.use(router)
app.mount('#app')
