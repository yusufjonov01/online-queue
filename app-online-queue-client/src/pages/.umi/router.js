import React from 'react';
import {
  Router as DefaultRouter,
  Route,
  Switch,
  StaticRouter,
} from 'react-router-dom';
import dynamic from 'umi/dynamic';
import renderRoutes from 'umi/lib/renderRoutes';
import history from '@@/history';

const Router = DefaultRouter;

const routes = [
  {
    path: '/',
    component: require('../../layouts/index.js').default,
    routes: [
      {
        path: '/admin/category',
        exact: true,
        component: require('../admin/category.js').default,
      },
      {
        path: '/admin/companyInfo',
        exact: true,
        component: require('../admin/companyInfo.js').default,
      },
      {
        path: '/admin/model',
        exact: true,
        component: require('../admin/model.js').default,
      },
      {
        path: '/auth/login',
        exact: true,
        component: require('../auth/login/index.js').default,
      },
      {
        path: '/cabinet',
        exact: true,
        component: require('../cabinet/index.js').default,
      },
      {
        path: '/company/companyInfo',
        exact: true,
        component: require('../company/companyInfo/index.js').default,
      },
      {
        path: '/company',
        exact: true,
        component: require('../company/index.js').default,
      },
      {
        path: '/company/:id/direction',
        exact: true,
        component: require('../company/$id/direction/index.js').default,
      },
      {
        path: '/company/:id/direction/:id',
        exact: true,
        component: require('../company/$id/direction/$id/index.js').default,
      },
      {
        path: '/company/:id',
        exact: true,
        component: require('../company/$id/index.js').default,
      },
      {
        path: '/district',
        exact: true,
        component: require('../district/index.js').default,
      },
      {
        path: '/district/model',
        exact: true,
        component: require('../district/model.js').default,
      },
      {
        path: '/',
        exact: true,
        component: require('../index.js').default,
      },
      {
        path: '/message',
        exact: true,
        component: require('../message/index.js').default,
      },
      {
        path: '/message/model',
        exact: true,
        component: require('../message/model.js').default,
      },
      {
        path: '/profil',
        exact: true,
        component: require('../profil/index.js').default,
      },
      {
        path: '/region',
        exact: true,
        component: require('../region/index.js').default,
      },
      {
        path: '/region/model',
        exact: true,
        component: require('../region/model.js').default,
      },
      {
        path: '/service',
        exact: true,
        component: require('../service.js').default,
      },
      {
        path: '/sozlamalar',
        exact: true,
        component: require('../sozlamalar/index.js').default,
      },
      {
        path: '/utils',
        exact: true,
        component: require('../utils.js').default,
      },
      {
        component: () =>
          React.createElement(
            require('/Users/Abdurakhmon/practice/copy online queue/app-online-queue-client/node_modules/umi-build-dev/lib/plugins/404/NotFound.js')
              .default,
            { pagesPath: 'src/pages', hasRoutesInConfig: false },
          ),
      },
    ],
  },
  {
    component: () =>
      React.createElement(
        require('/Users/Abdurakhmon/practice/copy online queue/app-online-queue-client/node_modules/umi-build-dev/lib/plugins/404/NotFound.js')
          .default,
        { pagesPath: 'src/pages', hasRoutesInConfig: false },
      ),
  },
];
window.g_routes = routes;
const plugins = require('umi/_runtimePlugin');
plugins.applyForEach('patchRoutes', { initialValue: routes });

export { routes };

export default class RouterWrapper extends React.Component {
  unListen() {}

  constructor(props) {
    super(props);

    // route change handler
    function routeChangeHandler(location, action) {
      plugins.applyForEach('onRouteChange', {
        initialValue: {
          routes,
          location,
          action,
        },
      });
    }
    this.unListen = history.listen(routeChangeHandler);
    // dva 中 history.listen 会初始执行一次
    // 这里排除掉 dva 的场景，可以避免 onRouteChange 在启用 dva 后的初始加载时被多执行一次
    const isDva =
      history.listen
        .toString()
        .indexOf('callback(history.location, history.action)') > -1;
    if (!isDva) {
      routeChangeHandler(history.location);
    }
  }

  componentWillUnmount() {
    this.unListen();
  }

  render() {
    const props = this.props || {};
    return <Router history={history}>{renderRoutes(routes, props)}</Router>;
  }
}
