<head>
	<title>好物</title>
	<link href="/resource/css/discovery/product.css" rel="stylesheet">
	<script type="text/javascript">
		$CONFIG['slotId'] = <?php echo $slotIds['h5DiscoveryProductId'];?>;
		$CONFIG['wapSlotId'] = <?php echo $slotIds['wapDiscoveryProductId'];?>;
		$CONFIG['cardTitle'] = "<?php echo $material->getTitle(); ?>";
		$CONFIG['cardImage'] = "<?php echo $material->getImage();?>";
		$CONFIG['cardDescription'] = "<?php echo $material->getDescription(); ?>";
	</script>
</head>
<body>
	<script>
	(function(win){var doc=win.document,html=doc.documentElement,option=html.getAttribute("data-use-rem");if(option===null){return}var baseWidth=parseInt(option).toString()==="NaN"?640:parseInt(option);var grids=baseWidth/100;var clientWidth=html.clientWidth||320;var fontsize=html.style.fontSize=clientWidth/grids+"px";fontsize=parseFloat(fontsize);var maxFontsize=Math.ceil(fontsize);var minFontsize=Math.floor(fontsize);var testDom=document.createElement("div");var testDomWidth=0;var adjustRatio=0;testDom.style.cssText="height:0;width:1rem;";doc.body.appendChild(testDom);var calcTestDom=function(){testDomWidth=testDom.offsetWidth;if(testDomWidth<minFontsize||testDomWidth>maxFontsize){adjustRatio=clientWidth/grids/testDomWidth;var reCalcRem=clientWidth*adjustRatio/grids;html.style.fontSize=reCalcRem+"px"}else{doc.body.removeChild(testDom)}};setTimeout(calcTestDom,20);var reCalc=function(){var newCW=html.clientWidth;if(newCW===clientWidth){return}clientWidth=newCW;html.style.fontSize=newCW*(adjustRatio?adjustRatio:1)/grids+"px"};if(!doc.addEventListener){return}var resizeEvt="orientationchange"in win?"orientationchange":"resize";win.addEventListener(resizeEvt,reCalc,false);doc.addEventListener("DOMContentLoaded",reCalc,false)})(window);
	</script>
	<section class="product-detail">
		<a href="<?php echo $item->getH5Url();?>">
			<div class="proudct-img">
				<img src="<?php echo $material->getImage();?>">
			</div>
			<div class="product-title clearfix">
				<h2><?php echo $material->getTitle(); ?></h2>
				<p class="product-price">￥<span><?php echo $item->getRealSalePrice(); ?></span></p>
			</div>
		</a>
		<ul class="share_tips" style="display:none">
			<li id="J_shareRebat" class="share left"></li>
			<li id="J_researchRebate" class="question left"></li>
			<li class="rebate-btn left"><em>?</em> 返利说明</li>
		</ul>
		<a href="<?php echo $item->getH5Url();?>">
			<p class="product-desc">
				<?php echo $material->getDescription(); ?>
			</p>
			<div class="product-count clearfix">
				<div class="collect clearfix">
					<img src="/resource/images/message.png">
					<span><?php echo $item->getCommentQuantity(); ?></span>
				</div>
			</div>
		</a>
	</section>
	<div class="product-others">
		<p class="title">你可能还喜欢</p>
		<ul class="product-others-list">
			<?php foreach ($material->getRelatedItems() as $relatedItem) { ?>
				<li class="list-item">
					<a href="<?php echo $relatedItem['h5Url'];?>">
						<img src="<?php echo $relatedItem['image'];?>">
						<p class="item-price price_<?php echo $relatedItem['itemId']; ?>" data-id="<?php echo $relatedItem['productId']; ?>-<?php echo $relatedItem['itemId']; ?>"><span></span></p>
					</a>
				</li>
			<?php } ?>
		</ul>
	</div>
	<div class="detail_footer">
		<p>价格以商品详情页为准</p>
	</div>
	<div class="bottom_tools">
		<div class="toolsItem" data-type="parise"><em class="tool_icon parise">赞</em></div>
		<div class="toolsItem" data-type="question" style="display:none;"><span class="toolsItem_span"><em class="tool_icon question">问卷</em></span></div>
		<div class="toolsItem" data-type="share"><span><em class="tool_icon share">分享</em></span></div>
	</div>
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
