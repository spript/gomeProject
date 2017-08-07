package com.gomeplus.ams.controller;

import com.gomeplus.adm.biz.model.ad.Format;
import com.gomeplus.adm.biz.service.FormatService;
import com.gomeplus.adm.biz.web.AjaxResponse;
import com.gomeplus.adm.biz.web.Page;
import com.gomeplus.adm.biz.web.Pageable;
import com.gomeplus.adportal.client.PrincipalUtil;
import com.gomeplus.ams.form.FormatForm;

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
public class FormatController {

	@Autowired
	private FormatService formatService;

	private Logger logger = LogManager.getLogger(getClass());


	/**
	 * 获取单个广告形式详情回显
	 *
	 * @param formatId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/format", method = RequestMethod.GET)
	public AjaxResponse get(@RequestParam(required = false) Integer formatId) {

		if (ObjectUtils.isEmpty(formatId)) {
			return AjaxResponse.error(400, "广告形式Id不能为空");
		}

		try {
			Format format = formatService.getByFormatId(formatId);

			return AjaxResponse.success(format, "获取单个广告形式详情数据成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "获取单个广告形式详情数据失败");
		}
	}


	/**
	 * 添加广告形式
	 *
	 * @param formatForm
	 * @param bindingResult
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/format", method = RequestMethod.POST)
	public AjaxResponse add(@Validated @RequestBody FormatForm formatForm, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return AjaxResponse.error(400, "广告形式参数校验失败");
		}


		Format format = new Format();
		BeanUtils.copyProperties(formatForm, format);
		format.setAccountId(PrincipalUtil.getAccountId());
		try {
			formatService.save(format);
			return AjaxResponse.success(format, "添加广告形式成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "添加广告形式失败");
		}
	}

	/**
	 * 修改单个广告形式
	 *
	 * @param formatForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/format", method = RequestMethod.PUT)
	public AjaxResponse edit(@RequestBody FormatForm formatForm) {


		Format format = new Format();
		BeanUtils.copyProperties(formatForm, format);

		try {
			formatService.update(format);
			return AjaxResponse.success(format, "修改广告形式成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "修改广告形式失败");
		}
	}


	/**
	 * 查询广告形式状态进行是否满足删除条件的判断
	 *
	 * @param formatId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/format/status", method = RequestMethod.GET)
	public AjaxResponse getStatus(@RequestParam(required = false) Integer formatId) {

		if (ObjectUtils.isEmpty(formatId)) {
			return AjaxResponse.error(400, "广告形式ID不能为空");
		}
		Map<String, String> datas = new LinkedHashMap<>();
		try {
			Format format = formatService.getByFormatId(formatId);
			datas.put("status", format.getStatus().toString());
			return AjaxResponse.success(datas, "获取广告形式状态成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "获取广告形式状态失败");
		}
	}

	/**
	 * 删除单个广告形式
	 *
	 * @param formatForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/format", method = RequestMethod.DELETE)
	public AjaxResponse del(@RequestBody FormatForm formatForm) {

		if (ObjectUtils.isEmpty(formatForm.getFormatId())) {
			return AjaxResponse.error(400, "广告形式参数校验失败");
		}

		Format format = new Format();
		format.setFormatId(formatForm.getFormatId());
		format.setStatus(Format.Status.DELETE.getValue());
		try {
			formatService.updateStatus(format);
			return AjaxResponse.success(format, "删除广告形式成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "删除广告形式失败");
		}
	}

	/**
	 * 修改广告形式状态
	 *
	 * @param formatForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/format/status", method = RequestMethod.PUT)
	public AjaxResponse editStatus(@RequestBody FormatForm formatForm) {

		if (ObjectUtils.isEmpty(formatForm.getFormatId()) || ObjectUtils.isEmpty(formatForm.getStatus())) {
			return AjaxResponse.error(400, "广告形式参数校验失败");
		}

		Format format = new Format();
		format.setFormatId(formatForm.getFormatId());
		format.setStatus(formatForm.getStatus());
		try {
			formatService.updateStatus(format);
			return AjaxResponse.success(format, "修改广告形式状态成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "修改广告形式状态失败");
		}
	}


	/**
	 * 判断广告形式是否满足下线条件
	 *
	 * @param formatId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/format/checkOfflineConditon", method = RequestMethod.GET)
	public AjaxResponse checkOfflineCondition(@RequestParam(required = false) Integer formatId) {

		if (ObjectUtils.isEmpty(formatId)) {
			return AjaxResponse.error(400, "广告形式参数校验失败");
		}
		Map<String, Boolean> datas = new HashedMap();

		Format format = new Format();
		format.setFormatId(formatId);

		try {
			// 校验该广告形式是否有绑定的已经上线广告，若有，则校验失败并弹窗提醒，
			boolean result = formatService.checkOfflineCondition(format);

			datas.put("result", result);
			return AjaxResponse.success(datas, "判断广告形式是否满足下线条件成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "判断广告形式是否满足下线条件失败");
		}
	}

	/**
	 * 根据查询条件获取广告形式列表
	 *
	 * @param page
	 * @param number
	 * @param keyword
	 * @param status
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/formats", method = RequestMethod.GET)
	public AjaxResponse list(@RequestParam(required = false, defaultValue = "1") Integer page,
							 @RequestParam(required = false, defaultValue = "30") Integer number,
							 @RequestParam(required = false, defaultValue = "") String keyword,
							 @RequestParam(required = false, defaultValue = "") Integer status) {

		Pageable pageable = new Pageable(page, number);

		try {
			int accountId = PrincipalUtil.getAccountId();
			Page<Format> formats = formatService.getByPageable(pageable, keyword, accountId, status);
			return AjaxResponse.success(formats, "获取广告形式列表成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "获取广告形式列表失败");
		}
	}
}
