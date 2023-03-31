<template>
  <div>
    <h1>Список користувачів</h1>
    <ul>
      <li v-for="user in users" :key="user.id">
        {{user.id}}: {{ user.username }} ({{ user.email }})
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  data() {
    return {
      users: []
    }
  },
  mounted() {
    this.getUsers();
  },
  methods: {
    async getUsers() {
      try {
        const response = await fetch('http://localhost:8080/testServlet_war/user');

        if (response.ok) {
          const data = await response.json();
          console.log(data)
          this.users = data;
        } else {
          console.error('Сталася помилка');
        }
      } catch (error) {
        console.error(error);
      }
    }
  }
}
</script>
