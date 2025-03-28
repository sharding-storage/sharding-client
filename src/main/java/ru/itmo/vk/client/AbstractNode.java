package ru.itmo.vk.client;

import lombok.Getter;

@Getter
public abstract class AbstractNode {
    private final String address;

    public AbstractNode(String address) {
        this.address = address;
    }
}
