package ru.itmo.vk.command;

import ru.itmo.vk.client.Client;

public class AddServerCommand extends Command{
    public static final String NAME = "addServer";

    protected AddServerCommand(Client client) {
        super(client);
    }

    @Override
    public void execute(Object... args) {
        if (args.length != 1) {
            System.out.println("Неправильное количество аргументов");
            throw new IllegalArgumentException();
        }

        var address = (String) args[0];
        System.out.println(super.getClient().addServer(address));
    }

    @Override
    public void helpInfo() {
        System.out.println("<address> - добавление новой ноды");
    }
}
