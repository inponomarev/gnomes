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
import apiStore from '@/apistore';

export default defineComponent({
  data() {
    return {
      plan: [] as Array<string>,
      token: apiStore.state.token,
      conf: apiStore.state.getHeaders(),
    };
  },

  async created() {
    const response = await API.secretplan(apiStore.state.getHeaders());
    this.plan = response.data.points ?? [];
  },
});
</script>
