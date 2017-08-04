export default [{
	path: '/',
	redirect: '/app/index'
}, {
	path: '/app',
	component: require('../components/app/main.vue'),
	redirect: '/app/index',
	children: [{
		path: 'index', // 首页
		name: 'index',
		component: require('../components/app/index/main.vue')
	}, {
		path: 'account', // 我的账户
		component: require('../components/app/account/main.vue'),
		redirect: '/app/account/account-overview',
		children: [{
			path: 'account-overview', // 账户概览
			component: require('../components/app/account/account-overview.vue'),
		}, {
			path: 'account-detail', // 账户明细
			component: require('../components/app/account/account-detail.vue'),
			redirect: '/app/account/account-detail/expense',
			children:[{
				path: '/app/account/account-detail/expense',
				name: 'account-expense',
				component: require('../components/app/account/account-expense.vue'),
			},{
				path: '/app/account/account-detail/income',
				name: 'account-income',
				component: require('../components/app/account/account-income.vue'),
			},{
				path: '/app/account/account-detail/expense-daily/:date',
				name: 'account-expense-daily',
				component: require('../components/app/account/account-expense-daily.vue'),
			}]
		}, {
			path: 'virtual-money-account', //虚拟金账户
			component: require('../components/app/account/virtual-money-account.vue'),
		}, {
			path: 'contact-way', // 联系方式
			component: require('../components/app/account/contact-way.vue'),
		}, {
			path: 'password', // 修改密码
			component: require('../components/app/account/password.vue'),
		}, {
			path: 'message', // 站内信
			component: require('../components/app/account/message.vue'),
			redirect: '/app/account/message/list/1',
			children: [{
				path: 'list/:type', // 站内信
				name: 'message-list',
				component: require('../components/app/account/messageList.vue')
			},{
				path: 'detail/:type/:id', // 站内信
				name: 'message-detail',
				component: require('../components/app/account/messageDetail.vue')
			}]
		}, {
			path: 'balance', // 余额提醒
			component: require('../components/app/account/balance.vue'),
		}, {
			path: 'certificate', // 商家资质
			component: require('../components/app/account/certificate.vue'),
		}, {
			path: 'ad-standard', // 广告审核规范
			component: require('../components/app/account/ad-standard.vue'),
		}, {
			path: 'agreement1', // 用户协议 1
			component: require('../components/app/account/agreement1.vue'),
		}]
	}, {
		path: 'put', // 投放管理
		component: require('../components/app/put/cpc/main.vue'),
		redirect: '/app/put/plan',
		children: [{
			path: 'plan', // 投放计划
			component: require('../components/app/put/cpc/plan.vue'),
		}, {
			path: 'unit/:id', // 投放单元 params [id -> 投放单元 id]
			name: 'unit',
			component: require('../components/app/put/cpc/unit.vue'),
		}, {
			path: 'idea/:id', // 投放单元 params [id -> 投放单元 id]
			name: 'idea',
			component: require('../components/app/put/cpc/idea.vue'),
		}, {
			path: 'report/plan/campaign',
			name: 'plan-campaign',
			component: require('../components/app/report/cpc/plan-campaign.vue')
		},{
			path: 'report/plan/flight',
			name: 'plan-flight',
			component: require('../components/app/report/cpc/plan-flight.vue')
		},{
			path: 'report/plan/material',
			name: 'plan-material',
			component: require('../components/app/report/cpc/plan-material.vue')
		},{
			path: 'report/effect/order',
			name: 'effect-order',
			component: require('../components/app/report/cpc/effect-order.vue')
		},{
			path: 'report/effect/summary',
			name: 'effect-summary',
			component: require('../components/app/report/cpc/effect-summary.vue')
		},{
			path: 'report/rebate/campaign',
			name: 'rebate-campaign',
			component: require('../components/app/report/cpc/rebate-campaign.vue')
		},{
			path: 'report/rebate/flight',
			name: 'rebate-flight',
			component: require('../components/app/report/cpc/rebate-flight.vue')
		},{
			path: 'report/rebate/material',
			name: 'rebate-material',
			component: require('../components/app/report/cpc/rebate-material.vue')
		}]
	}, {
		path: 'tools', // 工具
		component: require('../components/app/tools/webpage/main.vue'),
		redirect: '/app/tools/webpage',
		children: [{
			path: 'webpage', // 自建活动
			component: require('../components/app/tools/webpage/page.vue'),
		},{
			path: 'survey', // 调查问卷
			component: require('../components/app/tools/survey/survey.vue'),
		}]
	}, {
		path: 'register', // 商家入驻,
		name: 'register',
		component: require('../components/app/register/main.vue')
	}, {
			path: 'succeed',
			name: 'succeed',
			component: require('../components/app/register/succeed.vue')
	},{
			path: 'refused',
			name: 'refused',
			component: require('../components/app/register/refused.vue')
	}, {
		path: 'bidcpc',
		component: require('../components/app/put/bidcpc/main.vue'),
		redirect: '/app/bidcpc/plan',
		children: [{
			path: 'plan', // 投放计划
			component: require('../components/app/put/bidcpc/plan.vue'),
		}, {
			path: 'unit/:id', // 投放单元 params [id -> 投放单元 id]
			name: 'bidcpcUnit',
			component: require('../components/app/put/bidcpc/unit.vue'),
		}, {
			path: 'idea/:id', // 投放单元 params [id -> 投放单元 id]
			name: 'bidcpcIdea',
			component: require('../components/app/put/bidcpc/idea.vue'),
		}, {
			path: 'report/plan/campaign',
			name: 'bidcpc-plan-campaign',
			component: require('../components/app/report/bidCpc/plan-campaign.vue')
		}, {
			path: 'report/plan/flight',
			name: 'bidcpc-plan-flight',
			component: require('../components/app/report/bidCpc/plan-flight.vue')
		}, {
			path: 'report/plan/material',
			name: 'bidcpc-plan-material',
			component: require('../components/app/report/bidCpc/plan-material.vue')
		}, {
			path: 'report/plan/keywords',
			name: 'bidcpc-plan-keywords',
			component: require('../components/app/report/bidCpc/plan-keywords.vue')
		}, {
			path: 'report/effect/summary',
			name: 'bidcpc-effect-summary',
			component: require('../components/app/report/bidCpc/effect-summary.vue')
		}, {
			path: 'report/effect/order',
			name: 'bidcpc-effect-order',
			component: require('../components/app/report/bidCpc/effect-order.vue')
		}]
	}]
}, {
	path: '/login', // 登录
	name: 'login',
	component: require('../components/login/main.vue'),

},
{
	path: '/second', // 官宣二级页面
	name: 'second',
	component: require('../components/login/second.vue'),
}, {
	path: '/test-end',
	name: 'testEnd',
	component: require('../components/error/test.vue')
}, {
	path:'*',
	name:'error',
	component: require('../components/error/notfound.vue'),
}];
