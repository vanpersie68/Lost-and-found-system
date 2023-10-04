package cn.zwz.basics.security.jwt;

import cn.zwz.basics.utils.ResponseUtil;
import cn.zwz.basics.exception.ZwzAuthException;
import cn.zwz.basics.parameter.ZwzLoginProperties;
import cn.zwz.data.utils.ZwzNullUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;


@ApiOperation(value = "Login failure callback")
@Slf4j
@Component
public class AuthenticationFailHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private ZwzLoginProperties tokenProperties;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String LOGIN_FAIL_TIMES_PRE = "LOGIN_FAIL_TIMES_PRE:";

    private static final String REQUEST_PARAMETER_USERNAME = "username:";

    private static final boolean RESPONSE_FAIL_FLAG = false;

    private static final int RESPONSE_FAIL_CODE = 500;

    @ApiOperation(value = "Check number of login failures")
    public boolean recordLoginTime(String username) {
        String loginFailTimeStr = stringRedisTemplate.opsForValue().get(LOGIN_FAIL_TIMES_PRE + username);
        int loginFailTime = 0;
        // Number of errors so far
        if(!ZwzNullUtils.isNull(loginFailTimeStr)){
            loginFailTime = Integer.parseInt(loginFailTimeStr) + 1;
        }
        stringRedisTemplate.opsForValue().set(LOGIN_FAIL_TIMES_PRE + username, loginFailTime + "", tokenProperties.getLoginFailMaxThenLockTimes(), TimeUnit.MINUTES);
        if(loginFailTime >= tokenProperties.getMaxLoginFailTimes()){
            stringRedisTemplate.opsForValue().set("userLoginDisableFlag:"+username, "fail", tokenProperties.getLoginFailMaxThenLockTimes(), TimeUnit.MINUTES);
            return false;
        }
        return true;
    }

    @Override
    @ApiOperation(value = "Login failure callback")
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        if (exception instanceof BadCredentialsException || exception instanceof UsernameNotFoundException) {
            recordLoginTime(request.getParameter(REQUEST_PARAMETER_USERNAME));
            String failTimesStr = stringRedisTemplate.opsForValue().get(LOGIN_FAIL_TIMES_PRE + request.getParameter(REQUEST_PARAMETER_USERNAME));
            // Number of errors so far
            int userFailTimes = 0;
            if(!ZwzNullUtils.isNull(failTimesStr)){
                userFailTimes = Integer.parseInt(failTimesStr);
            }
            int restLoginTime = tokenProperties.getMaxLoginFailTimes() - userFailTimes;
            if(restLoginTime < 5 && restLoginTime > 0){
                ResponseUtil.out(response, ResponseUtil.resultMap(RESPONSE_FAIL_FLAG,RESPONSE_FAIL_CODE,"Incorrect username or password, you can try to log in " + restLoginTime + " more times"));
            } else if(restLoginTime < 1) {
                ResponseUtil.out(response, ResponseUtil.resultMap(RESPONSE_FAIL_FLAG,RESPONSE_FAIL_CODE,"Retry limit exceeded. Please try again after " + tokenProperties.getLoginFailMaxThenLockTimes() + " minutes"));
            } else {
                ResponseUtil.out(response, ResponseUtil.resultMap(RESPONSE_FAIL_FLAG,RESPONSE_FAIL_CODE,"Incorrect username or password"));
            }
        } else if (exception instanceof ZwzAuthException){
            ResponseUtil.out(response, ResponseUtil.resultMap(RESPONSE_FAIL_FLAG,RESPONSE_FAIL_CODE,((ZwzAuthException) exception).getMsg()));
        } else if (exception instanceof DisabledException) {
            ResponseUtil.out(response, ResponseUtil.resultMap(RESPONSE_FAIL_FLAG,RESPONSE_FAIL_CODE,"Account is disabled and cannot login"));
        } else {
            ResponseUtil.out(response, ResponseUtil.resultMap(RESPONSE_FAIL_FLAG,RESPONSE_FAIL_CODE,"System is currently unable to log in. Please try again later"));
        }
    }
}

