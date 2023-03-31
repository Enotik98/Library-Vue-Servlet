<template>
  <HeaderMenu />
<div class="container d-flex justify-content-center align-items-center h-100 w-75 mt-5">
  <form class="border rounded p-4" @submit.prevent="submitForm">
  <h2 class="mb-4">Реєстрація</h2>
    <div class="form-group">
      <label for="username">Username:</label>
      <input type="text" id="username" class="form-control" v-model="formData.username">
    </div>
    <div class="form-group">
      <label for="email">Email:</label>
      <input type="email" id="email" class="form-control" v-model="formData.email">
    </div>
    <div class="form-group">
      <label for="password">Password:</label>
      <input type="password" id="password" class="form-control" v-model="formData.password">
    </div>
    <button type="submit" class="btn btn-dark float-right">Зареєструватись</button>
  </form>
</div>
</template>

<script>
import HeaderMenu from "@/components/Header.vue";

export default {
  name: "RegistrationUser",
  components: {HeaderMenu},
  data(){
    return {
      formData:{
        username: '',
        password: '',
        email: '',
        role: 'CLIENT'
      }
    }
  },
  methods: {
    async submitForm(){
      try {
        const response = await fetch('http://localhost:8080/testServlet_war/user', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(this.formData)
        });
        if (response.ok){
          this.$router.push('/login')
        }else {
          console.error('Сталася помилка');
        }
      }catch (error){
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