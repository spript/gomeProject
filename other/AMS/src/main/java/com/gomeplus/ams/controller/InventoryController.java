package com.gomeplus.ams.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gomeplus.adm.biz.dto.ad.InventoryDTO;
import com.gomeplus.adm.biz.dto.ad.SlotDTO;
import com.gomeplus.adm.biz.service.InventoryService;
import com.gomeplus.adm.biz.service.SlotService;
import com.gomeplus.adm.biz.util.TimeUtil;
import com.gomeplus.adm.biz.web.AjaxResponse;
import com.gomeplus.adportal.client.PrincipalUtil;
import com.gomeplus.ams.form.InventoryForm;

/**
 * Created by suna01 on 2017/04/07
 */
@RestController
public class InventoryController {
	private Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	private SlotService slotService;
	@Autowired
	private InventoryService inventoryService;

	/**
	 * 添加库存  post请求
	 * @param result
	 * @param slotId
	 * @param dates
	 * @param cpdAmount
	 * @param cpmAmount
	 * @return
	 */
	@RequestMapping(value = "/api/inventory", method = RequestMethod.POST)
	public AjaxResponse save(@RequestBody InventoryForm inventoryForm) {
	
		Integer slotId = inventoryForm.getSlotId();
		List<String> dates = inventoryForm.getDates();
		Integer cpdAmount = inventoryForm.getCpdAmount();
		Integer cpmAmount = inventoryForm.getCpmAmount();
		Integer limitedCpdAmount = inventoryForm.getLimitedCpdAmount();
		
		if (!(cpdAmount.intValue() > 0 || cpmAmount.intValue() > 0
				|| (cpdAmount.intValue() > 0 && cpmAmount.intValue() > 0))) {
			return AjaxResponse.error(500, "添加库存失败,请填写有效的CPD和CPM的库存!");
		}

		//CPD数量不能超过广告位轮播数，当CPD数量==轮播数，CPM不能设置
		SlotDTO slot = slotService.getBySlotId(slotId);
		if (cpdAmount > slot.getRotators() || (cpdAmount.intValue() == slot.getRotators().intValue() && cpmAmount >
				0)) {
			return AjaxResponse.error(500, "库存不足!");
		}

		//选择时间不能为空
		if (CollectionUtils.isEmpty(dates)) {
			return AjaxResponse.error(500, "添加库存失败！请选择时间!");
		}
		// 运营cpd不能超过cpd数量
		if (limitedCpdAmount.intValue() < 0 ) {
			return AjaxResponse.error(500, "添加库存失败，请填写有效的运营CPD库存！");
		} else if (limitedCpdAmount.intValue() > cpdAmount.intValue()) {
			return AjaxResponse.error(500, "添加库存失败，运营CPD库存不能大于CPD库存");
		}
		List<Date> allDates = new ArrayList<Date>();
		for (String datesRange : dates) {
			if (datesRange.contains("~")) {
				if (dates.size() == 1 && (datesRange.split("~").length < 2 || StringUtils.isBlank(datesRange.split
						("~")[0]))) {
					return AjaxResponse.error(500, "添加库存失败！请选择时间!");
				}
				allDates.addAll(TimeUtil.getDays(datesRange.split("~")[0] + " ~ " + datesRange.split("~")[1]));
			} else {
				allDates.add(TimeUtil.stringToDate(datesRange));
			}
		}
		try {
			Integer accountId = PrincipalUtil.getAccountId();
			inventoryService.save(slotId, cpdAmount, limitedCpdAmount, cpmAmount, allDates, new ArrayList<String>(dates), accountId);
		} catch (Exception e) {
			logger.error("inventoryController.addInventory [" + e + "]");
			return AjaxResponse.error(500, "添加库存失败！" + e);
		}
		return AjaxResponse.success("添加库存成功！");
	}

	/**
	 * 获取库存数据
	 * @param slotId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@RequestMapping(value = "/api/inventories", method =RequestMethod.GET)
	public AjaxResponse getInventories(@RequestParam("slotId") Integer slotId,
										@RequestParam("startTime") long startTime,
										@RequestParam("endTime") long endTime) {

		try {
			
			List<InventoryDTO> inventories = inventoryService.getInventories(slotId, startTime, endTime);
			return AjaxResponse.success(inventories, "获取库存数据成功！");
		} catch (Exception e) {
			logger.error("获取库存数据失败" + e);
			return AjaxResponse.error(500, "获取库存数据失败！");
		}
	}
}
