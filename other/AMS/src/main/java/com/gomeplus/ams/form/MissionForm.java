package com.gomeplus.ams.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户任务Form类
 * Created by DengBin on 17/6/22.
 */
@Data
public class MissionForm implements Serializable {
    private static final long serialVersionUID = -740107630893930323L;

    private Integer missionId;

    @NotEmpty(message = "用户任务名称不能为空")
    private String name;
    private String description;

    private Date startTime;
    private Date endTime;

    private Integer cycleMode;
    @Max(value = 99_999_999L, message = "周期天数最大不能超过99999999")
    private Integer cycleDays;

    @Max(value = 99_999_999L, message = "奖励金额最大不能超过99999999")
    private Long awardAmount;
    @Max(value = 99_999_999L, message = "领取数量最大不能超过99999999")
    private Integer totalCount;

    private Integer typeUpVote;
    @Max(value = 99_999_999L, message = "点赞数量最大不能超过99999999")
    private Integer typeUpVoteCount;

    private Integer typeShare;
    @Max(value = 99_999_999L, message = "分享数量最大不能超过99999999")
    private Integer typeShareCount;

    private Integer typeResearch;
    @Max(value = 99_999_999L, message = "调研数量最大不能超过99999999")
    private Integer typeResearchCount;

    private Integer typeWatch;
    @Max(value = 99_999_999L, message = "视频数量最大不能超过99999999")
    private Integer typeWatchCount;

    private Integer status;
}
