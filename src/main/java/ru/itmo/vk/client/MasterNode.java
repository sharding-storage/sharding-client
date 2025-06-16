package ru.itmo.vk.client;

import lombok.Getter;
import lombok.SneakyThrows;
import ru.itmo.sharding.master.api.MainApi;
import ru.itmo.sharding.master.invoker.ApiClient;
import ru.itmo.sharding.master.model.ChangeShardRequest;
import ru.itmo.sharding.master.model.NodeRequest;

import java.util.List;

public class MasterNode extends AbstractNode {

    private final MainApi mainApi;
    @Getter
    private List<Node> nodes;
    @Getter
    private int virtualNodes;
    @Getter
    private int version;

    public MasterNode(String address) {
        super(address);
        var ip = address.split(":");

        if (ip.length != 2) {
            throw new IllegalArgumentException("Адрес должен состоять из адреса и порта, например 127.0.0.1:8001");
        }
        int port;
        try {
            port = Integer.parseInt(ip[1]);
        } catch (Exception e) {
            throw new IllegalArgumentException("Что-то не так с портом!");
        }

        var client = new ApiClient();

        client.setScheme("http");
        client.setHost(ip[0]);
        client.setPort(port);

        mainApi = new MainApi(client);

        version = 1;
    }

    @SneakyThrows
    public void refreshSchema() {
        var response = mainApi.refreshSchema();
        var addresses = response.getNodes();
        if (addresses == null || addresses.isEmpty()) {
            nodes = List.of();
        }

        nodes = addresses.stream()
                .map(e -> new Node(e.getAddress(), e.getSalts())).toList();
        virtualNodes = response.getVirtualNodes();
        setVersion(response.getVersion());
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
        var request = new ChangeShardRequest();
        request.setShardCount(count);

        return mainApi.updateShards(request).getAnswer();
    }

    public void setVersion(int version) {
        if (this.version < version) {
            this.version = version;
        }
    }
}
