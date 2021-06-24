import { MutationTree } from 'vuex';

import { LoginResultDTO } from '@/apiclient';
import { State } from '@/store/state';

// eslint-disable-next-line no-shadow
export enum MutationType {
  Login = 'LOGIN',
  Logout = 'LOGOUT',
}

export type Mutations = {
  [MutationType.Login](state: State, loginResult: LoginResultDTO): void;
  [MutationType.Logout](state: State): void;
};

export const mutations: MutationTree<State> & Mutations = {
  [MutationType.Logout](state: State): void {
    state.loggedIn = false;
    state.currentUser = '';
    state.token = '';
    state.type = '';
  },
  [MutationType.Login](state: State, loginResult: LoginResultDTO): void {
    state.loggedIn = true;
    state.currentUser = loginResult.username ?? '';
    state.token = loginResult.token ?? '';
    state.type = loginResult.type ?? '';
  },
};
