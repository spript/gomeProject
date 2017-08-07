package com.gomeplus.ams.controller;

import com.gomeplus.adm.biz.dto.ad.PublisherDTO;
import com.gomeplus.adm.biz.model.ad.Publisher;
import com.gomeplus.adm.biz.service.PublisherService;
import com.gomeplus.adm.biz.web.AjaxResponse;
import com.gomeplus.adm.biz.web.Page;
import com.gomeplus.adm.biz.web.Pageable;
import com.gomeplus.adportal.client.PrincipalUtil;
import com.gomeplus.ams.form.PublisherForm;

import org.apache.commons.lang3.StringUtils;
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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


@Controller
public class PublisherController {

	@Autowired
	private PublisherService publisherService;

	private Logger logger = LogManager.getLogger(getClass());


	/**
	 * 获取单个媒体详情回显
	 *
	 * @param publisherId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/publisher", method = RequestMethod.GET)
	public AjaxResponse get(@RequestParam(required = false) Integer publisherId) {

		if (ObjectUtils.isEmpty(publisherId)) {
			return AjaxResponse.error(400, "媒体Id不能为空");
		}

		try {
			PublisherDTO publisherDTO = publisherService.getByPublisherId(publisherId);

			return AjaxResponse.success(publisherDTO, "获取单个媒体详情数据成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "获取单个媒体详情数据失败");
		}
	}


	/**
	 * 添加媒体
	 *
	 * @param publisherForm
	 * @param bindingResult
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/publisher", method = RequestMethod.POST)
	public AjaxResponse add(@Validated @RequestBody PublisherForm publisherForm, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return AjaxResponse.error(400, "媒体参数校验失败");
		}

		// 默认为空；若设备类型选择WAP或PC，页面URL必填；
		if (publisherForm.getPlatform().compareTo(PublisherForm.Platform.APP.getValue()) > 0
				&& StringUtils.isEmpty(publisherForm.getUrl())) {
			return AjaxResponse.error(400, "媒体参数校验失败");
		}

		Publisher publisher = new Publisher();
		BeanUtils.copyProperties(publisherForm, publisher);
		publisher.setAccountId(PrincipalUtil.getAccountId());
		try {
			publisherService.save(publisher);
			return AjaxResponse.success(publisher, "添加媒体成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "添加媒体失败");
		}
	}

	/**
	 * 修改单个媒体
	 *
	 * @param publisherForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/publisher", method = RequestMethod.PUT)
	public AjaxResponse edit(@RequestBody PublisherForm publisherForm) {

		// 媒体名称：原数据回显；必填 && 描述：原数据回显；必填；最大长度为200个字符；
		if (ObjectUtils.isEmpty(publisherForm.getPublisherId()) || StringUtils.isEmpty(publisherForm.getDescription()
		)) {
			return AjaxResponse.error(400, "媒体参数校验失败");
		}

		Publisher publisher = new Publisher();
		BeanUtils.copyProperties(publisherForm, publisher);

		try {
			publisherService.update(publisher);
			return AjaxResponse.success(publisher, "修改媒体成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "修改媒体失败");
		}
	}


	/**
	 * 查询媒体状态进行是否满足删除条件的判断
	 *
	 * @param publisherId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/publisher/status", method = RequestMethod.GET)
	public AjaxResponse getStatus(@RequestParam(required = false) Integer publisherId) {

		if (ObjectUtils.isEmpty(publisherId)) {
			return AjaxResponse.error(400, "媒体ID不能为空");
		}
		Map<String, String> datas = new HashMap<>();
		try {
			PublisherDTO publisherDTO = publisherService.getByPublisherId(publisherId);
			datas.put("status", publisherDTO.getStatus().toString());
			return AjaxResponse.success(datas, "获取媒体状态成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "获取媒体状态失败");
		}
	}

	/**
	 * 删除单个媒体
	 *
	 * @param publisherForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/publisher", method = RequestMethod.DELETE)
	public AjaxResponse del(@RequestBody PublisherForm publisherForm) {

		if (ObjectUtils.isEmpty(publisherForm.getPublisherId())) {
			return AjaxResponse.error(400, "媒体参数校验失败");
		}


		Publisher publisher = new Publisher();
		publisher.setPublisherId(publisherForm.getPublisherId());
		publisher.setStatus(Publisher.Status.DELETE.getValue());
		try {
			publisherService.updateStatus(publisher);
			return AjaxResponse.success(publisher, "删除媒体成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "删除媒体失败");
		}
	}

	/**
	 * 修改媒体状态
	 *
	 * @param publisherForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/publisher/status", method = RequestMethod.PUT)
	public AjaxResponse editStatus(@RequestBody PublisherForm publisherForm) {

		if (ObjectUtils.isEmpty(publisherForm.getPublisherId()) || ObjectUtils.isEmpty(publisherForm.getStatus())) {
			return AjaxResponse.error(400, "媒体参数校验失败");
		}

		Publisher publisher = new Publisher();
		publisher.setPublisherId(publisherForm.getPublisherId());
		publisher.setStatus(publisherForm.getStatus());
		try {
			publisherService.updateStatus(publisher);
			return AjaxResponse.success(publisher, "修改媒体状态成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "修改媒体状态失败");
		}
	}


	/**
	 * 判断媒体是否获得下线的条件:该媒体下所有频道都已经下线
	 *
	 * @param publisherId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/publisher/offline", method = RequestMethod.GET)
	public AjaxResponse getOfficeCondition(@RequestParam(required = false) Integer publisherId) {

		if (ObjectUtils.isEmpty(publisherId)) {
			return AjaxResponse.error(400, "媒体Id不能为空");
		}
		Map<String, Integer> datas = new LinkedHashMap();
		try {
			Integer result = publisherService.getOfflineCondition(publisherId);
			datas.put("result", result);
			return AjaxResponse.success(datas, "查询媒体下线条件成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "查询媒体下线条件失败");
		}

	}


	/**
	 * 根据查询条件获取媒体列表
	 *
	 * @param page
	 * @param number
	 * @param keyword
	 * @param platform
	 * @param isInternal
	 * @param status
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/publishers", method = RequestMethod.GET)
	public AjaxResponse list(@RequestParam(required = false, defaultValue = "1") Integer page,
							 @RequestParam(required = false, defaultValue = "30") Integer number,
							 @RequestParam(required = false, defaultValue = "") String keyword,
							 @RequestParam(required = false, defaultValue = "0") Integer platform,
							 @RequestParam(required = false, defaultValue = "-1") Integer isInternal,
							 @RequestParam(required = false, defaultValue = "-2") Integer status) {

		Pageable pageable = new Pageable(page, number);

		try {
			int accountId = PrincipalUtil.getAccountId();
			Page<PublisherDTO> publishers = publisherService.getByPageable(pageable, keyword, platform, isInternal,
					accountId, status);
			return AjaxResponse.success(publishers, "获取媒体列表成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "获取媒体列表失败");
		}
	}
}
