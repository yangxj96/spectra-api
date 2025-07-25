/*
 *  Copyright 2025 yangxj96.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.yangxj96.spectra.framework.advice;

import cn.dev33.satoken.error.SaErrorCode;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import com.yangxj96.spectra.common.annotation.ULog;
import com.yangxj96.spectra.common.exception.DataExistException;
import com.yangxj96.spectra.common.exception.DataNotExistException;
import com.yangxj96.spectra.common.response.R;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import javax.security.auth.login.LoginException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 全局异常处理
 *
 * @author Jack Young
 * @version 1.0
 * @since 2025-6-14
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    private static final String PREFIX = "[GlobalException]:";

    /**
     * SQL语法错误
     *
     * @param e        错误信息
     * @param response 响应
     * @return 格式化为正常的响应返回
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public R<Object> duplicateKeyException(DuplicateKeyException e, HttpServletResponse response) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        String message = e.getMessage();
        Pattern pattern = Pattern.compile("键值\"\\(name\\)=\\((?<value>[^)]+)\\)\" 已经存在");
        Matcher matcher = pattern.matcher(message);
        String errorMessage = "数据重复，请检查输入内容";
        if (matcher.find()) {
            String value = matcher.group("value");

            return R.failure("\"%s\"已存在,请更换名称".formatted(value));
        }
        // 记录日志（这里假设你有 log 对象）
        log.atError().log(PREFIX + errorMessage + ", detail: {}", message, e);
        return R.failure(errorMessage);
    }

    /**
     * SQL语法错误
     *
     * @param e        错误信息
     * @param response 响应
     * @return 格式化为正常的响应返回
     */
    @ExceptionHandler(BadSqlGrammarException.class)
    public R<Object> badSqlGrammarException(Exception e, HttpServletResponse response) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        log.atError().log(PREFIX + "SQL语法错误,{}", e.getMessage(), e);
        return R.failure();
    }

    /**
     * 未找到资源
     *
     * @param e        错误信息
     * @param response 响应
     * @return 格式化为正常的响应返回
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public R<Object> noResourceFoundException(Exception e, HttpServletResponse response) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        log.atError().log(PREFIX + "未找到资源,{}", e.getMessage(), e);
        return R.failure("未找到资源");
    }

    /**
     * 无权限异常
     *
     * @param e        错误信息
     * @param response 响应
     * @return 格式化为正常的响应返回
     */
    @ULog("无权操作")
    @ExceptionHandler(NotPermissionException.class)
    public R<Object> notPermissionException(Exception e, HttpServletResponse response) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        log.atError().log(PREFIX + "无权限异常,{}", e.getMessage(), e);
        return R.failure(HttpStatus.FORBIDDEN, "无权操作");
    }

    /**
     * 未登录异常
     *
     * @param e        错误信息
     * @param response 响应
     * @return 格式化为正常的响应返回
     */
    @ExceptionHandler(NotLoginException.class)
    public R<Object> notLoginException(NotLoginException e, HttpServletResponse response) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        log.atError().log(PREFIX + "未登录异常,{}", e.getMessage(), e);
        if (e.getCode() == SaErrorCode.CODE_11016) {
            return R.failure(HttpStatus.UNAUTHORIZED, "您的会话已过期，请重新登录以继续。");
        }
        return R.failure(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    /**
     * 登录异常
     *
     * @param e        错误信息
     * @param response 响应
     * @return 格式化为正常的响应返回
     */
    @ExceptionHandler(LoginException.class)
    public R<Object> loginException(Exception e, HttpServletResponse response) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        log.atError().log(PREFIX + "登录异常,{}", e.getMessage(), e);
        return R.failure(e.getMessage());
    }

    /**
     * 未进行功能实现异常
     *
     * @param e        错误信息
     * @param response 响应
     * @return 格式化为正常的响应返回
     */
    @ExceptionHandler(NotImplementedException.class)
    public R<Object> notImplementedException(Exception e, HttpServletResponse response) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        log.atError().log(PREFIX + "未进行功能实现异常,{}", e.getMessage(), e);
        return R.failure();
    }

    /**
     * 数据已存在异常
     *
     * @param e        错误信息
     * @param response 响应
     * @return 格式化为正常的响应返回
     */
    @ExceptionHandler(DataExistException.class)
    public R<Object> dataExistException(Exception e, HttpServletResponse response) {
        response.setStatus(HttpStatus.CONFLICT.value());
        log.atError().log(PREFIX + "数据已存在异常,{}", e.getMessage(), e);
        return R.failure(HttpStatus.CONFLICT);
    }

    /**
     * 数据不存在异常
     *
     * @param e        错误信息
     * @param response 响应
     * @return 格式化为正常的响应返回
     */
    @ExceptionHandler(DataNotExistException.class)
    public R<Object> dataNotExistException(Exception e, HttpServletResponse response) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        log.atError().log(PREFIX + "数据不存在异常,{} ", e.getMessage(), e);
        return R.failure(HttpStatus.NOT_FOUND);
    }

    /**
     * 参数验证异常
     *
     * @param e        错误信息
     * @param response 响应
     * @return 格式化为正常的响应返回
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Object> methodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletResponse response) {
        log.atError().log(PREFIX + "参数验证异常,{} ", e.getMessage(), e);
        response.setStatus(HttpStatus.BAD_REQUEST.value());

        var errors = e.getBindingResult().getAllErrors();
        if (!errors.isEmpty()) {
            return R.failure(HttpStatus.BAD_REQUEST, errors.get(0).getDefaultMessage());
        } else {
            return R.failure(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 运行时异常
     *
     * @param e        错误信息
     * @param response 响应
     * @return 格式化为正常的响应返回
     */
    @ExceptionHandler(RuntimeException.class)
    public R<Object> runtimeException(RuntimeException e, HttpServletResponse response) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        log.atError().log(PREFIX + "运行时异常,{}", e.getMessage(), e);
        return R.failure(e.getMessage());
    }

    /**
     * 兜底异常处理
     *
     * @param e        错误信息
     * @param response 响应
     * @return 格式化为正常的响应返回
     */
    @ExceptionHandler(Exception.class)
    public R<Object> handleException(Exception e, HttpServletResponse response) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        log.atError().log(PREFIX + "兜底异常处理,{}", e.getMessage(), e);
        return R.failure();
    }

}
