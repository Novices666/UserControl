/**
 * request 网络请求工具
 * 更详细的 api 文档: https://github.com/umijs/umi-request
 */
import {extend} from 'umi-request';
import {message} from 'antd';
import {history} from "@@/core/history";
import {stringify} from "querystring";


/**
 * 配置request请求时的默认参数
 */
const request = extend({
  credentials: 'include', // 默认请求是否带上cookie
  // requestType: 'form',
});

/**
 * 所以请求拦截器
 */
request.interceptors.request.use((url, options): any => {
  return {
    url,
    options: {
      ...options,
      headers: {},
    },
  };
});

/**
 * 所有响应拦截器
 */
request.interceptors.response.use(async (response, options): Promise<any> => {


  const res = await response.clone().json();
  // console.log(data)
  if (res.code == 40100) {
    history.replace({
      pathname: '/user/login',
      search: stringify({
        redirect: location.pathname,
      }),
    });
  }


  if (res.code == 20000) {
    return res.data;
  } else {
    message.error(res.description);
    if (history.location.pathname.indexOf('login') > -1) {
      const {query} = history.location;
      history.push({
        pathname: '/user/login',
        query
      });
    }
    return;
  }
  return res.data;
});

export default request;
