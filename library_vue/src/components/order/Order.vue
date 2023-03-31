<template>
  <table class="table">
    <thead>
    <tr>
      <th scope="col">#</th>
      <th scope="col">Book</th>
      <th scope="col">Date order</th>
      <th scope="col">Ticket</th>
      <th scope="col">Status</th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="order in orders" :key="order.id">
      <td>{{ order.id }}</td>
      <td>{{ bookName(order.book_id, books) }}</td>
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
    bookName: getBookName,
    async getUserOrder() {
      try {
        const response = await sendRequest("/user/order", "GET", null, localStorage.getItem('AccessToken'));
        if (response.ok) {
          const data = await response.json();
          this.orders = data;
          console.log(data)
          await this.getBooks();
        } else {
          console.log('fail getUserOrder')
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
          console.log('fail getBooks')
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