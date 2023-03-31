<template>
  <div class="container my-5">
    <div class="row justify-content-center">
      <div class="col-md-6">
        <div class="card">
          <div class="card-header">
            <h3 class="text-center">Реєстрація</h3>
          </div>
          <div class="card-body">
            <form @submit.prevent="submitForm">
              <div class="form-group">
                <label for="name">Ім'я</label>
                <input type="text" class="form-control" id="name" v-model="form.name" required>
              </div>
              <div class="form-group">
                <label for="email">Email</label>
                <input type="email" class="form-control" id="email" v-model="form.email" required>
              </div>
              <div class="form-group">
                <label for="password">Пароль</label>
                <input type="password" class="form-control" id="password" v-model="form.password" required>
              </div>
              <button type="submit" class="btn btn-primary btn-block">Зареєструватися</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      form: {
        name: '',
        email: '',
        password: ''
      }
    }
  },
  methods: {
    submitForm() {
      // Тут ви можете реалізувати логіку відправки даних на сервер
      console.log(this.form);
      alert('Дякуємо за реєстрацію!');
      // Очистити форму після успішної реєстрації
      this.form.name = '';
      this.form.email = '';
      this.form.password = '';
    }
  }
}
</script>

<style scoped>
.card {
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
}

.card-header {
  background-color: #007bff;
  color: #fff;
}

.btn-primary {
  background-color: #007bff;
  border-color: #007bff;
}
</style>