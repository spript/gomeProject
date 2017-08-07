export default [
	{
		path: '/',
		redirect: '/app/index',
		meta: { permission: '' }
	},{
		path: '/app',
		name: 'app',
		component: require('../components/app/main.vue'),
		redirect: '/app/index',
		meta: { permission: '' },
		children: [{
			path: 'index', // 首页
			name: 'index',
			meta: { permission: '' },
			component: require('../components/app/index/main.vue'),
		}, {
			path: 'report', // 报表
			nam: 'report',
			component: require('../components/app/report/main.vue'),
			redirect: '/app/report/slot-slot',
			meta: { permission: 'report' },
			children: [{
				path: 'anicer-anice-summary', // 广告主广告汇总
				name: 'anice-summary',
				meta: { permission: 'report:anicer:anice:summary' },
				component: require('../components/app/report/anicer/anice-summary.vue'),
			}, {
				path: 'anicer-anice-detail', // 广告主广告明细
				name: 'anice-detial',
				meta: { permission: 'report:anicer:anice:detail' },
				component: require('../components/app/report/anicer/anice-detail.vue'),
			},{
				path: 'anicer-rebate-summary', // 广告主返利汇总
				name: 'rebate-summary',
				meta: { permission: 'report:anicer:rebate:summary' },
				component: require('../components/app/report/anicer/rebate-summary.vue'),
			}, {
				path: 'anicer-rebate-detail', // 广告主返利明细
				name: 'rebate-detial',
				meta: { permission: 'report:anicer:rebate:detail' },
				component: require('../components/app/report/anicer/rebate-detail.vue'),
			},{
				path: 'slot-slot', // 广告主位报表
				name: 'slot',
				meta: { permission: 'report:slot' },
				component: require('../components/app/report/slot/slot.vue'),
			},{
				path: 'income-anice-income', // 广告收入
				name: 'anice-income',
				meta: { permission: 'report:income:anice' },
				component: require('../components/app/report/income/anice-income.vue'),
			},{
				path: 'income-rebate-income', // 返利收入
				name: 'rebate-income',
				meta: { permission: 'report:income:rebate' },
				component: require('../components/app/report/income/rebate-income.vue'),
			},{
				path: 'record-cosume-summary', // 广告主返利汇总
				name: 'cosume-summary',
				meta: { permission: 'report:record:cosume:summary' },
				component: require('../components/app/report/record/cosume-summary.vue'),
			}, {
				path: 'record-cosume-detail', // 广告主返利明细
				name: 'cosume-detail',
				meta: { permission: 'report:record:cosume:detail' },
				component: require('../components/app/report/record/cosume-detail.vue'),
			},{
				path: 'record-recharge-summary', // 广告主返利汇总
				name: 'recharge-summary',
				meta: { permission: 'report:record:recharge:summary' },
				component: require('../components/app/report/record/recharge-summary.vue'),
			}, {
				path: 'record-recharge-detail', // 广告主返利明细
				name: 'recharge-detail',
				meta: { permission: 'report:record:recharge:detail' },
				component: require('../components/app/report/record/recharge-detail.vue'),
			}]
		},{
			path: 'error403',
			name: 'error403',
			component: require('../components/error/forbidden.vue')
		}]
	}, {
		path: '/login', // 登录
		name: 'login',
		component: require('../components/login/main.vue')
	},{
		path:'*',
		name:'error404',
		component: require('../components/error/notfound.vue')
	}
];
