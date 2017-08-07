package com.gomeplus.ams.form;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import lombok.Data;

/*
 * suna01
 * @date 2017-05-09
 */
@Data
public class PriceForm implements Serializable {
	
	private static final long serialVersionUID = 6560170336662167435L;
	
	private Integer advertisementId;
	private BigInteger cpmPriceFloat;
	private List<String> dates;
	private BigInteger cpdPriceFloat;
	private BigInteger cpcPriceFloat;
	
}
