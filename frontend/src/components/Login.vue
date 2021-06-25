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
import { apiStore } from '@/api/store';
import { AUTH_REQUEST } from '@/api/store/auth/auth-actions';
import { API } from '@/api';

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
      window.console.log('***1');

      apiStore.dispatch(AUTH_REQUEST, { login: this.login, password: this.password }).then(
        () => {
          this.setPlan();
        },
        (/* errorMessage: string */) => {
          // window.alert(errorMessage);
        },
      );
    },

    async setPlan() {
      const planReply = await API.secretplan();
      this.plan = planReply.data.points?.join('.') || '';
    },
  },

});
</script>

<style>
.hidden {
  display: none !important;
}
</style>
