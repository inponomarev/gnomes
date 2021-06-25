export type State = {
  loggedIn: boolean,
  currentUser: string,
  token: string,
  type: string,
}

export const state: State = {
  loggedIn: false,
  currentUser: '',
  token: '',
  type: 'Bearer',
};
