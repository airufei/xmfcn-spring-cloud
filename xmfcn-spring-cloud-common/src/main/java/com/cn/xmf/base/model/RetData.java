
package com.cn.xmf.base.model;


public class RetData {
    private int code = ResultCodeMessage.PARMS_ERROR;
    private String message= ResultCodeMessage.PARMS_ERROR_MESSAGE;
    private Object data;


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "RetData{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
