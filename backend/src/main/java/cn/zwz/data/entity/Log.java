package cn.zwz.data.entity;

import cn.zwz.basics.baseClass.ZwzBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.Column;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;


@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "a_log")
@TableName("a_log")
@ApiModel(value = "log")
public class Log extends ZwzBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Log title")
    private String name;

    @ApiModelProperty(value = "Log type")
    private Integer logType;

    @ApiModelProperty(value = "Log code")
    private String code;

    @ApiModelProperty(value = "equipment")
    private String device;

    @ApiModelProperty(value = "Request URL")
    private String requestUrl;

    @ApiModelProperty(value = "Request method")
    private String requestType;

    @Column(columnDefinition ="TEXT")
    @ApiModelProperty(value = "parameter")
    private String requestParam;

    @ApiModelProperty(value = "Trigger")
    private String username;

    @ApiModelProperty(value = "IP address")
    private String ip;

    @ApiModelProperty(value = "IP positioning")
    private String ipInfo;

    @ApiModelProperty(value = "Number of milliseconds consumed")
    private Integer costTime;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "Search start time")
    private String startDate;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "Search end time")
    private String endDate;
}
