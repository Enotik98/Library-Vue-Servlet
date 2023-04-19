import {createStore} from 'vuex';
import {sendRequest} from "@/script/request";

const store = createStore({
    state: {
        isLoggedIn: !!localStorage.getItem("AccessToken"),
        isClient: true,
    },
    mutations: {
        login(state) {
            state.isLoggedIn = !!localStorage.getItem("AccessToken");
            if (state.isLoggedIn) state.isClient = true
        },
        setClient(state, value) {
            state.isClient = value !== "ADMIN";

        },
        logout(state) {
            localStorage.removeItem("AccessToken")
            localStorage.removeItem("RefreshToken")
            state.isLoggedIn = false;
            state.isClient = true
        }
    },
    actions: {
        async fetchClient({commit}) {
            if (localStorage.getItem("AccessToken")) {
                const resp = await sendRequest('/user/info', 'GET', null);
                if (resp.ok) {
                    const data = await resp.json()
                    commit('setClient', data['role'])
                } else {
                    commit('setClient', 'CLIENT')
                }
            }else {
                commit('setClient', 'CLIENT')
            }
        }
    }
})

export default store