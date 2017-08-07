package com.gomeplus.ams.form;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;


@Data
public class SlotForm implements Serializable {


	private static final long serialVersionUID = -6888179027659926300L;

	private Integer slotId;
	@NotNull(message = "媒体不能为空")
	private Integer publisherId;
	@NotNull(message = "频道不能为空")
	private Integer channelId;

	@NotEmpty(message = "广告位名称不能为空")
	private String name;

	private String description = "";
	// 审核流程
	private Integer approveFlowId;
	
	private Integer width;
	private Integer height;

	@NotNull(message = "轮播数不能为空")
	@Min(value = 1, message = "请输入有效的轮播数")
	private Integer rotators;

	@NotNull(message = "外采流量不能为空")
	private Integer outSourceFlow;

	@NotNull(message = "单次请求返回素材数量不能为空")
	private Integer materialNumber;

	private String parentId;

	private Integer status;

	@DateTimeFormat(iso= DateTimeFormat.ISO.DATE)
	private Date effectiveTime;
}
