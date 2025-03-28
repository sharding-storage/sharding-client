package ru.itmo.vk.client;

import lombok.Getter;
import lombok.SneakyThrows;
import ru.itmo.sharding.api.MainApi;
import ru.itmo.sharding.invoker.ApiClient;
import ru.itmo.sharding.model.NodeRequest;
import ru.itmo.sharding.model.ShardRequest;

import java.util.List;

public class MasterNode extends AbstractNode {

    @Getter
    private List<Node> nodes;

    private final MainApi mainApi;

    public MasterNode(String address) {
        super(address);
        var ip = address.split(":");

        if (ip.length != 2) {
            throw new IllegalArgumentException("Адрес должен состоять из адреса и порта, например 127.0.0.1:8001");
        }
        int port = 0;
        try {
            port = Integer.parseInt(ip[1]);
        } catch (Exception e){
            throw new IllegalArgumentException("Что-то не так с портом!");
        }

        var client = new ApiClient();

        client.setScheme("http");
        client.setHost(ip[0]);
        client.setPort(port);

        mainApi = new MainApi(client);
    }

    @SneakyThrows
    public void refreshSchema() {
        var addresses = mainApi.refreshSchema().getNodes();
        if (addresses == null || addresses.isEmpty()) {
            nodes = List.of();
        }

        nodes = addresses.stream()
            .map(Node::new).toList();

//        return List.of(
//            new Node("192.168.1.147:5001"),
//            new Node("192.168.1.147:5002"),
//            new Node("192.168.1.147:5003")
//        );
    }

    @SneakyThrows
    public String addServer(String address) {
        var request = new NodeRequest();
        request.address(address);

        return mainApi.addNode(request).getAnswer();
    }

    @SneakyThrows
    public String deleteServer(String address) {
        return mainApi.removeNode(address).getAnswer();
    }

    @SneakyThrows
    public String changeShards(int count) {
        var request = new ShardRequest();
        request.setShardCount(count);

        return mainApi.updateShards(request).getAnswer();
    }

}
