package ru.itmo.vk.client;

import sharding.NodeGrpc;
import static sharding.Database.*;

public class Node extends AbstractNode{
    private final NodeGrpc.NodeBlockingStub nodeClient;

    public Node(String address) {
        super(address);
        nodeClient = NodeGrpc.newBlockingStub(getChannel());
    }

    public String getValue(String key) {
        KeyRequest request = KeyRequest.newBuilder()
            .setKey(key)
            .build();

        //return nodeClient.getValue(request).getValue();
        return null; //stub
    }

    public void setValue(String key, String value) {
        KeyValueRequest request = KeyValueRequest.newBuilder()
            .setKey(key)
            .setValue(value)
            .build();

        //nodeClient.setValue(request);
    }
}
