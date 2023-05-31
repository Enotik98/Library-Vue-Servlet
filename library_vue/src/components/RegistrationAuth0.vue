<template>
  <div class="container d-flex justify-content-center align-items-center mt-5 position-relative">
    <div>
      <form @submit.prevent="submitUser" class="border rounded p-4">
        <div class="row">
          <label class="col-4">Ім'я:</label>
          <div class="col-8">
            <input type="text" class="form-control" v-model="user.username" required>
          </div>
        </div>
        <div class="row">
          <label class="col-4">Прізвище:</label>
          <div class="col-8">
            <input type="text" class="form-control" v-model="user.surname" required>
          </div>
        </div>
        <div class="row">
          <label class="col-4">Email:</label>
          <div class="col-8">
            <input type="text" class="form-control"  v-model="user.email" disabled>
          </div>
        </div>
        <div class="row">
          <label class="col-4">Адреса:</label>
          <div class="col-8">
            <input type="text" class="form-control" v-model="user.address" required>
          </div>
        </div>
        <div class="row">
          <div>
            <button type="submit" class="btn btn-dark">Registration</button>
          </div>
        </div>
      </form>
    </div>

  </div>
</template>

<script>
import {sendRequest} from "@/script/request";

export default {
  name: "RegistrationAuth0",
  data() {
    return {
      user: {
        username: "",
        surname: "",
        email: "",
        address: "",
        password: "",
        type_auth: "auth0",
      }
    }
  },
  async mounted() {
    try {
      this.user.email = this.$route.query.email;
      console.log(this.user)
    } catch (e) {
      console.log(e)
    }
  },
  methods: {
    async submitUser() {
      try {
        const response = await sendRequest('/user', 'POST', this.user)
        if (response.ok) {
          window.location.href = `https://dev-sx5kw23vuznwkzbt.us.auth0.com/continue?state=${this.$route.query.state}`
        }
      } catch (e) {
        console.log(e)
      }
    }
  }
}
</script>

<style scoped>
form {
  width: 420px;
}
</style>