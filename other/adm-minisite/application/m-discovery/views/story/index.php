<head>
	<title>发现</title>
	<link href="/resource/css/discovery/topic.css" rel="stylesheet">
	<script type="text/javascript">
		$CONFIG['slotId'] = <?php echo $slotIds['h5DiscoveryTopicId'];?>;
		$CONFIG['wapSlotId'] = <?php echo $slotIds['wapDiscoveryTopicId'];?>;
		$CONFIG['cardTitle'] = "<?php echo $webpage->getCardTitle(); ?>";
		$CONFIG['cardImage'] = "<?php echo $webpage->getCardImage(); ?>";
		$CONFIG['cardDescription'] = "<?php echo $webpage->getCardDescription(); ?>";
	</script>
	<script src="https://js.meixincdn.com/gvs-player/dist/vod/js/meixinplayer-min.js"></script>
</head>
<body class="opg">
	<script type="text/javascript">
	(function(win){var doc=win.document,html=doc.documentElement,option=html.getAttribute("data-use-rem");if(option===null){return}var baseWidth=parseInt(option).toString()==="NaN"?640:parseInt(option);var grids=baseWidth/100;var clientWidth=html.clientWidth||320;var fontsize=html.style.fontSize=clientWidth/grids+"px";fontsize=parseFloat(fontsize);var maxFontsize=Math.ceil(fontsize);var minFontsize=Math.floor(fontsize);var testDom=document.createElement("div");var testDomWidth=0;var adjustRatio=0;testDom.style.cssText="height:0;width:1rem;";doc.body.appendChild(testDom);var calcTestDom=function(){testDomWidth=testDom.offsetWidth;if(testDomWidth<minFontsize||testDomWidth>maxFontsize){adjustRatio=clientWidth/grids/testDomWidth;var reCalcRem=clientWidth*adjustRatio/grids;html.style.fontSize=reCalcRem+"px"}else{doc.body.removeChild(testDom)}};setTimeout(calcTestDom,20);var reCalc=function(){var newCW=html.clientWidth;if(newCW===clientWidth){return}clientWidth=newCW;html.style.fontSize=newCW*(adjustRatio?adjustRatio:1)/grids+"px"};if(!doc.addEventListener){return}var resizeEvt="orientationchange"in win?"orientationchange":"resize";win.addEventListener(resizeEvt,reCalc,false);doc.addEventListener("DOMContentLoaded",reCalc,false)})(window);
	</script>
	<section class="selectedDetail">
		<div class="selectedContent">
			<h2 class="detail-title"><?php echo $webpage->getTitle();?></h2>
			<ul class="share_tips" style="display:none">
				<li id="J_shareRebat" class="share left"></li>
				<li id="J_researchRebate" class="question left"></li>
				<li class="rebate-btn left"><em>?</em> 返利说明</li>
			</ul>
			<?php foreach($items as $item) {?>
				<?php if($item->getIsWords()) {?>
				<p class="tpc-para"><?php echo $item->getWords();?></p>
				<?php } elseif($item->getIsImage()) {?>
				<div class="tpc-img-box">
					<img src="<?php echo $item->getImage();?>" alt="图片" onerror="javascript:this.src='http://css.meixincdn.com/m/h5/dist/images/default_product.png'">
				</div>
				<?php } elseif($item->getIsVideo()) {?>
				<div class="video-box videoContainer" id="videoContainer_<?php echo $item->getVideoId();?>" style="width:100%;"></div>
				<script type="text/javascript">
					var v = new MeixinPlayer();
					var config = {
						autoplay: 0,
						autoSize: 1,
						env: 'dist',
						poster: '<?php echo $item->getImage();?>',
					}
					v.init(<?php echo $item->getVideoId();?>, 'videoContainer_<?php echo $item->getVideoId();?>', config);
				</script>
				<?php } elseif($item->getIsProduct()) {?>
				<a class="block-a item-price" href="<?php echo $item->getItemUrl(); ?>" data-id="<?php echo $item->getProductId(); ?>-<?php echo $item->getSkuId(); ?>">
					<figure class="tpc-good-zone">
						<img class="tpc-gd-img" src="<?php echo $item->getImage();?>" data-source="<?php echo $item->getImage(); ?>" alt="<?php echo $item->getName();?>">
						<figcaption class="tpc-fig">
							<h4 class="tpc-gd-title"><?php echo $item->getName();?></h4>
							<p class="tpc-price price_<?php echo $item->getSkuId();?>" style="color: #f66a6b"><span>¥ <?php echo $item->getPrice();?></span></p>
						</figcaption>
					</figure>
				</a>
				<?php } ?>
			<?php } ?>
			<div class="author_box">
				<img class="header_img" src="<?php echo $webpage->getPlatformAvatar();?>" alt="" onerror="this.onerror=null;this.src='http://js.gomein.net.cn/plus/plus-public/images/public/fail-face.png'">
				<p><?php echo $webpage->getPlatformNickname();?></p>
			</div>
		</div>
		<div class="bottom_tools">
			<div class="toolsItem" data-type="parise"><em class="tool_icon parise">赞</em></div>
			<div class="toolsItem" data-type="question" style="display:none;"><span class="toolsItem_span"><em class="tool_icon question">问卷</em></span></div>
			<div class="toolsItem" data-type="share"><span><em class="tool_icon share">分享</em></span></div>
		</div>
	</section>
</body>