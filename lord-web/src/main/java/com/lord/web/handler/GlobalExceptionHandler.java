package com.lord.web.handler;

import com.lord.utils.dto.Code;
import com.lord.utils.dto.Result;
import com.lord.utils.exception.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 * Created by xiaocheng on 2017/3/23.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler
    @ResponseBody
    public Result allException(HttpServletRequest req, Exception e) throws Exception {
        logger.error("出现错误：" + e.getMessage(), e);
        return new Result(Code.ErrSystem);
    }

    @ExceptionHandler(CommonException.class)
    @ResponseBody
    public Result commonException(HttpServletRequest req, CommonException e) throws Exception {
        logger.error("出现错误：" + e.getMessage(), e);
        Result r = new Result();
        r.setCode(e.getCode());
        r.setMsg(e.getMessage());
        r.setSuccess(false);
        return r;
    }
}
