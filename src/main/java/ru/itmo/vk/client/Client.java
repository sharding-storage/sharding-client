package ru.itmo.vk.client;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.itmo.vk.hash.HashFunction;

import java.util.SortedMap;
import java.util.TreeMap;

@Slf4j
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

        node.setValue(key, value);
    }

    private Node getNode(String key) {
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
        masterNode.refreshSchema().forEach((node) -> {
            circle.put(hashFunction.hash(node.toString()), node);
        });
    }
}
