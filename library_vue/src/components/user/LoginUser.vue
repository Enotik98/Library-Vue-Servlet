<template>
  <div class="container d-flex justify-content-center align-items-center mt-5">
    <form class="border rounded p-4 align-items-center" @submit.prevent="submitForm">
      <h2 class="mb-4">Login</h2>
      <div class="form-group">
        <label>Email:</label>
        <input type="email" maxlength="30" class="form-control form-control-sm" v-model="formData.email" required>
      </div>
      <div class="form-group my-3">
        <label>Password:</label>
        <input type="password" maxlength="20" minlength="4" class="form-control form-control-sm" v-model="formData.password" required>
      </div>
      <router-link to="/registration">Зареєструватись</router-link>
      <button type="submit" class="btn btn-dark float-end">Login</button>
    </form>
  </div>

</template>
<script>


import {mapMutations} from "vuex";

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
    async submitForm() {
      try {

        const response = await fetch('http://localhost:8080/testServlet_war/login', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(this.formData)
        });

        // console.log(response)
        // console.log(response.json())
        if (response.status === 401){
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
  width: 400px;
}
</style>
