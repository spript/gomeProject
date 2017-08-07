package com.gomeplus.ams.form;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class SurveyForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer surveyId;
	
	private List<Object> rejectReason;
	
	private List<Integer> surveyIds;
}
