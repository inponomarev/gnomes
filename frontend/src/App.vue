<template>
  <div class="app">
    <login></login>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import Login from './components/Login.vue';

export default defineComponent({
  components: {
    Login,
  },
});

</script>

<style>
</style>
