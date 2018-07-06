package pers.yxb.share.sharemodel.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * @author Yuxb.
 * @description.
 * @create 2018-7-5 11:01
 */
public class ShareException extends AuthenticationException {

    public ShareException() {
        super();
    }

    public ShareException(String message) {
        super(message);
    }

    public ShareException(Throwable cause) {
        super(cause);
    }

    public ShareException(String message, Throwable cause) {
        super(message, cause);
    }
}
