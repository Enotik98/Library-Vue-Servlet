<template>
  <div class="container d-flex justify-content-center align-items-center mt-5 position-relative ">
    <div class="position-relative">
      <h2 class="d-flex justify-content-center">Інформація про користувача</h2>
      <form  class="user-card rounded" @submit.prevent="updateUser">
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
            <input type="email" maxlength="30" class="form-control form-control-sm" v-model="editedUser.email" required disabled>
          </div>
        </div>
        <div class="row">
          <label class="col-6">Адреса:</label>
          <div v-if="!showUpdateForm" class="col-6">{{ user.address }}</div>
          <div v-else class="col-6">
            <input type="text" maxlength="50" class="form-control form-control-sm" v-model="editedUser.address">
          </div>
        </div>
        <div class="row offset-7 mt-3 ">
          <button v-if="!showUpdateForm" @click="changeStatus" class="btn btn-dark btn-sm">Редагувати</button>
          <button v-else type="submit" class="btn btn-outline-dark btn-sm col-6 me-1">Зберегти</button>
          <button v-if="showUpdateForm" @click="changeStatus" class="btn btn-dark btn-sm col-5">Відміна</button>
        </div>
      </form>
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
import {mapMutations, mapState} from "vuex";

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
      errorMessage: "Тут повідомлення про помилку",
      user_info: ""
    };
  },
  mounted() {
    this.getUser();
  },
  methods: {
    ...mapState(['typeAuth']),
    changeStatus() {
      this.showUpdateForm = !this.showUpdateForm;
      this.editedUser = Object.assign({}, this.user);
    },
    async getUser() {
      try {
        const response = await sendRequest("/user/info", "GET", null);
        if (response.ok) {
          const data = await response.json();
          this.user = data;
          this.setClient(this.user["role"])

        } else {
          // this.$router.push('/login')
        }
      } catch (error) {
        console.log(error);
      }
    },
    ...mapMutations(['setClient']),
    async updateUser() {
      try {
        const response = await sendRequest("/user", 'PUT', this.editedUser);
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
  border: none;
  padding: 20px;
  border-radius: 0;
  box-shadow: 0px 1px 4px 4px rgba(0, 0, 0, 0.2);
}
</style>