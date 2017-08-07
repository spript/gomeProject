package com.gomeplus.ams.controller;


import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gomeplus.adm.biz.dao.system.RoleDao;
import com.gomeplus.adm.biz.dto.approval.ApprovalMaterialDTO;
import com.gomeplus.adm.biz.dto.launch.CertificateDTO;
import com.gomeplus.adm.biz.dto.system.ApprovalLogDTO;
import com.gomeplus.adm.biz.model.system.Role;
import com.gomeplus.adm.biz.service.CertificateService;
import com.gomeplus.adm.biz.service.MaterialService;
import com.gomeplus.adm.biz.service.RoleService;
import com.gomeplus.adm.biz.web.AjaxResponse;
import com.gomeplus.adm.biz.web.Page;
import com.gomeplus.adm.biz.web.Pageable;
import com.gomeplus.adportal.client.PrincipalUtil;
import com.gomeplus.ams.form.MaterialForm;

/**
 * 素材审核
 *
 * @author suna01
 */
@RestController
public class MaterialApprovalController {
	
	private Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	private MaterialService materialService;
	@Autowired
	private CertificateService certificateService;
	@Autowired
	private RoleService roleService;
	
	/**
	 * 素材审核列表
	 * @param request
	 * @param advertisementId
	 * @param advertiserId
	 * @param startTime
	 * @param endTime
	 * @param advertisementType
	 * @param accountType
	 * @param approveStatus
	 * @return
	 */

	@RequestMapping(value = "/api/approval/materials", method = RequestMethod.GET)
	public AjaxResponse getApprovalMaterials(
			@RequestParam(required = false, defaultValue = "1") Integer page,
			@RequestParam(required = false, defaultValue = "30") Integer number,
			@RequestParam(required = false, defaultValue = "0") Long startTime,
			@RequestParam(required = false, defaultValue = "0") Long endTime,
			@RequestParam(required = false, defaultValue = "0") Integer advertisementType,
			@RequestParam(required = false, defaultValue = "0") Integer accountType,
			@RequestParam(required = false, defaultValue = "0") Integer approveStatus,
			@RequestParam(required = false, defaultValue = "asc") String order,
			@RequestParam(required = false, defaultValue = "createTime") String orderBy,
			@RequestParam(required = false, defaultValue = "") String advertisementName,
			@RequestParam(required = false, defaultValue = "") String advertiserName) {
		
		Pageable pageable = new Pageable(page, number);
		try {
			Integer accountId = PrincipalUtil.getAccountId();
			List<Role> roles = roleService.getByAccountId(accountId);
			List<String> roleNames = new ArrayList<String>();
			for (Role role : roles) {
				String roleName = role.getRoleName();
				if (!roleNames.contains(roleName)) {
					roleNames.add(roleName);
				}
			}
			Page<ApprovalMaterialDTO> materials = materialService.getMaterials(pageable, roleNames, advertisementName, advertiserName, startTime, endTime, advertisementType, accountType, approveStatus, order, orderBy);
			return AjaxResponse.success(materials, "获取素材信息成功！");
		} catch (Exception e) {
			logger.error("get approval materials failed", e);
			return AjaxResponse.error(500, "获取素材信息失败！" + e);
		}
	}
	
	/**
	 * 批量通过
	 * @param materialIds 素材id集合
	 * @return
	 */
	@RequestMapping(value = "/api/approval/materials/pass", method=RequestMethod.PUT)
	public AjaxResponse batchPassMaterials(@RequestBody MaterialForm materialForm) {
		List<Integer> materialIds = materialForm.getMaterialIds();
		try {
			Integer accountId = PrincipalUtil.getAccountId();
			String name = PrincipalUtil.getName();
			materialService.batchPass(materialIds, accountId, name);
		} catch (Exception exception) {
			logger.error("pass materialIds failed ",exception);
			return AjaxResponse.error(400, "批量通过素材失败！" + exception.getMessage());
		}

		return AjaxResponse.success("素材审核通过！");
	}
	
	/**
	 * 根据materialId获取创意
	 * @param materialId
	 * @return
	 */
	@RequestMapping(value = "/api/approval/material", method = RequestMethod.GET)
	public AjaxResponse getMaterial(@RequestParam Integer materialId) {

		try {
			
			ApprovalMaterialDTO approvalMaterialDTO = materialService.getMaterial(materialId);
			return AjaxResponse.success(approvalMaterialDTO, "获取创意数据成功！");
		} catch (Exception exception) {
			logger.error("material", exception);
			return AjaxResponse.error(400, "获取创意数据失败！" + exception.getMessage());
		}
	}
	
	/**
	 * 获取商家资质信息
	 * @return
	 */
	@RequestMapping(value = "/api/certificate", method = RequestMethod.GET)
	public AjaxResponse getCertificate(@RequestParam(required = true) Integer userId) {

		try {
			List<CertificateDTO> certificates = certificateService.getCertificates(userId);
			return AjaxResponse.success(certificates, "获取商家资质成功!");
		} catch (Exception e) {
			logger.error("获取商家资质失败! 原因如下:" + e.getMessage());
			return AjaxResponse.error(400, "获取商家资质失败!");
		}
	}
	
	/**
	 * 素材审核拒绝
	 * @param materialForm
	 * @return
	 */
	@RequestMapping(value = "/api/approval/materials/reject", method=RequestMethod.PUT)
	public AjaxResponse rejectMaterial(@RequestBody MaterialForm materialForm) {
		Integer materialId = materialForm.getMaterialId();
		List<Object> attachments = materialForm.getAttachments();
		List<Object> rejectReasons = materialForm.getRejectReasons();
		try {
			Integer accountId = PrincipalUtil.getAccountId();
			String name = PrincipalUtil.getName();
			materialService.reject(materialId, attachments, rejectReasons, accountId, name);
		} catch (Exception exception) {
			logger.error("reject material failed ", exception);
			return AjaxResponse.error(400, "素材审核拒绝失败！" + exception.getMessage());
		}

		return AjaxResponse.success("素材审核拒绝通过！");
	}
	
	/**
	 * 根据materialId获取审核的全部记录
	 * @param materialId
	 * @return
	 */
	@RequestMapping(value = "/api/approval/logs", method = RequestMethod.GET)
	public AjaxResponse getMaterialReasons(@RequestParam Integer materialId) {

		try {
			
			List<ApprovalLogDTO> approvalLogDTOs = materialService.getMaterialApprovalLogs(materialId);
			return AjaxResponse.success(approvalLogDTOs, "获取审核记录数据成功！");
		} catch (Exception exception) {
			logger.error("material", exception);
			return AjaxResponse.error(400, "获取审核记录数据失败！" + exception.getMessage());
		}
	}
}