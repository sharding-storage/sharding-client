package ru.itmo.vk.client;

import lombok.Getter;

@Getter
public abstract class AbstractNode {
    private final String address;

    protected AbstractNode(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
