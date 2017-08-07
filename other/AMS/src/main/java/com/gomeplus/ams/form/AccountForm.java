package com.gomeplus.ams.form;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AccountForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer accountId;
	private Integer employeeId;
	private String name;
	private String givenName;
	private String password;
	private String email;
	private String phone;
	private String mobile;
	private Integer status;
}
