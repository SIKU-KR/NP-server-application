package core.dto;

import core.common.RequestType;

import java.io.Serializable;

public class DTO implements Serializable {

    private RequestType requestType;
    private Object requestMsg;

    public DTO(RequestType requestType, Object requestMsg) {
        this.requestType = requestType;
        this.requestMsg = requestMsg;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public Object getRequestMsg() {
        return requestMsg;
    }
}


