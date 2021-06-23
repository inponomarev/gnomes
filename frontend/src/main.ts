import { createApp } from 'vue';
import App from './App.vue';

/*
// TODO: API.setSessionId()
const sessionId = localStorage.getItem('session-id')
if (sessionId) {
  apiAxios.defaults.headers.common['Authorization'] = 'Bearer ' + sessionId
}
*/

createApp(App).mount('#app');
