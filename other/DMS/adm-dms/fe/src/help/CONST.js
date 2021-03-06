export default {
	DRAWERPLAN: { // 新建投放计划默认值
		'name': '',
		'isContinuous': 1, // 是否连续投放
		'isUnlimited': 0, // 是否限制结束时间   0 限定  1 不限
		'startTime': Date.now(), // 开始时间
		'endTime': Date.now(), // 结束时间
		'schedule': [], // 时间段数组
		'saleMode': 3, // 计费方式
		'adLimited': 0, // 广告日是否限制预算
		'dailyAdBudget': 0, // 广告日预算
		'isRebate': 0, // 返利活动开启与否
		'rebateLimited': 0, // 返利日是否限制预算
		'dailyRebateBudget': 0 // 返利日日预算
	},
	DRAWERUNIT: { // 新建投放单元默认值
		// 额外的字段需要手动传入  单元所属计划 ID 等
		'name': '',
		'platform': 1, // 投放平台（1-APP 2-WAP 3-PC）
		'type': 1, // 投放类型（1-商品推广 2-活动推广 ）
		'timeType': 0, // 时间定向类型（0-全时段 1-自定义）
		'time': { // 时间定向
			'1': [0,1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23],
			'2': [0,1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23],
			'3': [0,1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23],
			'4': [0,1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23],
			'5': [0,1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23],
			'6': [0,1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23],
			'7': [0,1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23]
		},
		'regionType': 0, // 地域定向类型（0-不限 1-自定义 ）
		'region': [], // 地域定向
		'rebateBid': 1, // 返利出价
		'advertisements': []
	},
	DRAWERIDEA: { // 新建创意默认值
		// 额外的字段需要手动传入  创意所属单元 ID 等
		'name': '',
		'itemId': '',
		'type': 1, // 创意类型（1图片 2图文 3视频 4flash 5文字）
		'linkType': 1, // 链接类型（1商品 2店铺 3圈子 4话题 5url）
		'title': '', // 标题
		'image': '', // 图片
		'description': '', // 文案
		'relatedItems': [] // 联合推广商品
	},
	ADREPORTHEADER: [{
		title: '',
		fields: [
			{msg: '展现量（PV）', checked: false},
			{msg: '展现量（UV）', checked: false},
			{msg: '点击量', checked: false},
			{msg: '点击率', checked: false},
			{msg: '平均点击单价（元）', checked: false},
			{msg: '千次曝光成本（元）', checked: false},
			{msg: '消耗（元）', checked: false},
			{msg: 'ROI', checked: false},
			{msg: '总商品行金额（元）', checked: false}
		]
	}, {
		title: '',
		fields: [
			{msg: '直接商品行数量', checked: false},
			{msg: '直接商品行金额（元）', checked: false},
			{msg: '间接商品行数量', checked: false},
			{msg: '间接商品行金额（元）', checked: false},
			{msg: '总商品行数量', checked: false}
		]}
	]
};
