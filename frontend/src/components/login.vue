<template>
  <div class="login">
    <form>
      <p>Логин<input name="aaaa" v-model="login"/></p>
      <p>Пароль<input name="bbbb" v-model="password"/></p>
    </form>
    <button @click="submit()">Click me</button>
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
    };
  },
  methods: {
    submit() {
      api.login({
        login: this.login,
        password: this.password,
      }).then((response) => {
        if (response.status === 200) {
          window.alert('success');
        }
      });
    },
  },
});
</script>
