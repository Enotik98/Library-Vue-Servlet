<template>
<div class="container d-flex justify-content-center align-items-center h-100 w-75 mt-5">
  <form class="border rounded p-4" @submit.prevent="createBook">
    <div class="d-flex justify-content-center">
      <img src="../../assets/add-book.png" class="card-img">
    </div>
    <h2>Створення Книги</h2>
    <div class="form-group">
      <label>Назва:</label>
      <input type="text" maxlength="80" class="form-control form-control-sm" v-model="formBook.name" required>
    </div>
    <div class="form-group">
      <label>Автор:</label>
      <input type="text" maxlength="30" class="form-control form-control-sm" v-model="formBook.author" required>
    </div>
    <div class="form-group">
      <label>Жанр:</label>
      <input type="text" maxlength="20" class="form-control form-control-sm" v-model="formBook.genre" required>
    </div>
    <div class="form-group">
      <label>Рік:</label>
      <input type="number" max="9999" min="1" class="form-control form-control-sm" v-model="formBook.year" required>
    </div>
    <div class="form-group">
      <label>Кількість:</label>
      <input type="number" max="999" min="1" class="form-control form-control-sm" v-model="formBook.quantity" required>
    </div>
    <button type="submit" class="btn btn-dark">Створити</button>
  </form>
</div>
</template>

<script>
import {sendRequest} from "@/script/request";

export default {
  name: "CreateBook",
  data(){
    return{
      formBook:{
        name: '',
        author: '',
        genre: '',
        quantity: ''
      }
    }
  },
  methods:{
    async createBook(){
      const response = await sendRequest('/book', 'POST', this.formBook);
      if (response.ok){
        this.$Notiflix.Notify.success("Успішно!")
        this.$router.push('/')
      }else {
        this.$Notiflix.Notify.success("Виникла помилка!")
      }
    }
  }
}
</script>

<style scoped>
form{
  width: 450px;
}
.card-img {
  width: 30%;
  object-fit: cover;
  border-radius: 0;
}
.form-group{
  margin: 10px 0;
}
</style>