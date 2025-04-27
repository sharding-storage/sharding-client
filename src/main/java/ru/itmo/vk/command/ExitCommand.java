package ru.itmo.vk.command;

public class ExitCommand extends Command {
    public static final String NAME = "exit";

    @Override
    public void execute(Object... args) {
        System.exit(0);
    }

    @Override
    public void helpInfo() {
        System.out.println("- выход из приложения");
    }
}
