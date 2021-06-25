import { createStore } from 'vuex';
import auth from './auth/auth-modules';

const apiStore = createStore({
  modules: {
    auth,
  },
  strict: false,
});

export default apiStore;
