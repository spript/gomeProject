package com.gomeplus.ams.form;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ChannelForm implements Serializable {


	private static final long serialVersionUID = -6888179027659926300L;

	private Integer channelId;

	@NotNull(message = "媒体值不能为空")
	@Min(value = 1, message = "媒体值不正确")
	private Integer publisherId;

	@NotEmpty(message = "频道名称不能为空")
	private String name;

	private String description = "";
	private String url = "";
	private String path = "";

	@NotNull(message = "父频道值不能为空")
	@Min(value = 0, message = "父频道值不正确")
	private Integer parentId;

	private Integer status;
}
