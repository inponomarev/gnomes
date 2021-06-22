import Vue from 'vue'
import Vuex from 'vuex'
import user from './user/user-modules'
import auth from './auth/auth-modules'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    user,
    auth
  },
  strict: false
})
