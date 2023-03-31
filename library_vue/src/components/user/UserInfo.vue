<template>
  <HeaderMenu/>
  <div class="container d-flex justify-content-center align-items-center mt-5">
    <div>
      <h2>Інформація про користувача</h2>
      <div class="user-card rounded">
        <div class="row">
          <label class="col-6">Ім'я:</label>
          <div v-if="!showUpdateForm" class="col-6">{{ user.username }}</div>
          <div v-else class="col-6">
            <input type="text" class="form-control form-control-sm" v-model="editedUser.username">
          </div>
        </div>
        <div class="row">
          <label class="col-6">Email:</label>
          <div v-if="!showUpdateForm" class="col-6">{{ user.email }}</div>
          <div v-else class="col-6">
            <input type="email" class="form-control form-control-sm" v-model="editedUser.email">
          </div>
        </div>
        <div class="row">
          <label class="col-6">Пароль:</label>
          <div v-if="!showUpdateForm" class="col-6">**********</div>
          <div v-else class="col-6">
            <input type="password" class="form-control form-control-sm" v-model="editedUser.password">
          </div>
        </div>
        <div class="row offset-7 mt-3 me-1">
          <button v-if="!showUpdateForm" @click="changeStatus" class="btn btn-dark ">Редагувати</button>
          <button v-else @click="updateUser" class="btn btn-dark">Зберегти зміни</button>
        </div>
      </div>
      <div class="mt-5">
        <OrderCard />
      </div>
    </div>
  </div>
</template>

<script>
import {sendRequest} from '../../script/request';
import HeaderMenu from "@/components/Header.vue";
import {getBookName, formatDate} from "../../script/utils";
import order from "../order/Order.vue";
import OrderCard from "@/components/order/Order.vue";

export default {
  name: "UserInfo",
  computed: {
    order() {
      return order
    }
  },
  components: {OrderCard, HeaderMenu},
  data() {
    return {
      showUpdateForm: false,
      user: {},
      editedUser: {},
      showModal: false,
      errorMessage: "Тут повідомлення про помилку"
    };
  },
  mounted() {
    this.getUser();
  },
  methods: {
    formatDate,
    bookName: getBookName,
    changeStatus() {
      this.showUpdateForm = !this.showUpdateForm;
    },
    async getUser() {
      try {
        const accessToken = localStorage.getItem('AccessToken');
        const response = await sendRequest("/user/info", "GET", null, accessToken);
        if (response.ok) {
          const data = await response.json();
          this.user = data;
          this.editedUser = Object.assign({}, data);
          console.log("Its OK")
        } else {
          this.$router.push('/login')
        }
      } catch (error) {
        console.log(error);
      }
    },
    async updateUser(event) {
      event.preventDefault();
      try {
        const accessToken = localStorage.getItem('AccessToken');
        const response = await sendRequest("/user", 'PUT', this.editedUser, accessToken);

        if (response.ok) {
          await this.getUser();
          await this.changeStatus();
        } else {
          console.error('Сталася помилка');
        }
      } catch (error) {
        console.log(error)
      }
    }
  }
}
</script>
<style scoped>
.container {
}

.user-card {
  width: 450px;
  /*height: 200px;*/
  border: none;
  padding: 20px;
  border-radius: 0;
  box-shadow: 0px 1px 4px 4px rgba(0, 0, 0, 0.2);
}
</style>