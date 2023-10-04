package cn.zwz.lost.entity;

import cn.zwz.basics.baseClass.ZwzBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "a_message")
@TableName("a_message")
@ApiModel(value = "留言")
public class Message extends ZwzBaseEntity {

    private static final long serialVersionUID = 1L;

    //@ApiModelProperty(value = "留言内容")
    @ApiModelProperty(value = "Message content")
    private String content;

    //@ApiModelProperty(value = "留言人ID")
    @ApiModelProperty(value = "Messager ID")
    private String userId;

    //@ApiModelProperty(value = "留言人")
    @ApiModelProperty(value = "Messager")
    private String userName;

    //@ApiModelProperty(value = "留言时间")
    @ApiModelProperty(value = "Message time")
    private String time;

    //@ApiModelProperty(value = "回复内容")
    @ApiModelProperty(value = "Reply content")
    private String replyContent;

    //@ApiModelProperty(value = "回复人ID")
    @ApiModelProperty(value = "Replier ID")
    private String replyId;

    //@ApiModelProperty(value = "回复人")
    @ApiModelProperty(value = "Replier")
    private String replyName;

    //@ApiModelProperty(value = "回复时间")
    @ApiModelProperty(value = "Reply time")
    private String replyTime;
}