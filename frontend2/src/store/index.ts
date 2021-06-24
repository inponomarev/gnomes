import { createStore } from 'vuex';
import createPersistedState from 'vuex-persistedstate';
import { State, state } from './state';
import { mutations } from './mutations';

export default createStore<State>({
  state,
  mutations,
  plugins: [createPersistedState()],
});
