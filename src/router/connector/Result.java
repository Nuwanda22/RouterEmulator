package router.connector;

/**
 * Created by Kyle John on 5/10/2017.
 */
public class Result {
    /** Property message */
    private String message;

    /** Property success */
    private boolean success;

    /**
     * Constructor
     */
    public Result() {
    }

    /**
     * Gets the message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Sets the message
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * Gets the success
     */
    public boolean isSuccess() {
        return this.success;
    }

    /**
     * Sets the success
     */
    public void setSuccess(boolean value) {
        this.success = value;
    }
}
