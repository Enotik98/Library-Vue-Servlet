<template>
  <HeaderMenu />
  <div class="container d-flex justify-content-center align-items-center h-100 w-75 mt-5">
    <form class="border rounded p-4" @submit.prevent="submitForm">
      <h2 class="mb-4">Login</h2>
      <div class="form-group">
        <label for="username">Username:</label>
        <input type="text" class="form-control" id="username" v-model="formData.username" required>
      </div>
      <div class="form-group">
        <label for="password">Password:</label>
        <input type="password" class="form-control" id="password" v-model="formData.password" required>
      </div>
      <router-link to="/registration">Зареєструватись</router-link>
      <button type="submit" class="btn btn-dark float-right">Log in</button>
    </form>
  </div>
</template>
<script>

import HeaderMenu from "@/components/Header.vue";

export default {
  name: "LoginUser",
  components: {HeaderMenu},
  data() {
    return {
      formData: {
        username: '',
        password: ''
      }
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

        if (response.ok) {
          const data = await response.json();
          localStorage.setItem('AccessToken', data['AccessToken']);
          localStorage.setItem('RefreshToken', data['RefreshToken']);
          // let id = data["id"];
          // console.log(id);
          this.$router.push("/profile");
          console.log(data)
          console.log('Дані успішно відправлені');
        } else {
          console.error('Сталася помилка');
        }
      } catch (error) {
        console.error(error);
      }
    }
  }
}
</script>
<style scoped>

form{
  width: 400px;
}
</style>
