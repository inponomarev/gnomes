import { createApp } from 'vue';
import { apiStore } from '@/api/store';
import App from './App.vue';

/*
// TODO: API.setSessionId()
const sessionId = localStorage.getItem('session-id')
if (sessionId) {
  apiAxios.defaults.headers.common['Authorization'] = 'Bearer ' + sessionId
}
*/

const app = createApp(App);
app.use(apiStore);

app.mount('#app');
