package ru.itmo.vk.command;

import ru.itmo.vk.client.Client;

public class SetValueCommand extends Command {
    public static final String NAME = "set";

    protected SetValueCommand(Client client) {
        super(client);
    }

    @Override
    public void execute(Object... args) {
        if (args.length != 2) {
            System.out.println("Неправильное количество аргументов");
            throw new IllegalArgumentException();
        }

        super.getClient().setValue((String) args[0], (String) args[1]);

        System.out.println("Новое значение успешно выставлено");
    }

    @Override
    public void helpInfo() {
        System.out.println("<key> <value> - upsert значения в БД");
    }
}
