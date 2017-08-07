package com.gomeplus.ams.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.gomeplus.adm.biz.dto.launch.DataMissionDTO;
import com.gomeplus.adm.biz.dto.launch.MissionAccountDTO;
import com.gomeplus.adm.biz.dto.launch.MissionDTO;
import com.gomeplus.adm.biz.dto.launch.MissionListDTO;
import com.gomeplus.adm.biz.model.launch.Mission;
import com.gomeplus.adm.biz.model.system.History;
import com.gomeplus.adm.biz.service.HistoryService;
import com.gomeplus.adm.biz.service.MissionService;
import com.gomeplus.adportal.client.PrincipalUtil;
import com.gomeplus.ams.form.MissionForm;
import com.gomeplus.ams.form.MissionTerminateForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.gomeplus.adm.biz.web.AjaxResponse;
import com.gomeplus.adm.biz.web.Page;
import com.gomeplus.adm.biz.web.Pageable;
import com.google.gson.Gson;

/**
 * 任务相关控制器
 * @author xiaogengen
 *
 */
@RestController
public class MissionController {
	//日志
	private Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private MissionService missionService;
	
	@Autowired
	private HistoryService historyService;
	

	
	@Value("${privilege_system_id}")
	private Integer privilegeSystemId;
	
	private Gson gson = new Gson();
	/**
	 * 新建用户任务
	 * @param missionForm
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "/api/mission", method = RequestMethod.POST)
	public AjaxResponse saveMission(@Validated @RequestBody MissionForm missionForm, 
			BindingResult bindingResult,
			HttpServletRequest request) {

		if (bindingResult.hasErrors()) {
			return AjaxResponse.error(400, "用户任务参数校验失败");
		}

		try {
			Mission mission = new Mission();
			BeanUtils.copyProperties(missionForm, mission);

			mission.setAccountId(PrincipalUtil.getAccountId());
			missionService.saveMission(mission);
			
			History history = new History();
			
			history.setAccountId(PrincipalUtil.getAccountId());
			history.setAccountName(PrincipalUtil.getName());
			history.setCreateTime(new Date());
			history.setField(gson.toJson(missionForm));
			history.setNewData(gson.toJson(mission));
			history.setObject("MISSION");
			history.setObjectId("[" + mission.getMissionId() + "]");
			history.setObjectName("["+ mission.getName() + "]");
			history.setOldData("{}");
			history.setSystemId(privilegeSystemId);
			history.setType(1);
			history.setIp(request.getRemoteAddr());
			historyService.saveHistory(history);
			return AjaxResponse.success("添加用户任务成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "添加用户任务失败! " + ex.getMessage());
		}

	}

	/**
	 * 修改用户任务
	 * @param missionForm
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "/api/mission", method = RequestMethod.PUT)
	public AjaxResponse updateMission(@Validated @RequestBody MissionForm missionForm, 
			BindingResult bindingResult,
			HttpServletRequest request) {
		if (bindingResult.hasErrors()) {
			return AjaxResponse.error(400, "用户任务参数校验失败");
		}

		try {
			Mission mission = new Mission();
			BeanUtils.copyProperties(missionForm, mission);
			MissionDTO oldMission = missionService.getMissionByMissionId(mission.getMissionId());
			missionService.updateMission(mission);
			History history = new History();
			
			history.setAccountId(PrincipalUtil.getAccountId());
			history.setAccountName(PrincipalUtil.getName());
			history.setCreateTime(new Date());
			history.setField(gson.toJson(missionForm));
			history.setNewData(gson.toJson(mission));
			history.setObject("MISSION");
			history.setObjectId("[" + mission.getMissionId() + "]");
			history.setObjectName("["+ mission.getName() + "]");
			history.setOldData(gson.toJson(oldMission));
			history.setSystemId(privilegeSystemId);
			history.setType(2);
			history.setIp(request.getRemoteAddr());
			historyService.saveHistory(history);
			return AjaxResponse.success("修改用户任务成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "修改用户任务失败! " + ex.getMessage());
		}
	}

	/**
	 * 终止用户任务
	 * @param missionForm
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "/api/mission/terminate", method = RequestMethod.PUT)
	public AjaxResponse terminateMission(@Validated @RequestBody MissionTerminateForm missionForm, 
			BindingResult bindingResult,
			HttpServletRequest request) {
		if (bindingResult.hasErrors()) {
			return AjaxResponse.error(400, "用户任务参数校验失败");
		}

		try {
			Mission mission = new Mission();
			BeanUtils.copyProperties(missionForm, mission);
			MissionDTO oldMission = missionService.getMissionByMissionId(mission.getMissionId());
			missionService.terminateMission(mission);
			History history = new History();
			
			history.setAccountId(PrincipalUtil.getAccountId());
			history.setAccountName(PrincipalUtil.getName());
			history.setCreateTime(new Date());
			Map<String,Object> map = new HashMap<>();
			map.put("status", mission.getStatus());
			history.setField(gson.toJson(map));
			history.setNewData(gson.toJson(map));
			history.setObject("MISSION");
			history.setObjectId("[" + mission.getMissionId() + "]");
			history.setObjectName("["+ mission.getName() + "]");
			Map<String,Object> oldMap = new HashMap<>();
			oldMap.put("status", oldMission.getStatus());
			history.setOldData(gson.toJson(oldMap));
			history.setSystemId(privilegeSystemId);
			history.setType(4);
			history.setIp(request.getRemoteAddr());
			historyService.saveHistory(history);
			return AjaxResponse.success("终止用户任务成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "终止用户任务失败! " + ex.getMessage());
		}
	}

	/**
	 * 查询任务列表
	 * @param page
	 * @param number
	 * @param startTime
	 * @param endTime
	 * @param missionStatus(全部、有效、过期、未开始)
	 * @param keyword
	 * @return
	 */
	@GetMapping(value = "/api/missions")
	public AjaxResponse getMissions(
			@RequestParam(required = false, defaultValue = "") String keyword,
			@RequestParam(required = false, defaultValue = "1") Integer page,
			@RequestParam(required = false, defaultValue = "30") Integer number,
			@RequestParam(required = false, defaultValue = "0") Long startTime,
			@RequestParam(required = false, defaultValue = "0") Long endTime,
			@RequestParam(required = false, defaultValue = "0") Integer missionStatus
			) {
		try {
			Pageable pageable = new Pageable(page, number);
			Page<MissionListDTO> missions = missionService.getMissions(keyword, startTime, endTime,
					missionStatus, pageable);
			return AjaxResponse.success(missions, "查询用户任务列表成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "查询用户任务列表失败");
		}

	}
	/**
	 * 查询单条用户任务信息
	 * @param missionId
	 * @return
	 */
	@GetMapping(value = "/api/mission")
	public AjaxResponse getMission(@RequestParam(required = true, defaultValue = "0") Integer missionId) {
		try {
			MissionDTO missionDTO = missionService.getMissionByMissionId(missionId);
			return AjaxResponse.success(missionDTO, "查询单条用户任务成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "查询单条用户任务失败");
		}

	}
	/**
	 * 获取任务报表数据
	 * @param keyword
	 * @param startTime
	 * @param endTime
	 * @param status
	 * @param page
	 * @param number
	 * @return
	 */
	@RequestMapping(value = "/api/report/mission", method = RequestMethod.GET)
	public AjaxResponse getReportMission(@RequestParam(required = false) String keyword,
			@RequestParam(required = false) Long startTime,
			@RequestParam(required = false) Long endTime,
			@RequestParam(required = false) Integer status,
			@RequestParam(required = false, defaultValue = "1") Integer page,
			@RequestParam(required = false, defaultValue = "30") Integer number) {
		Page<DataMissionDTO> dataMissionDTOs = null;
		Pageable pageable = new Pageable(page,number);
		try {
			dataMissionDTOs = missionService.getReportMission(startTime, endTime, keyword, status, pageable);
			return AjaxResponse.success(dataMissionDTOs, "获取任务列表数据成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "获取任务列表数据失败");
		}
	}
	
	
	/**
	 * 导出任务列表
	 * @param keyword
	 * @param startTime
	 * @param endTime
	 * @param status
	 * @param page
	 * @param number
	 * @return
	 */
	@RequestMapping(value = "/api/report/mission/export", method = RequestMethod.GET)
	public AjaxResponse exportReportMission(@RequestParam(required = false) String keyword,
			@RequestParam(required = false) Long startTime,
			@RequestParam(required = false) Long endTime,
			@RequestParam(required = false) Integer status,
			@RequestParam(required = false, defaultValue = "1") Integer page,
			@RequestParam(required = false, defaultValue = "30") Integer number,
			HttpServletResponse res) {
		Page<DataMissionDTO> dataMissionDTOs = null;
		Pageable pageable = new Pageable(page,number);
		try {
			dataMissionDTOs = missionService.getReportMission(startTime, endTime, keyword, status, pageable);
			missionService.exportReportMission(dataMissionDTOs.getList(), res);
			return AjaxResponse.success("导出任务列表数据成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "导出任务列表数据失败");
		}
	}
	/**
	 * 获取任务帐户信息
	 * @return
	 */
	@RequestMapping(value = "/api/mission/account", method = RequestMethod.GET)
	public AjaxResponse getMissionAccount(){
		MissionAccountDTO missionAccountDTO = null;
		try {
			missionAccountDTO = missionService.getMissioinAccount();
			return AjaxResponse.success(missionAccountDTO, "获取任务帐户信息成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "获取任务帐户信息失败");
		}
		
	}
}
