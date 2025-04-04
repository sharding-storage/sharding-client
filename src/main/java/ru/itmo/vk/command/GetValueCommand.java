package ru.itmo.vk.command;

import ru.itmo.vk.client.Client;

public class GetValueCommand extends Command {
    public static final String NAME = "get";

    protected GetValueCommand(Client client) {
        super(client);
    }

    @Override
    public void execute(Object... args) {
        if (args.length != 1) {
            System.out.println("Неправильное количество аргументов");
            throw new IllegalArgumentException();
        }

        var key = (String) args[0];
        System.out.println("Полученное значение: " + super.getClient().getValue(key));
    }

    @Override
    public void helpInfo() {
        System.out.println("<key> - получение значения по ключу");
    }
}
