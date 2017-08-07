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

import com.gomeplus.adm.biz.model.launch.DataConsume;
import com.gomeplus.adm.biz.model.launch.DataConsumeDetail;
import com.gomeplus.adm.biz.service.DataAdvertiserConsumeServiceImpl;
import com.gomeplus.adm.biz.web.AjaxResponse;
import com.gomeplus.adm.biz.web.Page;
import com.gomeplus.adm.biz.web.Pageable;


/**
 * 广告主消费Controller
 * @author xiaogengen
 *
 */
@Controller
public class DataAdvertiserConsumeController {

	private static Logger logger = LogManager.getLogger(DataAdvertiserConsumeController.class);
	
	@Autowired
	private DataAdvertiserConsumeServiceImpl dataAdvertiserConsumeServiceImpl;
	
	/**
	 * 广告主消费汇总查询
	 * @param startTime
	 * @param endTime
	 * @param advertiserIds
	 * @param advertiserName
	 * @param accountType
	 * @param page
	 * @param number
	 * @return
	 */

	@RequiresPermissions("report:record:cosume:summary")
	@RequestMapping(value = "/api/report/anicer/consume/summary", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResponse getAdvertiserConsumeSummary(
			@RequestParam(name = "startTime", required = false) Long startTime,
			@RequestParam(name = "endTime", required = false) Long endTime,
			@RequestParam(name = "optionType", required = false) Integer optionType,
			@RequestParam(name = "optionTypeText", required = false) String optionTypeText,
			@RequestParam(name = "accountType", required = false) Integer accountType,
			@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(name = "number", required = false, defaultValue = "20") Integer number) {
		Pageable pageable = new Pageable(page, number);
		Page<DataConsume> pageInfo = null;
		String advertiserIds = null;
		String advertiserName = null;
		if (optionType != null) {
			switch(optionType){
				case 0: advertiserIds = optionTypeText; break;
				case 1: advertiserName = optionTypeText; break;
			}
		}
		if (accountType != null && accountType == 0 ) {
			accountType = null;
		}
		try {
			pageInfo = dataAdvertiserConsumeServiceImpl.getAdvertiserConsumeSummary(startTime, 
					endTime, advertiserIds, advertiserName, accountType, pageable);
		} catch(Exception e) {
			logger.error("广告主消费汇总报表查询错误：", e);
			return AjaxResponse.error(400, "广告主消费汇总报表查询错误" + e.getMessage());
		}
		
		return AjaxResponse.success(pageInfo, "查询成功");
	}
	
	
	/**
	 * 广告主消费汇总导出
	 * @param startTime
	 * @param endTime
	 * @param advertiserIds
	 * @param advertiserName
	 * @param accountType
	 * @param page
	 * @param number
	 * @param response
	 * @return
	 */
	@RequiresPermissions("report:record:cosume:summary:export")
	@RequestMapping(value = "/api/consume/summary", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResponse exportAdvertiserRechargeSummary(
			@RequestParam(name = "startTime", required = false) Long startTime,
			@RequestParam(name = "endTime", required = false) Long endTime,
			@RequestParam(name = "optionType", required = false) Integer optionType,
			@RequestParam(name = "optionTypeText", required = false) String optionTypeText,
			@RequestParam(name = "accountType", required = false) Integer accountType,
			@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(name = "pageEnd", required = false, defaultValue = "1") Integer pageEnd,
			@RequestParam(name = "number", required = false, defaultValue = "20") Integer number,
			HttpServletResponse response) {
		Pageable pageable = null;
		List<DataConsume> list = new ArrayList<>();
		Page<DataConsume> pageInfo = null;
		String advertiserIds = null;
		String advertiserName = null;
		if ( optionType != null) {
			switch(optionType){
				case 0: advertiserIds = optionTypeText; break;
				case 1: advertiserName = optionTypeText; break;
			}
		}
		if (accountType != null && accountType == 0 ) {
			accountType = null;
		}
		try {
			for (int i = page; i <= pageEnd; i++) {
				pageable = new Pageable(i, number);
				pageInfo = dataAdvertiserConsumeServiceImpl.getAdvertiserConsumeSummary(startTime,
						 endTime, advertiserIds, advertiserName, accountType, pageable);
				dataAdvertiserConsumeServiceImpl.getCombineSummary(pageInfo.getList(), list);
			}
			
			 dataAdvertiserConsumeServiceImpl.exportDataAdvertiserConsumeSummary(list, 
					 response);
		} catch(Exception e) {
			logger.error("广告主消费汇总报表导出：", e);
			return AjaxResponse.error(400, "广告主消费汇总报表导出错误" + e.getMessage());
		}
		
		return AjaxResponse.success(pageInfo, "导出成功");
	}
	/**
	 * 广告消费明细报表查询
	 * @param startTime
	 * @param endTime
	 * @param advertiserIds
	 * @param advertiserName
	 * @param accountType
	 * @param amountType
	 * @param page
	 * @param number
	 * @return
	 */
	@RequiresPermissions("report:record:cosume:detail")
	@RequestMapping(value = "/api/report/anicer/consume/detail", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResponse getAdvertiserConsumeDetail(
			@RequestParam(name = "startTime", required = false) Long startTime,
			@RequestParam(name = "endTime", required = false) Long endTime,
			@RequestParam(name = "optionType", required = false) Integer optionType,
			@RequestParam(name = "optionTypeText", required = false) String optionTypeText,
			@RequestParam(name = "accountType", required = false) Integer accountType,
			@RequestParam(name = "amountType", required = false) Integer amountType,
			@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(name = "number", required = false, defaultValue = "20") Integer number) {
		Pageable pageable = new Pageable(page, number);
		Page<DataConsumeDetail> pageInfo = null;
		String advertiserIds = null;
		String advertiserName = null;
		if (optionType != null) {
			switch(optionType){
				case 0: advertiserIds = optionTypeText; break;
				case 1: advertiserName = optionTypeText; break;
			}
		}
		if (accountType != null && accountType == 0 ) {
			accountType = null;
		}
		if (amountType != null && amountType==0) {
			amountType = null;
		}
		try {
			pageInfo = dataAdvertiserConsumeServiceImpl.getAdvertiserConsumeDetail(startTime, endTime, 
					advertiserIds, advertiserName, accountType, amountType, pageable);
		} catch(Exception e) {
			logger.error("广告主消费汇总报表查询错误：", e);
			return AjaxResponse.error(400, "广告主消费汇总报表查询错误" + e.getMessage());
		}
		
		return AjaxResponse.success(pageInfo, "查询成功");
	}
	@RequiresPermissions("report:record:cosume:detail:export")
	@RequestMapping(value = "/api/consume/detail", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResponse exportAdvertiserRechargeDetail(
			@RequestParam(name = "startTime", required = false) Long startTime,
			@RequestParam(name = "endTime", required = false) Long endTime,
			@RequestParam(name = "optionType", required = false) Integer optionType,
			@RequestParam(name = "optionTypeText", required = false) String optionTypeText,
			@RequestParam(name = "accountType", required = false) Integer accountType,
			@RequestParam(name = "amountType", required = false) Integer amountType,
			@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(name = "pageEnd", required = false, defaultValue = "1") Integer pageEnd,
			@RequestParam(name = "number", required = false, defaultValue = "20") Integer number,
			HttpServletResponse response) {
		Pageable pageable = null;
		List<DataConsumeDetail> list = new ArrayList<>();
		Page<DataConsumeDetail> pageInfo = null;
		String advertiserIds = null;
		String advertiserName = null;
		if (optionType != null) {
			switch(optionType){
				case 0: advertiserIds = optionTypeText; break;
				case 1: advertiserName = optionTypeText; break;
			}
		}
		if (accountType != null && accountType == 0) {
			accountType = null;
		}
		if (amountType != null && amountType == 0) {
			amountType = null;
		}
		try {
			for (int i = page; i <= pageEnd; i++) {
				pageable = new Pageable(i, number);
				pageInfo = dataAdvertiserConsumeServiceImpl.getAdvertiserConsumeDetail(startTime, endTime, 
						advertiserIds, advertiserName, accountType, amountType, pageable);
				dataAdvertiserConsumeServiceImpl.getCombineDetail(pageInfo.getList(), list);
			}
			
			dataAdvertiserConsumeServiceImpl.exportDataAdvertiserConsumeDetail(list, response);
		} catch(Exception e) {
			logger.error("广告主消费汇总报表导出：", e);
			return AjaxResponse.error(400, "广告主消费汇总报表导出错误" + e.getMessage());
		}
		
		return AjaxResponse.success(pageInfo, "导出成功");
	}
}
