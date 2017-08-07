package com.gomeplus.ams.controller;

import com.gomeplus.adm.biz.dto.system.PrivilegeDTO;
import com.gomeplus.adm.biz.model.system.Privilege;
import com.gomeplus.adm.biz.service.PrivilegeService;
import com.gomeplus.adm.biz.web.AjaxResponse;
import com.gomeplus.adm.biz.web.Page;
import com.gomeplus.adm.biz.web.Pageable;
import com.gomeplus.ams.form.PrivilegeForm;

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
 * 权限控制器
 *
 * @author baishen
 */
@Controller
public class PrivilegeController {

	@Autowired
	private PrivilegeService privilegeService;

	private Logger logger = LogManager.getLogger(getClass());


	/**
	 * 获取单个权限详情回显
	 *
	 * @param privilegeId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/privilege", method = RequestMethod.GET)
	public AjaxResponse get(@RequestParam(required = false) Integer privilegeId) {

		if (ObjectUtils.isEmpty(privilegeId)) {
			return AjaxResponse.error(400, "权限Id不能为空");
		}

		try {
			PrivilegeDTO privilege = privilegeService.getByPrivilegeId(privilegeId);

			return AjaxResponse.success(privilege, "获取单个权限详情数据成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "获取单个权限详情数据失败");
		}
	}

	/**
	 * 添加权限
	 *
	 * @param privilegeForm
	 * @param bindingResult
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/privilege", method = RequestMethod.POST)
	public AjaxResponse add(@Validated @RequestBody PrivilegeForm privilegeForm, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return AjaxResponse.error(400, "权限参数校验失败");
		}

		Privilege privilege = new Privilege();
		BeanUtils.copyProperties(privilegeForm, privilege);

		try {
			privilegeService.save(privilege);
			return AjaxResponse.success(privilege, "添加权限成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "添加权限失败");
		}
	}

	/**
	 * 修改单个权限
	 *
	 * @param privilegeForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/privilege", method = RequestMethod.PUT)
	public AjaxResponse edit(@RequestBody PrivilegeForm privilegeForm) {

		if (ObjectUtils.isEmpty(privilegeForm.getPrivilegeId()) || ObjectUtils.isEmpty(privilegeForm.getName())) {
			return AjaxResponse.error(400, "权限参数校验失败");
		}

		Privilege privilege = new Privilege();
		BeanUtils.copyProperties(privilegeForm, privilege);

		try {
			privilegeService.update(privilege);
			return AjaxResponse.success(privilege, "修改权限成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "修改权限失败");
		}
	}


}
