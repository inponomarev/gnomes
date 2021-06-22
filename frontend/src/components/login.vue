<template>
  <div class="login">
    <form>
      <p>Логин<input name="aaaa" v-model="login"/></p>
      <p>Пароль<input name="bbbb" v-model="password"/></p>
    </form>
    <button @click="submit()">Click me</button>

    <p>Secret plan: {{ plan }}</p>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import { API } from '@/api';
import apiStore from '@/api/store';

export default defineComponent({

  data() {
    return {
      login: '' as string,
      password: '' as string,
      plan: '' as string,
    };
  },

  methods: {
    async submit() {
      // TODO:
      apiStore.dispatch(AUTH_REQUEST, { this.login, this.password }).then(
        () => {
          const planReply = await API.secretplan();
          this.plan = planReply.data.points?.join('.') || '';          
        },
        (errorMessage) => {
          window.alert(errorMessage)
        })
/*
      const response = await API.login({
        login: this.login,
        password: this.password,
      });
      / * Вот тут мы по идее должны:
      1. перейти на другую страницу
      2. на другой странице уже показывать секретный план
      3. Но самое главное: мы должны использовать авторизацию при обращении к API.secretplan!
       * /
      if (response.status === 200) {
        const planReply = await API.secretplan();
        this.plan = planReply.data.points?.join('.') || '';
      }
*/
    },
  },

});
</script>

<style>
.hidden {
  display: none !important;
}
</style>
