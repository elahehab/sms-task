package com.example.messagingrabbitmq.models;

public class Response {

    private RESPONSE_STATUS status;
    private String body;
    public static enum RESPONSE_STATUS {
        SUCCESS,
        FAILURE
    };



    public Response() {
    }

    public Response(RESPONSE_STATUS status, String body) {
        this.status = status;
        this.body = body;
    }

    public RESPONSE_STATUS getStatus() {
        return this.status;
    }

    public void setStatus(RESPONSE_STATUS status) {
        this.status = status;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    @Override
    public String toString() {
        return "{" +
            " status='" + getStatus() + "'" +
            ", body='" + getBody() + "'" +
            "}";
    }
    
}
