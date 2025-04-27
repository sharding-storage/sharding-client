package ru.itmo.vk.command;

import ru.itmo.vk.client.Client;

public class DeleteServerCommand extends Command{
    public static final String NAME = "removeNode";

    protected DeleteServerCommand(Client client) {
        super(client);
    }

    @Override
    public void execute(Object... args) {
        if (args.length != 1) {
            System.out.println("Неправильное количество аргументов");
            throw new IllegalArgumentException();
        }

        if (super.getClient().getMasterNode().getNodes().isEmpty()) {
            System.out.println("Нет доступных нод для удаления!");
            throw new IllegalArgumentException();
        }

        var address = (String) args[0];
        System.out.println(super.getClient().deleteServer(address));
    }

    @Override
    public void helpInfo() {
        System.out.println("<address> - удаление ноды");
    }
}
