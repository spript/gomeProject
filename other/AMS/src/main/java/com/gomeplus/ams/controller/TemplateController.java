package com.gomeplus.ams.controller;

import com.gomeplus.adm.biz.dto.ad.TemplateDTO;
import com.gomeplus.adm.biz.model.ad.Template;
import com.gomeplus.adm.biz.service.TemplateService;
import com.gomeplus.adm.biz.web.AjaxResponse;
import com.gomeplus.adm.biz.web.Page;
import com.gomeplus.adm.biz.web.Pageable;
import com.gomeplus.adportal.client.PrincipalUtil;
import com.gomeplus.ams.form.TemplateForm;

import org.apache.commons.collections.map.HashedMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Created by liuchen on 2017/3/21.
 */
@Controller
public class TemplateController {

	@Autowired
	private TemplateService templateService;

	private Logger logger = LogManager.getLogger(getClass());


	/**
	 * 获取单个模板详情回显
	 *
	 * @param templateId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/template", method = RequestMethod.GET)
	public AjaxResponse get(@RequestParam(required = false) Integer templateId) {

		if (ObjectUtils.isEmpty(templateId)) {
			return AjaxResponse.error(400, "模板Id不能为空");
		}

		try {
			TemplateDTO templateDTO = templateService.getByTemplateId(templateId);

			return AjaxResponse.success(templateDTO, "获取单个模板详情数据成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(400, "获取单个模板详情数据失败");
		}
	}


	/**
	 * 添加模板
	 *
	 * @param templateForm
	 * @param bindingResult
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/template", method = RequestMethod.POST)
	public AjaxResponse add(@Validated @RequestBody TemplateForm templateForm, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return AjaxResponse.error(400, "模板参数校验失败");
		}


		Template template = new Template();
		BeanUtils.copyProperties(templateForm, template);
		template.setAccountId(PrincipalUtil.getAccountId());
		try {
			templateService.save(template);
			return AjaxResponse.success(template, "添加模板成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "添加模板失败");
		}
	}

	/**
	 * 修改单个模板
	 *
	 * @param templateForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/template", method = RequestMethod.PUT)
	public AjaxResponse edit(@RequestBody TemplateForm templateForm) {


		Template template = new Template();
		BeanUtils.copyProperties(templateForm, template);

		try {
			templateService.update(template);
			return AjaxResponse.success(template, "修改模板成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "修改模板失败");
		}
	}


	/**
	 * 查询模板状态进行是否满足删除条件的判断
	 *
	 * @param templateId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/template/status", method = RequestMethod.GET)
	public AjaxResponse getStatus(@RequestParam(required = false) Integer templateId) {

		if (ObjectUtils.isEmpty(templateId)) {
			return AjaxResponse.error(400, "模板ID不能为空");
		}
		Map<String, String> datas = new LinkedHashMap<>();
		try {
			TemplateDTO templateDTO = templateService.getByTemplateId(templateId);
			datas.put("status", templateDTO.getStatus().toString());
			return AjaxResponse.success(datas, "获取模板状态成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "获取模板状态失败");
		}
	}

	/**
	 * 删除单个模板
	 *
	 * @param templateForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/template", method = RequestMethod.DELETE)
	public AjaxResponse del(@RequestBody TemplateForm templateForm) {

		if (ObjectUtils.isEmpty(templateForm.getTemplateId())) {
			return AjaxResponse.error(500, "模板参数校验失败");
		}

		Template template = new Template();
		template.setTemplateId(templateForm.getTemplateId());
		template.setStatus(Template.Status.DELETE.getValue());
		try {
			templateService.updateStatus(template);
			return AjaxResponse.success(template, "删除模板成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "删除模板失败");
		}
	}

	/**
	 * 修改模板状态
	 *
	 * @param templateForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/template/status", method = RequestMethod.PUT)
	public AjaxResponse editStatus(@RequestBody TemplateForm templateForm) {

		if (ObjectUtils.isEmpty(templateForm.getTemplateId()) || ObjectUtils.isEmpty(templateForm.getStatus())) {
			return AjaxResponse.error(400, "模板参数校验失败");
		}

		Template template = new Template();
		template.setTemplateId(templateForm.getTemplateId());
		template.setStatus(templateForm.getStatus());
		try {
			templateService.updateStatus(template);
			return AjaxResponse.success(template, "修改模板状态成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "修改模板状态失败");
		}
	}

	/**
	 * 判断模板是否满足下线条件
	 *
	 * @param templateId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/template/checkOfflineConditon", method = RequestMethod.GET)
	public AjaxResponse checkOfflineConditon(@RequestParam(required = false) Integer templateId) {

		if (ObjectUtils.isEmpty(templateId)) {
			return AjaxResponse.error(400, "模板参数校验失败");
		}
		Map<String, Boolean> datas = new HashedMap();

		Template template = new Template();
		template.setTemplateId(templateId);

		try {
			// 校验是否有已经上线广告绑定该模板，若有，则不能下线该模板，
			boolean result = templateService.checkOfflineCondition(template);

			datas.put("result", result);
			return AjaxResponse.success(datas, "判断模板是否满足下线条件成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "判断模板是否满足下线条件失败");
		}
	}


	/**
	 * 根据查询条件获取模板列表
	 *
	 * @param page
	 * @param number
	 * @param keyword
	 * @param status
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/templates", method = RequestMethod.GET)
	public AjaxResponse list(@RequestParam(required = false, defaultValue = "1") Integer page,
							 @RequestParam(required = false, defaultValue = "30") Integer number,
							 @RequestParam(required = false, defaultValue = "") String keyword,
							 @RequestParam(required = false, defaultValue = "") Integer status) {

		Pageable pageable = new Pageable(page, number);

		try {
			int accountId = PrincipalUtil.getAccountId();
			Page<TemplateDTO> templates = templateService.getByPageable(pageable, keyword, accountId, status);
			return AjaxResponse.success(templates, "获取模板列表成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "获取模板列表失败");
		}
	}
}
