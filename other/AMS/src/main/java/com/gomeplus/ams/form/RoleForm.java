package com.gomeplus.ams.form;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

import lombok.Data;

@Data
public class RoleForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer roleId;
    @NotEmpty(message = "角色名称不能为空")
    private String name;
    @NotEmpty(message = "角色不能为空")
    private String roleName;
}
