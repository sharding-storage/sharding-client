package ru.itmo.vk;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.itmo.vk.client.Client;
import ru.itmo.vk.client.MasterNode;
import ru.itmo.vk.hash.MD5HashFunction;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProgramRunner {
    public static void start() {
        MasterNode masterNode = new MasterNode("address");
        Client client = new Client(new MD5HashFunction(), masterNode);
    }
}

