package core.dto;

import core.common.RequestType;

import java.io.Serializable;

/**
 * All requests to server socket should be wrapped by this DTO class.
 * 1. RequestType : Enum type value (core/common/RequestType.java)
 * 2. RequestMsg : RequestMsgs as an object (core/dto/*)
 */
public class DTO {
    RequestType requestType;
    Object requestMsg;

    public DTO() {
    }

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


