package com.gomeplus.ams.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gomeplus.adm.biz.service.PriceService;
import com.gomeplus.adm.biz.util.TimeUtil;
import com.gomeplus.adm.biz.web.AjaxResponse;
import com.gomeplus.adportal.client.PrincipalUtil;
import com.gomeplus.ams.form.PriceForm;

/**
 * Created by suna01 on 2017/4/7
 */
@RestController
public class PriceController {
	private Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	private PriceService priceService;

	/**
	 * 获取价格数据
	 * @param advertisementId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@RequestMapping(value = "/api/prices", method = RequestMethod.GET)
	public AjaxResponse getPrices(@RequestParam("advertisementId") Integer advertisementId,
							@RequestParam("startTime") long startTime,
							@RequestParam("endTime") long endTime) {

		try {
			
			List<Map<String, Object>> prices = priceService.getPrices(advertisementId, startTime, endTime);
			return AjaxResponse.success(prices, "获取价格数据成功！");
		} catch (Exception e) {
			logger.error("获取价格数据失败！" + e);
			return AjaxResponse.error(500, "获取价格数据失败！" + e);
		}
	}

	/**
	 * 广告价格保存
	 * @param price
	 * @param result
	 * @param dates	  日历插件选择集合
	 * @param cpdPrice  CPD价格
	 * @param cpmPrice	CPM价格
	 * @param cpcPrice  CPC价格
	 * @return
	 */
	@RequestMapping(value = "/api/price", method = RequestMethod.POST)
	public AjaxResponse savePrice(@RequestBody PriceForm priceForm) {
		
		Integer advertisementId = priceForm.getAdvertisementId();
		List<String> dates = priceForm.getDates();
		
		BigDecimal cpdPrice = new BigDecimal(priceForm.getCpdPriceFloat());
		BigDecimal cpmPrice = new BigDecimal(priceForm.getCpmPriceFloat());
		
		if (!(cpdPrice.compareTo(BigDecimal.ZERO) > 0 || cpmPrice.compareTo(BigDecimal.ZERO) > 0 
			|| (cpdPrice.compareTo(BigDecimal.ZERO) > 0 && cpmPrice.compareTo(BigDecimal.ZERO) > 0))) {
			return AjaxResponse.error(400, "添加广告价格失败,请填写有效的CPD和CPM的价格!");
		}

		//选择时间不能为空
		if (CollectionUtils.isEmpty(dates)) {
			return AjaxResponse.error(400, "添加广告价格失败！请选择时间!");
		}

		List<Date> allDates = new ArrayList<Date>();
		for (String datesRange : dates) {
			if (datesRange.contains("~")) {
				if (dates.size() == 1 && (datesRange.split("~").length < 2 || StringUtils.isBlank(datesRange.split
						("~")[0]))) {
					return AjaxResponse.error(400, "添加广告价格失败！请选择时间!");
				}
				allDates.addAll(TimeUtil.getDays(datesRange.split("~")[0] + " ~ " + datesRange.split("~")[1]));
			} else {
				allDates.add(TimeUtil.stringToDate(datesRange));
			}
		}
		try {
			Integer accountId = PrincipalUtil.getAccountId();
			priceService.save(advertisementId, cpdPrice, cpmPrice, allDates, new ArrayList<String>(dates), accountId);
		} catch (Exception exception) {
			logger.error("add price failed [" + exception + "]");
			return AjaxResponse.error(500, "添加广告价格失败！" + exception);
		}
		return AjaxResponse.success("添加广告价格成功！");
	}
	
	/**
	 * 广告cpc价格保存
	 * @param price
	 * @param result
	 * @param cpcPrice	CPC价格
	 * @return
	 */
	@RequestMapping(value = "/api/price/cpc", method = RequestMethod.POST)
	public AjaxResponse saveCpdPrice(@RequestBody PriceForm priceForm) {

		Integer advertisementId = priceForm.getAdvertisementId();
		BigDecimal cpcPrice = (new BigDecimal(priceForm.getCpcPriceFloat()));
		
		if (!(cpcPrice.compareTo(BigDecimal.ZERO) > 0)) {
			return AjaxResponse.error(400, "添加广告价格失败,请填写有效的CPD价格!");
		}
		try {
			Integer accountId = PrincipalUtil.getAccountId();
			priceService.saveCpcPrice(advertisementId, cpcPrice, accountId);
		} catch (Exception exception) {
			logger.error("add price failed [" + exception + "]");
			return AjaxResponse.error(500, "添加广告CPD价格失败！" + exception);
		}
		return AjaxResponse.success("添加广告CPD价格成功！");
	}
}
