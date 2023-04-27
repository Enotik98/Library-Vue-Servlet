<template>
  <div class="container mt-5">
    <div class="d-flex justify-content-center align-items-center">
      <form class="border rounded p-4" @submit.prevent="submitForm">
        <div class="d-flex justify-content-center">
          <img src="../../assets/user.png" class="card-img">
        </div>
        <h2 class="mb-4 mt-2 d-flex justify-content-center">Вхід</h2>
        <div class="form-group">
          <label>E-mail:</label>
          <input type="email" maxlength="30" class="form-control " v-model="formData.email"
                 placeholder="name@example.com" required>
        </div>
        <div class="form-group my-3 ">
          <label>Пароль:</label>
          <input type="password" maxlength="20" minlength="4" class="form-control "
                 v-model="formData.password" placeholder="******" required>
        </div>
        <div>
          <button type="submit" class="btn btn-dark ">
            Вхід
            <img src="../../assets/sign-in.png" class="btn-img">
          </button>
        </div>
        <div class="d-flex justify-content-center align-items-center form-landing">
          <span class="form-decoration">або</span>
        </div>
        <div>
          <router-link class="btn btn-dark " to="/registration">
            Зареєструватись
            <img src="../../assets/add-user.png" class="btn-img">
          </router-link>
        </div>
        <div class="d-flex justify-content-center align-items-center form-landing my-1">
          <span class="form-decoration"></span>
        </div>
        <div>
          <button @click="log_in" class="btn btn-outline-dark">Вхід з Auth0</button>
        </div>

      </form>
    </div>
  </div>

</template>
<script>


import {mapMutations} from "vuex";
import {sendRequest} from "@/script/request";

export default {
  name: "LoginUser",
  data() {
    return {
      formData: {
        email: '',
        password: ''
      },
    }
  },

  methods: {
    log_in(){
      try {
        this.$auth0.loginWithRedirect();
      }catch (e) {
        console.log("1 + " + e)
      }
    },

    async submitForm() {
      try {
        const response = await sendRequest('/login', 'POST', this.formData)

        if (response.status === 401) {
          const data = await response.json();
          this.$Notiflix.Notify.failure(data["error"])
        }
        if (response.ok) {
          const data = await response.json();
          localStorage.setItem('AccessToken', data['AccessToken']);
          localStorage.setItem('RefreshToken', data['RefreshToken']);

          //check localStorage for Header.vue
          this.login()

          this.$router.push("/profile");
        }
      } catch (error) {
        console.error(error);
      }
    },
    ...mapMutations(['login'])
  }
}
</script>
<style scoped>

form {
  width: 420px;
}

.form-landing:after,
.form-landing:before {
  content: "";
  width: 100%;
  height: 2px;
  background-color: #cfd1d3;
}

.form-decoration {
  position: relative;
  padding: 8px;
}

.card-img {
  width: 30%;
  object-fit: cover;
  border-radius: 0;
}

.btn-img {
  width: 17px;
  object-fit: cover;
  border-radius: 0;
}

.btn {
  width: 100%;
}
</style>
