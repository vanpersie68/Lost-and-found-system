package cn.zwz.test.entity;

import cn.zwz.basics.baseClass.ZwzBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "a_teacher")
@TableName("a_teacher")
//@ApiModel(value = "教师")
@ApiModel(value = "teacher")
public class Teacher extends ZwzBaseEntity {

    private static final long serialVersionUID = 1L;

    //@ApiModelProperty(value = "姓名")
    @ApiModelProperty(value = "name")
    private String name;

    //@ApiModelProperty(value = "学历")
    @ApiModelProperty(value = "education")
    private String education;

    //@ApiModelProperty(value = "年龄")
    @ApiModelProperty(value = "age")
    private BigDecimal age;

    //@ApiModelProperty(value = "毕业院校")
    @ApiModelProperty(value = "graduated")
    private String graduated;

    //@ApiModelProperty(value = "工资")
    @ApiModelProperty(value = "wages")
    private BigDecimal wages;

    //@ApiModelProperty(value = "在职状态")
    @ApiModelProperty(value = "status")
    private String status;

    //@ApiModelProperty(value = "签名")
    @ApiModelProperty(value = "autograph")
    private String autograph;

    //@ApiModelProperty(value = "个人简历")
    @ApiModelProperty(value = "resume")
    private String resume;

    //@ApiModelProperty(value = "备注")
    @ApiModelProperty(value = "remark")
    private String remark;
}