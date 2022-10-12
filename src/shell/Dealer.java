package shell;

import controller.Command;

/**
 * Deal with your order and execute some orders
 */
public class Dealer {

    private Command command;

    public Dealer(Command cmd) {
        this.command = cmd;
    }

    public void deal(Result result) {
        CommandCode code = result.getCode();
        switch (code){
            case RELEASE:
                this.release(result);
                break;
            case REQUEST:
                this.request(result);
                break;
            case CREATE:
                this.create(result);
                break;
            case KILL:
                this.kill(result);
                break;
            case TIMEOUT:
                this.timeout();
                break;
            case SHOW_RESOURCE:
                this.showResource();
                break;
            case SHOW_READY:
                this.showReady();
                break;
            case SHOW_BLOCKED:
                this.showBlocked();
                break;
            case UNKNOWN:
                this.unknown();
                break;
            case WRONG_NUMBER_ARGS:
                this.wrongNumberArgs();
                break;
            case SHOW_RUNNING:
                this.showRunning();
                break;
        }
    }

    private void showRunning() {
        this.command.showRunning();
    }

    private void kill(Result result) {
        this.command.kill(((String) result.getArgs().get("name")));
    }

    private void wrongNumberArgs() {
        System.out.println("We know what you want, " +
                "but the number of parameters you gave us is not correct.");
    }

    private void unknown() {
        System.out.println("Unknown command, please check your input.");
    }

    private void showBlocked() {
        this.command.showBlockedQueue();
    }

    private void showReady() {
        this.command.showReadyQueue();
    }

    private void showResource() {
        this.command.showResource();
    }

    private void timeout() {
        this.command.timeout();
    }

    private void create(Result result) {
        String name = ((String) result.getArgs().get("name"));
        int priority = ((int) result.getArgs().get("priority"));
        this.command.create(name, priority);
    }


    private void request(Result result) {
        long rid = (long) result.getArgs().get("rid");
        this.command.request(rid);
    }

    private void release(Result result) {
        long rid = (long) result.getArgs().get("rid");
        this.command.release(rid);
    }

    public void afterAction() {
        this.command.afterAction();
    }

}
