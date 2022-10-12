package shell;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * this class is used to parse your order
 * and return the order to dealer
 */
public class CommandParser {

    private List<String> history;

    public CommandParser() {
        this.history = new LinkedList<>();
    }

    public Result parse(String str) {
        history.add(str);
        CommandCode code = parsePrefix(str);
        Result result = new Result(code);
        if (CommandCode.CREATE == code) {
            String[]order = str.split(" ");
            if (order.length != 3) {
                result.setCode(CommandCode.WRONG_NUMBER_ARGS);
                return result;
            } else {
                Map<String, Object> map = new HashMap<>();
                map.put("name", order[1]);
                map.put("priority", Integer.parseInt(order[2]));
                result.setArgs(map);
                return result;
            }
        } else if (CommandCode.REQUEST == code) {
            String[]order = str.split(" ");
            if (order.length != 2) {
                result.setCode(CommandCode.WRONG_NUMBER_ARGS);
                return result;
            } else {
                Map<String, Object> map = new HashMap<>();
                map.put("rid", Long.parseLong(order[1]));
                result.setArgs(map);
                return result;
            }
        } else if (CommandCode.RELEASE == code) {
            if (str.split(" ").length != 2) {
                result.setCode(CommandCode.WRONG_NUMBER_ARGS);
                return result;
            } else {
                Map<String, Object> map = new HashMap<>();
                map.put("rid", Long.parseLong(str.split(" ")[1]));
                result.setArgs(map);
                return result;
            }
        } else if (CommandCode.KILL == code) {
            if (str.split(" ").length != 2) {
                result.setCode(CommandCode.WRONG_NUMBER_ARGS);
                return result;
            } else {
                Map<String, Object> map = new HashMap<>();
                map.put("name", str.split(" ")[1]);
                result.setArgs(map);
                return result;
            }
        }
        return result;
    }


    /**
     * parse your order's type
     * @param str str is your order
     * @return code represents your command type
     */
    public CommandCode parsePrefix(String str) {
        String []order = str.split(" ");
        if (order[0].equals("cr")) {
            return CommandCode.CREATE;
        } else if (order[0].equals("req")) {
            return CommandCode.REQUEST;
        } else if (order[0].equals("release")) {
            return CommandCode.RELEASE;
        } else if (order[0].equals("timeout")) {
            return CommandCode.TIMEOUT;
        } else if (order[0].equals("show")) {
            if (order[1].equals("ready")) {
                return CommandCode.SHOW_READY;
            } else if (order[1].equals("blocked")) {
                return CommandCode.SHOW_BLOCKED;
            } else if (order[1].equals("resource")) {
                return CommandCode.SHOW_RESOURCE;
            } else if (order[1].equals("run")) {
                return CommandCode.SHOW_RUNNING;
            }
        } else if (order[0].equals("kill")) {
            return CommandCode.KILL;
        }
        return CommandCode.UNKNOWN;
    }

}
