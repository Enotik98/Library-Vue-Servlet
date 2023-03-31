<template>
  <div>
    <h1>Реєстрація</h1>
    <form @submit.prevent="submitForm">
      <div>
        <label for="username">Ім'я:</label>
        <input type="text" id="username" v-model="formData.username">
      </div>
      <div>
        <label for="password">Пароль:</label>
        <input type="password" id="password" v-model="formData.password">
      </div>
      <button type="submit">Зареєструватись</button>
    </form>
  </div>
</template>

<script>

export default {
  data() {
    return {
      formData: {
        name: '',
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
          alert("Welcome")
          // this.$router.push("user.html")
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
