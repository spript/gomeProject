package com.gomeplus.ams.form;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
@Data
public class InventoryForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer slotId;
	private List<String> dates;
	private Integer cpdAmount;
	private Integer cpmAmount;
	private Integer limitedCpdAmount;

}
