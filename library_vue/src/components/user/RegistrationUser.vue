<template>
  <div class="container mt-5">
    <div class="d-flex justify-content-center align-items-center">
      <form class="border rounded p-4 mb-5" @submit.prevent="submitForm">
        <div class="d-flex justify-content-center">
          <img src="../../assets/registration.png" class="card-img">
        </div>
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
          <label>Пароль:</label>
          <input type="password" maxlength="20" minlength="6" class="form-control" v-model="formData.password" required>
        </div>
        <div>
          <button type="submit" class="btn btn-dark ">
            Зареєструватись
            <img src="../../assets/add-user.png" class="btn-img">
          </button>
        </div>
        <div class="d-flex justify-content-center align-items-center form-landing">
          <span class="form-decoration">або</span>
        </div>
        <div>
          <router-link class="btn btn-dark " to="/login">
            Вхід
            <img src="../../assets/sign-in.png" class="btn-img">
          </router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script>

import {sendRequest} from "@/script/request";

export default {
  name: "RegistrationUser",
  data() {
    return {
      formData: {
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
    async submitForm() {
      try {
        const response = await sendRequest('/user', 'POST', this.formData)
        if (response.ok) {
          this.$Notiflix.Notify.success("Успішно!")
          this.$router.push('/login')
        } else {
          this.$Notiflix.Notify.failure("Виникла помилка")
        }
      } catch (error) {
        console.error(error);
      }
    }
  }
}
</script>

<style scoped>
form {
  width: 420px;
}
.card-img {
  width: 30%;
  object-fit: cover;
  border-radius: 0;
  /*background-color: #212529;*/
}
.btn-img{
  width: 17px;
  object-fit: cover;
  border-radius: 0;
}
.btn{
  width: 100%;
}
.form-landing:after,
.form-landing:before{
  content: "";
  width: 100%;
  height: 2px;
  background-color: #cfd1d3;
}
.form-decoration{
  position: relative;
  padding: 8px;
}
.form-group{
  margin: 12px 0;
}
</style>