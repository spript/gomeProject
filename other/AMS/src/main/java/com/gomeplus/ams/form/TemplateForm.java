package com.gomeplus.ams.form;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class TemplateForm implements Serializable {


	private static final long serialVersionUID = -6888179027659926300L;

	private Integer templateId;

	@NotEmpty(message = "模板名称不能为空")
	private String name;
	private Integer width;
	private Integer height;
	@NotEmpty(message = "模板代码不能为空")
	private String content;
	@NotNull(message = "代码类型不能为空")
	@Min(value = 1, message = "请输入有效代码类型")
	private Integer type;
	@NotNull(message = "素材个数不能为空")
	@Min(value = 1, message = "请输入有效的素材个数")
	private Integer materialNumber;
	private Integer status;
}
