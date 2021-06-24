import { DefaultApi } from '@/apiclient/api';
import { Configuration } from '@/apiclient';
import { API_URL } from './constants';

const API = new DefaultApi(
  new Configuration({
    basePath: API_URL,
  }),
);

export default API;
export { API };
