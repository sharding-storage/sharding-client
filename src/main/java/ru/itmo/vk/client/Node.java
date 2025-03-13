package ru.itmo.vk.client;

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

    @Override
    public String getAddress() {
        return super.getAddress();
    }
}
