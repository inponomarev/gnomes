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
import { DefaultApi } from '@/apiclient/api';
import { Configuration } from '@/apiclient';

const api = new DefaultApi(
  new Configuration({
    basePath: window.location.origin,
  }),
);

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
      const response = await api.login({
        login: this.login,
        password: this.password,
      });
      /* Вот тут мы по идее должны:
      1. перейти на другую страницу
      2. на другой странице уже показывать секретный план
      3. Но самое главное: мы должны использовать авторизацию при обращении к api.secretplan!
       */
      if (response.status === 200) {
        const planReply = await api.secretplan();
        this.plan = planReply.data.points?.join('.') || '';
      }
    },
  },
});
</script>

<style>
.hidden {
  display: none !important;
}
</style>
