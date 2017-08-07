package com.gomeplus.ams.controller;

import com.gomeplus.adm.biz.model.system.Role;
import com.gomeplus.adm.biz.service.RoleService;
import com.gomeplus.adm.biz.web.AjaxResponse;
import com.gomeplus.adm.biz.web.Page;
import com.gomeplus.adm.biz.web.Pageable;
import com.gomeplus.ams.form.RoleForm;

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
 * 角色控制器
 *
 * @author baishen
 */
@Controller
public class RoleController {

	@Autowired
	private RoleService roleService;

	private Logger logger = LogManager.getLogger(getClass());


	/**
	 * 获取单个角色详情回显
	 *
	 * @param roleId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/role", method = RequestMethod.GET)
	public AjaxResponse get(@RequestParam(required = false) Integer roleId) {

		if (ObjectUtils.isEmpty(roleId)) {
			return AjaxResponse.error(400, "角色Id不能为空");
		}

		try {
			Role role = roleService.getByRoleId(roleId);

			return AjaxResponse.success(role, "获取单个角色详情数据成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "获取单个角色详情数据失败");
		}
	}

	/**
	 * 添加角色
	 *
	 * @param roleForm
	 * @param bindingResult
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/role", method = RequestMethod.POST)
	public AjaxResponse add(@Validated @RequestBody RoleForm roleForm, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return AjaxResponse.error(400, "角色参数校验失败");
		}

		Role role = new Role();
		BeanUtils.copyProperties(roleForm, role);

		try {
			roleService.save(role);
			return AjaxResponse.success(role, "添加角色成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "添加角色失败");
		}
	}

	/**
	 * 修改单个角色
	 *
	 * @param roleForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/role", method = RequestMethod.PUT)
	public AjaxResponse edit(@RequestBody RoleForm roleForm) {

		if (ObjectUtils.isEmpty(roleForm.getRoleId()) || ObjectUtils.isEmpty(roleForm.getName())) {
			return AjaxResponse.error(400, "角色参数校验失败");
		}

		Role role = new Role();
		BeanUtils.copyProperties(roleForm, role);

		try {
			roleService.update(role);
			return AjaxResponse.success(role, "修改角色成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "修改角色失败");
		}
	}

	/**
	 * 根据查询条件获取角色列表
	 *
	 * @param page
	 * @param number
	 * @param keyword
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/roles", method = RequestMethod.GET)
	public AjaxResponse list(@RequestParam(required = false, defaultValue = "1") Integer page,
							 @RequestParam(required = false, defaultValue = "30") Integer number,
							 @RequestParam(required = false, defaultValue = "") String keyword) {

		Pageable pageable = new Pageable(page, number);

		try {
			Page<Role> roles = roleService.getByPageable(pageable, keyword);
			return AjaxResponse.success(roles, "获取角色列表成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "获取角色列表失败");
		}
	}
}
