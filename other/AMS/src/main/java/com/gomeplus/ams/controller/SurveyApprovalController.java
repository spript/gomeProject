package com.gomeplus.ams.controller;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gomeplus.adm.biz.dto.approval.ApprovalSurveyDTO;
import com.gomeplus.adm.biz.service.SurveyService;
import com.gomeplus.adm.biz.web.AjaxResponse;
import com.gomeplus.adm.biz.web.Page;
import com.gomeplus.adm.biz.web.Pageable;
import com.gomeplus.adportal.client.PrincipalUtil;
import com.gomeplus.ams.form.SurveyForm;

/**
 * 文件审核
 * @author suna01
 *
 */
@RestController
public class SurveyApprovalController {
	private Logger logger = LogManager.getLogger(getClass());
	@Autowired
	private SurveyService surveyService;
	
	/**
	 * 分页获取问卷列表数据
	 * @param page
	 * @param number
	 * @param startTime
	 * @param endTime
	 * @param approveStatus
	 * @param order
	 * @param orderBy
	 * @param surveyId
	 * @param name
	 * @param advertiserName
	 * @return
	 */
	@RequestMapping(value = "/api/surveys", method = RequestMethod.GET)
	public AjaxResponse getApprovalSurveys(
			@RequestParam(required = false, defaultValue = "1") Integer page,
			@RequestParam(required = false, defaultValue = "30") Integer number,
			@RequestParam(required = false, defaultValue = "0") Long startTime,
			@RequestParam(required = false, defaultValue = "0") Long endTime,
			@RequestParam(required = false, defaultValue = "0") Integer approveStatus,
			@RequestParam(required = false, defaultValue = "asc") String order,
			@RequestParam(required = false, defaultValue = "updateTime") String orderBy,
			@RequestParam(required = false, defaultValue = "") String keyword,
			@RequestParam(required = false, defaultValue = "") String advertiserName) {
		
		try {
			Pageable pageable = new Pageable(page, number);
			Page<ApprovalSurveyDTO> surveys = surveyService.getByPageables(pageable, startTime, endTime, approveStatus, order, orderBy, keyword, advertiserName);
			return AjaxResponse.success(surveys, "获取问卷信息列表成功！");
		} catch (Exception e) {
			logger.error("get approval surveys failed", e);
			return AjaxResponse.error(400, "获取问卷信息列表失败！" + e);
		}
	}
	
	/**
	 * 批量通过问卷
	 * @param SurveyForm
	 * @return
	 */
	
	@RequestMapping(value = "/api/survey/pass", method=RequestMethod.PUT)
	public AjaxResponse surveyPass(@RequestBody SurveyForm surveyForm) {
		List<Integer> surveyIds = surveyForm.getSurveyIds();
		try {
			Integer accountId = PrincipalUtil.getAccountId();
			String name = PrincipalUtil.getName();
			surveyService.batchPass(surveyIds, accountId, name);
		} catch (Exception exception) {
			logger.error("pass surveyIds failed ",exception);
			return AjaxResponse.error(400, "问卷审核失败！" + exception.getMessage());
		}
		return AjaxResponse.success("问卷审核通过！");
	}
	
	/**
	 * 问卷审核拒绝
	 * @param advertiserForm
	 * @return
	 */
	@RequestMapping(value = "/api/survey/reject", method=RequestMethod.PUT)
	public AjaxResponse surveyReject(@RequestBody SurveyForm surveyForm) {
	
		Integer surveyId = surveyForm.getSurveyId();
		List<Object> rejectReason = surveyForm.getRejectReason();
		try {
			Integer accountId = PrincipalUtil.getAccountId();
			String name = PrincipalUtil.getName();
			surveyService.reject(surveyId, rejectReason, accountId, name);
		} catch (Exception exception) {
			logger.error("reject survey failed ", exception);
			return AjaxResponse.error(400, "问卷审核拒绝失败！" + exception.getMessage());
		}

		return AjaxResponse.success("问卷审核拒绝通过！");
	}
	
	/**
	 * 问卷预览
	 * @param surveyId
	 * @return
	 */
	@RequestMapping(value = "/api/survey/preview", method = RequestMethod.GET)
	public AjaxResponse previewSurvey(@RequestParam Integer surveyId) {

		try {
			Map<String, Object> data = surveyService.previewSurvey(surveyId);
			return AjaxResponse.success(data, "获取问卷预览成功");
		} catch (Exception ex) {
			logger.error("获取标准题库失败: " + ex.getMessage());
			return AjaxResponse.error(500, "获取问卷预览失败");
		}
	}
}
