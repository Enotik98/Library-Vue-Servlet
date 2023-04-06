<template>
  <h2>Список Замовлень</h2>
  <table class="table">
    <thead>
    <tr>
      <th scope="col">#</th>
      <th scope="col">Назва</th>
      <th scope="col">Дата замовлення</th>
      <th scope="col">Квиток</th>
      <th scope="col">Статус</th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="order in orders" :key="order.id">
      <td>{{ order.id }}</td>
      <td>{{ getBookName(order.book_id, books) }}</td>
      <td>{{ formatDate(order.date_order) }}</td>
      <td>{{ formatType(order.type) }}</td>
      <td>{{ formatStatus(order.status) }}</td>
    </tr>
    </tbody>
  </table>
</template>

<script>
import {sendRequest} from "@/script/request";
import {getBookName, formatDate, formatStatus, formatType} from "@/script/utils";

export default {
  name: "OrderCard",
  data() {
    return {
      orders: [],
      books: []
    }
  },
  mounted() {
    this.getUserOrder();
  },
  methods: {
    formatType,
    formatStatus,
    formatDate,
    getBookName,
    async getUserOrder() {
      try {
        const response = await sendRequest("/user/order", "GET", null, localStorage.getItem('AccessToken'));
        if (response.ok) {
          const data = await response.json();
          this.orders = data;
          await this.getBooks();
        } else {
          if (response.status === 401) {
            this.$router.push('/login')
          }
        }
      } catch (error) {
        console.error(error)
      }
    },
    async getBooks() {
      try {
        const response = await sendRequest("/book", "GET", null, localStorage.getItem("AccessToken"));
        if (response.ok) {
          const data = await response.json();
          this.books = data['books'];
        } else {
          this.$Notiflix.Notify.success("Виникла помилка!")
        }
      } catch (error) {
        console.log(error)
      }
    }
  }
}
</script>

<style scoped>

</style>