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

Http.get('/api/auth/account', {
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
			//if (res.data.data.isApproved === 0) {
			//	actions.error(store, 403);
			//} else {
			//	getList();
			//}
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


const getList = function() {
	Http.get('/api/auth/privileges', {
			hideLoading: true
		}).then((res) => {
			if (res.data.code === 200) {
				actions.setUserPrivilege(store, res.data.data.privileges);
			} else {
				console.log("获取顶部导航权限失败!");
			}
		}).catch((error) => {
			console.log(error);
		});
};
