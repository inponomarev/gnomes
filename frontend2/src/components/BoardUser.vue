<template>
  <p>This should be shown only for a registered user!</p>
  <ul>
    <li v-for="item in plan" v-bind:key="item">
      {{ item }}
    </li>
  </ul>
</template>

<script lang="ts">
import { API } from '@/apiconf';
import { defineComponent } from 'vue';
import store from '@/store';

export default defineComponent({
  data() {
    return {
      plan: [] as Array<string>,
      token: store.state.token,
      conf: store.state.getHeaders(),
    };
  },

  async created() {
    const response = await API.secretplan(store.state.getHeaders());
    this.plan = response.data.points ?? [];
  },
});
</script>
