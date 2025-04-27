package ru.itmo.vk.command;

import ru.itmo.vk.client.Client;

public class SchemaPrintCommand extends Command {
    public static final String NAME = "printSchema";

    protected SchemaPrintCommand(Client client) {
        super(client);
    }

    @Override
    public void execute(Object... args) {
        if (args.length != 0) {
            System.out.println("Неправильное количество аргументов");
            throw new IllegalArgumentException();
        }

        System.out.println("Полученные ноды: " + super.getClient().getMasterNode().getNodes());
        System.out.println("Кол-во виртуальных нод: " + super.getClient().getMasterNode().getVirtualNodes());
        System.out.println("Версия схемы: " + super.getClient().getMasterNode().getVersion());
    }

    @Override
    public void helpInfo() {
        System.out.println("- вывести информацию о нодах");
    }

}
