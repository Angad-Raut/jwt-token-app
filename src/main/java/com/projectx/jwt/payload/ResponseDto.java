package com.projectx.jwt.payload;

public class ResponseDto <T>{
    private T result;
    private String errorMessage;

    public ResponseDto(){

    }

    public ResponseDto(T result, String errorMessage) {
        this.result = result;
        this.errorMessage = errorMessage;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "ResponseDto{" +
                "result=" + result +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
