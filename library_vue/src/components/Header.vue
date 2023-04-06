<template>
  <div>
    <div class="container">
      <nav class="navbar navbar-expand-lg navbar-dark bg-dark border rounded p-3">
        <div class="container-fluid">
          <a class="navbar-brand" href="#">Library</a>
          <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                  aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
          </button>
          <div class="collapse navbar-collapse justify-content-end" id="navbarNav">
            <ul class="navbar-nav ">
              <li class="nav-item">
                <router-link class="nav-link" to="/books">Books</router-link>
              </li>
              <li class="nav-item">
                <router-link class="nav-link" to="/profile">Profile</router-link>
              </li>
              <li class="nav-item">
                <router-link class="nav-link" to="/orders">Orders</router-link>
              </li>
              <li class="nav-item">
                <router-link class="nav-link" to="/login" v-if="isLoggedIn" @click.prevent="logout">Logout</router-link>
                <router-link class="nav-link" to="/login" v-else>Login</router-link>
              </li>
            </ul>
          </div>
        </div>
      </nav>
    </div>
  </div>
</template>

<script>


import {mapMutations, mapState} from "vuex";

export default {
  name: "HeaderMenu",
  computed:{
    ...mapState(['isLoggedIn'])
  },
  methods:{
    ...mapMutations(['login', 'logout'])
  }
}
</script>
<style scoped>

</style>