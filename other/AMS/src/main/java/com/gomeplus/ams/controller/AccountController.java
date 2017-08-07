package com.gomeplus.ams.controller;

import com.gomeplus.adm.biz.dto.system.AccountDTO;
import com.gomeplus.adm.biz.model.system.Account;
import com.gomeplus.adm.biz.service.AccountService;
import com.gomeplus.adm.biz.web.AjaxResponse;
import com.gomeplus.adm.biz.web.Page;
import com.gomeplus.adm.biz.web.Pageable;
import com.gomeplus.ams.form.AccountForm;

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
 * 账号控制器
 *
 * @author baishen
 */
@Controller
public class AccountController {

	@Autowired
	private AccountService accountService;

	private Logger logger = LogManager.getLogger(getClass());


	/**
	 * 获取单个账号详情回显
	 *
	 * @param accountId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/account", method = RequestMethod.GET)
	public AjaxResponse get(@RequestParam(required = false) Integer accountId) {

		if (ObjectUtils.isEmpty(accountId)) {
			return AjaxResponse.error(400, "账号Id不能为空");
		}

		try {
			AccountDTO account = accountService.getByAccountId(accountId);

			return AjaxResponse.success(account, "获取单个账号详情数据成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "获取单个账号详情数据失败");
		}
	}

	/**
	 * 添加账号
	 *
	 * @param accountForm
	 * @param bindingResult
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/account", method = RequestMethod.POST)
	public AjaxResponse add(@Validated @RequestBody AccountForm accountForm, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return AjaxResponse.error(400, "账号参数校验失败");
		}

		Account account = new Account();
		BeanUtils.copyProperties(accountForm, account);

		try {
			accountService.save(account);
			return AjaxResponse.success(account, "添加账号成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "添加账号失败");
		}
	}

	/**
	 * 修改单个账号
	 *
	 * @param accountForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/account", method = RequestMethod.PUT)
	public AjaxResponse edit(@RequestBody AccountForm accountForm) {

		if (ObjectUtils.isEmpty(accountForm.getAccountId()) || ObjectUtils.isEmpty(accountForm.getName())) {
			return AjaxResponse.error(400, "账号参数校验失败");
		}

		Account account = new Account();
		BeanUtils.copyProperties(accountForm, account);

		try {
			accountService.update(account);
			return AjaxResponse.success(account, "修改账号成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "修改账号失败");
		}
	}

	/**
	 * 删除单个账号
	 *
	 * @param accountForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/account", method = RequestMethod.DELETE)
	public AjaxResponse delete(@RequestBody AccountForm accountForm) {

		if (ObjectUtils.isEmpty(accountForm.getAccountId())) {
			return AjaxResponse.error(500, "账号参数校验失败");
		}

		//账号下线

		Account account = new Account();
		account.setAccountId(accountForm.getAccountId());
		account.setStatus(accountForm.getStatus());
		try {
			accountService.updateStatus(account);
			return AjaxResponse.success(account, "删除账号成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "删除账号失败");
		}
	}

	/**
	 * 修改账号状态
	 *
	 * @param accountForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/account/status", method = RequestMethod.PUT)
	public AjaxResponse editStatus(@RequestBody AccountForm accountForm) {

		if (ObjectUtils.isEmpty(accountForm.getAccountId()) || ObjectUtils.isEmpty(accountForm.getStatus())) {
			return AjaxResponse.error(400, "账号参数校验失败");
		}

		// 账号上线需要判断媒体是否已经上线
		Account account = new Account();
		account.setAccountId(accountForm.getAccountId());
		account.setStatus(accountForm.getStatus());
		try {
			accountService.updateStatus(account);
			return AjaxResponse.success(account, "修改账号状态成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "修改账号状态失败");
		}
	}

	/**
	 * 根据查询条件获取账号列表
	 *
	 * @param page
	 * @param number
	 * @param keyword
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/accounts", method = RequestMethod.GET)
	public AjaxResponse list(@RequestParam(required = false, defaultValue = "1") Integer page,
							 @RequestParam(required = false, defaultValue = "30") Integer number,
							 @RequestParam(required = false, defaultValue = "") String keyword) {

		Pageable pageable = new Pageable(page, number);

		try {
			Page<AccountDTO> accounts = accountService.getByPageable(pageable, keyword);
			return AjaxResponse.success(accounts, "获取账号列表成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "获取账号列表失败");
		}
	}
}
