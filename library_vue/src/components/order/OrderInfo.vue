<template>
  <HeaderMenu/>
  <div class="container d-flex justify-content-center align-items-center mt-5">
    <div>
      <h2>Інформація про замовлення</h2>
      <div class="order-card rounded">
        <div class="row">
          <span class="col-6">Номер замовлення:</span>
          <span class="col-6">{{ order.id }}</span>
        </div>
        <div class="row">
          <span class="col-6">Замовник:</span>
          <span class="col-6">{{ username }}</span>
        </div>
        <div class="row">
          <span class="col-6">Книга:</span>
          <span class="col-6">{{ book.name }}</span>
        </div>
        <div class="row">
          <span class="col-6">Дата замовлення:</span>
          <span class="col-6">{{ formatDate(order.date_order) }}</span>
        </div>
        <div class="row">
          <span class="col-6">Тип квитка:</span>
          <span class="col-6">{{ order.type }}</span>
        </div>
        <div class="row">
          <span class="col-6">Статус замовлення:</span>
          <span class="col-6">{{ order.status }}</span>
        </div>
        <div class="row my-2" v-if="showDropdown">
          <div class="offset-6 col-6">
            <select v-model="order.status" class="form-select form-select-sm ">
              <option value="WAITING">WAITING</option>
              <option value="ISSUED">ISSUED</option>
              <option value="RETURNED">RETURNED</option>
            </select>
          </div>
        </div>
        <div class="row me-1 my-1">
          <button v-if="!showDropdown" @click="changeShow" class="btn btn-sm btn-dark col-4 offset-8">Редагувати
          </button>
          <button v-else @click="updateOrder" class="btn btn-sm btn-dark col-4 offset-8">Зберігти</button>
        </div>
        <div class="row">
          <ConfirmationWindow :orderId="order.id" urlPath="/order"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {sendRequest} from "@/script/request";
import {formatDate, getDateForRequest} from "../../script/utils";
import HeaderMenu from "@/components/Header.vue";
import ConfirmationWindow from "@/components/Confirmation.vue";

export default {
  name: "OrderInfo",
  components: {ConfirmationWindow, HeaderMenu},
  data() {
    return {
      order: {},
      showDropdown: false,
      book: {},
      user: {},
      username: this.$route.query.username
    }
  },
  props: {},
  mounted() {
    this.getOrder()
  },
  methods: {
    changeShow() {
      this.showDropdown = !this.showDropdown
    },
    formatDate,
    async getOrder() {
      const response = await sendRequest('/order/' + this.$route.params.id, 'GET', null, localStorage.getItem('AccessToken'))
      if (response.ok) {
        const data = await response.json();
        this.order = data;
        await this.getBook();
      }
    },
    async getBook() {
      const response = await sendRequest('/book/' + this.order.book_id, 'GET', null, localStorage.getItem('AccessToken'));
      if (response.ok) {
        const data = await response.json()
        this.book = data;
      } else {
        console.log('fail get book')
      }
    },
    async updateOrder() {
      this.order.date_order = getDateForRequest(this.order.date_order);
      console.log(this.order)
      const response = await sendRequest('/order/' + this.order.id, 'PUT', this.order, localStorage.getItem('AccessToken'))
      if (response.ok) {
        this.$router.push('/orders')
        console.log('Update Success')
      } else {
        console.log('Fail update')
      }
    }
  }
}
</script>

<style scoped>
.order-card {
  width: 450px;
  /*height: 200px;*/
  border: none;
  padding: 20px;
  border-radius: 0;
  box-shadow: 0px 1px 4px 4px rgba(0, 0, 0, 0.2);
}
</style>