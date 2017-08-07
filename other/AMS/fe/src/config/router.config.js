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
		path: 'approval', // 审核,
			name: 'approval',
			component: require('../components/app/approval/main.vue'),
			redirect: '/app/approval/material/not',
			children: [{
			path: 'material',// 素材审核
			name: 'material',
			component: require('../components/app/approval/material/list.vue'),
			children: [{
				name: 'material-not', // 未审核素材
				path: 'not',
				component: require('../components/app/approval/material/list.vue')
			},{
				name: 'material-passed',
				path: 'passed', // 已通过素材
				component: require('../components/app/approval/material/list.vue')
			},{
				name: 'material-not-passed',
				path: 'notpassed', // 未通过素材
				component: require('../components/app/approval/material/list.vue')
			}]
			},{
				path: 'advertiser',// 广告主审核
				name: 'advertiser',
				component: require('../components/app/approval/advertiser/list.vue'),
				children: [{
				name: 'advertiser-not', // 未审核广告
				path: 'not',
				component: require('../components/app/approval/advertiser/list.vue')
			},{
				name: 'advertiser-passed',
				path: 'passed', // 已通过广告
				component: require('../components/app/approval/advertiser/list.vue')
			},{
				name: 'advertiser-not-passed',
				path: 'notpassed', // 未通过广告
				component: require('../components/app/approval/advertiser/list.vue')
			}]
		},{
			path: 'survey',// 问卷审核
				name: 'survey',
				component: require('../components/app/approval/survey/list.vue'),
				children: [{
				name: 'survey-not', // 未审核问卷
				path: 'not',
				component: require('../components/app/approval/survey/list.vue')
			},{
				name: 'survey-passed',
				path: 'passed', // 已通过问卷
				component: require('../components/app/approval/survey/list.vue')
			},{
				name: 'survey-reject',
				path: 'reject', // 未通过问卷
				component: require('../components/app/approval/survey/list.vue')
			}]
		}]
	}, {
		path: 'resource', // 资源位,
		name: 'resource',
		component: require('../components/app/resource/main.vue'),
		redirect: '/app/resource/publisher',
		children:[{
			path: 'publisher',// 媒体列表
			component: require('../components/app/resource/publisher/media-list.vue')
		}, {
			path: 'channel/:id', //频道列表
			name: 'channel',
			component: require('../components/app/resource/channel/channel-list.vue'),
		}, {
			path: 'advertisingList/:id', //广告位列表
			name: 'advertisingList',
			component: require('../components/app/resource/advertisingList/advertising-list.vue')
		}, {
			path: 'advert/:id', //广告列表
			name: 'advert',
			component: require('../components/app/resource/advert/advert-list.vue')
		}, {
			path: 'advertising', //广告位
			name: 'advertising',
			component: require('../components/app/resource/advertising/advertising.vue'),
		}, {
			path: 'advertForm', //广告形式
			name: 'advertForm',
			component: require('../components/app/resource/advertForm/advertForm.vue'),
		}, {
			path: 'template', //模板
			name: 'template',
			component: require('../components/app/resource/template/template.vue'),
		}]
	}, {
        path: 'consumer', // 用户任务
        name: 'consumer',
        component: require('../components/app/consumer/main.vue'),
        redirect: '/app/consumer/missionManage',
        children: [{
            path: 'missionManage', // 用户管理
            path: 'missionManage', // 用户管理
            component: require('../components/app/consumer/mission/manageList.vue'),
        }, {
            path: 'missionReport', // 数据报表
            path: 'missionReport', // 数据报表
            component: require('../components/app/consumer/mission/reportList.vue'),
        }]
	}]
}];

 	