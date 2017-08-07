package com.gomeplus.ams.form;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PrivilegeForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer privilegeId;
	private String name;
	private Integer systemId;
	private Integer parentId;
	private String path;
	private String link;
	private String permission;
	private String icon;
	private Integer isDisplay;
	private Integer sequence;
}
