import Vue from 'vue';

// directive
import './directive/index.js';

// filter
import './filter/index.js';

// app component
import App from './app.vue';

// router
import router from './route.js';

// vuex
import store from './vuex/index.js';

// element-ui
import './ui.js';

// 检测登录 记录用户信息 跳转页面
import Http from 'http';
import actions from 'actions';
import moment from 'moment';
import vueMoment from 'vue-moment';

Vue.use(vueMoment);

Http.get('api/user', {
		hideLoading: true
	})
	.then((res) => {
		if (res.data.iserror && res.data.code === 401) {
			// 未登录
			router.push({
				name: 'login'
			});
		} else {
			actions.setUserInfo(store, res.data.data);
			Http.defaults.headers.common['Authorization'] = store.state.userInfo.token;
			if (!res.data.data.isRegistered) {
				router.push({
					name: 'register'
				});
			} else if (!res.data.data.isApproved){
				router.push({
					name: 'succeed'
				});
			} else if (res.data.data.isApproved === -1){
				router.push({
					name: 'refused'
				});
			} else if (res.data.data.isTest === 1 && moment().valueOf() > res.data.data.testEndTime) {
				router.push({
					name: 'testEnd'
				});
			}
		}
		new Vue({
			el: '#app',
			store: store,
			template: '<app></app>',
			components: {
				app: App
			},
			router: router
		});
	});
