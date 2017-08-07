package com.gomeplus.ams.controller;

import com.gomeplus.adm.biz.dto.ad.ChannelDTO;
import com.gomeplus.adm.biz.model.ad.Channel;
import com.gomeplus.adm.biz.service.ChannelService;
import com.gomeplus.adm.biz.web.AjaxResponse;
import com.gomeplus.adm.biz.web.Page;
import com.gomeplus.adm.biz.web.Pageable;
import com.gomeplus.adportal.client.PrincipalUtil;
import com.gomeplus.ams.form.ChannelForm;

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
import java.util.List;
import java.util.Map;


/**
 * Created by liuchen on 2017/3/21.
 */
@Controller
public class ChannelController {

	@Autowired
	private ChannelService channelService;

	private Logger logger = LogManager.getLogger(getClass());


	/**
	 * 获取单个频道详情回显
	 *
	 * @param channelId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/channel", method = RequestMethod.GET)
	public AjaxResponse get(@RequestParam(required = false) Integer channelId) {

		if (ObjectUtils.isEmpty(channelId)) {
			return AjaxResponse.error(400, "频道Id不能为空");
		}

		try {
			ChannelDTO channelDTO = channelService.getByChannelId(channelId);

			return AjaxResponse.success(channelDTO, "获取单个频道详情数据成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "获取单个频道详情数据失败");
		}
	}


	/**
	 * 添加频道
	 *
	 * @param channelForm
	 * @param bindingResult
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/channel", method = RequestMethod.POST)
	public AjaxResponse add(@Validated @RequestBody ChannelForm channelForm, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return AjaxResponse.error(400, "频道参数校验失败");
		}


		Channel channel = new Channel();
		BeanUtils.copyProperties(channelForm, channel);

		channel.setAccountId(PrincipalUtil.getAccountId());
		try {
			channelService.save(channel);
			return AjaxResponse.success(channel, "添加频道成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "添加频道失败");
		}
	}

	/**
	 * 修改单个频道
	 *
	 * @param channelForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/channel", method = RequestMethod.PUT)
	public AjaxResponse edit(@RequestBody ChannelForm channelForm) {

		if (ObjectUtils.isEmpty(channelForm.getChannelId()) || ObjectUtils.isEmpty(channelForm.getName())) {
			return AjaxResponse.error(400, "频道参数校验失败");
		}

		Channel channel = new Channel();
		BeanUtils.copyProperties(channelForm, channel);
		channel.setAccountId(PrincipalUtil.getAccountId());
		try {
			channelService.update(channel);
			return AjaxResponse.success(channel, "修改频道成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "修改频道失败");
		}
	}

	/**
	 * 查询频道状态进行是否满足删除条件的判断
	 *
	 * @param channelId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/channel/status", method = RequestMethod.GET)
	public AjaxResponse getStatus(@RequestParam(required = false) Integer channelId) {

		if (ObjectUtils.isEmpty(channelId)) {
			return AjaxResponse.error(400, "频道ID不能为空");
		}
		Map<String, String> datas = new LinkedHashMap<>();
		try {
			ChannelDTO channelDTO = channelService.getByChannelId(channelId);
			datas.put("status", channelDTO.getStatus().toString());
			return AjaxResponse.success(datas, "获取频道状态成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "获取频道状态失败");
		}
	}

	/**
	 * 删除单个频道
	 *
	 * @param channelForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/channel", method = RequestMethod.DELETE)
	public AjaxResponse del(@RequestBody ChannelForm channelForm) {

		if (ObjectUtils.isEmpty(channelForm.getChannelId())) {
			return AjaxResponse.error(400, "频道参数校验失败");
		}

		//频道下线

		Channel channel = new Channel();
		channel.setChannelId(channelForm.getChannelId());
		channel.setStatus(Channel.Status.DELETE.getValue());
		try {
			channelService.updateStatus(channel);
			return AjaxResponse.success(channel, "删除频道成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "删除频道失败");
		}
	}

	/**
	 * 修改频道状态
	 *
	 * @param channelForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/channel/status", method = RequestMethod.PUT)
	public AjaxResponse editStatus(@RequestBody ChannelForm channelForm) {

		if (ObjectUtils.isEmpty(channelForm.getChannelId()) || ObjectUtils.isEmpty(channelForm.getStatus())) {
			return AjaxResponse.error(400, "频道参数校验失败");
		}

		// 频道上线需要判断媒体是否已经上线

		Channel channel = new Channel();
		channel.setChannelId(channelForm.getChannelId());
		channel.setStatus(channelForm.getStatus());
		try {
			channelService.updateStatus(channel);
			return AjaxResponse.success(channel, "修改频道状态成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "修改频道状态失败");
		}
	}

	/**
	 * 判断频道是否获得下线的条件:该频道以及子频道下所有广告位都下线
	 *
	 * @param channelId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/channel/offline", method = RequestMethod.GET)
	public AjaxResponse getOfficeCondition(@RequestParam(required = false) Integer channelId) {

		if (ObjectUtils.isEmpty(channelId)) {
			return AjaxResponse.error(400, "频道Id不能为空");
		}
		Map<String, Integer> datas = new LinkedHashMap();
		try {
			Integer result = channelService.getOfflineCondition(channelId);
			datas.put("result", result);
			return AjaxResponse.success(datas, "查询频道下线条件成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "查询频道下线条件失败");
		}

	}


	/**
	 * 判断频道是否获得上线的条件
	 *
	 * @param channelId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/channel/online", method = RequestMethod.GET)
	public AjaxResponse getOnlineCondition(@RequestParam(required = false) Integer channelId) {

		if (ObjectUtils.isEmpty(channelId)) {
			return AjaxResponse.error(400, "频道Id不能为空");
		}
		Map<String, Integer> datas = new LinkedHashMap();
		try {
			int result = channelService.getOnlineCondition(channelId);
			datas.put("result", result);
			return AjaxResponse.success(datas, "查询频道上线条件成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "查询频道上线条件失败");
		}

	}


	/**
	 * 判断是否有子频道
	 *
	 * @param channelId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/channel/children", method = RequestMethod.GET)
	public AjaxResponse getChildrenChannel(@RequestParam(required = false) Integer channelId) {

		if (ObjectUtils.isEmpty(channelId)) {
			return AjaxResponse.error(400, "频道Id不能为空");
		}
		Map<String, Integer> datas = new LinkedHashMap();
		try {
			Integer result = channelService.getChildrenChannel(channelId);
			datas.put("result", result);
			return AjaxResponse.success(datas, "查询子频道成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "查询子频道失败");
		}

	}


	/**
	 * 根据查询条件获取频道列表
	 *
	 * @param page
	 * @param number
	 * @param keyword
	 * @param parentId
	 * @param status
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/channels", method = RequestMethod.GET)
	public AjaxResponse list(@RequestParam Integer publisherId,
							 @RequestParam(required = false, defaultValue = "1") Integer page,
							 @RequestParam(required = false, defaultValue = "30") Integer number,
							 @RequestParam(required = false, defaultValue = "") String keyword,
							 @RequestParam(required = false, defaultValue = "") Integer parentId,
							 @RequestParam(required = false, defaultValue = "") Integer status) {

		Pageable pageable = new Pageable(page, number);

		try {
			int accountId = PrincipalUtil.getAccountId();
			Page<ChannelDTO> channels = channelService.getByPageable(pageable, publisherId, keyword, parentId,
					accountId, status);
			return AjaxResponse.success(channels, "获取频道列表成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "获取频道列表失败");
		}
	}


	/**
	 * 根据publisherId 或者 parentId查询所有频道
	 *
	 * @param publisherId
	 * @param channelId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/api/allChannels", method = RequestMethod.GET)
	public AjaxResponse getAllSecondChannels(@RequestParam(required = false) Integer publisherId,
											 @RequestParam(required = false) Integer channelId) {

		if (ObjectUtils.isEmpty(channelId) && ObjectUtils.isEmpty(publisherId)) {
			return AjaxResponse.error(400, "媒体Id和频道Id不能同时为空");
		}
		Channel channel = new Channel();
		if (!ObjectUtils.isEmpty(publisherId)) {
			channel.setPublisherId(publisherId);
		}
		if (!ObjectUtils.isEmpty(channelId)) {
			channel.setParentId(channelId);
		}

		try {
			List<ChannelDTO> allChannels = channelService.getAllChannels(channel);

			return AjaxResponse.success(allChannels, "根据publisherId或者channelId获取所有频道成功");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return AjaxResponse.error(500, "根据publisherId或者channelId获取所有频道失败");
		}
	}
}
