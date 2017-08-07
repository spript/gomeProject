package com.gomeplus.ams.controller;


import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gomeplus.adm.biz.dto.approval.ApprovalAdvertiserDTO;
import com.gomeplus.adm.biz.service.AdvertiserService;
import com.gomeplus.adm.biz.web.AjaxResponse;
import com.gomeplus.adm.biz.web.Page;
import com.gomeplus.adm.biz.web.Pageable;
import com.gomeplus.adportal.client.PrincipalUtil;
import com.gomeplus.ams.form.AdvertiserForm;

/**
 * 系统审核
 *
 * @author suna01
 */
@RestController
public class AdvertiserApprovalController {
	
	private Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	private AdvertiserService advertiserService;
	
	/**
	 * 广告主审核列表
	 * @param request
	 * @param startTime
	 * @param endTime
	 * @param approveStatus
	 * @param order
	 * @param orderBy
	 * @param advertiserName
	 * @return
	 */
	@RequestMapping(value = "/api/approval/advertisers", method = RequestMethod.GET)
	public AjaxResponse getApprovalAdvertisers(
			@RequestParam(required = false, defaultValue = "1") Integer page,
			@RequestParam(required = false, defaultValue = "30") Integer number,
			@RequestParam(required = false, defaultValue = "0") Long startTime,
			@RequestParam(required = false, defaultValue = "0") Long endTime,
			@RequestParam(required = false, defaultValue = "0") Integer approveStatus,
			@RequestParam(required = false, defaultValue = "asc") String order,
			@RequestParam(required = false, defaultValue = "createTime") String orderBy,
			@RequestParam(required = false, defaultValue = "") String advertiserName) {
		
		try {
			Pageable pageable = new Pageable(page, number);
			Page<ApprovalAdvertiserDTO> advertisers = advertiserService.getAdvertisers(pageable, startTime, endTime, approveStatus, order, orderBy, advertiserName);
			return AjaxResponse.success(advertisers, "获取广告主信息列表成功！");
		} catch (Exception e) {
			logger.error("get approval advertisers failed", e);
			return AjaxResponse.error(400, "获取广告主信息列表失败！" + e);
		}
	}
	
	/**
	 * 根据advertiserId获取某个广告主信息
	 * @param advertiserId
	 * @return
	 */
	@RequestMapping(value = "/api/approval/advertiser", method = RequestMethod.GET)
	public AjaxResponse getAdvertiser(@RequestParam Integer advertiserId) {
		
		try {
			
			ApprovalAdvertiserDTO advertiserDTO = advertiserService.getAdvertiser(advertiserId);
			return AjaxResponse.success(advertiserDTO, "获取广告主细信息成功！");
		} catch (Exception e) {
			logger.error("get advertiser failed", e);
			return AjaxResponse.error(400, "获取广告主信息失败！" + e);
		}
		
	}
	
	/**
	 * 审核通过广告主
	 * @param advertiserId 广告主id
	 * @return
	 */
	@RequestMapping(value = "/api/approval/advertiser/pass", method=RequestMethod.PUT)
	public AjaxResponse passAdvertiser(@RequestBody AdvertiserForm advertiserForm) {
		logger.info("advertiser pass startTime : " + new Date().getTime());
		Integer advertiserId = advertiserForm.getAdvertiserId();
		try {
			Integer accountId = PrincipalUtil.getAccountId();
			String name = PrincipalUtil.getName();
			advertiserService.pass(advertiserId, accountId, name);
		} catch (Exception exception) {
			logger.error("pass materialIds failed ",exception);
			return AjaxResponse.error(400, "广告主审核失败！" + exception.getMessage());
		}
		logger.info("advertiser end startTime : " + new Date().getTime());
		return AjaxResponse.success("广告主审核通过！");
	}
	
	/**
	 * 广告主审核拒绝
	 * @param advertiserForm
	 * @return
	 */
	@RequestMapping(value = "/api/approval/advertiser/reject", method=RequestMethod.PUT)
	public AjaxResponse rejectAdvertiser(@RequestBody AdvertiserForm advertiserForm) {
	
		Integer advertiserId = advertiserForm.getAdvertiserId();
		List<Object> rejectReason = advertiserForm.getRejectReason();
		try {
			Integer accountId = PrincipalUtil.getAccountId();
			String name = PrincipalUtil.getName();
			advertiserService.reject(advertiserId, rejectReason, accountId, name);
		} catch (Exception exception) {
			logger.error("reject advertiser failed ", exception);
			return AjaxResponse.error(400, "广告主审核拒绝失败！" + exception.getMessage());
		}

		return AjaxResponse.success("广告主审核拒绝通过！");
	}
	
}