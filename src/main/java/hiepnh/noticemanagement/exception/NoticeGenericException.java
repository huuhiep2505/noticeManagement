package hiepnh.noticemanagement.exception;

public class NoticeGenericException extends Exception{
    private static final long serialVersionUID = 1L;

    public NoticeGenericException() {
    }

    public NoticeGenericException(String message) {
        super(message);
    }

    public NoticeGenericException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoticeGenericException(Throwable cause) {
        super(cause);
    }

    public NoticeGenericException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
