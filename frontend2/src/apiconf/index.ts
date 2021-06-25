import apiStore from '@/apistore';
import { DefaultApi } from '@/apiclient/api';
import { setBearerAuthToObject } from '@/apiclient/common';
import { Configuration } from '@/apiclient';
import { API_URL } from './constants';

class Api extends DefaultApi {
  public updateAccessToken() {
    const token = apiStore.state.token ? apiStore.state.token : null;
    setBearerAuthToObject(
      this.axios.defaults.headers.common, { accessToken: token } as Configuration,
    );
  }
}

const API = new Api(
  new Configuration({
    basePath: API_URL,
  }),
);

API.updateAccessToken();

export default API;
export { API };
