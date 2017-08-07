package com.gomeplus.ams.form;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AdvertisementForm implements Serializable {


	private static final long serialVersionUID = -6888179027659926300L;

	private Integer advertisementId;

	@NotEmpty(message = "广告位名称不能为空")
	private String name;

	@Min(value = 1, message = "广告位名称不能为空")
	@NotNull(message = "广告位名称不能为空")
	private Integer slotId;
	@Min(value = 1, message = "请选择广告形式")
	@NotNull(message = "请选择广告形式")
	private Integer formatId;
	// 计计费模式 0-可选竞价CPC、1-定价CPC、2-竞价CPM、3-定价CPM（合约）、4-定价CPM（非合约）、5-CPD
	@Min(value = 0, message = "请选择计费模式")
	@NotNull(message = "请选择计费模式")
	private Integer saleMode;
	// 推广类型 0商品广告、1活动广告、2店铺广告
	@Min(value = 0, message = "请选择推广类型")
	@NotNull(message = "请选择推广类型")
	private Integer generalizeType;
	@Min(value = 1, message = "请选择模板类型")
	@NotNull(message = "请选择推广类型")
	private Integer templateId;

	private Integer  status;

	private Date effectiveTime;


}
