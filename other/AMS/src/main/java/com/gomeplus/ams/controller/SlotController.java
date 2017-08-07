package com.gomeplus.ams.controller;

import com.google.gson.Gson;

import com.gomeplus.adm.biz.dto.ad.SlotDTO;
import com.gomeplus.adm.biz.model.ad.Slot;
import com.gomeplus.adm.biz.service.SlotService;
import com.gomeplus.adm.biz.web.AjaxResponse;
import com.gomeplus.adm.biz.web.Page;
import com.gomeplus.adm.biz.web.Pageable;
import com.gomeplus.adportal.client.PrincipalUtil;
import com.gomeplus.ams.form.SlotForm;

import org.apache.commons.collections.map.HashedMap;
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



@Controller
public class SlotController {

	@Autowired
	private SlotService slotService;

	private Gson gson = new Gson();

	private Logger logger = LogManager.getLogger(getClass());


	/**
	 * 获取单个广告位详情回显
	 *
	 * @param slotId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/slot", method = RequestMethod.GET)
	public AjaxResponse get(@RequestParam(required = false) Integer slotId) {

		if (ObjectUtils.isEmpty(slotId)) {
			return AjaxResponse.error(400, "广告位Id不能为空");
		}

		try {
			SlotDTO slotDTO = slotService.getBySlotId(slotId);

			return AjaxResponse.success(slotDTO, "获取单个广告位详情数据成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "获取单个广告位详情数据失败");
		}
	}


	/**
	 * 添加广告位
	 *
	 * @param slotForm
	 * @param bindingResult
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/slot", method = RequestMethod.POST)
	public AjaxResponse add(@Validated @RequestBody SlotForm slotForm, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return AjaxResponse.error(400, "广告位参数校验失败");
		}

		Slot slot = new Slot();
		BeanUtils.copyProperties(slotForm, slot);
		slot.setAccountId(PrincipalUtil.getAccountId());
		try {
			slotService.save(slot);
			return AjaxResponse.success(slot, "添加广告位成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "添加广告位失败");
		}
	}

	/**
	 * 修改单个广告位
	 *
	 * @param slotForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/slot", method = RequestMethod.PUT)
	public AjaxResponse edit(@RequestBody SlotForm slotForm) {


		Slot slot = new Slot();
		BeanUtils.copyProperties(slotForm, slot);

		try {
			slotService.update(slot);
			return AjaxResponse.success(slot, "修改广告位成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "修改广告位失败");
		}
	}

	/**
	 * 查询广告位状态进行是否满足删除条件的判断
	 *
	 * @param slotId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/slot/status", method = RequestMethod.GET)
	public AjaxResponse getStatus(@RequestParam(required = false) Integer slotId) {

		if (ObjectUtils.isEmpty(slotId)) {
			return AjaxResponse.error(400, "广告位ID不能为空");
		}
		Map<String, String> datas = new LinkedHashMap<>();
		try {
			SlotDTO slotDTO = slotService.getBySlotId(slotId);
			datas.put("status", slotDTO.getStatus().toString());
			return AjaxResponse.success(datas, "获取广告位状态成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "获取广告位状态失败");
		}
	}

	/**
	 * 删除单个广告位
	 *
	 * @param slotForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/slot", method = RequestMethod.DELETE)
	public AjaxResponse del(@RequestBody SlotForm slotForm) {

		if (ObjectUtils.isEmpty(slotForm.getSlotId())) {
			return AjaxResponse.error(500, "广告位参数校验失败");
		}

		Slot slot = new Slot();
		slot.setSlotId(slotForm.getSlotId());
		slot.setStatus(Slot.Status.DELETE.getValue());
		slot.setAccountId(PrincipalUtil.getAccountId());
		try {
			slotService.updateStatus(slot);
			return AjaxResponse.success(slot, "删除广告位成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "删除广告位失败");
		}
	}


	/**
	 * 判断广告位状态切换是否满足条件（售卖->非售卖/下线）
	 *
	 * @param slotId
	 * @param status
	 * @param effectiveTime
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/slot/onsale", method = RequestMethod.GET)
	public AjaxResponse getSaleToUnSaleResult(@RequestParam(required = false) Integer slotId,
											  @RequestParam(required = false) Integer status,
											  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO
													  .DATE) Date effectiveTime) {

		if (ObjectUtils.isEmpty(slotId) || ObjectUtils.isEmpty(status)) {
			return AjaxResponse.error(400, "广告位参数校验失败");
		}
		Map<String, Boolean> datas = new HashedMap();
		Slot slot = new Slot();
		slot.setSlotId(slotId);
		slot.setStatus(status);
		slot.setEffectiveTime(effectiveTime);
		try {
			boolean result = slotService.getSaleToUnSaleResult(slot);

			datas.put("result", result);
			return AjaxResponse.success(datas, "售卖->非售卖广告位状态切换条件判断成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "售卖->非售卖广告位状态切换条件判断失败");
		}
	}

	/**
	 * 修改广告位状态
	 *
	 * @param slotForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/slot/status", method = RequestMethod.PUT)
	public AjaxResponse editStatus(@RequestBody SlotForm slotForm) {

		if (ObjectUtils.isEmpty(slotForm.getSlotId()) || ObjectUtils.isEmpty(slotForm.getStatus())) {
			return AjaxResponse.error(400, "广告位参数校验失败");
		}

		Slot slot = new Slot();
		slot.setSlotId(slotForm.getSlotId());
		slot.setStatus(slotForm.getStatus());
		slot.setEffectiveTime(slotForm.getEffectiveTime());
		try {
			slotService.updateStatus(slot);
			return AjaxResponse.success(slot, "修改广告位状态成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "修改广告位状态失败");
		}
	}


	/**
	 * 根据查询条件获取广告位列表
	 *
	 * @param page
	 * @param number
	 * @param keyword
	 * @param publisherId
	 * @param status
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/slots", method = RequestMethod.GET)
	public AjaxResponse list(@RequestParam(required = false) Integer channelId,
							 @RequestParam(required = false) Integer publisherId,
							 @RequestParam(required = false, defaultValue = "1") Integer page,
							 @RequestParam(required = false, defaultValue = "30") Integer number,
							 @RequestParam(required = false, defaultValue = "") String keyword,
							 @RequestParam(required = false, defaultValue = "") Integer status) {

		Pageable pageable = new Pageable(page, number);

		try {
			int accountId = PrincipalUtil.getAccountId();
			Page<SlotDTO> slots = slotService.getByPageable(pageable, channelId, keyword, publisherId, accountId, status);
			return AjaxResponse.success(slots, "获取广告位列表成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "获取广告位列表失败");
		}
	}
}
