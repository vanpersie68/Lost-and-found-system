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
@Table(name = "a_student")
@TableName("a_student")
//@ApiModel(value = "学生")
@ApiModel(value = "Student")
public class Student extends ZwzBaseEntity {

    private static final long serialVersionUID = 1L;

    //@ApiModelProperty(value = "姓名")
    @ApiModelProperty(value = "Name")
    private String name;

    //@ApiModelProperty(value = "性别")
    @ApiModelProperty(value = "sex")
    private String sex;

    //@ApiModelProperty(value = "年龄")
    @ApiModelProperty(value = "age")
    private BigDecimal age;

    //@ApiModelProperty(value = "学号")
    @ApiModelProperty(value = "number")
    private String number;

    //@ApiModelProperty(value = "学校")
    @ApiModelProperty(value = "school")
    private String school;
}