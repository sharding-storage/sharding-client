package ru.itmo.vk.client;

import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

public class Node extends AbstractNode{
    public Node(String address) {
        super(address);
    }

    public String getValue(String key) {
        return null; //stub
    }

    public String setValue(String key, String value) {
        return null; // stub
    }

}
