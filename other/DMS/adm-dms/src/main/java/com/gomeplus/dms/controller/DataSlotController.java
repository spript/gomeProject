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

import com.gomeplus.adm.biz.dto.ad.DataSlotReportDTO;
import com.gomeplus.adm.biz.service.DataAdvertiserService;
import com.gomeplus.adm.biz.web.AjaxResponse;
import com.gomeplus.adm.biz.web.Page;
import com.gomeplus.adm.biz.web.Pageable;

/**
 * 广告位Controller
 * @author xiaogengen
 *
 */
@Controller
public class DataSlotController {
	
	private static Logger logger = LogManager.getLogger(DataSlotController.class);
	
	@Autowired
	private DataAdvertiserService dataAdvertiserService;
	/**
	 * 广告位Controller
	 * @param startTime
	 * @param endTime
	 * @param advertiserId
	 * @param advertiserName
	 * @param mediaName
	 * @param deviceType
	 * @param sellType
	 * @param page
	 * @param number
	 * @return
	 */
	@RequiresPermissions("report:slot")
	@RequestMapping(value = "/api/report/anicer/slot", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResponse getDataAbsense(
			@RequestParam(name = "startTime", required = false) Long startTime,
			@RequestParam(name = "endTime", required = false) Long endTime,
			@RequestParam(name = "optionType", required = false) Integer optionType,
			@RequestParam(name = "optionTypeText", required = false) String optionTypeText,
			@RequestParam(name = "mediaName", required = false) String mediaName,
			@RequestParam(name = "deviceType", required = false) String deviceType,
			@RequestParam(name = "sellType", required = false) Integer sellType,
			@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(name = "number", required = false, defaultValue = "20") Integer number) {
		Pageable pageable = new Pageable(page, number);
		Page<DataSlotReportDTO> pageInfo = null;
		String advertiserIds = null;
		String advertiserName = null;
		if (optionType != null) {
			switch(optionType){
				case 0: advertiserIds = optionTypeText; break;
				case 1: advertiserName = optionTypeText; break;
			}
		}
		logger.info("slotIds" + advertiserIds);
		if (deviceType != null && deviceType.equals("0")) {
			deviceType = null;
		}
		if (sellType != null && sellType == -1) {
			sellType = null;
		}
		try {
			 pageInfo = dataAdvertiserService.getSlotData(startTime, endTime, advertiserIds, 
					 advertiserName, mediaName, deviceType, sellType, pageable);;
			logger.info(pageInfo.getList().size());
		} catch(Exception e) {
			logger.error("广告位报表查询错误：", e);
			return AjaxResponse.error(400, "广告位报表查询错误" + e.getMessage());
		}
		return AjaxResponse.success(pageInfo, "查询成功");
	}
	
	
	/**
	 * 导出广告位报表
	 * @param startTime
	 * @param endTime
	 * @param advertiserId
	 * @param advertiserName
	 * @param mediaName
	 * @param deviceType
	 * @param sellType
	 * @param page
	 * @param number
	 * @return
	 */
	@RequiresPermissions("report:slot:export")
	@RequestMapping(value = "/api/report/slot", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResponse exportDataAbsense(
			@RequestParam(name = "startTime", required = false) Long startTime,
			@RequestParam(name = "endTime", required = false) Long endTime,
			@RequestParam(name = "optionType", required = false) Integer optionType,
			@RequestParam(name = "optionTypeText", required = false) String optionTypeText,
			@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "mediaName", required = false) String mediaName,
			@RequestParam(name = "deviceType", required = false) String deviceType,
			@RequestParam(name = "sellType", required = false) Integer sellType,
			@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(name = "pageEnd", required = false, defaultValue = "1") Integer pageEnd,
			@RequestParam(name = "number", required = false, defaultValue = "20") Integer number,
			HttpServletResponse response) {
		Pageable pageable = null;
		Page<DataSlotReportDTO> pageInfo = null;
		List<DataSlotReportDTO> list = new ArrayList<>();
		String advertiserIds = null;
		String advertiserName = null;
		if (optionType != null) {
			switch(optionType){
				case 0: advertiserIds = optionTypeText; break;
				case 1: advertiserName = optionTypeText; break;
			}
		}
		if (deviceType != null && deviceType.equals("0")) {
			deviceType = null;
		}
		if (sellType != null && sellType == -1) {
			sellType = null;
		}
		logger.info("结束页：" + pageEnd);
		logger.info("开始页:" + page);
		try {
			 for (int i = page; i <= pageEnd; i++) {
				 pageable = new Pageable(i, number);
				 pageInfo = dataAdvertiserService.getSlotData(startTime, endTime, advertiserIds, 
						 advertiserName, mediaName, deviceType, sellType, pageable);
				 dataAdvertiserService.getLists(pageInfo.getList(), list);
			 }
			 
			 dataAdvertiserService.exportSlotData(list, response, name);
		} catch(Exception e) {
			logger.error("广告位报表导出错误：", e);
			return AjaxResponse.error(400, "广告位报表导出错误" + e.getMessage());
		}
		return AjaxResponse.success("导出成功");
	}
}
