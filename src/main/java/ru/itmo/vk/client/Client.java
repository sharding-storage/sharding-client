package ru.itmo.vk.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.itmo.vk.hash.HashFunction;

import java.util.*;

@Slf4j
@Getter
@RequiredArgsConstructor
public class Client {
    private final SortedMap<Integer, Node> circle = new TreeMap<>();

    private final HashFunction hashFunction;
    private final MasterNode masterNode;

    public String getValue(String key) {
        var node = getNode(key);
        if (node == null) refreshSchema();

        node = getNode(key);
        if (node == null) return null;

        System.out.println("got node " + node);

        try {
            System.out.println("first try");
            var response = node.getValue(key);

            if (response.getVersion() == null || response.getVersion() > masterNode.getVersion()) {
                System.out.println("new version -> " + response.getVersion() + ", and old -> " + masterNode.getVersion());
                throw new Exception("Updating version");
            }

            return response.getValue();
        } catch (Exception e) {
            System.out.println("second try");
            refreshSchema();
            node = getNode(key);
            return node.getValue(key).getValue();
        }
    }

    public void setValue(String key, String value) throws Exception {
        var node = getNode(key);
        if (node == null) refreshSchema();

        node = getNode(key);
        if (node == null) {
            throw new Exception("Нужно добавить хотя бы один сервер");
        }

        try {
            var version = node.getVersion();

            if (version > masterNode.getVersion()) {
                System.out.println("new version -> " + version + ", and old -> " + masterNode.getVersion());
                throw new Exception("Updating version");
            }
            node.setValue(key, value);
        } catch (Exception e) {
            refreshSchema();
            node = getNode(key);
            node.setValue(key, value);
        }
    }

    public Node getNode(String key) {
        if (circle.isEmpty()) {
            return null;
        }
        int hash = hashFunction.hash(key);
        if (!circle.containsKey(hash)) {
            var tailMap = circle.tailMap(hash);
            hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
        }

        return circle.get(hash);
    }

    public void refreshSchema() {
        circle.clear();
        masterNode.refreshSchema();
        List<Node> nodes = masterNode.getNodes();
        int virtualNodeCount = masterNode.getVirtualNodes();
        for (Node node : nodes) {
            addNode(node, virtualNodeCount);
        }
        System.out.println("got new nodes: " + nodes);
        System.out.println("got new virtual nodes: " + virtualNodeCount);
        System.out.println("got new version: " + masterNode.getVersion());
    }

    public void addNode(Node node, int virtualNodes) {
        for (int i = 0; i < virtualNodes; i++) {
            int hash = hashFunction.hash(node.toString() + "-" + node.getSalts().getOrDefault("" + i, "" + i));
            circle.put(hash, node);
        }
    }

    public String addServer(String address) {
        try {
            var ans =  masterNode.addServer(address);
            refreshSchema();
            return ans;
        } catch (Exception e){
            return "Can't access server: " + e.getMessage();
        }
    }

    public String deleteServer(String address) {
        try {
            var ans = masterNode.deleteServer(address);
            refreshSchema();
            return ans;
        } catch (Exception e){
            return "Can't access server: " + e.getMessage();
        }
    }

    public String changeShards(int count) {
        try {
            var ans = masterNode.changeShards(count);
            refreshSchema();
            return ans;
        } catch (Exception e){
            return "Can't access server: " + e.getMessage();
        }
    }

    public void printAllHashRanges() {
        masterNode.getNodes().forEach(e ->
            System.out.println(e.toString() + ": " + getHashRanges(e))
        );
    }

    private List<HashRange> getHashRanges(Node node) {
        List<HashRange> ranges = new ArrayList<>();
        if (circle.isEmpty()) {
            return ranges;
        }

        // Собираем все хэши для данного узла
        List<Integer> nodeHashes = new ArrayList<>();
        for (Map.Entry<Integer, Node> entry : circle.entrySet()) {
            if (entry.getValue().equals(node)) {
                nodeHashes.add(entry.getKey());
            }
        }

        if (nodeHashes.isEmpty()) {
            return ranges;
        }

        Collections.sort(nodeHashes);

        for (Integer hash : nodeHashes) {
            SortedMap<Integer, Node> headMap = circle.headMap(hash);
            int startHash;
            if (headMap.isEmpty()) {
                if (!circle.get(circle.lastKey()).equals(node)) {
                    startHash = circle.lastKey() + 1;
                } else {
                    startHash = circle.lastKey();
                }
            } else {
                if (!headMap.get(headMap.lastKey()).equals(node)) {
                    startHash = headMap.lastKey() + 1;
                } else {
                    startHash = headMap.lastKey();
                }
            }
            ranges.add(new HashRange(startHash, hash));
        }

        return ranges;
    }
}
