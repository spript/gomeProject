<head>
	<title>好店</title>
	<link href="/resource/css/discovery/shop.css" rel="stylesheet">
	<script type="text/javascript">
		$CONFIG['slotId'] = <?php echo $slotIds['h5DiscoveryShopId'];?>;
		$CONFIG['wapSlotId'] = <?php echo $slotIds['wapDiscoveryShopId'];?>;
		$CONFIG['shopId']= <?php echo $webpage->getShopId();?>;
		$CONFIG['cardTitle'] = "<?php echo $webpage->getCardTitle(); ?>";
		$CONFIG['cardImage'] = "<?php echo $webpage->getCardImage(); ?>";
		$CONFIG['cardDescription'] = "<?php echo $webpage->getCardDescription(); ?>";
	</script>
</head>
<body>
	<script type="text/javascript">
	(function(win){var doc=win.document,html=doc.documentElement,option=html.getAttribute("data-use-rem");if(option===null){return}var baseWidth=parseInt(option).toString()==="NaN"?640:parseInt(option);var grids=baseWidth/100;var clientWidth=html.clientWidth||320;var fontsize=html.style.fontSize=clientWidth/grids+"px";fontsize=parseFloat(fontsize);var maxFontsize=Math.ceil(fontsize);var minFontsize=Math.floor(fontsize);var testDom=document.createElement("div");var testDomWidth=0;var adjustRatio=0;testDom.style.cssText="height:0;width:1rem;";doc.body.appendChild(testDom);var calcTestDom=function(){testDomWidth=testDom.offsetWidth;if(testDomWidth<minFontsize||testDomWidth>maxFontsize){adjustRatio=clientWidth/grids/testDomWidth;var reCalcRem=clientWidth*adjustRatio/grids;html.style.fontSize=reCalcRem+"px"}else{doc.body.removeChild(testDom)}};setTimeout(calcTestDom,20);var reCalc=function(){var newCW=html.clientWidth;if(newCW===clientWidth){return}clientWidth=newCW;html.style.fontSize=newCW*(adjustRatio?adjustRatio:1)/grids+"px"};if(!doc.addEventListener){return}var resizeEvt="orientationchange"in win?"orientationchange":"resize";win.addEventListener(resizeEvt,reCalc,false);doc.addEventListener("DOMContentLoaded",reCalc,false)})(window);
	</script>
	<section class="shopDetail">
		<div class="shop_header">
			<div class="shop_inner">
				<div class="shop_logo">
					<img class="shop_icon" src="<?php echo $webpage->getImage();?>" alt="">
				</div>
				<h2><?php echo $webpage->getTitle();?></h2>
				<ul class="share_tips clear-font-size" style="display:none">
					<li id="J_shareRebat" class="share inline"></li>
					<li id="J_researchRebate" class="question inline"></li>
					<li class="rebate-btn inline"><em>?</em> 返利说明</li>
				</ul>
				<p>关注数：<span id="J_shopCollection_<?php echo $webpage->getShopId();?>"></span></p>
				<a href="<?php echo $webpage->getShopUrl();?>">进入店铺</a>
			</div>
		</div>
		<div class="shop_content">
			<div class="shop_info">
				<p><?php echo $webpage->getDescription();?></p>
			</div>
			<?php foreach($items as $item) {?>
				<img src="<?php echo $item->getImage();?>" alt="">
				<h2 class="item-price" data-id="<?php echo $item->getProductId(); ?>-<?php echo $item->getSkuId(); ?>"><?php echo $item->getName();?></h2>
				<a href="<?php echo $item->getItemUrl();?>" class="info_btn"><span class="price_<?php echo $item->getSkuId();?>"></span>/ 查看详情</a>
				<p><?php echo $item->getDescription();?></p>
			<?php } ?>
		</div>
		<div class="detail_footer">
			<p>价格以商品详情页为准</p>
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
