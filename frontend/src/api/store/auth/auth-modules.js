import { API } from '@/api';
import {
  AUTH_REQUEST, AUTH_ERROR, AUTH_SUCCESS, AUTH_LOGOUT,
} from './auth-actions';

const state = {
  sessionId: localStorage.getItem('session-id') || '',
  status: '',
  isLoadedOnce: false,
};

const getters = {
  isAuthenticated: (state) => (!!state.sessionId),
  isLoadedOnce: (state) => state.isLoadedOnce,
};

const actions = {
  [AUTH_REQUEST]: ({ commit, dispatch }, user) => {
    return new Promise((resolve, reject) => { // The Promise used for router redirect in login
      API.login({
        login: user.username,
        password: user.password,
      }).then((resp) => {
        window.console.log('***', resp);
        /*
        const sessionId = resp.data.sessionId
        // store the session id in localstorage
        localStorage.setItem('session-id', sessionId)
        // add the axios default header
        // TODO: API.setSessionId()
        apiAxios.defaults.headers.common['Authorization'] = 'Bearer ' + sessionId
        commit(AUTH_SUCCESS, sessionId)
        // you have your session id, now log in your user :)
        dispatch(USER_REQUEST)
        resolve(resp)
        */
      });
      /*
      .catch((err) => {
          commit(AUTH_ERROR)
          window.console.error('POST auth/login', err)
          // if the request fails, remove any possible session id if possible
          localStorage.removeItem('session-id') // localStorage.clear()
          const status = err.request.status
          if (status == 401) {
            reject('Incorrect login and/or password')
          } else {
            reject('Server connection error: ' + status)
          }
        })
      */
    });
  },

  [AUTH_LOGOUT]: ({ commit }) => {
    const sessionId = state.sessionId;
    if (!sessionId) {
      return;
    }

    localStorage.clear();

    commit(AUTH_LOGOUT);

    // TODO: API.logout()
    /*
    apiAxios.post("auth/logout", null, {
      params: {
        sessionId: sessionId
      }
    }).catch((err) => {
      window.console.error('POST auth/logout', err)
    })
    */
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
