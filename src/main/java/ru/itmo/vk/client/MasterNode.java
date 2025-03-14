package ru.itmo.vk.client;

import sharding.MasterNodeGrpc;

import java.util.List;

public class MasterNode extends AbstractNode {
    private final MasterNodeGrpc.MasterNodeBlockingStub masterNodeClient;

    public MasterNode(String address) {
        super(address);
        masterNodeClient = MasterNodeGrpc.newBlockingStub(getChannel());
    }

    public List<Node> refreshSchema() {
        //SchemaReply schemaReply = masterNodeClient.refreshSchema(Empty.getDefaultInstance());

        //return schemaReply.getMessageList().stream().map(Node::new).toList();

        return List.of(
            new Node("192.168.1.147:5001"),
            new Node("192.168.1.147:5002"),
            new Node("192.168.1.147:5003")
        );
    }
}
