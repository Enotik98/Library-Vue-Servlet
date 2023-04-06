<!--при update потрібно мабуть видалити count -->
<template>
  <div class="container d-flex justify-content-center align-items-center mt-5">
    <div class="card mb-5 rounded">
      <div class="row no-gutters">
        <div class="col-md-3">
          <img src="../../assets/porsche_sportkar_vid_szadi_128748_2560x1440.jpeg" class="card-img rounded"
               :alt="book.name">
        </div>
        <div class="col-md-9">
          <div class="card-body">
            <div class="row">
              <span class="text-muted col-3">Назва:</span>
              <span class="card-title col-9" v-if="!showUpdate">{{ book.name }}</span>
              <div v-else class="col-9">
                <input type="text" class="form-control form-control-sm col-6" v-model="editedBook.name">
              </div>
            </div>
            <div class="row">
              <span class="text-muted col-3">Автор:</span>
              <span class="card-text col-9" v-if="!showUpdate">{{ book.author }}</span>
              <div v-else class="col-9">
                <input type="text" class="form-control form-control-sm " v-model="editedBook.author">
              </div>
            </div>
            <div class="row">
              <span class="text-muted col-3">Жанр:</span>
              <p class="card-text col-8" v-if="!showUpdate">{{ book.genre }}</p>
              <div v-else class="col-9">
                <input type="text" class="form-control form-control-sm col-6" v-model="editedBook.genre">
              </div>
            </div>
            <div class="row">
              <span class="text-muted col-3">Рік:</span>
              <p class="card-text col-8" v-if="!showUpdate">{{ book.year }}</p>
              <div v-else class="col-9">
                <input type="number" class="form-control form-control-sm col-6" v-model="editedBook.year">
              </div>
            </div>
            <div class="row" v-if="showUpdate">
              <span class="text-muted col-3">Quantity:</span>
              <div class="col-9">
                <input type="text" class="form-control form-control-sm col-6" v-model="editedBook.quantity">
              </div>
            </div>
            <div class="row col-12 mt-3" v-if="!showUpdate">
              <span v-if="isHaveBook" class="text-muted" style="font-size: 12px">Виберіть тип замовлення</span>
              <span v-else class="text-muted" style="font-size: 12px">Книги не має в наявності</span>
            </div>
            <div class="" v-if="!showUpdate">
              <CreateOrder :is-have-book="isHaveBook "/>
            </div>
            <div class="row col-12 justify-content-end">
            </div>
          </div>
        </div>
      </div>
      <div class="row d-flex align-items-center">
      </div>
    </div>
  </div>
  <div class="d-flex justify-content-center" v-if="button">
    <ConfirmationWindow url-path="/book" :removeId="book.id"/>
    <button v-if="!showUpdate" @click="changeStatus" class="btn btn-outline-dark btn-sm mx-3">Редагувати</button>
    <button v-else @click="updateBook" class="btn btn-outline-dark btn-sm mx-3">Зберегти зміни</button>
  </div>
</template>

<script>
import {sendRequest} from '@/script/request';
import CreateOrder from "@/components/order/CreateOrder.vue";
import ConfirmationWindow from "@/components/Confirmation.vue";

export default {
  name: "BookInfo",
  components: {ConfirmationWindow, CreateOrder},
  data() {
    return {
      book: {},
      isHaveBook: false,
      editedBook: {},
      showUpdate: false,
      button: false
    }
  },
  mounted() {
    this.getBook()
  },
  methods: {
    changeStatus() {
      this.showUpdate = !this.showUpdate;
    },
    async getBook() {
      try {
        const response = await sendRequest('/book/' + this.$route.params.id, "GET", null, localStorage.getItem("AccessToken"))

        if (response.ok) {
          const data = await response.json();
          this.book = data;
          this.editedBook = Object.assign({}, data);
          this.button = data['button'];
          delete this.editedBook.count;
          delete this.editedBook.button;
          this.isHaveBook = this.book.quantity > this.book.count;
        } else {
          this.$Notiflix.Notify.success("Виникла помилка!")
        }
      } catch (error) {
        console.error(error);
      }
    },
    async updateBook() {
      const response = await sendRequest('/book', 'PUT', this.editedBook, localStorage.getItem('AccessToken'));
      if (response.ok) {
        await this.getBook();
        await this.changeStatus();
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
  width: 600px;
  /*height: 200px;*/
  border: none;
  padding: 20px;
  border-radius: 0;
  box-shadow: 0px 1px 4px 4px rgba(0, 0, 0, 0.2);
}

.card-img {
  height: 100%;
  object-fit: cover;
  border-radius: 0;
}

/*.card-body {*/
/*  padding: 1rem;*/
/*}*/

.card-title {
  font-size: 1.25rem;
  font-weight: bold;
  margin: 0;
  /*margin-bottom: 0.5rem;*/
}

.card-subtitle {
  font-size: 1rem;
  margin-bottom: 0.25rem;
  /*color: #888;*/
}

.card-text {
  font-size: 1rem;
  /*margin-bottom: 1rem;*/
}
</style>
