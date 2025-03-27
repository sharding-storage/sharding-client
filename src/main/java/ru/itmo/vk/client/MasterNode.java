package ru.itmo.vk.client;

import java.util.ArrayList;
import java.util.List;

public class MasterNode extends AbstractNode {

    public MasterNode(String address) {
        super(address);
    }

    public List<Node> refreshSchema() {

        return List.of(
            new Node("192.168.1.147:5001"),
            new Node("192.168.1.147:5002"),
            new Node("192.168.1.147:5003")
        );
    }
}
