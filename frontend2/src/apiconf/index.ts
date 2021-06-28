import apiStore from '@/apistore';
import { DefaultApi } from '@/apiclient/api';
import { Configuration } from '@/apiclient';
import { API_URL } from './constants';

class Api extends DefaultApi {
  constructor(configuration?: Configuration) {
    super(configuration);
    apiStore.subscribe((mutation, state) => {
      const headers = this.axios.defaults.headers.common;
      headers.Authorization = `${state.type} ${state.token}`;
    });
  }
}

const API = new Api(
  new Configuration({
    basePath: API_URL,
  }),
);

export default API;
export { API };
