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

export default defineComponent({
  data() {
    return {
      login: '' as string,
      password: '' as string,
    };
  },
  methods: {
    submit() {
      window.alert(`${this.login}:${this.password}`);
    },
  },
});
</script>
