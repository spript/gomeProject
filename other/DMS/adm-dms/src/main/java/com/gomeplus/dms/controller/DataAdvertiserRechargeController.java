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



import com.gomeplus.adm.biz.model.launch.DataRecharge;
import com.gomeplus.adm.biz.model.launch.DataRechargeDetail;
import com.gomeplus.adm.biz.service.DataAdvertiserRechargeServiceImpl;
import com.gomeplus.adm.biz.web.AjaxResponse;
import com.gomeplus.adm.biz.web.Page;
import com.gomeplus.adm.biz.web.Pageable;
/**
 * 广告主充值Controller
 * @author xiaogengen
 *
 */
@Controller
public class DataAdvertiserRechargeController {

	private static Logger logger = LogManager.getLogger(DataAdvertiserRechargeController.class);
	
	@Autowired
	private DataAdvertiserRechargeServiceImpl dataAdvertiserRechargeServiceImpl;
	
	
	/**
	 * 广告主充值汇总报表查询
	 * @param startTime
	 * @param endTime
	 * @param advertiserIds
	 * @param advertiserName
	 * @param accountType
	 * @param page
	 * @param number
	 * @return
	 */
	@RequiresPermissions("report:record:recharge:summary")
	@RequestMapping(value = "/api/report/anicer/recharge/summary", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResponse getAdvertiserRechargeSummary(
			@RequestParam(name = "startTime", required = false) Long startTime,
			@RequestParam(name = "endTime", required = false) Long endTime,
			@RequestParam(name = "optionType", required = false) Integer optionType,
			@RequestParam(name = "optionTypeText", required = false) String optionTypeText,
			@RequestParam(name = "accountType", required = false) Integer accountType,
			@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(name = "number", required = false, defaultValue = "20") Integer number) {
		Pageable pageable = new Pageable(page, number);
		Page<DataRecharge> pageInfo = null;
		String advertiserIds = null;
		String advertiserName = null;
		if (optionType != null) {
			switch(optionType) {
				case 0: advertiserIds = optionTypeText; break;
				case 1: advertiserName = optionTypeText; break;
			}
		}
		if (accountType != null && accountType == 0 ) {
			accountType = null;
		}
		try {
			 pageInfo = dataAdvertiserRechargeServiceImpl.getDataAdvertiserRechargeSummary(startTime,
					 endTime, advertiserIds, 
					 advertiserName, accountType, pageable);
		} catch(Exception e) {
			logger.error("广告主充值汇总报表查询错误：", e);
			return AjaxResponse.error(400, "广告主充值汇总报表查询错误" + e.getMessage());
		}
		
		return AjaxResponse.success(pageInfo, "查询成功");
	}
	
	/**
	 * 广告主充值明细报表
	 * @param startTime
	 * @param endTime
	 * @param advertiserIds
	 * @param advertiserName
	 * @param accountType
	 * @param page
	 * @param number
	 * @return
	 */
	@RequiresPermissions("report:record:recharge:detail")
	@RequestMapping(value = "/api/report/anicer/recharge/detail", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResponse getAdvertiserRechargeDetail(
			@RequestParam(name = "startTime", required = false) Long startTime,
			@RequestParam(name = "endTime", required = false) Long endTime,
			@RequestParam(name = "optionType", required = false) Integer optionType,
			@RequestParam(name = "optionTypeText", required = false) String optionTypeText,
			@RequestParam(name = "accountType", required = false) Integer accountType,
			@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(name = "number", required = false, defaultValue = "20") Integer number) {
		Pageable pageable = new Pageable(page, number);
		Page<DataRechargeDetail> pageInfo = null;
		String advertiserIds = null;
		String advertiserName = null;
		if (optionType != null) {
			switch(optionType) {
				case 0: advertiserIds = optionTypeText; break;
				case 1: advertiserName = optionTypeText; break;
			}
		}
		if (accountType != null && accountType == 0) {
			accountType = null;
		}
		try {
			 pageInfo = dataAdvertiserRechargeServiceImpl.getDataAdvertiserRechargeDetail(startTime, endTime,
					 advertiserIds, advertiserName, accountType, pageable);
		} catch(Exception e) {
			logger.error("广告主充值明细报表查询错误：", e);
			return AjaxResponse.error(400, "广告主充值明细报表查询错误" + e.getMessage());
		}
		
		return AjaxResponse.success(pageInfo, "查询成功");
	}
	
	
	/**
	 * 导出广告主充值汇总报表
	 * @param startTime
	 * @param endTime
	 * @param advertiserIds
	 * @param advertiserName
	 * @param accountType
	 * @param page
	 * @param number
	 * @return
	 */
	@RequiresPermissions("report:record:recharge:summary:export")
	@RequestMapping(value = "/api/recharge/summary", method = RequestMethod.GET)
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
		List<DataRecharge> list = new ArrayList<>();
		Page<DataRecharge> pageInfo = null;
		String advertiserIds = null;
		String advertiserName = null;
		if (optionType != null) {
			switch(optionType) {
				case 0: advertiserIds = optionTypeText; break;
				case 1: advertiserName = optionTypeText; break;
			}
		}
		if(accountType != null && accountType == 0 ) {
			accountType = null;
		}
		try {
			for(int i = page; i <= pageEnd; i++) {
				pageable = new Pageable(i, number);
				pageInfo = dataAdvertiserRechargeServiceImpl.getDataAdvertiserRechargeSummary(startTime, 
						 endTime, advertiserIds, 
						 advertiserName, accountType, pageable);
				dataAdvertiserRechargeServiceImpl.getCombineSummary(pageInfo.getList(), list);
			}
			 
			 dataAdvertiserRechargeServiceImpl.exportDataAdvertiserRechargeSummary(list,
					 response);
		} catch(Exception e) {
			logger.error("广告主充值汇总报表导出：", e);
			return AjaxResponse.error(400, "广告主充值汇总报表导出错误" + e.getMessage());
		}
		
		return AjaxResponse.success(pageInfo, "导出成功");
	}
	
	/**
	 * 导出广告主充值明细报表
	 * @param startTime
	 * @param endTime
	 * @param advertiserIds
	 * @param advertiserName
	 * @param accountType
	 * @param page
	 * @param number
	 * @return
	 */
	@RequiresPermissions("report:record:recharge:detail:export")
	@RequestMapping(value = "/api/recharge/detail", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResponse exportAdvertiserRechargeDetail(
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
		List<DataRechargeDetail> list = new ArrayList<>();
		Page<DataRechargeDetail> pageInfo = null;
		String advertiserIds = null;
		String advertiserName = null;
		if( optionType != null) {
			switch(optionType) {
				case 0: advertiserIds = optionTypeText; break;
				case 1: advertiserName = optionTypeText; break;
			}
		}
		if (accountType != null && accountType == 0) {
			accountType = null;
		}
		try {
			for (int i = page; i<= pageEnd; i++) {
				pageable = new Pageable(i, number);
				 pageInfo = dataAdvertiserRechargeServiceImpl.getDataAdvertiserRechargeDetail(startTime, 
						 endTime, advertiserIds, 
						 advertiserName, accountType, pageable);
				 dataAdvertiserRechargeServiceImpl.getCombineDetail(pageInfo.getList(), list);
			}
			
			 dataAdvertiserRechargeServiceImpl.exportDataAdvertiserRechargeDetail(list,
					 response);
		} catch(Exception e) {
			logger.error("广告主充值明细报表导出：", e);
			return AjaxResponse.error(400, "广告主充值明细报表导出错误" + e.getMessage());
		}
		
		return AjaxResponse.success(pageInfo, "导出成功");
	}
}
