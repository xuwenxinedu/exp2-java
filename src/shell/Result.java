package shell;

import java.util.List;
import java.util.Map;

/**
 * The return type after parsing your order
 */
public class Result {
    private CommandCode code;
    private Map<String ,Object> args;

    public Result(CommandCode code) {
        this.code = code;
        args = null;
    }

    public Result(CommandCode code, Map<String, Object> args) {
        this.code = code;
        this.args = args;
    }

    public CommandCode getCode() {
        return code;
    }

    public void setCode(CommandCode code) {
        this.code = code;
    }

    public Map<String, Object> getArgs() {
        return args;
    }

    public void setArgs(Map<String, Object> args) {
        this.args = args;
    }
}
