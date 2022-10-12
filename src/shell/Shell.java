package shell;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

public class Shell {

    private CommandParser parser;
    private Dealer dealer;
    private Map<String, String> man;
    private final String key = "cr req release timeout kill show";

    public Shell(CommandParser parser, Dealer dealer) {
        this.parser = parser;
        this.dealer = dealer;
        this.man = new HashMap<>();
        man.put("cr", "create a new process with its name and priority,\n" +
                "for example, 'cr A 3' , by this way, u create a new process\n" +
                "named 'A' and priority is 3.");
        man.put("req", "request resource for the running process,\n" +
                "for example, 'req 1', then the running process will\n" +
                "get the the resource whose RID is 1 if it is free.");
        man.put("release", "make running process release resource with RID.");
        man.put("timeout", "intimate timeout, running->ready.");
        man.put("kill", "kill a process with its name\n" +
                "for example, 'kill A', you will kill the process named A.");
        man.put("show", "you can choose one from blow:\n" +
                "ready, run, blocked, resource\n" +
                "shell will show you what you want to see.");
    }

    public void start() {
        this.welcome();
        String order;
        Scanner scanner = new Scanner(System.in);
        /**
         *this code would make your system time out every 0.5 second
         *
        new Thread(()->{
            while (true) {
                Result result = new Result(CommandCode.TIMEOUT);
                this.dealer.deal(result);
                Thread.sleep(500);
            }
        }).start();
         */

        while (true) {
            System.out.print(">>");
            order = scanner.nextLine();
            if (order.equals("EXIT!")) {
                break;
            } else if (order.split(" ")[0].equals("man")) {
                this.shellCommandProcess(order);
            } else if (order.equals("")){
                continue;
            }else {
                //the code used to dispose the order
                dealer.deal(parser.parse(order));
                dealer.afterAction();
            }
//            System.out.println(order.split(" ").length);
        }
    }

    private void shellCommandProcess(String order) {
        String[]temp = order.split(" ");
        int length = temp.length;
        if (2 == length) {
            System.out.println(this.man.getOrDefault(
                    temp[1],
                    "No man entry for " + temp[1] +
                            ", maybe you want:" + this.key
            ));
        } else if (length > 2) {
            System.out.println("Too many parameters!");
        }
    }

    private void welcome() {
        System.out.println("WELCOME");
        System.out.print("Command: ");
        System.out.println("cr req release timeout kill show");
        System.out.println("You can use 'man' command to see more about the commands.");
    }

}
