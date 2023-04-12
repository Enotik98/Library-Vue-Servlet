import {createStore} from 'vuex';

const store = createStore({
    state: {
        isLoggedIn: !!localStorage.getItem("AccessToken"),
        isClient: null
    },
    mutations: {
        login(state) {
            state.isLoggedIn = !!localStorage.getItem("AccessToken");
            if (state.isLoggedIn) state.isClient = true
        },
        setClient(state, value) {
            if (value === "ADMIN") {
                state.isClient = false
            }else if (value === "CLIENT"){
                state.isClient = true
            }
        },
        logout(state) {
            localStorage.removeItem("AccessToken")
            localStorage.removeItem("RefreshToken")
            state.isLoggedIn = false;
            state.isClient = null
        }
    },
})
export default store