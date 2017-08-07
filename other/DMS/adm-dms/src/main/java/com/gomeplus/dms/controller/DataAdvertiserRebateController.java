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

import com.gomeplus.adm.biz.dto.ad.DataAdvertiserRebateDetailReportDTO;
import com.gomeplus.adm.biz.dto.ad.DataAdvertiserRebateSummaryReportDTO;
import com.gomeplus.adm.biz.service.DataAdvertiserService;
import com.gomeplus.adm.biz.web.AjaxResponse;
import com.gomeplus.adm.biz.web.Page;
import com.gomeplus.adm.biz.web.Pageable;

/**
 * 广告主返利报表
 * @author xiaogengen
 *
 */
@Controller
public class DataAdvertiserRebateController {
		
	private static Logger logger = LogManager.getLogger(DataAdvertiserRebateController.class);
	
	@Autowired
	private DataAdvertiserService dataAdvertiserService;
	/**
	 * 广告主返利汇总报表查询
	 * @param startTime
	 * @param endTime
	 * @param advertiserId
	 * @param advertiserName
	 * @param advertiserType
	 * @param page
	 * @param number
	 * @return
	 */
	@RequiresPermissions("report:anicer:rebate:summary")
	@RequestMapping(value = "/api/report/anicer/rebate/summary", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResponse getDataAdvertiserSummry(
			@RequestParam(name = "startTime", required = false) Long startTime,
			@RequestParam(name = "endTime", required = false) Long endTime,
			@RequestParam(name = "optionType", required = false) Integer optionType,
			@RequestParam(name = "optionTypeText", required = false) String optionTypeText,
			@RequestParam(name = "advertiserType", required = false) Integer advertiserType,
			@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(name = "number", required = false, defaultValue = "20") Integer number) {
		Pageable pageable = new Pageable(page, number);
		Page<DataAdvertiserRebateSummaryReportDTO> pageInfo = null;
		String advertiserIds = null;
		String advertiserName = null;
		if (optionType != null) {
			switch(optionType) {
				case 0: advertiserIds = optionTypeText; break;
				case 1: advertiserName = optionTypeText; break;
			}
		}
		if (advertiserType == 0) {
			advertiserType = null;
		}
		logger.info("advertiserIds : " + advertiserIds);
		logger.info("advertiserName : " + advertiserName);
		logger.info("advertiserType : " + advertiserType);
		try {
			 pageInfo = dataAdvertiserService.getRebateSummaryData(startTime, endTime, 
					 advertiserIds, advertiserName, advertiserType, pageable);
		} catch(Exception e) {
			logger.error("广告主返利汇总报表查询错误：", e);
			return AjaxResponse.error(400, "广告主返利汇总报表查询错误" + e.getMessage());
		}
		
		return AjaxResponse.success(pageInfo, "查询成功");
	}
	
	
	/**
	 * 广告主返利明细接口Controller
	 * @param startTime
	 * @param endTime
	 * @param advertiserId
	 * @param advertiserName
	 * @param campaginId
	 * @param advertiserType
	 * @param page
	 * @param number
	 * @return
	 */
	@RequiresPermissions("report:anicer:rebate:detail")
	@RequestMapping(value = "/api/report/anicer/rebate/detail", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResponse getDataAdvertiserDetail(
			@RequestParam(name = "startTime", required = false) Long startTime,
			@RequestParam(name = "endTime", required = false) Long endTime,
			@RequestParam(name = "optionType", required = false) Integer optionType,
			@RequestParam(name = "optionTypeText", required = false) String optionTypeText,
			@RequestParam(name = "advertiserType", required = false) Integer advertiserType,
			@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(name = "number", required = false, defaultValue = "20") Integer number) {
		Pageable pageable = new Pageable(page, number);
		Page<DataAdvertiserRebateDetailReportDTO> pageInfo = null;
		String advertiserIds = null;
		String advertiserName = null;
		String campaginIds = null;
		if (optionType != null) {
			switch(optionType){
				case 0: advertiserIds = optionTypeText; break;
				case 1: advertiserName = optionTypeText; break;
				case 2: campaginIds = optionTypeText; break;
			}
		}
		if (advertiserType == 0) {
			advertiserType = null;
		}
		logger.info("advertiserIds : " + advertiserIds);
		logger.info("advertiserName : " + advertiserName);
		logger.info("advertiserType : " + advertiserType);
		logger.info("campaginIds" + campaginIds);
		try {
			 pageInfo = dataAdvertiserService.getRebateDetailData(startTime, endTime, 
					 advertiserIds, advertiserName, campaginIds, advertiserType, pageable);
		} catch(Exception e) {
			logger.error("广告主返利明细报表查询错误：", e);
			return AjaxResponse.error(400, "广告主返利明细报表查询错误" + e.getMessage());
		}
		
		return AjaxResponse.success(pageInfo, "查询成功");
	}
	
	/**
	 * 导出广告主返利汇总报表
	 * @param startTime
	 * @param endTime
	 * @param advertiserId
	 * @param advertiserName
	 * @param advertiserType
	 * @param page
	 * @param number
	 * @return
	 */
	@RequiresPermissions("report:anicer:rebate:summary:export")
	@RequestMapping(value = "/api/rebate/summary", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResponse exportDataAdvertiserSummry(
			@RequestParam(name = "startTime", required = false) Long startTime,
			@RequestParam(name = "endTime", required = false) Long endTime,
			@RequestParam(name = "optionType", required = false) Integer optionType,
			@RequestParam(name = "optionTypeText", required = false) String optionTypeText,
			@RequestParam(name = "advertiserType", required = false) Integer advertiserType,
			@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(name = "pageEnd", required = false, defaultValue = "1") Integer pageEnd,
			@RequestParam(name = "number", required = false, defaultValue = "20") Integer number,
			HttpServletResponse response) {
		Pageable pageable = null;
		List<DataAdvertiserRebateSummaryReportDTO> list = new ArrayList<>();
		Page<DataAdvertiserRebateSummaryReportDTO> pageInfo = null;
		String advertiserIds = null;
		String advertiserName = null;
		if (optionType != null) {
			switch(optionType) {
				case 0: advertiserIds = optionTypeText; break;
				case 1: advertiserName = optionTypeText; break;
			}
		}
		if (advertiserType == 0) {
			advertiserType = null;
		}
		logger.info("advertiserIds : " + advertiserIds);
		logger.info("advertiserName : " + advertiserName);
		logger.info("advertiserType : " + advertiserType);
		try {
			 for (int i = page; i <= pageEnd; i++) {
				 pageable = new Pageable(i, number);
				 pageInfo = dataAdvertiserService.getRebateSummaryData(startTime, endTime, 
						 advertiserIds, advertiserName, advertiserType, pageable);
				 dataAdvertiserService.getListRebateSummary(pageInfo.getList(), list);
			 }
			 
			 dataAdvertiserService.exportRebateSummaryData(list, response);
		} catch(Exception e) {
			logger.error("广告主返利汇总报表导出错误：", e);
			return AjaxResponse.error(400, "广告主返利汇总导出查询错误" + e.getMessage());
		}
		
		return AjaxResponse.success("导出成功");
	}
	
	/**
	 * 导出广告主返利明细报表
	 * @param startTime
	 * @param endTime
	 * @param advertiserId
	 * @param advertiserName
	 * @param campaginId
	 * @param advertiserType
	 * @param page
	 * @param number
	 * @return
	 */
	@RequiresPermissions("report:anicer:rebate:detail:export")
	@RequestMapping(value = "/api/rebate/detail", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResponse exportDataAdvertiserDetail(
			@RequestParam(name = "startTime", required = false) Long startTime,
			@RequestParam(name = "endTime", required = false) Long endTime,
			@RequestParam(name = "optionType", required = false) Integer optionType,
			@RequestParam(name = "optionTypeText", required = false) String optionTypeText,
			@RequestParam(name = "advertiserType", required = false) Integer advertiserType,
			@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(name = "pageEnd", required = false, defaultValue = "1") Integer pageEnd,
			@RequestParam(name = "number", required = false, defaultValue = "20") Integer number,
			HttpServletResponse response) {
		Pageable pageable = null;
		List<DataAdvertiserRebateDetailReportDTO> list = new ArrayList<>();
		Page<DataAdvertiserRebateDetailReportDTO> pageInfo = null;
		String advertiserIds = null;
		String advertiserName = null;
		String campaginIds = null;
		if (optionType != null) {
			switch(optionType) {
				case 0: advertiserIds = optionTypeText; break;
				case 1: advertiserName = optionTypeText; break;
				case 2: campaginIds = optionTypeText; break;
			}
		}
		if (advertiserType == 0) {
			advertiserType = null;
		}
		logger.info("advertiserIds : " + advertiserIds);
		logger.info("advertiserName : " + advertiserName);
		logger.info("advertiserType : " + advertiserType);
		logger.info("campaginIds" + campaginIds);
		try {
			 for (int i = page; i <= pageEnd; i++) {
				 pageable = new Pageable(i, number);
				 pageInfo = dataAdvertiserService.getRebateDetailData(startTime, endTime, 
						 advertiserIds, advertiserName, campaginIds, advertiserType, pageable);
				 dataAdvertiserService.getLIstRebateDetail(pageInfo.getList(), list);
			 }
			
			 dataAdvertiserService.exportRebateDetailData(list, response);

		} catch(Exception e) {
			logger.error("广告主返利明细报表导出错误：", e);
			return AjaxResponse.error(400, "广告主返利明细报表导出错误" + e.getMessage());
		}
		
		return AjaxResponse.success("导出成功");
	}
}
