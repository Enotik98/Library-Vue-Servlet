<template>
  <div>Loading...</div>
</template>

<script>

import {sendRequest} from "@/script/request";
import {mapMutations} from "vuex";

export default {
  name: "CallBack",
  async mounted() {
    if (this.$auth0.isAuthenticated) {
      try {
        const token = await this.$auth0.getAccessTokenSilently();
        console.log(token);
        const response = await sendRequest("/login/auth0", "POST", {"token": token})
        if (response.ok) {
          const data = await response.json();
          localStorage.setItem("AccessToken", data['AccessToken'])
          localStorage.setItem("RefreshToken", data['RefreshToken'])
          this.login();
          this.$store.dispatch('fetchClient')

          await this.$router.push('/')
        } else {
          console.log("Fail")
        }

        console.log(this.$auth0.user)

      } catch (e) {
        console.error(e)
      }
    }
  },
  methods: {
    ...mapMutations(["login"])
  }
}
</script>

<style scoped>

</style>