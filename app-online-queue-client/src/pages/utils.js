import {API_PREFIX, TOKEN_NAME} from "../constants/constants";
import axios from "axios";

const bolta = (uroq) => {
  let {
    method = 'get',
    data,
    url,
    headers = {'Content-Type': 'application/json'}
  } = uroq;
  url = API_PREFIX + url;

  let tokenHeader = '';
  try {
    const orderAppToken = localStorage.getItem(TOKEN_NAME);
    if (orderAppToken) {
      tokenHeader = {
        'Authorization': orderAppToken,
        ...headers,
        'ketmon': 888
      }
    } else {
      tokenHeader = {
        ...headers,
      };
    }
  } catch (e) {
  }

  console.log(tokenHeader);
  if (data && data.id) {
    url = url + '/' + data.id;
  }
  // console.log(url);
  switch (method.toLocaleLowerCase()) {
    case "get":
      if (data) {
        let kk = JSON.stringify(data);
        kk = kk.replace(/["{}]/g, "").replace(/,/g, "&").replace(/:/g, '=');
        url = url + '?' + kk;
      }
      let a = {headers: tokenHeader};
      return axios.get(url, {
        headers: tokenHeader
      });
    case "delete":
      return axios.delete(url,
        {
          headers: tokenHeader
        });
    case "put":
      return axios.put(url, data,
        {headers: tokenHeader}
      );
    case "patch":
      // console.log(tokenHeader);
      return axios.patch(url, data,
        {headers: tokenHeader}
      );
    case "post":
      return axios.post(url,
        data,
        {headers: tokenHeader});
    default:
      return axios(uroq);
  }
};

export default function request(ketmon) {
  return bolta(ketmon).then(res => {
    const {statusText, status, data} = res;
    return Promise.resolve({
      success: true,
      message: statusText,
      statusCode: status,
      ...data
    })
  }).catch(err => {
    // console.log(err);
    const {response, message} = err;
    // console.log(response);
    // console.log(message);
    return Promise.resolve({
      success: false,
      statusText: response.data.message,
      statusCode: response.data.status,
      errors: response.data.errors,
      error: response.data.error
    })
  })
}
