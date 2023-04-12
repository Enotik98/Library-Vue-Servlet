<template>
<div class="container d-flex justify-content-center align-items-center h-100 w-75 mt-5">
  <form class="border rounded p-4" @submit.prevent="submitForm">
  <h2 class="mb-4">Реєстрація</h2>
    <div class="form-group">
      <label>Email:</label>
      <input type="email" maxlength="30" class="form-control" v-model="formData.email" required>
    </div>
    <div class="form-group">
      <label>Ім'я:</label>
      <input type="text" maxlength="25" class="form-control" v-model="formData.username" required>
    </div>
    <div class="form-group">
      <label>Прізвище:</label>
      <input type="text" maxlength="30" class="form-control" v-model="formData.surname" required>
    </div>
    <div class="form-group">
      <label>Адреса:</label>
      <input type="text" class="form-control" maxlength="50" v-model="formData.address" required>
    </div>
    <div class="form-group">
      <label>Password:</label>
      <input type="password" maxlength="20" minlength="6" class="form-control" v-model="formData.password" required>
    </div>
    <button type="submit" class="btn btn-dark float-right mt-3">Зареєструватись</button>
  </form>
</div>
</template>

<script>

export default {
  name: "RegistrationUser",
  data(){
    return {
      formData:{
        username: '',
        surname: '',
        address: '',
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
          this.$Notiflix.Notify.success("Успішно!")
          this.$router.push('/login')
        }else {
          this.$Notiflix.Notify.failure("Виникла помилка")
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