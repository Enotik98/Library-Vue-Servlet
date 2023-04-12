<template>
  <div class="container d-flex justify-content-center align-items-center mt-5">
    <div>
      <h2>Інформація про замовлення</h2>
      <div class="card rounded mb-4">
        <div class="row">
          <span class="col-6">Номер замовлення:</span>
          <span class="col-6">{{ order.id }}</span>
        </div>
        <div class="row">
          <span class="col-6">Замовник:</span>
          <span class="col-6">{{ userData.username }}</span>
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
          <span class="col-6">{{ formatType(order.type) }}</span>
        </div>
        <div class="row" v-if="showEdit">
          <div class="offset-6 col-6">
            <select v-model="editedOrder.type" class="form-select form-select-sm">
              <option value="ROOM">Зала</option>
              <option value="SUBSCRIPTION">Абонемент</option>
            </select>
          </div>
        </div>
        <div class="row">
          <span class="col-6">Статус замовлення:</span>
          <span class="col-6">{{ formatStatus(order.status) }}</span>
        </div>
        <div class="row" v-if="showEdit">
          <div class="offset-6 col-6">
            <select v-model="editedOrder.status" class="form-select form-select-sm ">
              <option value="WAITING">Очікує</option>
              <option value="ISSUED">Видана</option>
              <option value="RETURNED">Повернута</option>
            </select>
          </div>
        </div>
        <div class="row my-2 offset-7">
          <button v-if="!showEdit" @click="changeShow" class="btn btn-sm btn-dark col-12 ">Редагувати</button>
          <button v-else @click="updateOrder" class="btn btn-sm btn-outline-dark col-6 me-1">Зберігти</button>
          <button v-if="showEdit" @click="changeShow" class="btn btn-sm btn-dark col-5 ">Відміна</button>
        </div>
        <div class="row">
          <ConfirmationWindow :removeId="order.id" urlPath="/order"/>
        </div>
      </div>
      <h2 >Замовник:</h2>
      <div class="card rounded">
        <div class="row">
          <div class="col-6">Ім'я</div>
          <div class="col-6">{{userData.username}}</div>
        </div>
        <div class="row">
          <div class="col-6">Прізвище</div>
          <div class="col-6">{{userData.surname}}</div>
        </div>
        <div class="row">
          <div class="col-6">Email</div>
          <div class="col-6">{{userData.email}}</div>
        </div>
        <div class="row">
          <div class="col-6">Адреса</div>
          <div class="col-6">{{userData.address}}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {sendRequest} from "@/script/request";
import {formatDate, formatType, getDateForRequest, formatStatus} from "@/script/utils";
import ConfirmationWindow from "@/components/Confirmation.vue";


export default {
  name: "OrderInfo",
  components: {ConfirmationWindow},
  data() {
    return {
      order: {},
      editedOrder: {},
      showEdit: false,
      book: {},
      user: {},
      userData: JSON.parse(this.$route.query.userData)
      // orderUser: this.$route.query.username,
    }
  },
  mounted() {
    this.getOrder()
  },
  methods: {
    formatStatus,
    formatType,
    changeShow() {
      this.showEdit = !this.showEdit
    },
    formatDate,

    async getOrder() {
      // console.log(JSON.parse(this.userData))
      const response = await sendRequest('/order/' + this.$route.params.id, 'GET', null, localStorage.getItem('AccessToken'))
      if (response.ok) {
        const data = await response.json();
        this.order = data;
        this.editedOrder = Object.assign({}, data);
        await this.getBook();
      }
    },
    async getBook() {
      const response = await sendRequest('/book/' + this.order.book_id, 'GET', null, localStorage.getItem('AccessToken'));
      if (response.ok) {
        const data = await response.json()
        this.book = data;
      }
    },
    async updateOrder() {
      this.editedOrder.date_order = getDateForRequest(this.editedOrder.date_order);
      const response = await sendRequest('/order/' + this.order.id, 'PUT', this.editedOrder, localStorage.getItem('AccessToken'))
      if (response.ok) {
        this.$router.push('/orders')
        this.$Notiflix.Notify.success("Успішно!")
      } else {
        this.$Notiflix.Notify.success("Виникла помилка!")
      }
    }
  }
}
</script>

<style scoped>
.card {
  width: 450px;
  /*height: 200px;*/
  border: none;
  padding: 20px;
  border-radius: 0;
  box-shadow: 0px 1px 4px 4px rgba(0, 0, 0, 0.2);
}
</style>