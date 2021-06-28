import { DefaultApi } from '@/apiclient/api';
import { setBearerAuthToObject } from '@/apiclient/common';
import { Configuration } from '@/apiclient';
import { API_URL } from '@/api/constants';

class Api extends DefaultApi {
  public setSessionId(sessionId: string) {
    setBearerAuthToObject(
      this.axios.defaults.headers.common, { accessToken: sessionId } as Configuration,
    );
  }
}

const API = new Api(
  new Configuration({
    basePath: API_URL,
  }),
);

export default API;
export { API };
