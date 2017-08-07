package com.gomeplus.ams.form;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PublisherForm implements Serializable {

	private static final long serialVersionUID = 730863452165463427L;

	private Integer publisherId;
	@NotEmpty(message = "媒体名称不能为空")
	private String name;
	private String url = "";
	@NotNull
	@Min(value = 1, message = "设备类型值不正确")
	private Integer platform;
	@NotNull
	@Min(value = 0, message = "媒体属性值不正确")
	private Integer isInternal;

	private Integer status;

	private String description = "";


	public enum Platform {
		APP(1), WAP(2), PC(3);
		private Integer value;

		private Platform(Integer value) {
			this.value = value;
		}

		public Integer getValue() {
			return value;
		}
	}

	public enum IsInternal {
		OUTER(0), INNER(1);
		private Integer value;

		private IsInternal(Integer value) {
			this.value = value;
		}

		public Integer getValue() {
			return value;
		}
	}
}
