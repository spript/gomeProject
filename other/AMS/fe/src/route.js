import Vue from 'vue';
import VueRouter from 'vue-router';

Vue.use(VueRouter);

import actions from 'actions';
import store from './vuex/index.js';

// router
import routerConfig from './config/router.config.js';

import { isEmptyObj } from 'utils/common.js';

const router = new VueRouter({
	routes: routerConfig
});

/**
router.beforeEach((to, from, next) => {
	if (!isEmptyObj(store.state.userInfo)) {
		if (store.state.userInfo.isApproved === 1) {
			if (to.path === '/app/index' || to.path === '/app/register' || to.name === 'error') {
				// hack 掉左侧白边
				document.body.style['background-image'] = 'none';
			} else if (to.path === '/login') {
				document.body.className = 'body-none';
			} else {
				document.body.style['background-image'] = '';
			}
			next();
		} else if (to.path === '/app/register' || to.path === '/login') {
			document.body.style['background-image'] = 'none';
			next();
		} else {
			actions.error(store, 403);
		}
	} else if (from.name === 'login' && isEmptyObj(store.state.userInfo)) {
		next(false);
	} else{
		next();
	}
});
*/
export default router;
