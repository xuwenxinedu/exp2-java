import controller.Command;
import controller.MySystem;
import shell.CommandParser;
import shell.Dealer;
import shell.Shell;

import java.util.Scanner;

public class Example {

    public static void main(String[] args) {
        MySystem system = new MySystem();
        Command command = new Command(system);
        CommandParser parser = new CommandParser();
        Dealer dealer = new Dealer(command);
        Shell shell = new Shell(parser, dealer);
        shell.start();
    }
}
