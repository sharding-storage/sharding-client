package ru.itmo.vk;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.itmo.vk.client.Client;
import ru.itmo.vk.client.MasterNode;
import ru.itmo.vk.command.Invoker;
import ru.itmo.vk.hash.MD5HashFunction;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProgramRunner {
    public static void start(String[] args) {
        if (args.length != 1) {
            System.out.println("Для запуска приложения необходимо указать только адрес мастер-узла");
            System.exit(1);
        }
        var address = args[0];

        MasterNode masterNode = new MasterNode(address);
        Client client = new Client(new MD5HashFunction(), masterNode);

        try {
            client.refreshSchema();
        } catch (Exception e){
            System.out.println("Невозможно подключиться к указанному узлу: " + address);
            System.out.println(e.getMessage());
            System.exit(1);
        }

        System.out.println("Подключение успешно");

        Invoker invoker = new Invoker(client);
        invoker.start();
    }
}

