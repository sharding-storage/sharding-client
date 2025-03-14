package ru.itmo.vk.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.Getter;

@Getter
public abstract class AbstractNode {
    private final String address;
    private final ManagedChannel channel;

    public AbstractNode(String address) {
        this.address = address;
        this.channel = ManagedChannelBuilder.forTarget(address)
            .usePlaintext()
            .build();
    }
}
