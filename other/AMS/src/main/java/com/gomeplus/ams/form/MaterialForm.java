package com.gomeplus.ams.form;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class MaterialForm implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer materialId;

	private String name;

	private Integer linkType;
	
	private String title;
	
	private String description;
	
	private List<Map<String,Object>> images;
	
	private String landingPage;

	private Integer type;
	
	private List<Object> rejectReasons;
	
	private List<Object> attachments;
	
	private List<Integer> materialIds;
}
