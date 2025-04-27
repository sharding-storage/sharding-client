package ru.itmo.vk.client;

import lombok.SneakyThrows;
import ru.itmo.sharding.api.StorageApi;
import ru.itmo.sharding.invoker.ApiClient;
import ru.itmo.sharding.model.KeyValueRequest;

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
    public String getValue(String key) {
        return storageApi.getValue(key).getValue();
    }

    @SneakyThrows
    public void setValue(String key, String value) {
        var request = new KeyValueRequest();
        request.setValue(value);

        storageApi.setValue(key, request);
    }

    @Override
    public String toString() {
        return super.getAddress();
    }
}
