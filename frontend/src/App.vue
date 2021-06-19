<template>
  <div class="app">
    <login></login>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import login from './components/login.vue';

export default defineComponent({
  components: {
    login,
  },
});

</script>

<style>
</style>
