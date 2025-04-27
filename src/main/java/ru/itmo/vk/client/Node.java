package ru.itmo.vk.client;

import lombok.SneakyThrows;
import ru.itmo.sharding.slave.api.StorageApi;
import ru.itmo.sharding.slave.invoker.ApiClient;
import ru.itmo.sharding.slave.model.KeyValueRequest;
import ru.itmo.sharding.slave.model.ValueResponse;

public class Node extends AbstractNode{

    private final StorageApi storageApi;

    public Node(String address) {
        super(address);

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
}
