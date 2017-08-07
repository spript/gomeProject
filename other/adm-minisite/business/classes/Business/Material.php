<?php
/**
 * 素材信息业务逻辑层
 *
 * @author baishen
 */
class Business_Material extends Business {

	/**
	 * 根据素材id获取素材
	 *
	 * @param materialId
	 */
	public function getMaterialByMaterialId($materialId) {
		if (!Valid::numeric($materialId)) {
			throw new Business_Exception('素材id参数错误！');
		}
		$material = Dao::factory("Material")->getMaterialByMaterialId($materialId)->current();
		if ($material == NULL) {
			throw new Business_Exception('素材不存在！');
		}
		return $material;
	}

	/**
	 * 根据adId获取素材
	 *
	 * @param adId
	 */
	public function getMaterialByAdId($adId) {
		if (!Valid::numeric($adId)) {
			throw new Business_Exception('adId参数错误！');
		}
		$material = Dao::factory("Material")->getMaterialByAdId($adId)->current();
		if ($material == NULL) {
			throw new Business_Exception('素材不存在！');
		}
		return $material;
	}
}
