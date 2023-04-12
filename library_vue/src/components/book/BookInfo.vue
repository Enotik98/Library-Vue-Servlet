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
                <input type="text" maxlength="20" class="form-control form-control-sm col-6" v-model="editedBook.name"
                       required>
              </div>
            </div>
            <div class="row">
              <span class="text-muted col-3">Автор:</span>
              <span class="card-text col-9" v-if="!showUpdate">{{ book.author }}</span>
              <div v-else class="col-9">
                <input type="text" maxlength="30" class="form-control form-control-sm " v-model="editedBook.author"
                       required>
              </div>
            </div>
            <div class="row">
              <span class="text-muted col-3">Жанр:</span>
              <p class="card-text col-8" v-if="!showUpdate">{{ book.genre }}</p>
              <div v-else class="col-9">
                <input type="text" maxlength="20" class="form-control form-control-sm col-6" v-model="editedBook.genre"
                       required>
              </div>
            </div>
            <div class="row">
              <span class="text-muted col-3">Рік:</span>
              <p class="card-text col-8" v-if="!showUpdate">{{ book.year }}</p>
              <div v-else class="col-9">
                <input type="number" max="9999" min="1" class="form-control form-control-sm col-6"
                       v-model="editedBook.year" required>
              </div>
            </div>
            <div class="row" v-if="showUpdate">
              <span class="text-muted col-3">Quantity:</span>
              <div class="col-9">
                <input type="text" max="999" min="1" class="form-control form-control-sm col-6"
                       v-model="editedBook.quantity" required>
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
  <div class="d-flex justify-content-center" v-if="!isClient">
    <ConfirmationWindow url-path="/book" :removeId="book.id"/>
    <button v-if="!showUpdate" @click="changeStatus" class="btn btn-outline-dark btn-sm mx-3">Редагувати</button>
    <button v-else @click="updateBook" class="btn btn-outline-success btn-sm mx-3">Зберегти</button>
    <button v-if="showUpdate" @click="changeStatus" class="btn btn-outline-dark btn-sm " >Відміна</button>
  </div>
</template>

<script>
import {sendRequest} from '@/script/request';
import CreateOrder from "@/components/order/CreateOrder.vue";
import ConfirmationWindow from "@/components/Confirmation.vue";
import {mapState} from "vuex";

export default {
  name: "BookInfo",
  components: {ConfirmationWindow, CreateOrder},
  data() {
    return {
      book: {},
      isHaveBook: false,
      editedBook: {},
      showUpdate: false,
    }
  },
  computed:{
    ...mapState(['isClient'])
  },
  mounted() {
    this.getBook()
    console.log(this.isClient)
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
          this.$Notiflix.Notify.failure("Виникла помилка!")
        }
      } catch (error) {
        console.error(error);
        this.$Notiflix.Notify.failure("Виникла помилка!")
      }
    },
    async updateBook() {
      const response = await sendRequest('/book', 'PUT', this.editedBook, localStorage.getItem('AccessToken'));
      if (response.ok) {
        await this.getBook();
        await this.changeStatus();
        this.$Notiflix.Notify.success("Успішно!")
      } else {
        this.$Notiflix.Notify.failure("Виникла помилка!")
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
