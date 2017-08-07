<?php

class Model_Material extends Model {

	public function getTitle() {
		return HTML::chars(urldecode($this->title));
	}

	public function getDescription() {
		return HTML::chars(urldecode($this->description));
	}

	public function getRelatedItems() {
		// 相关商品取3的倍数，确保对齐
		$relatedItems = json_decode($this->related_items, true);

		$count = count($relatedItems); 
		$divisor = floor($count / 3);
		$remainder = $count % 3;
		//if ($divisor <= 9 && $remainder == 0) {
		//	return $relatedItems;
		//}
		$length = ($divisor * 3 < 9) ? $divisor * 3 : 9;

		$relatedItems = array_slice($relatedItems, 0, $length);
		$newRelatedItems = array();
		// 国美Plus商品
		if (is_numeric($this->getPromotionId()) && $this->getPromotionId() > 0) {
			foreach ($relatedItems as $relatedItem) {
				$relatedItem['h5Url'] = "gomeplus://product/detail?itemId=" . intval($relatedItem['itemId']) . "&shopId=" . intval($relatedItem['shopId']);
				$newRelatedItems[] = $relatedItem;
			}
		// 国美在线商品
		} else {
			$gomeItemHost = Kohana::$config->load('default.globalConfigs.gomeItemHost');
			foreach ($relatedItems as $relatedItem) {
				if (isset($relatedItem['productId'])) {
					$itemUrl = $gomeItemHost . 'p-' . $relatedItem['productId'] . '-' . $relatedItem['itemId'] . '.html';
				} else {
					$itemUrl = $gomeItemHost . 'p-' . '-' . $relatedItem['itemId'] . '.html';
				}

				$relatedItem['h5Url'] = $itemUrl;
				$relatedItem['url'] = $itemUrl;
				$newRelatedItems[] = $relatedItem;
			}

		}
		return $newRelatedItems;
	}
}
