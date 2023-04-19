<template>
  <div class="container justify-content-center mt-5 p-4 table-responsive">
    <h2>Список книжок</h2>
    <div class="mb-3">
      <input type="text" class="form-control" placeholder="Пошук" v-model="searchTerm" @input="searchBooks" maxlength="150">
    </div>
    <table class="table table-striped">
      <thead>
      <tr>
        <!--        <th scope="col">#</th>-->
        <th scope="col">Назва</th>
        <th scope="col">Автор</th>
        <th scope="col">Жанр</th>
        <th scope="col">Рік</th>
        <th scope="col"></th>
      </tr>
      </thead>
      <tbody>
      <tr v-if="books.length === 0">
        <td colspan="4">Знайдено жодної книги</td>
      </tr>
      <tr v-for="book in books" :key="book.id">
        <!--        <td>{{ book.id }}</td>-->
        <td>{{ book.name }}</td>
        <td>{{ book.author }}</td>
        <td>{{ book.genre }}</td>
        <td>{{ book.year }}</td>
        <td>
          <router-link class="btn btn-dark btn-sm" :to="{name: 'BookInfo', params: {id: book.id}} ">Замовити
          </router-link>
        </td>
      </tr>
      </tbody>
    </table>
    <button v-if="!isClient" class="btn btn-dark" @click="this.$router.push('/add-book')">Add Book</button>
  </div>
</template>

<script>
import {sendRequest} from "@/script/request";
import {mapMutations, mapState} from "vuex";

export default {
  name: "ListBook",
  computed: {
    ...mapState(['isClient'])
  },
  data() {
    return {
      books: [],
      searchTerm: '',
    };
  },
  mounted() {
    this.getBooks()
  },
  methods: {
    searchBooks() {
      if (this.searchTerm === '') {
        this.getBooks();
      } else {
        const filterBooks = this.books.filter((book) => {
          return (
              book.name.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
              book.author.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
              book.genre.toLowerCase().includes(this.searchTerm.toLowerCase())
          );
        });
        if (filterBooks.length === 0) {
          this.books = []
        } else {
          this.books = filterBooks;
        }
      }
    },
    async getBooks() {
      try {
        const response = await sendRequest('/book', "GET", null);
        if (response.ok) {
          const data = await response.json();
          this.books = data['books'];
          console.log(this.isClient)
        }
      } catch (error) {
        console.error(error)
      }
    },
    ...mapMutations(['setClient'])
  }
}
</script>
