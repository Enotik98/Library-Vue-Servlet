<template>
  <div class="container mt-5">
    <OrderCard v-if="!isAdmin"/>
    <div v-if="isAdmin" class="mt-5 p-4 table-responsive">
      <h2>Список Замовлень</h2>
      <table class="table">
        <thead>
        <tr>
          <th >#</th>
          <th >Замовник</th>
          <th >Книга</th>
          <th >Дата замовлення</th>
          <th >Квиток</th>
          <th >Статус</th>
          <th ></th>
          <th ></th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="order in orders" :key="order.id">
          <td>{{ order.id }}</td>
          <td>{{ getUsername(order.user_id, users).username }}</td>
          <td>{{ getBookName(order.book_id, books) }}</td>
          <td>{{ formatDate(order.date_order) }}</td>
          <td>{{ formatType(order.type) }}</td>
          <td>{{ formatStatus(order.status) }}</td>
          <td>
            <router-link class="btn btn-dark btn-sm" :to="{path:'/order/' + order.id, query: {userData: JSON.stringify(getUsername(order.user_id, users))}}">
              Детально
            </router-link>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import {sendRequest} from "@/script/request";
import {getBookName, formatDate, getUsername, formatType, formatStatus} from "@/script/utils"
import OrderCard from "@/components/order/Order.vue";

export default {
  name: "ListOrder",
  components: {OrderCard},
  data() {
    return {
      orders: [],
      books: [],
      users: [],
      isAdmin: false,
    }
  },
  mounted() {
    this.getOrders()
  },
  methods: {
    formatStatus,
    formatType,
    getUsername,
    getBookName,
    formatDate,
    async getOrders() {
      try {
        const response = await sendRequest('/order', 'GET', null);
        if (response.ok) {
          const data = await response.json();
          this.orders = data;
          await this.getUsers();
          await this.getBooks();
        }
        this.isAdmin = response.status !== 403;
      } catch (error) {
        console.error(error);
      }
    },
    async getBooks() {
      const response = await sendRequest('/book', 'GET', null);
      if (response.ok) {
        const data = await response.json();
        this.books = data['books'];
      }

    },
    async getUsers() {
      const response = await sendRequest('/user', 'GET', null);
      if (response.ok) {
        const data = await response.json();
        this.users = data;
      }
    }
  }
}
</script>

<style scoped>

</style>