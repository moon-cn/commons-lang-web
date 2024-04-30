//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.moon.lang.web;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result {
    public static final String ERROR_MESSAGE = "服务器忙";

    public static final int OK = 200;
    public static final int ERR_EXCEPTION = 500; // 异常
    public static final int ERR_BIZ = 520; // 业务错误，比如一些提示

    boolean success = true;
    int code = OK;
    Object data = null;
    String message = "操作成功";

    private Result() {
    }

    public static Result ok() {
        return new Result();
    }

    public static Result err() {
        Result rs = new Result();
        rs.setMessage(ERROR_MESSAGE);
        rs.setSuccess(false);
        rs.setCode(ERR_BIZ);
        return rs;
    }

    public Result data(Object data) {
        this.data = data;
        return this;
    }

    public Result msg(String msg) {
        this.message = msg;
        return this;
    }

    public Result code(int code) {
        this.code = code;
        return this;
    }
}
