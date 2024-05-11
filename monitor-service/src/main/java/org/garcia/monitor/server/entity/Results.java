package org.garcia.monitor.server.entity;

public final class Results {

    public static <T> Result<T> success(T data){
        return new Result<T>().setCode(Result.SUCCESS_CODE).setData(data);
    }

    public static <T> Result<T> success(T data, String message){
        return new Result<T>().setCode(Result.SUCCESS_CODE).setData(data).setMessage(message);
    }

    public static Result<Void> success(){
        return new Result<Void>().setCode(Result.SUCCESS_CODE);
    }

    public static Result<Void> success(String message){return new Result<Void>().setCode(Result.SUCCESS_CODE).setMessage(message);}

    public static Result<Void> failure(){
        return new Result<Void>().setCode(Result.ERROR_CODE)
                .setMessage("出现未知异常");
    }

    public static<T> Result<T> failure(T data,String message){
        return new Result<T>().setCode(Result.ERROR_CODE)
                .setMessage(message).setData(data);

    }

    public static Result<Void> failure(String message){
        return new Result<Void>().setCode(Result.ERROR_CODE)
                .setMessage(message);

    }
}
