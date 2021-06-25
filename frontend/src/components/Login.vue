<template>
  <div class="login">
    <form>
      <p>Логин<input name="aaaa" v-model="login"/></p>
      <p>Пароль<input name="bbbb" v-model="password"/></p>
    </form>
    <button @click="doLogin()">Log in</button>
    <button @click="doLogout()">Log out</button>

    <p>Secret plan: {{ plan }}</p>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import apiStore from '@/api/store';
import { AUTH_REQUEST, AUTH_LOGOUT } from '@/api/store/auth/auth-actions';
import { API } from '@/api';

export default defineComponent({

  data() {
    return {
      login: '' as string,
      password: '' as string,
      plan: '' as string,
    };
  },

  mounted() {
    if (apiStore.getters.isAuthenticated) {
      this.setPlan();
    }
  },

  methods: {

    async doLogin() {
      apiStore.dispatch(AUTH_REQUEST, { login: this.login, password: this.password }).then(
        () => {
          this.setPlan();
        },
        (error: Error) => {
          window.alert(error);
        },
      );
    },

    async doLogout() {
      apiStore.dispatch(AUTH_LOGOUT).then(
        () => {
          this.hidePlan();
        },
        (error: Error) => {
          window.alert(error);
        },
      );
    },

    async setPlan() {
      const planReply = await API.secretplan();
      this.plan = planReply.data.points?.join('.') || '';
    },

    async hidePlan() {
      this.plan = '';
    },
  },

});
</script>

<style>
.hidden {
  display: none !important;
}
</style>
