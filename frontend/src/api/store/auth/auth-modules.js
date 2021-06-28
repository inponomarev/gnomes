import { API } from '@/api';
import {
  AUTH_REQUEST, AUTH_ERROR, AUTH_SUCCESS, AUTH_LOGOUT,
} from './auth-actions';

const state = {
  sessionId: localStorage.getItem('session-id') || '',
  status: '',
  isLoadedOnce: false,
};

if (state.sessionId) {
  API.setSessionId(state.sessionId);
}

const getters = {
  isAuthenticated: (state) => (!!state.sessionId),
  isLoadedOnce: (state) => state.isLoadedOnce,
};

const actions = {
  [AUTH_REQUEST]: ({ commit, dispatch }, user) => {
    // The Promise used for router redirect in login
    return new Promise((resolve, reject) => {
      API.login({
        login: user.login,
        password: user.password,
      }).then((resp) => {
        const sessionId = resp.data.token;
        // store the session id in local storage
        localStorage.setItem('session-id', sessionId);
        API.setSessionId(sessionId);
        commit(AUTH_SUCCESS, sessionId);
        // you have your session id, now log in your user :)
        // dispatch(USER_REQUEST)
        resolve(resp);
      }).catch((err) => {
        commit(AUTH_ERROR);
        window.console.error('API.login()', err);
        // if the request fails, remove any possible session id if possible
        localStorage.removeItem('session-id'); // localStorage.clear()
        const status = err.request.status;
        if (status === 401) {
          reject(new Error('Incorrect login and/or password'));
        } else {
          reject(new Error(`Server connection error: ${status}`));
        }
      });
    });
  },

  [AUTH_LOGOUT]: ({ commit }) => {
    const sessionId = state.sessionId;
    if (!sessionId) {
      return;
    }

    localStorage.clear();

    commit(AUTH_LOGOUT);

    // TODO: API.logout();
  },

};

const mutations = {
  [AUTH_REQUEST]: (state) => {
    state.status = 'loading';
  },

  [AUTH_SUCCESS]: (state, sessionId) => {
    state.status = 'success';
    state.sessionId = sessionId;
    state.isLoadedOnce = true;
  },

  [AUTH_ERROR]: (state) => {
    state.status = 'error';
    state.isLoadedOnce = true;
  },

  [AUTH_LOGOUT]: (state) => {
    state.sessionId = '';
    state.isLoadedOnce = false;
  },
};

export default {
  state,
  getters,
  actions,
  mutations,
};
