package router.rpi;

/**
 * Created by Nuwanda on 7/11/2017.
 */
public class RPiRequest {
    private String command;
    private Object value;

    public RPiRequest(String command, Object value){
        this.command = command;
        this.value = value;
    }
}
