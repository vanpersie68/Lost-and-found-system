package cn.zwz.data.entity;

import cn.zwz.basics.baseClass.ZwzBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;


@Data
@Accessors(chain = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "a_file")
@TableName("a_file")
@ApiModel(value = "document")
public class File extends ZwzBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Upload file name")
    private String name;

    @ApiModelProperty(value = "storage hard drive")
    private Integer location;

    @ApiModelProperty(value = "File storage path")
    private String url;

    @ApiModelProperty(value = "File size")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long size;

        @ApiModelProperty(value = "actual file name")
    private String fKey;

    @ApiModelProperty(value = "file type")
    private String type;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "Uploader")
    private String nickname;
}