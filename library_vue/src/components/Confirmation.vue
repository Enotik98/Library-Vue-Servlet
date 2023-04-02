<template>
  <div>
    <!-- Button trigger modal -->
    <button type="button" class="btn btn-outline-danger btn-sm" data-bs-toggle="modal" data-bs-target="#exampleModal">
       Видалити
    </button>

    <!-- Modal -->
    <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="exampleModalLabel">Підтвердження видалення</h5>
            <button type="button" class="close" data-bs-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            Ви впевнені, що хочете видалити замовлення?
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-dark" data-bs-dismiss="modal">Відміна</button>
            <button type="button" class="btn btn-outline-danger" data-bs-dismiss="modal" @click="deleteOrder">Видалити</button>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import {sendRequest} from "@/script/request";

export default {
  name: "ConfirmationWindow",
  data(){
    return {
      order : {
        id: ''
      }
    }
  },
  props: {
    removeId: this,
    urlPath: {
      type: String,
      required: true
    }
  },
  methods:{
    deleteOrder(){
      this.deleteObject();
    },
    async deleteObject(){
      this.order.id = this.removeId
      console.log(this.order)
      const response = await sendRequest(this.urlPath, 'DELETE', this.order, localStorage.getItem('AccessToken'));
      if (response.ok){
        if (this.urlPath === '/user'){
          localStorage.clear();
        }
        console.log("Delete Ok" + response)
        this.$router.push('/books')
      }else {
        console.log('fail delete')
      }
    }
  }

}
</script>

<style scoped>

</style>