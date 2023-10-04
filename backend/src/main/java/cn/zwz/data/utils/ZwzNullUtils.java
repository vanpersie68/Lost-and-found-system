package cn.zwz.data.utils;

import io.swagger.annotations.Api;

import java.util.Objects;


@Api(tags = "Judged as an empty tool class")
public class ZwzNullUtils {
    public static boolean isNull(String str){
        if(str == null || Objects.equals("",str) || Objects.equals("null",str) || Objects.equals("undefined",str)) {
            return true;
        }
        return false;
    }
}
