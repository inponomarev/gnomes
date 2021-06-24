import { AxiosRequestConfig } from 'axios';

export type State = {
  loggedIn: boolean,
  currentUser: string,
  token: string,
  type: string,
  getHeaders: () => AxiosRequestConfig
}

export const state: State = {
  loggedIn: false,
  currentUser: '',
  token: '',
  type: 'Bearer',
  getHeaders() {
    const headers = this.loggedIn ? { Authorization: `${this.type} ${this.token}` } : {};
    return {
      headers,
    } as AxiosRequestConfig;
  },
};
