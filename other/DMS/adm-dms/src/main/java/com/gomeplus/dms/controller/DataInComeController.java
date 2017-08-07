package com.gomeplus.dms.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gomeplus.adm.biz.dto.ad.DataAdvertisementIncomeReportDTO;
import com.gomeplus.adm.biz.dto.ad.DataRebateIncomeReportDTO;
import com.gomeplus.adm.biz.service.DataIncomeService;
import com.gomeplus.adm.biz.web.AjaxResponse;
import com.gomeplus.adm.biz.web.Page;
import com.gomeplus.adm.biz.web.Pageable;

/**
 * 收入Controller
 * @author xiaogengen
 *
 */
@Controller
public class DataInComeController {
	
	private static Logger logger = LogManager.getLogger(DataAdvertiserController.class);
	
	@Autowired
	private DataIncomeService dataIncomeService;
	/**
	 * 广告收入
	 * @param startTime
	 * @param endTime
	 * @param mediaName
	 * @param deviceType
	 * @param advertisementType
	 * @param sellType
	 * @param page
	 * @param number
	 * @return
	 */
	@RequiresPermissions("report:income:anice")
	@RequestMapping(value = "/api/report/anice/income", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResponse getDataInCome(
			@RequestParam(name = "startTime", required = false) Long startTime,
			@RequestParam(name = "endTime", required = false) Long endTime,
			@RequestParam(name = "mediaName", required = false) String mediaName,
			@RequestParam(name = "deviceType", required = false) String deviceType,
			@RequestParam(name = "advertisementType", required = false) String advertisementType,
			@RequestParam(name = "sellType", required = false) String sellType,
			@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(name = "number", required = false, defaultValue = "20") Integer number) {
		Pageable pageable = new Pageable(page, number);
		Page<DataAdvertisementIncomeReportDTO> pageInfo = null;
		if (sellType != null && sellType.equals("-1")) {
			sellType = null;
		}
		if (deviceType != null && deviceType.equals("0")) {
			deviceType = null;
		}
		try {
			 pageInfo = dataIncomeService.getAdvertisementIncomeData(startTime, endTime, mediaName, 
					 deviceType, advertisementType, sellType, pageable);
		} catch(Exception e) {
			logger.error("广告收入报表查询错误：", e);
			return AjaxResponse.error(400, "广告收入报表查询错误" + e.getMessage());
		}
		
		return AjaxResponse.success(pageInfo, "查询成功");
	}
	/**
	 * 返利收入
	 * @param startTime
	 * @param endTime
	 * @param deviceType
	 * @param advertisementType
	 * @param page
	 * @param number
	 * @return
	 */
	@RequiresPermissions("report:income:rebate")
	@RequestMapping(value = "/api/report/anice/rebate", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResponse getDataRebateInCome(
			@RequestParam(name = "startTime", required = false) Long startTime,
			@RequestParam(name = "endTime", required = false) Long endTime,
			@RequestParam(name = "deviceType", required = false) String deviceType,
			@RequestParam(name = "advertisementType", required = false) String advertisementType,
			@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(name = "number", required = false, defaultValue = "20") Integer number) {
		Pageable pageable = new Pageable(page, number);
		Page<DataRebateIncomeReportDTO> pageInfo = null;
		if (deviceType != null && deviceType.equals("0")) {
			deviceType = null;
		}
		try {
			 pageInfo = dataIncomeService.getRebateIncomeData(startTime, endTime, 
					 deviceType, advertisementType, pageable);
		} catch(Exception e) {
			logger.error("返利收入报表查询错误：", e);
			return AjaxResponse.error(400, "返利收入报表查询错误" + e.getMessage());
		}
		
		return AjaxResponse.success(pageInfo, "查询成功");
	}
	/**
	 * 导出广告报表
	 * @param startTime
	 * @param endTime
	 * @param mediaName
	 * @param deviceType
	 * @param advertisementType
	 * @param sellType
	 * @param page
	 * @param number
	 * @param response
	 * @return
	 */
	@RequiresPermissions("report:income:anice:export")
	@RequestMapping(value = "/api/anice/income", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResponse exportAdvertiseInCome(
			@RequestParam(name = "startTime", required = false) Long startTime,
			@RequestParam(name = "endTime", required = false) Long endTime,
			@RequestParam(name = "mediaName", required = false) String mediaName,
			@RequestParam(name = "deviceType", required = false) String deviceType,
			@RequestParam(name = "advertisementType", required = false) String advertisementType,
			@RequestParam(name = "sellType", required = false) String sellType,
			@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(name = "pageEnd", required = false, defaultValue = "1") Integer pageEnd,
			@RequestParam(name = "number", required = false, defaultValue = "20") Integer number,
			HttpServletResponse response) {
		Pageable pageable = null;
		Page<DataAdvertisementIncomeReportDTO> pageInfo = null;
		List<DataAdvertisementIncomeReportDTO> list = new ArrayList<>();
		if (sellType != null && sellType.equals("-1")) {
			sellType = null;
		}
		if (deviceType != null && deviceType.equals("0")) {
			deviceType = null;
		}
		try {
			for (int i = page; i <= pageEnd; i++) {
				pageable = new Pageable(i, number);
				pageInfo = dataIncomeService.getAdvertisementIncomeData(startTime, endTime, mediaName, 
						 deviceType, advertisementType, sellType, pageable);
				dataIncomeService.getCombineAdvertiser(pageInfo.getList(), list);
			}
			
			 dataIncomeService.exportAdvertisementIncome(list, response);
		} catch(Exception e) {
			logger.error("广告收入报表导出错误：", e);
			return AjaxResponse.error(400, "广告收入报表导出错误" + e.getMessage());
		}
		
		return AjaxResponse.success("导出成功");
	}
	/**
	 * 导出返利收入
	 * @param startTime
	 * @param endTime
	 * @param mediaName
	 * @param deviceType
	 * @param advertisementType
	 * @param sellType
	 * @param page
	 * @param number
	 * @param response
	 * @return
	 */
	@RequiresPermissions("report:income:rebate:export")
	@RequestMapping(value = "/api/rebate/income", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResponse exportRebateInCome(
			@RequestParam(name = "startTime", required = false) Long startTime,
			@RequestParam(name = "endTime", required = false) Long endTime,
			@RequestParam(name = "deviceType", required = false) String deviceType,
			@RequestParam(name = "advertisementType", required = false) String advertisementType,
			@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(name = "pageEnd", required = false, defaultValue = "1") Integer pageEnd,
			@RequestParam(name = "number", required = false, defaultValue = "20") Integer number,
			HttpServletResponse response) {
		Pageable pageable = null;
		Page<DataRebateIncomeReportDTO> pageInfo = null;
		List<DataRebateIncomeReportDTO> list = new ArrayList<>();
		if (deviceType != null && deviceType.equals("0")) {
			deviceType = null;
		}
		try {
			for (int i = page; i <= pageEnd; i++) {
				pageable = new Pageable(i, number);
				 pageInfo = dataIncomeService.getRebateIncomeData(startTime, endTime, 
						 deviceType, advertisementType, pageable);
				 dataIncomeService.getCombineRebate(pageInfo.getList(), list);
			}
			
			 dataIncomeService.exportRebateIncome(list, response);
		} catch(Exception e) {
			logger.error("返利收入报表导出错误：", e);
			return AjaxResponse.error(400, "返利收入报表导出错误" + e.getMessage());
		}
		
		return AjaxResponse.success("导出成功");
	}
}
