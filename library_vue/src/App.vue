<template>
  <HeaderMenu />
  <!--  <img alt="Vue logo" src="./assets/logo.png">-->
  <router-view/>
  <!--  <HelloWorld msg="Welcome to Your Vue.js App"/>-->
</template>

<script>


import HeaderMenu from "@/components/Header.vue";
// import {ref, watch} from 'vue'

export default {
  name: 'App',
  components: {
    HeaderMenu
  }

}
</script>

<style>

</style>
