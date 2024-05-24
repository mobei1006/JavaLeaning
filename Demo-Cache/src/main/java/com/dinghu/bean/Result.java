package com.dinghu.bean;

import lombok.Data;

/**
 * @author 1.0
 * @author huding
 * @Date: 2024/05/24 11:06
 * @Description:
 */

@Data
public class Result<T> {

    private Boolean success;

    private Integer code;


    private T data;

    public static Result ok(){
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(200);
        return result;
    }

    public static <T> Result ok(T data){
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(200);
        result.setData(data);
        return result;
    }

    public static Result fail(){
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(-1);
        return result;
    }

    public static <T> Result fail(T data){
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(-1);
        result.setData(data);
        return result;
    }

}
