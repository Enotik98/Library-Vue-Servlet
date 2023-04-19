<template>
  <div v-if="isHaveBook">
    <form @submit.prevent="addOrder">
      <div class="row ">
        <div class="col-6">
          <select name="type" id="typeTicket" class="form-select form-select-sm " v-model="order.type" required>
            <option value="ROOM" selected>Зала</option>
            <option value="SUBSCRIPTION">Абонемент</option>
          </select>
        </div>
        <button type="submit" class="btn btn-dark btn-sm col-4">Замовити</button>
      </div>
    </form>
  </div>
</template>

<script>
import {sendRequest} from "@/script/request";

export default {
  name: "CreateOrder",
  props: {
    isHaveBook: this
  },
  data() {
    return {
      order: {
        book_id: this.$route.params.id,
        type: ''
      }
    }
  },
  methods: {
    async addOrder(event) {
      event.preventDefault();
      console.log(this.order);
      const response = await sendRequest('/order', 'POST', this.order);
      if (response.ok) {
        this.$Notiflix.Notify.success("Успішно!")
        this.$router.push('/orders');
      } else {
        if (response.status === 401) {
          this.$router.push('/login')
          this.$Notiflix.Notify.info("Виконайте вхід!")
        } else {
          this.$Notiflix.Notify.failure("Виникла помилка!")
        }
      }
    }
  }
}
</script>

<style scoped>

</style>