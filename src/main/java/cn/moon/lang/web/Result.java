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

    boolean success = true;
    int code = 200;
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
        rs.setCode(500);
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
