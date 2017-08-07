<?php
return array (
	'globalConfigs' => array (
		'adrequestUrl' => 'https://flight-pre.gomeplus.com/adrequest',
		'adrequestIdUrl' => 'https://flight-pre.gomeplus.com/rebate',
		'shareUrl' => 'https://shareserver-pre.gomeplus.com/share',
		'shareImpressionUrl' => 'https://shareserver-pre.gomeplus.com/shareImpression',
		'rebateBudgetUrl' => 'https://shareserver-pre.gomeplus.com/rebateBudget',
		'middlePageImpression' => 'https://shareserver-pre.gomeplus.com/middlePageImpression',

		'pcHost' => 'http://awall.pre.gomeplus.com/',
		'wapHost' => 'http://m-awall.pre.gomeplus.com/',
		'h5Host' => 'http://h5-awall.pre.gomeplus.com/',

		'rebateUrl' => 'http://h5-awall.pre.gomeplus.com/item/rebate',
		'protocolUrl' => 'http://h5-awall.pre.gomeplus.com/item/protocol',

		'gomeItemHost' => 'http://item.m.uatplus.com/',
		'gomeHost' => 'http://m.uatplus.com/',
	),

	'configs' => array (
		'domain' => 'http://discovery.dev.ds.gome.com.cn/',
		'mdomain' => 'http://discovery.dev.ds.gome.com.cn/',

		// 广告请求接口（余招财）
		'adrequestUrl' => 'https://flight-pre.gomeplus.com/flight',
		// 查询返利预算状态&&查询三个返利标签的价格接口（徒康正）
		'rebateBudgetUrl' => 'https://shareserver-pre.gomeplus.com/rebateBudget',
		// 点赞/点赞状态接口（徒康正）
		'pariseUrl' => 'https://shareserver-pre.gomeplus.com/parise',
		// 获取点赞状态接口（徒康正）
		'pariseInfoUrl' => 'https://shareserver-pre.gomeplus.com/pariseInfo',
		// 分享数接口（徒康正）
		'shareCountUrl' => 'https://shareserver-pre.gomeplus.com/discoveryShare',
		// 获取列表页点赞数/点赞状态、分享数接口（徒康正）
		'batchPraiseShareUrl' => 'https://shareserver-pre.gomeplus.com/pariseShareBatch',
		// 分享返利接口（徒康正）
		'shareRebateUrl' => 'https://shareserver-pre.gomeplus.com/shareRebate',
		// 视频播放返利接口（徒康正）
		'videoShareUrl' => 'https://shareserver-pre.gomeplus.com/video',
		// 问卷调查返利/问卷调查数接口（徒康正）
		'questionRebateUrl' => 'https://shareserver-pre.gomeplus.com/questionRebate',
		// 问卷调查浏览数接口（徒康正）
		'questionViewUrl' => 'https://shareserver-pre.gomeplus.com/questionView',
		// 中间页曝光接口（徒康正）
		'middlePageImpressionUrl' => 'https://shareserver-pre.gomeplus.com/middlePageImpression',
		// 根据调查问卷返利数目和日预算显示调查问卷入口接口（徒康正）
		'questionInvestAppearUrl' => 'https://shareserver-pre.gomeplus.com/questionInvestAppear',

		// 问卷页面地址
		'surveyUrl' => 'http://discovery.dev.ds.gome.com.cn/survey/1.html',

		// 视频计数接口（熊仁海）
		'setVideoCountUrl' => 'http://10.69.218.102:8080/set/video/',
		'getVideoCountUrl' => 'http://10.69.218.102:8080/get/video/',

		//用户任务接口域名
		'missionDoMain' => 'http://10.69.205.17:8089/',
		//用户任务入口地址
		'missionUrl' => 'http://discovery.dev.ds.gome.com.cn/mission',
		// 视频播放环境
		'env' => 'pre',
	),

	'slotIds' => array (
		// 美媒--好东西（内嵌）
		'h5ItemSlotId' => 10090,
		// 美媒--好东西（wap）
		'wapItemSlotId' => 10091,
		// 美媒--有腔调（内嵌）
		'activeH5' => 10093,
		// 美媒--有腔调（wap）
		'activeWap' => 10094,

		// 探索频道--发现
		'h5DiscoveryTopicId' => 10250,
		// 探索频道--好店
		'h5DiscoveryShopId' => 10251,
		// 探索频道--清单
		'h5DiscoveryItemId' => 10252,
		// 探索频道--视频
		'h5DiscoveryVideoId' => 10253,

		'h5DiscoveryProductId' => 10250
	),

	'advertisementIds' => array (
		'h5ItemAdvertisementId' => 10115,
		'wapItemAdvertisementId' => 10116,
		'h5WebpageAdvertisementId' => 10118,
		'wapWebpageAdvertisementId' => 10119,
	),

	'wx' => array(
		'appid' => 'wx77edddc93a91176f',
		'gj_appid' => 'wx9f20920141def6e6',
		'app_secret' => 'a136798d345d6ad094e91b4679ecf8fa',
		'gj_app_secret' => 'da5a625b0c506ec9e436fff86ceda64d',
	),

	'entry' => array(
		'h5' => array(
			'imageUrl' => 'https://i5.meixincdn.com/v1/img/T1QvdTBTZv1R4cSCrK.png',
			'entryUrl' => 'http://h5-awall.pre.gomeplus.com/',
		),
		'wap' => array(
			'imageUrl' => 'https://i5.meixincdn.com/v1/img/T1QvdTBTZv1R4cSCrK.png',
			'entryUrl' => 'http://m-awall.pre.gomeplus.com/',
		)
	),
);





/**
return array (
	'globalConfigs' => array (
		'adrequestUrl' => 'http://flight.dev.gomeplus.com/adrequest',
		'adrequestIdUrl' => 'http://flight.dev.gomeplus.com/rebate',
		'shareUrl' => 'http://flight.dev.gomeplus.com:8087/share',
		'shareImpressionUrl' => 'http://flight.dev.gomeplus.com:8087/shareImpression',
		'rebateBudgetUrl' => 'http://flight.dev.gomeplus.com:8087/rebateBudget',
		'middlePageImpression' => 'http://flight.dev.gomeplus.com:8087/middlePageImpression',

		'pcHost' => 'http://awall.dev.gomeplus.com/',
		'wapHost' => 'http://m-awall.dev.gomeplus.com/',
		'h5Host' => 'http://h5-awall.dev.gomeplus.com/',

		'rebateUrl' => 'http://h5-awall.dev.gomeplus.com/item/rebate',
		'protocolUrl' => 'http://h5-awall.dev.gomeplus.com/item/protocol',

		'gomeItemHost' => 'http://item.m.uatplus.com/',
		'gomeHost' => 'http://m.uatplus.com/',
	),

	'configs' => array (
		// 广告请求接口（余招财）
		'adrequestUrl' => 'http://flight.dev.gomeplus.com/flight',
		// 查询返利预算状态&&查询三个返利标签的价格接口（徒康正）
		'rebateBudgetUrl' => 'http://flight.dev.gomeplus.com:8087/rebateBudget',
		// 点赞/点赞状态接口（徒康正）
		'pariseUrl' => 'http://flight.dev.gomeplus.com:8087/parise',
		// 分享数接口（徒康正）
		'shareCountUrl' => 'http://flight.dev.gomeplus.com:8087/discoveryShare',
		// 获取列表页点赞数/点赞状态、分享数接口（徒康正）
		'batchPraiseShareUrl' => 'http://flight.dev.gomeplus.com:8087/pariseShareBatch',
		// 分享返利接口（徒康正）
		'shareRebateUrl' => 'http://flight.dev.gomeplus.com:8087/shareRebate',
		// 视频播放返利接口（徒康正）
		'videoShareUrl' => 'http://flight.dev.gomeplus.com:8087/video',
		// 问卷调查返利/问卷调查数接口（徒康正）
		'questionRebateUrl' => 'http://flight.dev.gomeplus.com:8087/questionRebate',
		// 问卷调查浏览数接口（徒康正）
		'questionViewUrl' => 'http://flight.dev.gomeplus.com:8087/questionView',
		// 中间页曝光接口（徒康正）
		'middlePageImpressionUrl' => 'http://flight.dev.gomeplus.com:8087/middlePageImpression',
		// 根据调查问卷返利数目和日预算显示调查问卷入口接口（徒康正）
		'questionInvestAppearUrl' => 'http://flight.dev.gomeplus.com:8087/questionInvestAppear',

		// 视频计数接口（熊仁海）
		'setVideoCountUrl' => 'http://10.69.218.102:8080/set/video/',
		'getVideoCountUrl' => 'http://10.69.218.102:8080/get/video/',
		// 视频播放环境
		'env' => 'dev',
	),

	'slotIds' => array (
		// 美媒--好东西（内嵌）
		'h5ItemSlotId' => 10090,
		// 美媒--好东西（wap）
		'wapItemSlotId' => 10091,
		// 美媒--有腔调（内嵌）
		'activeH5' => 10093,
		// 美媒--有腔调（wap）
		'activeWap' => 10094,

		// 探索频道--发现
		'h5DiscoveryTopicId' => 10064,
		// 探索频道--好店
		'h5DiscoveryShopId' => 10065,
		// 探索频道--清单
		'h5DiscoveryItemId' => 10066,
		// 探索频道--视频
		'h5DiscoveryVideoId' => 10067,
	),


	'advertisementIds' => array (
		'h5ItemAdvertisementId' => 10115,
		'wapItemAdvertisementId' => 10116,
		'h5WebpageAdvertisementId' => 10118,
		'wapWebpageAdvertisementId' => 10119,
	),


	'wx' => array(
		'appid' => 'wx9b797be85ded9dc8',
		'app_secret' => '2de38750a777a3e05ad80b5e43fe1321',
	),

	'entry' => array(
		'h5' => array(
			'imageUrl' => 'https://i4.meixincdn.com/v1/img/T15aDTBTYg1RXrhCrK.jpg',
			'entryUrl' => 'http://h5-awall.dev.gomeplus.com/',
		),
		'wap' => array(
			'imageUrl' => 'https://i4.meixincdn.com/v1/img/T15aDTBTYg1RXrhCrK.jpg',
			'entryUrl' => 'http://m-awall.dev.gomeplus.com/',
		)
	),
);
*/
