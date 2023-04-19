<template>
  <HeaderMenu />
  <router-view/>
</template>

<script>


import HeaderMenu from "@/components/Header.vue";

export default {
  name: 'App',
  components: {
    HeaderMenu
  },
  mounted() {
    this.$store.dispatch('fetchClient')
  }

}
</script>

<style>

</style>
