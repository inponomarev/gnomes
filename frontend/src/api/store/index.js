import { createStore } from 'vuex'
import auth from './auth/auth-modules'

export const apiStore = createStore({
  modules: {
    auth
  },
  strict: false
})
