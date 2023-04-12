<template>
  <div class="container d-flex justify-content-center align-items-center mt-5 position-relative min-vw-100">
    <div class="position-relative">
      <h2>Інформація про користувача</h2>
      <div class="user-card rounded">
        <div class="row">
          <label class="col-6">Ім'я:</label>
          <div v-if="!showUpdateForm" class="col-6">{{ user.username }}</div>
          <div v-else class="col-6">
            <input type="text" maxlength="25" class="form-control form-control-sm" v-model="editedUser.username"
                   required>
          </div>
        </div>
        <div class="row">
          <label class="col-6">Прізвище:</label>
          <div v-if="!showUpdateForm" class="col-6">{{ user.surname }}</div>
          <div v-else class="col-6">
            <input type="text" maxlength="30" class="form-control form-control-sm" v-model="editedUser.surname"
                   required>
          </div>
        </div>
        <div class="row">
          <label class="col-6">Email:</label>
          <div v-if="!showUpdateForm" class="col-6">{{ user.email }}</div>
          <div v-else class="col-6">
            <input type="email" maxlength="30" class="form-control form-control-sm" v-model="editedUser.email" required>
          </div>
        </div>
        <div class="row">
          <label class="col-6">Адреса:</label>
          <div v-if="!showUpdateForm" class="col-6">{{ user.address }}</div>
          <div v-else class="col-6">
            <input type="text" maxlength="50" class="form-control form-control-sm" v-model="editedUser.address">
          </div>
        </div>
        <div class="row">
          <label class="col-6">Пароль:</label>
          <div v-if="!showUpdateForm" class="col-6">*******</div>
          <div v-else class="col-6">
            <input type="password" maxlength="20" minlength="6" class="form-control form-control-sm"
                   v-model="editedUser.password" required>
          </div>
        </div>
        <div class="row offset-7 mt-3 ">
          <button v-if="!showUpdateForm" @click="changeStatus" class="btn btn-dark btn-sm">Редагувати</button>
          <button v-else @click="updateUser" class="btn btn-outline-dark btn-sm col-6 me-1">Зберегти</button>
          <button v-if="showUpdateForm" @click="changeStatus" class="btn btn-dark btn-sm col-5">Відміна</button>
        </div>
      </div>
      <div class="my-5 position-relative">
        <OrderCard/>
      </div>
    </div>
  </div>
    <div class="d-flex justify-content-center">
      <ConfirmationWindow url-path="/user" :remove-id="user.id"/>
    </div>
</template>

<script>
import {sendRequest} from '@/script/request';
import order from "../order/Order.vue";
import OrderCard from "@/components/order/Order.vue";
import ConfirmationWindow from "@/components/Confirmation.vue";
import {mapMutations} from "vuex";

export default {
  name: "UserInfo",
  computed: {
    order() {
      return order
    }
  },
  components: {ConfirmationWindow, OrderCard},
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
          this.setClient(this.user["role"])
        } else {
          this.$router.push('/login')
        }
      } catch (error) {
        console.log(error);
      }
    },
    ...mapMutations(['setClient']),
    async updateUser() {
      // event.preventDefault();
      try {
        const accessToken = localStorage.getItem('AccessToken');
        const response = await sendRequest("/user", 'PUT', this.editedUser, accessToken);

        if (response.ok) {
          await this.getUser();
          this.changeStatus();
          this.$Notiflix.Notify.success("Успішно!")

        } else {
          this.$Notiflix.Notify.failure("Виникла помилка")
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