package cn.zwz.basics.utils;

import io.swagger.annotations.ApiOperation;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;


@ApiOperation(value = "public utility class") //公共工具类
public class CommonUtil {

    private static SecureRandom random = new SecureRandom();

    @ApiOperation(value = "generate a random file name") //生成随机文件名称
    public static String renamePic(String fileName) {
        return UUID.randomUUID().toString().replace("-", "") + fileName.substring(fileName.lastIndexOf("."));
    }

    @ApiOperation(value = "generate a random enterprise WeChat verification code") //生成随机企微验证码
    public static String getRandomTwoNum() {
        int num = random.nextInt(99);
        // 不足六位前面补0
        String str = String.format("%02d", num);
        return str;
    }

    @ApiOperation(value = "To avoid deleting DFS (Depth-First Search) infinite loops") //避免删除 DFS 死循环
    public static Boolean judgeIds(String tempString, String[] list){
        boolean flag = true;
        for(String id : list){
            if(Objects.equals(tempString,id)){
                flag = false;
                break;
            }
        }
        return !flag;
    }

    @ApiOperation(value = "generate a random 6-digit verification code") //生成随机6位验证码
    public static String getRandomNum() {
        Random random = new Random();
        int num = random.nextInt(999999);
        return String.format("%06d", num);
    }
}
