package com.gomeplus.ams.form;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class FormatForm implements Serializable {


	private static final long serialVersionUID = -1215644193284114025L;

	private Integer formatId;

	@NotEmpty(message = "广告形式名称不能为空")
	private String name;

//	@NotEmpty(message = "广告元素不能为空")
	private Map<String,List<Map<String,String>>> adElement;

	private Integer status;

	private Date offlineTime;


}
