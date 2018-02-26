package com.ym.imgurgallery.api;


import java.util.List;

public class ImgurResponse<T> {

    private boolean success;
    private int status;
    private List<T> data;

    private Throwable error;

    public ImgurResponse(Throwable error) {
        this.success = false;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Throwable getError() {
        return error;
    }
}
