<head>
	<title>视频</title>
	<link href="/resource/css/discovery/video.css" rel="stylesheet">
	<script type="text/javascript">
		$CONFIG['slotId'] = <?php echo $slotIds['h5DiscoveryVideoId'];?>;
		$CONFIG['wapSlotId'] = <?php echo $slotIds['wapDiscoveryVideoId'];?>;
		$CONFIG['videoId']= <?php echo $webpage->getVideoId();?>;
		$CONFIG['videoImage']= "<?php echo $webpage->getImage();?>";
		$CONFIG['cardTitle'] = "<?php echo $webpage->getCardTitle(); ?>";
		$CONFIG['cardImage'] = "<?php echo $webpage->getCardImage(); ?>";
		$CONFIG['cardDescription'] = "<?php echo $webpage->getCardDescription(); ?>";
	</script>
	<script type="text/javascript" src="https://js.meixincdn.com/gvs-player/dist/vod/js/meixinplayer-min.js"></script>
</head>
<body>
	<script type="text/javascript">
	(function(win){var doc=win.document,html=doc.documentElement,option=html.getAttribute("data-use-rem");if(option===null){return}var baseWidth=parseInt(option).toString()==="NaN"?640:parseInt(option);var grids=baseWidth/100;var clientWidth=html.clientWidth||320;var fontsize=html.style.fontSize=clientWidth/grids+"px";fontsize=parseFloat(fontsize);var maxFontsize=Math.ceil(fontsize);var minFontsize=Math.floor(fontsize);var testDom=document.createElement("div");var testDomWidth=0;var adjustRatio=0;testDom.style.cssText="height:0;width:1rem;";doc.body.appendChild(testDom);var calcTestDom=function(){testDomWidth=testDom.offsetWidth;if(testDomWidth<minFontsize||testDomWidth>maxFontsize){adjustRatio=clientWidth/grids/testDomWidth;var reCalcRem=clientWidth*adjustRatio/grids;html.style.fontSize=reCalcRem+"px"}else{doc.body.removeChild(testDom)}};setTimeout(calcTestDom,20);var reCalc=function(){var newCW=html.clientWidth;if(newCW===clientWidth){return}clientWidth=newCW;html.style.fontSize=newCW*(adjustRatio?adjustRatio:1)/grids+"px"};if(!doc.addEventListener){return}var resizeEvt="orientationchange"in win?"orientationchange":"resize";win.addEventListener(resizeEvt,reCalc,false);doc.addEventListener("DOMContentLoaded",reCalc,false)})(window);
	</script>
	<section class="videoDetail">
		<div class="video_header">
			<div class="video-box videoContainer" id="videoContainer" style="width:100%;">
			</div>
			<div class="video_content">
			<h2 class="detail-title"><?php echo $webpage->getTitle();?></h2>
				<ul class="share_tips" style="display:none">
					<li id="J_shareRebat" class="share left"></li>
					<li id="J_researchRebate" class="question left"></li>
					<li id="J_watchRebate" class="video left"></li>
					<li class="rebate-btn left"><em>?</em> 返利说明</li>
				</ul>
				<p><?php echo $webpage->getDescription();?></p>
			</div>
		</div>
		<?php if($webpage->getPromotionType() == 1) {?>
		<div class="video_templet_list">
			<div class="video_list">
				<ul>
				<?php foreach($items as $item) {?>
					<li class="item-price" data-id="<?php echo $item->getProductId(); ?>-<?php echo $item->getSkuId(); ?>">
						<a href="<?php echo $item->getItemUrl();?>">
							<img src="<?php echo $item->getImage();?>" alt="">
							<div class="shop_item_right">
								<h2><?php echo $item->getName();?></h2>
								<p><?php echo $item->getDescription();?></p>
								<span class="price_<?php echo $item->getSkuId();?>"></span>
							</div>
						</a>
					</li>
				<?php }?>
				</ul>
			</div>
			<div class="detail_footer">
				<p>价格以商品详情页为准</p>
			</div>
		</div>
		<?php } elseif($webpage->getPromotionType() == 2) {?>
		<div class="video_templet_shop">
			<?php foreach ($items as $item) {?>
			<div class="video_inner">
				<div class="shop_logo">
					<img src="<?php echo $item->getImage();?>" alt="">
				</div>
				<h2><?php echo $item->getName();?></h2>
				<p><span><?php echo $item->getDescription();?></span></p>
				<a href="<?php echo $item->getShopUrl();?>" class="goShop">进入店铺</a>
			</div>
			<?php }?>
		</div>
		<?php }?>
		<div class="video-login-box">
            <div class="login-box-text">想获得视频赚，请登录国美Plus</div>
            <div class="login-box-btn">登录</div>
        </div>
		<div class="bottom_tools">
			<div class="toolsItem" data-type="parise"><em class="tool_icon parise">赞</em></div>
			<div class="toolsItem" data-type="question" style="display:none;"><span class="toolsItem_span"><em class="tool_icon question">问卷</em></span></div>
			<div class="toolsItem" data-type="share"><span><em class="tool_icon share">分享</em></span></div>
		</div>
	</section>
	<div id="answered" class="none">
		<div class="content">
			<div class="top">
				<div class="top-finish">
					<p>您已参与过此次调研</p>
					<p>感谢您的参与</p>
				</div>
			</div>
			<a class="bottom">完成</a>
		</div>
	</div>
</body>
