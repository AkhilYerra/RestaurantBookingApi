package com.example.resy.data.error;

public class GenericException {
    private String errorMessage;
    private Integer httpStatus;
    private String exceptionType;

    public GenericException(String errorMessage, Integer httpStatus, String exceptionType){
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
        this.exceptionType = exceptionType;
    }
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getException() {
        return exceptionType;
    }

    public void setException(String exceptionType) {
        this.exceptionType = exceptionType;
    }
}
