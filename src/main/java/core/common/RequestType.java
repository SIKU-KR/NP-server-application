package core.common;

import java.io.Serializable;

/**
 * Enum class to specify request.
 * Imagine API Endpoint, works like it.
 */
public enum RequestType implements Serializable {
    ROOMLIST, NEWROOM, CONNECTCHAT
}
