<?php
/**
 * 探索频道接口
 * @author baishen
 */
class Controller_Api extends Controller_Render {

	protected $_contentType = 'application/json';

	/**
	 * 自建活动页批量获取价格 jsonp 格式
	 */
	public function action_prices() {
		$proSkuIds = Arr::get($_GET, 'ids', 0);
		$areaCode = Arr::get($_GET, 'areaCode', 0);
		$callback = Arr::get($_GET, 'callback', "");

		$data = Business::factory('Gome_Item')->getPricesByProductIdAndSkuId($proSkuIds, $areaCode);

		// @todo 错误处理

		$this->success($data, "success", $callback);
	}

	/**
	 * 获取店铺信息
	 */
	public function action_shop() {

		$shopId = Arr::get($_GET, 'id', 0);
		$callback = Arr::get($_GET, 'callback', "");

		$shop = Business::factory('Gome_Shop')->getShopByShopId($shopId);

		$data = array (
			"shopId" => $shop->getShopId(),
			"name" => $shop->getName(),
			"icon" => $shop->getIcon(),
			"collectionQuantity" => $shop->getCollectionQuantity(),
		);

		// @todo 错误处理

		$this->success($data, "success", $callback);
	}

	/**
	 * 获取分享卡信息
	 */
	public function action_card() {
		$adId = Arr::get($_GET, 'adId', 0);
		$callback = Arr::get($_GET, 'callback', '');
		$material = Business::factory('Material')->getMaterialByAdId($adId);

		$cacheKey = "d_card_" . $adId;
		$data = Redisd::instance('awall')->get($cacheKey);

		if ($data !== FALSE) {
			$data = unserialize($data);
			$card = $data['card'];
		} else {
			try {
				$webpageId = $material->getWebpageId();
				$promotionId = $material->getPromotionId();
				if ($webpageId != 0) {
					$webpage = Business::factory('Webpage')->getWebpageByWebpageId($webpageId);

					$card = array(
						'cardTitle' => $webpage->getCardTitle(),
						'cardDescription' => $webpage->getCardDescription(),
						'cardImage' => $webpage->getCardImage(),
					);
				} else if (strlen($promotionId) == 24) { // 好物与话题均没有webpageid，暂时使用话题id长度限制
					$topicId = $material->getPromotionId();
					$topic = Business::factory('Bs_Topic')->getTopicByTopicId($topicId);

					$card = array(
						'cardTitle' => $topic->getName(),
						'cardDescription' => $material->getDescription(),
						'cardImage' => $material->getImage(),
					);
				} else {
					$card = array(
						'cardTitle' => $material->getTitle(),
						'cardDescription' => $material->getDescription(),
						'cardImage' => $material->getImage(),
					);
				}
				$data = Array(
					'card' => $card
				);
				$data = serialize($data);
				Redisd::instance('awall')->set($cacheKey, $data, 60);
			} catch (Business_Exception $e) {
				// @todo 404
				echo $e->getMessage();
				return;
			}
		}

		$this->success($card, "success", $callback);
	}

	/**
	 * 获取微信配置信息
	 */
	public function action_weixin() {
		$callBack = Arr::get($_GET, 'callback', 0);
		$shareUrl = urldecode(Arr::get($_GET, 'shareUrl', ''));
		$isGj = Arr::get($_GET, 'isGj', false);
		$wxConfig = Business::factory('WxBase')->getWeiXinConfigInfo($shareUrl, $isGj);

		$this->success($wxConfig, "success", $callBack);
	}

	/**
	 * 批量获取商品评论数 商品数最多10个
	 */
	public function action_comments() {
		$proSkuIds = Arr::get($_GET, 'ids', 0);
		$callback = Arr::get($_GET, 'callback', '');

		$data = Business::factory('Gome_Item')->getItemsByProductIdAndSkuId($proSkuIds);

		$this->success($data, "success", $callback);
	}
}
