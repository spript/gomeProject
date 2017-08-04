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

router.beforeEach((to, from, next) => {

	if (to.path === '/app/index' || to.path === '/app/register' || to.name === 'error') {
		// hack 掉左侧白边
		document.body.style['background-image'] = 'none';
	} else if (to.path === '/login' || to.path === '/app/succeed') {
		document.body.style['background-image'] = 'none';
	} else {
		document.body.style['background-image'] = '';
	}
	if (!isEmptyObj(store.state.userInfo)) {
		if (from.name === 'testEnd' && to.name !== 'login' && to.name !== 'second') {
			next(false);
		} else {
			if (store.state.userInfo.isRegistered === 1) {
				if (store.state.userInfo.isApproved === 1) {
					next();
				} else if (store.state.userInfo.isApproved === -1 && (to.path === '/app/refused' || to.name === 'register')) {
					next();
					document.body.style['background-image'] = 'none';
				} else {
					if (to.name !== 'succeed') {
						next(false);
					} else {
						document.body.style['background-image'] = 'none';
						next();
					}
				}
			} else {
				if (to.name !== 'register') {
					next(false);
				} else {
					document.body.style['background-image'] = 'none';
					next();
				}
			}
		}
		/*
		if (store.state.userInfo.isRegistered === 1 && from.name !== 'succeed') {
			next();
		} else if (to.path === '/app/register' || to.path === '/login') {
			document.body.style['background-image'] = 'none';
			next();
		} else if (store.state.userInfo.isRegistered && !store.state.userInfo.isApproved) {
			document.body.style['background-image'] = 'none';
			next(false);
		} else if (store.state.userInfo.isRegistered && store.state.userInfo.isApproved) {
			next();
		} else {
			actions.error(store, 403);
		}
		*/
	} else if ((from.name === 'login' && to.name !== 'second' && isEmptyObj(store.state.userInfo)) || (from.name === 'second' && to.name !== 'login' && isEmptyObj(store.state.userInfo))) {
		next(false);
	} else {
		next();
	}
});

export default router;
