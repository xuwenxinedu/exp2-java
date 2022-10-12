package shell;

/**
 * Your order's type.
 */
public enum CommandCode {
    CREATE,
    RELEASE,
    REQUEST,
    KILL,
    TIMEOUT,
    SHOW_RESOURCE,
    SHOW_READY,
    SHOW_BLOCKED,
    UNKNOWN,
    WRONG_NUMBER_ARGS,
    SHOW_RUNNING
}
