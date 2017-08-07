<!DOCTYPE html>
<html lang="zh-CN" data-use-rem=750>
<head>
	<meta charset="UTF-8">
	<meta name="renderer" content="webkit">
	<meta name="format-detection" content="telephone=no, email=no" />
	<meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no,minimum-scale=1,maximum-scale=1">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="author" content="国美互联网技术有限公司">
	<script type="text/javascript" src="https://js.gomein.net.cn/sitemonitor/wap.js"></script>
	<script>
	var $CONFIG = {
		domain: '<?php echo isset($globalConfigs["domain"]) ? $globalConfigs["domain"] : ""; ?>',
		mdomain: '<?php echo isset($globalConfigs["mdomain"]) ? $globalConfigs["mdomain"] : ""; ?>',

		adrequestUrl: '<?php echo isset($globalConfigs["adrequestUrl"]) ? $globalConfigs["adrequestUrl"] : ""; ?>',
		rebateBudgetUrl: '<?php echo isset($globalConfigs["rebateBudgetUrl"]) ? $globalConfigs["rebateBudgetUrl"] : ""; ?>',
		pariseUrl: '<?php echo isset($globalConfigs["pariseUrl"]) ? $globalConfigs["pariseUrl"] : ""; ?>',
		pariseInfoUrl: '<?php echo isset($globalConfigs["pariseInfoUrl"]) ? $globalConfigs["pariseInfoUrl"] : ""; ?>',
		shareCountUrl: '<?php echo isset($globalConfigs["shareCountUrl"]) ? $globalConfigs["shareCountUrl"] : ""; ?>',
		batchPraiseShareUrl: '<?php echo isset($globalConfigs["batchPraiseShareUrl"]) ? $globalConfigs["batchPraiseShareUrl"] : ""; ?>',
		shareRebateUrl: '<?php echo isset($globalConfigs["shareRebateUrl"]) ? $globalConfigs["shareRebateUrl"] : ""; ?>',
		videoShareUrl: '<?php echo isset($globalConfigs["videoShareUrl"]) ? $globalConfigs["videoShareUrl"] : ""; ?>',
		questionRebateUrl: '<?php echo isset($globalConfigs["questionRebateUrl"]) ? $globalConfigs["questionRebateUrl"] : ""; ?>',
		questionViewUrl: '<?php echo isset($globalConfigs["questionViewUrl"]) ? $globalConfigs["questionViewUrl"] : ""; ?>',
		middlePageImpressionUrl: '<?php echo isset($globalConfigs["middlePageImpressionUrl"]) ? $globalConfigs["middlePageImpressionUrl"] : ""; ?>',
		questionInvestAppearUrl: '<?php echo isset($globalConfigs["questionInvestAppearUrl"]) ? $globalConfigs["questionInvestAppearUrl"] : ""; ?>',
		setVideoCountUrl: '<?php echo isset($globalConfigs["setVideoCountUrl"]) ? $globalConfigs["setVideoCountUrl"] : ""; ?>',
		getVideoCountUrl: '<?php echo isset($globalConfigs["getVideoCountUrl"]) ? $globalConfigs["getVideoCountUrl"] : ""; ?>',
		env: '<?php echo isset($globalConfigs["env"]) ? $globalConfigs["env"] : "dist"; ?>',
		surveyUrl: '<?php echo isset($globalConfigs["surveyUrl"]) ? $globalConfigs["surveyUrl"] : ""; ?>',

		topicSlotId: <?php echo $slotIds['h5DiscoveryTopicId'];?>,
		wapTopicSlotId: <?php echo $slotIds['wapDiscoveryTopicId'];?>,
		shopSlotId: <?php echo $slotIds['h5DiscoveryShopId'];?>,
		wapShopSlotId: <?php echo $slotIds['wapDiscoveryShopId'];?>,
		itemSlotId: <?php echo $slotIds['h5DiscoveryItemId'];?>,
		wapItemSlotId: <?php echo $slotIds['wapDiscoveryItemId'];?>,
		videoSlotId: <?php echo $slotIds['h5DiscoveryVideoId'];?>,
		wapVideoSlotId: <?php echo $slotIds['wapDiscoveryVideoId'];?>,
		productSlotId: <?php echo $slotIds['h5DiscoveryProductId'];?>,
		wapProductSlotId: <?php echo $slotIds['wapDiscoveryProductId'];?>,
		adRequestApi: '<?php echo $globalConfigs["adrequestUrl"]?>',
		pariseShareBatch: '<?php echo $globalConfigs["batchPraiseShareUrl"]?>',
		parise: '<?php echo $globalConfigs["pariseUrl"]?>',
		discoveryShare: '<?php echo $globalConfigs["shareCountUrl"]?>',
		shareRebate: '<?php echo $globalConfigs["shareRebateUrl"]?>',
		videoSetCount: '<?php echo $globalConfigs["setVideoCountUrl"]?>',
		videoGetCount: '<?php echo $globalConfigs["getVideoCountUrl"]?>',
	};
	</script>
</head>
<div class="m-downapp" id='downapp'>
	<div class="app-box" style="position: absolute;">
		<a class="app-close" onclick="document.getElementById('downapp').style.display = 'none';">
			<em class="iconn-25"></em>
		</a>
		<img src="https://css.meixincdn.com/m/h5/dist/images/gome_icon.png" class="app-logo">
		<h2 class="app-tit">
			更多优惠尽在国美Plus
		</h2>
		<a href="http://a.app.qq.com/o/simple.jsp?pkgname=cn.com.gome.meixin" class="app-btn">戳我打开</a>
	</div>
</div>
<?php echo isset($content) ? $content : ''; ?>
<div class="common-share-tips"></div>
<div class="share-container" style="">
	<div class="share-main">
		<span class="main-text">分享<span class="rebateBudget" id="J_rebateBudget"></span></span>
	</div>
	<div class="share-text-info rebateBudget">
		<p>1.分享后，内容被打开才有机会获得返利</p>
		<p>2.一条内容每天最多可获得十次返利，一次分享每天最多可获得一次返利</p>
	</div>
	<div class="share-common" id="shareCon">
		<div class="share-item" data-type="weixin">
			<img class="share-icon" src="/resource/images/tencent_wechat.png">
			<p class="share-item-name">微信</p>
		</div>
		<div class="share-item" data-type="pengyouquan">
			<img class="share-icon" src="/resource/images/tencent_wechat_friend.png">
			<p class="share-item-name">朋友圈</p>
		</div>
		<div class="share-item" data-type="qq">
			<img class="share-icon" src="/resource/images/tencent_QQ.png">
			<p class="share-item-name">QQ</p>
		</div>
		<div class="share-item" data-type="qqzone">
			<img class="share-icon" src="/resource/images/tencent_qzone.png">
			<p class="share-item-name">QQ空间</p>
		</div>
	</div>
	<div class="share-cancel">
		<p>取消</p>
	</div>
</div>
<div class="rebate-rule">
	<div class="share-rebate">
		<p class="rebate-title">站内返利：</p>
		<p class="rebate-desc">当您在国美Plus App端通过Plus好友、Plus朋友圈渠道分享一条带有分享返利标识的活动内容时，该内容被打开时将能够得到返利*；同时，当被分享人再次通过Plus好友、Plus朋友圈渠道分享时，该内容被打开，您仍然能够获得一定比例的返利；</p>
		<p class="rebate-desc">注：</p>
		<p class="rebate-desc">Q：为什么站内分享后，好友打开后没有获得返利？</p>
		<p class="rebate-desc">A：打开内容时活动已截止；打开活动内容的用户，之前已被分享过同一活动内容或已经分享过该活动内容并获得返利。</p>
		<p class="rebate-title">站外分享：</p>
		<p class="rebate-desc">当您在国美Plus App端通过站外分享渠道（微博、微信好友，微信朋友圈、QQ好友、QQ空间等）分享一条带有分享赚标识的内容被打开时有机会得到一定金额的返利，一条内容每自然日内最多可获得一次返利。</p>
		<p class="rebate-desc">注：</p>
		<p class="rebate-desc">Q：为什么站外分享后，好友打开后没有获得返利？</p>
		<p class="rebate-desc">A：打开内容时活动已截止；此条内容在当前自然日已被其他好友打开过。</p>
	</div>
	<div class="survey-rebate">
		<p class="rebate-title">调研赚：</p>
		<p class="rebate-desc">当您在国美Plus App端探索频道完成一份调研问卷并提交时，有机会得到一定金额的返利，一份调研问卷最多可填写一次。</p>
		<p class="rebate-desc">注：</p>
		<p class="rebate-desc">Q：为什么提交问卷后，没有获得返利？</p>
		<p class="rebate-desc">A：提交问卷时活动已截止。</p>
	</div>
	<div class="video-rebate">
		<p class="rebate-title">视频赚：</p>
		<p class="rebate-desc">当您在国美Plus App端探索频道视频频道完整观看完一条带有视频赚标识的视频内容，有机会得到一定金额的返利。一条视频每自然日最多可获得一次返利。</p>
		<p class="rebate-desc">注：</p>
		<p class="rebate-desc">Q：为什么完整观看视频后，没有获得返利？</p>
		<p class="rebate-desc">A：完整观看视频结束时活动已截止。</p>
	</div>
</div>
<script src="/resource/scripts/discovery/zepto.min.js"></script>
<script src="/resource/scripts/discovery/touch.min.js"></script>
<script src="/resource/scripts/common/md5.min.js"></script>
<script src="/resource/scripts/modules/share/appInterface.js"></script>
<script src="/resource/scripts/discovery/crypto-js.min.js"></script>
<script src="/resource/scripts/discovery/common.js"></script>
</html>
