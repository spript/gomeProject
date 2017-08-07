package com.gomeplus.ams.controller;

import com.gomeplus.adm.biz.web.AjaxResponse;
import com.gomeplus.adportal.client.PrincipalUtil;
import com.gomeplus.adportal.core.remote.RemoteShiroServiceInterface;
import com.gomeplus.adportal.core.remote.model.PrivilegeRemote;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

	@Value("${privilege_system_id}")
	private Integer SYSTEM_ID;

	@Autowired
	private RemoteShiroServiceInterface remoteShiroService;

	private Logger logger = LogManager.getLogger(getClass());

	/**
	 * AMS首页
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index() {

		String environment = System.getenv().get("ENVIRONMENT");
		if (environment == null) {
			environment = "development";
		}
		String indexPath = environment + "/index";

		//logger.info("index path is " + indexPath);

		return indexPath;
	}

	/**
	 * 获取有权限的目录
	 * @return
	 */
	@RequestMapping("/menus")
	@ResponseBody
	public AjaxResponse getMenu() {

		Integer accountId = PrincipalUtil.getAccountId();
		List<PrivilegeRemote> privileges = new ArrayList<PrivilegeRemote>();

		try {
			if(null != accountId) {
				List<PrivilegeRemote> sourcePrivileges = remoteShiroService.getPrivilegesByAccountIdAndSystemId(accountId, SYSTEM_ID);
				PrivilegeRemote.sortPrivileges(privileges, sourcePrivileges, 0);

			}

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("data", privileges);

			return AjaxResponse.success(data, "获取有权限的目录成功");
		} catch (Exception e) {
			logger.error("获取有权限的目录失败" + e.getMessage());
			return AjaxResponse.error(400, "获取有权限的目录失败");
		}
	}
}
