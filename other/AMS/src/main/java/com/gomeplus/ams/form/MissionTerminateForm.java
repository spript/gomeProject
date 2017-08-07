package com.gomeplus.ams.form;

import lombok.Data;
import java.io.Serializable;

/**
 * 用户任务终止Form类
 * Created by DengBin on 17/6/22.
 */
@Data
public class MissionTerminateForm implements Serializable {

	private static final long serialVersionUID = 872080795459697148L;

	private Integer missionId;

}
