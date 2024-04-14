package cn.moon.lang.web.exception;

import cn.hutool.core.util.StrUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import javax.persistence.RollbackException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Set;

/**
 * 将异常转为人类可读的字符串
 */
public class HumanExceptionTool {

    public String covert(Exception e) {
        if (e instanceof ConstraintViolationException) {
            return getHumanMessage((ConstraintViolationException) e);
        }
        if (e instanceof TransactionSystemException) {
            if (e.getCause() != null) {
                RollbackException cause = (RollbackException) e.getCause();
                if (cause != null) {
                    Throwable cause2 = cause.getCause();
                    if (cause2 instanceof ConstraintViolationException) {
                        String humanMessage = getHumanMessage((ConstraintViolationException) cause2);
                        return humanMessage;
                    }
                }
            }
        }

        if (e instanceof InvalidDataAccessApiUsageException) {
            return e.getCause().getMessage();
        }

        if (e instanceof MissingServletRequestParameterException) {
            return missParamException((MissingServletRequestParameterException) e);
        }
        if(e instanceof DataIntegrityViolationException ){
          return   dataIntegrityViolationException((DataIntegrityViolationException) e);

        }


        String msg = e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
        return msg;
    }

    private String getHumanMessage(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        StringBuilder sb = new StringBuilder();

        for (ConstraintViolation<?> v : constraintViolations) {
            String property = v.getPropertyPath().toString();
            String message = v.getMessage();
            sb.append(property);
            sb.append(message);
            sb.append("；");
        }

        return sb.toString();
    }

    public String dataIntegrityViolationException(DataIntegrityViolationException e) {
        if (e != null && e.getCause() != null && e.getCause().getCause() != null) {
            Throwable ex = e.getCause().getCause();
            String msg = ex.getMessage();

            if (msg.contains("Data too long")) {
                return "数据长度超过限制";
            }


            if (ex instanceof SQLIntegrityConstraintViolationException) {
                if (msg.startsWith("Duplicate")) {
                    return "数据重复,操作不能继续进行。 " + msg;
                }

                if(msg.contains("cannot be null")){
                    return "数据不能为空。" + msg;
                }

            }
        }

        return "违反数据库规则，操作不能继续进行";
    }

    /**
     * 请求参数缺失异常
     */
    public String missParamException(MissingServletRequestParameterException e) {
        String parameterType = e.getParameterType();
        String parameterName = e.getParameterName();
        String message = StrUtil.format(">>> 缺少请求的参数{}，类型为{}", parameterName, parameterType);
        return message;
    }

}
