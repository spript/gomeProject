<head>
	<title>清单</title>
	<link href="/resource/css/discovery/item.css" rel="stylesheet">
	<script type="text/javascript">
		$CONFIG['slotId'] = <?php echo $slotIds['h5DiscoveryItemId'];?>;
		$CONFIG['wapSlotId'] = <?php echo $slotIds['wapDiscoveryItemId'];?>;
		$CONFIG['cardTitle'] = "<?php echo $webpage->getCardTitle(); ?>";
		$CONFIG['cardImage'] = "<?php echo $webpage->getCardImage(); ?>";
		$CONFIG['cardDescription'] = "<?php echo $webpage->getCardDescription(); ?>";
	</script>
</head>
<body>
	<script>
	(function(win){var doc=win.document,html=doc.documentElement,option=html.getAttribute("data-use-rem");if(option===null){return}var baseWidth=parseInt(option).toString()==="NaN"?640:parseInt(option);var grids=baseWidth/100;var clientWidth=html.clientWidth||320;var fontsize=html.style.fontSize=clientWidth/grids+"px";fontsize=parseFloat(fontsize);var maxFontsize=Math.ceil(fontsize);var minFontsize=Math.floor(fontsize);var testDom=document.createElement("div");var testDomWidth=0;var adjustRatio=0;testDom.style.cssText="height:0;width:1rem;";doc.body.appendChild(testDom);var calcTestDom=function(){testDomWidth=testDom.offsetWidth;if(testDomWidth<minFontsize||testDomWidth>maxFontsize){adjustRatio=clientWidth/grids/testDomWidth;var reCalcRem=clientWidth*adjustRatio/grids;html.style.fontSize=reCalcRem+"px"}else{doc.body.removeChild(testDom)}};setTimeout(calcTestDom,20);var reCalc=function(){var newCW=html.clientWidth;if(newCW===clientWidth){return}clientWidth=newCW;html.style.fontSize=newCW*(adjustRatio?adjustRatio:1)/grids+"px"};if(!doc.addEventListener){return}var resizeEvt="orientationchange"in win?"orientationchange":"resize";win.addEventListener(resizeEvt,reCalc,false);doc.addEventListener("DOMContentLoaded",reCalc,false)})(window);
	</script>
	<section class="itemDetail">
		<div class="header_box">
			<img class="header_img" src="<?php echo $webpage->getImage();?>" alt="">
		</div>
		<div class="detail_content">
			<div class="content_header">
				<h2 class="detail-title"><?php echo $webpage->getTitle();?></h2>
				<ul class="share_tips" style="display:none">
					<li id="J_shareRebat" class="share left"></li>
					<li id="J_researchRebate" class="question left"></li>
					<li class="rebate-btn left"><em>?</em> 返利说明</li>
				</ul>
				<p class="p_box"><?php echo $webpage->getDescription();?></p>
			</div>
			<div class="content_main">
				<ul>
				<?php foreach($items as $item) {?>
					<li class="item-content item-price" data-id="<?php echo $item->getProductId(); ?>-<?php echo $item->getSkuId(); ?>">
					<a href="<?php echo $item->getItemUrl();?>">
						<img src="<?php echo $item->getImage();?>" alt="">
						<h2><?php echo $item->getDescription();?></h2>
						<p class="price_<?php echo $item->getSkuId();?>"></p>
					</a>
					</li>
				<?php } ?>
				</ul>
			</div>
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
