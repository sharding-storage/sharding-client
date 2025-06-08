package ru.itmo.vk.client;

import lombok.SneakyThrows;
import ru.itmo.sharding.slave.api.StorageApi;
import ru.itmo.sharding.slave.invoker.ApiClient;
import ru.itmo.sharding.slave.model.KeyValueRequest;
import ru.itmo.sharding.slave.model.ValueResponse;

import java.util.Map;

public class Node extends AbstractNode{

    private final Map<String, String> salts;
    private final StorageApi storageApi;

    public Node(String address, Map<String, String> salts) {
        super(address);
        this.salts = salts;

        var client = new ApiClient();
        client.setScheme("http");

        var ip = address.split(":");
        client.setHost(ip[0]);
        client.setPort(Integer.parseInt(ip[1]));

        storageApi = new StorageApi(client);
    }

    @SneakyThrows
    public ValueResponse getValue(String key) {
        return storageApi.getValue(key);
    }

    @SneakyThrows
    public void setValue(String key, String value) {
        var request = new KeyValueRequest();
        request.setValue(value);

        storageApi.setValue(key, request);
    }

    @SneakyThrows
    public int getVersion() {
        return storageApi.getVersion().getVersion();
    }

    @Override
    public String toString() {
        return super.getAddress();
    }

    public Map<String, String> getSalts() {
        return salts;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Node)) {
            return false;
        }
        return getAddress().equals(((Node) other).getAddress());
    }

    @Override
    public int hashCode() {
        return getAddress().hashCode();
    }

}
