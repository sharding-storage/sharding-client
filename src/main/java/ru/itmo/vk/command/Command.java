package ru.itmo.vk.command;

import lombok.Getter;
import ru.itmo.vk.client.Client;

@Getter
public abstract class Command {
    public static final String NAME = "";

    private Client client;

    protected Command() {
    }

    protected Command(Client client) {
        this.client = client;
    }

    public void execute(Object... args) {

    }

    public void helpInfo() {
        System.out.println();
    }
}
