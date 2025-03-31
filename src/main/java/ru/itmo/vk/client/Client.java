package ru.itmo.vk.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.itmo.vk.hash.HashFunction;

import java.util.SortedMap;
import java.util.TreeMap;

@Slf4j
@Getter
@RequiredArgsConstructor
public class Client {
    private final SortedMap<Integer, Node> circle = new TreeMap<>();

    private final HashFunction hashFunction;
    private final MasterNode masterNode;

    public String getValue(String key) {
        var node = getNode(key);

        if (node == null) return null;

        try {
            return node.getValue(key);
        } catch (Exception e) {
            refreshSchema();
            return node.getValue(key);
        }
    }

    public void setValue(String key, String value) {
        var node = getNode(key);

        try {
            node.setValue(key, value);
        } catch (Exception e) {
            System.out.println(e);
            refreshSchema();
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

        masterNode.getNodes().forEach((node) -> {
            circle.put(hashFunction.hash(node.getAddress()), node);
        });
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


}
