package router.rpi;

/**
 * Created by Nuwanda on 7/11/2017.
 */
public class RPiResponse {
    private String command;
    private Boolean status;
    private RPiResult result;
    private String message;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Object getResult() {
        return result.getValue();
    }

    public void setResult(Object result) {
        RPiResult r = new RPiResult();
        r.setValue(result);

        this.result = r;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
