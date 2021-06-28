import apiStore from '@/api/store';
import { createApp } from 'vue';
import App from './App.vue';

const app = createApp(App);
app.use(apiStore);

app.mount('#app');
