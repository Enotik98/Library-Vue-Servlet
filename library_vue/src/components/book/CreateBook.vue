<template>
  <HeaderMenu />
<div class="container d-flex justify-content-center align-items-center h-100 w-75 mt-5">
  <form class="border rounded p-4" @submit.prevent="createBook">
    <h2>Створення Книги</h2>
    <div class="form-group">
      <label for="name">Назва:</label>
      <input type="text" id="name" class="form-control form-control-sm" v-model="formBook.name">
    </div>
    <div class="form-group">
      <label for="author">Автор:</label>
      <input type="text" id="author" class="form-control form-control-sm" v-model="formBook.author">
    </div>
    <div class="form-group">
      <label for="genre">Жанр:</label>
      <input type="text" id="genre" class="form-control form-control-sm" v-model="formBook.genre">
    </div>
    <div class="form-group">
      <label for="quantity">Кількість:</label>
      <input type="number" id="quantity" class="form-control form-control-sm" v-model="formBook.quantity">
    </div>
    <button type="submit" class="btn btn-dark">Створити</button>
  </form>
</div>
</template>

<script>
import HeaderMenu from "@/components/Header.vue";
import {sendRequest} from "@/script/request";

export default {
  name: "CreateBook",
  components: {HeaderMenu},
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
      const response = await sendRequest('/book', 'POST', this.formBook, localStorage.getItem('AccessToken'));
      if (response.ok){
        console.log('create book')
        this.$router.push('/books')
      }
    }
  }
}
</script>

<style scoped>
form{
  width: 450px;
}
</style>