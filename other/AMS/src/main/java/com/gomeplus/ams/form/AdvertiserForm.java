package com.gomeplus.ams.form;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
@Data
public class AdvertiserForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer advertiserId;
	
	private List<Object> rejectReason;

}
