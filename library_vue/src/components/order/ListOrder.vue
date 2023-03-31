<template>
  <HeaderMenu/>
  <div class="container mt-5">
    <OrderCard v-if="!isAdmin"/>
    <div v-if="isAdmin" class="mt-5">
      <h2>Список Замовлень</h2>
      <table class="table">
        <thead>
        <tr>
          <th scope="col">#</th>
          <th scope="col">UserId</th>
          <th scope="col">Book</th>
          <th scope="col">Date_order</th>
          <th scope="col">Type</th>
          <th scope="col">Status</th>
          <th scope="col"></th>
          <th scope="col"></th>
        </tr>
        </thead>
        <tr v-for="order in orders" :key="order.id">
          <td>{{ order.id }}</td>
          <td>{{ getUsername(order.user_id, users) }}</td>
          <td>{{ getBookName(order.book_id, books) }}</td>
          <td>{{ formatDate(order.date_order) }}</td>
          <td>{{ order.type }}</td>
          <td>{{ order.status }}</td>
          <td>
            <router-link :to="{path:'/order/' + order.id, query: {username: getUsername(order.user_id, users)}}">
              Детально
            </router-link>
            <!--              <button @click="updateOrder(order.id)" class="btn btn-dark btn-sm">Update</button>-->
          </td>
          <td>
          </td>
        </tr>
      </table>
      <!--      <div>-->

    </div>
  </div>
</template>

<script>
import {sendRequest} from "@/script/request";
import {getBookName, formatDate, getUsername} from "@/script/utils"
import HeaderMenu from "@/components/Header.vue";
import OrderCard from "@/components/order/Order.vue";

export default {
  name: "ListOrder",
  components: { OrderCard, HeaderMenu},
  data() {
    return {
      orders: [],
      books: [],
      users: [],
      isAdmin: false,
      showConfirmModal: true
    }
  },
  mounted() {
    this.getOrders()
  },
  methods: {
    getUsername,
    getBookName,
    formatDate,
    updateOrder(orderId) {
      this.$router.push('/order/' + orderId)
    },
    async getOrders() {
      try {
        const response = await sendRequest('/order', 'GET', null, localStorage.getItem('AccessToken'));
        if (response.ok) {
          const data = await response.json();
          this.orders = data;
          await this.getUsers();
          await this.getBooks();
          console.log(data);
        } else {
          console.log('fail getOrders')
        }
        this.isAdmin = response.status !== 406;
      } catch (error) {
        console.error(error);
      }
    },
    async getBooks() {
      const response = await sendRequest('/book', 'GET', null, localStorage.getItem('AccessToken'));
      if (response.ok) {
        const data = await response.json();
        this.books = data['books'];
      } else {
        console.log('fail get books');
      }

    },
    async getUsers() {
      const response = await sendRequest('/user', 'GET', null, localStorage.getItem('AccessToken'));
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