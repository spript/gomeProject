package com.gomeplus.dms.controller;

import com.gomeplus.adm.biz.web.AjaxResponse;
import com.gomeplus.adm.biz.service.AuthService;
import com.gomeplus.adm.biz.dto.system.AuthAccountDTO;

import com.gomeplus.adportal.client.PrincipalUtil;
//import com.gomeplus.adportal.core.remote.RemoteShiroServiceInterface;
import com.gomeplus.adportal.core.remote.model.PrivilegeRemote;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 授权Controller
 * @author baishen
 */
@RestController
public class AuthController {

	// @Autowired
	// private RemoteShiroServiceInterface remoteShiroService;

	@Autowired
	private AuthService authService;

	private Logger logger = LogManager.getLogger(getClass());

	private static final String APP = "DMS";

	private static final Integer SYSTEM_ID = 2;

	/**
	 * 获取账号基本信息
	 */
	@RequestMapping(value = "/api/auth/account", method = RequestMethod.GET)
	public AjaxResponse account() {

		Integer accountId = 1;

		AuthAccountDTO authAccountDTO = authService.getByAccountIdAndSystemId(accountId, SYSTEM_ID);

		return AjaxResponse.success(authAccountDTO, "获取账号信息成功");
	}

	/**
	 * 退出登录
	 * @return
	 */
	@RequestMapping(value = "/api/logout", method = RequestMethod.POST)
	public AjaxResponse logout() {
		PrincipalUtil.getSubject().logout();
		return AjaxResponse.success("退出登录成功");
	}
}