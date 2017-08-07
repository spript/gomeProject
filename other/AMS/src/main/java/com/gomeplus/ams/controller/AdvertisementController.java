package com.gomeplus.ams.controller;

import com.gomeplus.adm.biz.dto.ad.AdvertisementDTO;
import com.gomeplus.adm.biz.model.ad.Advertisement;
import com.gomeplus.adm.biz.service.AdvertisementService;
import com.gomeplus.adm.biz.web.AjaxResponse;
import com.gomeplus.adm.biz.web.Page;
import com.gomeplus.adm.biz.web.Pageable;
import com.gomeplus.adportal.client.PrincipalUtil;
import com.gomeplus.ams.form.AdvertisementForm;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Created by liuchen on 2017/3/21.
 */
@Controller
public class AdvertisementController {

	@Autowired
	private AdvertisementService advertisementService;

	private Logger logger = LogManager.getLogger(getClass());


	/**
	 * 获取单个广告详情回显
	 *
	 * @param advertisementId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/advertisement", method = RequestMethod.GET)
	public AjaxResponse get(@RequestParam(required = false) Integer advertisementId) {

		if (ObjectUtils.isEmpty(advertisementId)) {
			return AjaxResponse.error(400, "广告Id不能为空");
		}

		try {
			AdvertisementDTO advertisementDTO = advertisementService.getByAdvertisementId(advertisementId);

			return AjaxResponse.success(advertisementDTO, "获取单个广告详情数据成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "获取单个广告详情数据失败");
		}
	}


	/**
	 * 添加广告
	 *
	 * @param advertisementForm
	 * @param bindingResult
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/advertisement", method = RequestMethod.POST)
	public AjaxResponse add(@Validated @RequestBody AdvertisementForm advertisementForm, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return AjaxResponse.error(400, "广告参数校验失败");
		}


		Advertisement advertisement = new Advertisement();
		BeanUtils.copyProperties(advertisementForm, advertisement);
		advertisement.setAccountId(PrincipalUtil.getAccountId());
		try {
			advertisementService.save(advertisement);
			return AjaxResponse.success(advertisement, "添加广告成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "添加广告失败");
		}
	}

	/**
	 * 修改单个广告
	 *
	 * @param advertisementForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/advertisement", method = RequestMethod.PUT)
	public AjaxResponse edit(@RequestBody AdvertisementForm advertisementForm) {

		// 广告名称：原数据回显；必填 && 描述：原数据回显；必填；最大长度为200个字符；
		if (ObjectUtils.isEmpty(advertisementForm.getAdvertisementId()) || StringUtils.isEmpty(advertisementForm
				.getName())) {
			return AjaxResponse.error(400, "广告参数校验失败");
		}

		Advertisement advertisement = new Advertisement();
		BeanUtils.copyProperties(advertisementForm, advertisement);

		try {
			advertisementService.update(advertisement);
			return AjaxResponse.success(advertisement, "修改广告成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "修改广告失败");
		}
	}


	/**
	 * 查询广告状态进行是否满足删除条件的判断
	 *
	 * @param advertisementId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/advertisement/status", method = RequestMethod.GET)
	public AjaxResponse getStatus(@RequestParam(required = false) Integer advertisementId) {

		if (ObjectUtils.isEmpty(advertisementId)) {
			return AjaxResponse.error(400, "广告ID不能为空");
		}
		Map<String, String> datas = new LinkedHashMap<>();
		try {
			AdvertisementDTO advertisement = advertisementService.getByAdvertisementId(advertisementId);
			datas.put("status", advertisement.getStatus().toString());
			return AjaxResponse.success(datas, "获取广告状态成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "获取广告状态失败");
		}
	}

	/**
	 * 删除单个广告
	 *
	 * @param advertisementForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/advertisement", method = RequestMethod.DELETE)
	public AjaxResponse del(@RequestBody AdvertisementForm advertisementForm) {

		if (ObjectUtils.isEmpty(advertisementForm.getAdvertisementId())) {
			return AjaxResponse.error(400, "广告参数校验失败");
		}

		Advertisement advertisement = new Advertisement();
		advertisement.setAdvertisementId(advertisementForm.getAdvertisementId());
		advertisement.setStatus(Advertisement.Status.DELETE.getValue());
		advertisement.setAccountId(PrincipalUtil.getAccountId());
		try {
			advertisementService.updateStatus(advertisement);
			return AjaxResponse.success(advertisement, "删除广告成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "删除广告失败");
		}
	}

	/**
	 * 修改广告状态
	 *
	 * @param advertisementForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/advertisement/status", method = RequestMethod.PUT)
	public AjaxResponse editStatus(@RequestBody AdvertisementForm advertisementForm) {

		if (ObjectUtils.isEmpty(advertisementForm.getAdvertisementId()) || ObjectUtils.isEmpty(advertisementForm
				.getStatus())) {
			return AjaxResponse.error(400, "广告参数校验失败");
		}

		Advertisement advertisement = new Advertisement();
		advertisement.setAdvertisementId(advertisementForm.getAdvertisementId());
		advertisement.setStatus(advertisementForm.getStatus());
		advertisement.setEffectiveTime(advertisementForm.getEffectiveTime());
		advertisement.setAccountId(PrincipalUtil.getAccountId());
		try {
			advertisementService.updateStatus(advertisement);
			return AjaxResponse.success(advertisement, "修改广告状态成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "修改广告状态失败");
		}
	}


	/**
	 * 判断广告是否满足下线条件
	 *
	 * @param advertisementId
	 * @param status
	 * @param effectiveTime
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/advertisement/checkOfflineCondition", method = RequestMethod.GET)
	public AjaxResponse checkOfflineCondition(@RequestParam(required = false) Integer advertisementId,
											 @RequestParam(required = false) Integer status,
											 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO
													 .DATE) Date effectiveTime) {

		if (ObjectUtils.isEmpty(advertisementId) || ObjectUtils.isEmpty(status)) {
			return AjaxResponse.error(400, "广告位参数校验失败");
		}
		Map<String, Boolean> datas = new HashedMap();
		Advertisement advertisement = new Advertisement();
		advertisement.setAdvertisementId(advertisementId);
		advertisement.setStatus(status);
		advertisement.setEffectiveTime(effectiveTime);
		try {
			boolean result = advertisementService.checkOfflineCondition(advertisement);

			datas.put("result", result);
			return AjaxResponse.success(datas, "判断广告是否满足下线条件成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "判断广告是否满足下线条件失败");
		}
	}


	/**
	 * 根据查询条件获取广告列表
	 *
	 * @param page
	 * @param number
	 * @param keyword
	 * @param channelId
	 * @param status
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/advertisements", method = RequestMethod.GET)
	public AjaxResponse list(@RequestParam(required = false) Integer channelId,
							 @RequestParam(required = false, defaultValue = "1") Integer page,
							 @RequestParam(required = false, defaultValue = "30") Integer number,
							 @RequestParam(required = false, defaultValue = "") String keyword,
							 @RequestParam(required = false, defaultValue = "") Integer slotId,
							 @RequestParam(required = false, defaultValue = "") Integer saleMode,
							 @RequestParam(required = false, defaultValue = "") Integer status) {

		Pageable pageable = new Pageable(page, number);

		try {
			int accountId = PrincipalUtil.getAccountId();
			Page<AdvertisementDTO> advertisements = advertisementService.getByPageable(pageable, keyword, slotId, saleMode,
					accountId, status);
			return AjaxResponse.success(advertisements, "获取广告列表成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "获取广告列表失败");
		}
	}
}
