package ru.itmo.vk.command;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.vk.client.Client;

public class ChangeShardsCountCommand extends Command{
    public static final String NAME = "changeShards";
    private static final Logger log = LoggerFactory.getLogger(ChangeShardsCountCommand.class);

    protected ChangeShardsCountCommand(Client client) {
        super(client);
    }

    @SneakyThrows
    @Override
    public void execute(Object... args) {
        if (args.length != 1) {
            System.out.println("Неправильное количество аргументов");
            throw new IllegalArgumentException();
        }

        var count = -1;
        try {
            count = Integer.parseInt((String) args[0]);
            if (count < 0) {
                throw new IllegalArgumentException("count не может быть меньше 0");
            }
        } catch (NumberFormatException e) {
            System.out.println("Введено не число");
            throw new Exception(e);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            throw new Exception(e);
        }
        System.out.println(super.getClient().changeShards(count));
    }

    @Override
    public void helpInfo() {
        System.out.println("<count> - изменение кол-ва шардов");
    }
}
