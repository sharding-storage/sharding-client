package ru.itmo.vk.command;

import ru.itmo.vk.client.Client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Invoker {
    private final Client client;
    private final Scanner scanner;
    private final HashMap<String, Command> commandHashMap;

    public Invoker(Client client) {
        this.client = client;
        this.scanner = new Scanner(System.in);
        this.commandHashMap = new HashMap<>();
    }

    public void start() {
        initCommands();

        while (true) {
            try {
                System.out.println("Введите команду: ");
                System.out.print("$ ");

                var args = scanner.nextLine().split(" ");
                if (args[0].equals("help")) throw new NoSuchMethodException();
                commandHashMap.get(args[0]).execute(Arrays.stream(args).skip(1).toArray());
            } catch (Exception e) {
                printHelp();
            }
        }
    }

    private void initCommands () {
        commandHashMap.put(GetValueCommand.NAME, new GetValueCommand(client));
        commandHashMap.put(SetValueCommand.NAME, new SetValueCommand(client));
        commandHashMap.put(ExitCommand.NAME, new ExitCommand());
    }

    private void printHelp(){
        final int[] i = {1};
        System.out.println("Введите нужную команду:");
        commandHashMap.forEach((name, command) -> {
            System.out.print(i[0] + ": " + name + " ");
            command.helpInfo();
            i[0]++;
        });
    }
}
