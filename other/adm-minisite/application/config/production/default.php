<?php
return array (
	'globalConfigs' => array (
		'adrequestUrl' => 'https://flight.gomeplus.com/flight',
		'adrequestIdUrl' => 'https://flight.gomeplus.com/rebate',
		'shareUrl' => 'https://shareserver.gomeplus.com/share',
		'shareImpressionUrl' => 'https://shareserver.gomeplus.com/shareImpression',
		'rebateBudgetUrl' => 'https://shareserver.gomeplus.com/rebateBudget',
		'middlePageImpression' => 'https://shareserver.gomeplus.com/middlePageImpression',

		'pcHost' => 'https://awall.gomeplus.com/',
		'wapHost' => 'https://m-awall.gomeplus.com/',
		'h5Host' => 'https://h5-awall.gomeplus.com/',

		'rebateUrl' => 'https://h5-awall.gomeplus.com/item/rebate',
		'protocolUrl' => 'https://h5-awall.gomeplus.com/item/protocol',

		'gomeItemHost' => 'https://item.m.gomeplus.com/',
		'gomeHost' => 'https://m.gomeplus.com/',

	),

	'configs' => array (
		'domain' => 'https://discovery.gomeplus.com/',
		'mdomain' => 'https://m-discovery.gomeplus.com/',

		// 广告请求接口（余招财）
		'adrequestUrl' => 'https://flight.gomeplus.com/flight',
		// 查询返利预算状态&&查询三个返利标签的价格接口（徒康正）
		'rebateBudgetUrl' => 'https://shareserver.gomeplus.com/rebateBudget',
		// 点赞/点赞状态接口（徒康正）
		'pariseUrl' => 'https://shareserver.gomeplus.com/parise',
		// 获取点赞状态接口（徒康正）
		'pariseInfoUrl' => 'https://shareserver.gomeplus.com/pariseInfo',
		// 分享数接口（徒康正）
		'shareCountUrl' => 'https://shareserver.gomeplus.com/discoveryShare',
		// 获取列表页点赞数/点赞状态、分享数接口（徒康正）
		'batchPraiseShareUrl' => 'https://shareserver.gomeplus.com/pariseShareBatch',
		// 分享返利接口（徒康正）
		'shareRebateUrl' => 'https://shareserver.gomeplus.com/shareRebate',
		// 视频播放返利接口（徒康正）
		'videoShareUrl' => 'https://shareserver.gomeplus.com/video',
		// 问卷调查返利/问卷调查数接口（徒康正）
		'questionRebateUrl' => 'https://shareserver.gomeplus.com/questionRebate',
		// 问卷调查浏览数接口（徒康正）
		'questionViewUrl' => 'https://shareserver.gomeplus.com/questionView',
		// 中间页曝光接口（徒康正）
		'middlePageImpressionUrl' => 'https://shareserver.gomeplus.com/middlePageImpression',
		// 根据调查问卷返利数目和日预算显示调查问卷入口接口（徒康正）
		'questionInvestAppearUrl' => 'https://shareserver.gomeplus.com/questionInvestAppear',

		// 问卷页面地址
		'surveyUrl' => 'https://m-discovery.gomeplus.com/survey/1.html',

		// 视频计数接口（熊仁海）
		'setVideoCountUrl' => 'https://count-v.gomeplus.com/set/video/',
		'getVideoCountUrl' => 'https://count-v.gomeplus.com/get/video/',

		//用户任务接口域名
		'missionDoMain' => 'http://10.125.192.169:8089/',
		//用户任务入口地址
		'missionUrl' => 'https://m-discovery.gomeplus.com/mission',
		// 视频播放环境
		'env' => 'dist',
	),

	'slotIds' => array (
		// 美媒--好东西（内嵌）
		'h5ItemSlotId' => 10040,
		// 美媒--好东西（wap）
		'wapItemSlotId' => 10041,
		// 美媒--有腔调（内嵌）
		'activeH5' => 10042,
		// 美媒--有腔调（wap）
		'activeWap' => 10043,

		// 探索频道--发现
		'h5DiscoveryTopicId' => 10070,
		'wapDiscoveryTopicId' => 10071,
		// 探索频道--好店
		'h5DiscoveryShopId' => 10064,
		'wapDiscoveryShopId' => 10065,
		// 探索频道--清单
		'h5DiscoveryItemId' => 10068,
		'wapDiscoveryItemId' => 10069,
		// 探索频道--视频
		'h5DiscoveryVideoId' => 10066,
		'wapDiscoveryVideoId' => 10067,
		// 探索频道--好物
		'h5DiscoveryProductId' => 10083,
		'wapDiscoveryProductId' => 10084,
	),

	'advertisementIds' => array (
		'h5ItemAdvertisementId' => 10038,
		'wapItemAdvertisementId' => 10039,
		'h5WebpageAdvertisementId' => 10040,
		'wapWebpageAdvertisementId' => 10041,
	),

	'wx' => array(
		'appid' => 'wx77edddc93a91176f',
		'gj_appid' => 'wx9f20920141def6e6',
		'app_secret' => 'a136798d345d6ad094e91b4679ecf8fa',
		'gj_app_secret' => 'da5a625b0c506ec9e436fff86ceda64d',
	),

	'entry' => array(
		'h5' => array(
			'imageUrl' => 'https://i8.meixincdn.com/v1/img/T1IPWTBCbv1R4cSCrK.png',
			'entryUrl' => 'https://h5-awall.gomeplus.com/',
		),
		'wap' => array(
			'imageUrl' => 'https://i8.meixincdn.com/v1/img/T1IPWTBCbv1R4cSCrK.png',
			'entryUrl' => 'https://m-awall.gomeplus.com/',
		)
	),
);
