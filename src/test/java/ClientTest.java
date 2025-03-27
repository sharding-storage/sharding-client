import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import ru.itmo.vk.client.Client;
import ru.itmo.vk.client.MasterNode;
import ru.itmo.vk.client.Node;
import ru.itmo.vk.hash.HashFunction;
import ru.itmo.vk.hash.MD5HashFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.MatcherAssert.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClientTest {

    HashMap<Node, List<String>> valuesCount;
    HashMap<Node, Integer> values;

    private final int SIZE = 100000;
    @BeforeAll
    void init () {
        client = new Client(new MD5HashFunction(), new MasterNode("test"));
        valuesCount = new HashMap<>();
        values = new HashMap<>();
    }

    @Test
    void collectData() {
        client.refreshSchema();

        var vls = new ArrayList<String>();
        for (int i = 0; i < SIZE; i++) {
            vls.add(RandomStringUtils.random(100));
        }

        client.getCircle().values().forEach(node -> {
            valuesCount.put(node, new ArrayList<>());
        });

        for (int i = 0; i < SIZE; i++) {
            valuesCount.get(client.getNode(vls.get(i))).add(vls.get(i));

            var value = values.getOrDefault(client.getNode(vls.get(i)), 0) + 1;
            //System.out.println(client.getNode(vls.get(i)) + " " + value);
            values.put(client.getNode(vls.get(i)), value);
        }
        AtomicInteger sum1 = new AtomicInteger();
        HashMap<Integer, Integer> sums = new HashMap<>();
        sums.put(0, 0);
        sums.put(1, 0);
        sums.put(2, 0);
        sums.put(3, 0);
        final int[] counter = {0};
        valuesCount.values().forEach(node -> {
            counter[0]++;
            sum1.addAndGet(node.size());
            sums.put(counter[0] % 4, sums.get(counter[0] % 4) + node.size());
            //System.out.println(node.size());
        });

        System.out.println(sum1);
        System.out.println(values.values().stream().reduce(Integer::sum));
        System.out.println(values);
        System.out.println(sums);
    }

    private Client client;
    private HashFunction hashFunction;
    private MasterNode masterNode;

    /*@BeforeEach
    void setUp() {
        hashFunction = Mockito.spy(MD5HashFunction.class);
        masterNode = Mockito.mock(MasterNode.class);
        client = new Client(hashFunction, masterNode);
    }

    @Test
    void testGetNode() {
        Node node1 = new Node("192.168.1.147:5001");
        Node node2 = new Node("192.168.1.147:5002");
        when(hashFunction.hash("key3")).thenReturn(789);
        when(masterNode.refreshSchema()).thenReturn(List.of(node1, node2));

        client.refreshSchema();
        Node result = client.getNode("key3");

        assertThat(result, is(notNullValue()));
    }

    @Test
    void testRefreshSchema() {
        Node node1 = new Node("192.168.1.147:5001");
        Node node2 = new Node("192.168.1.147:5002");
        when(masterNode.refreshSchema()).thenReturn(List.of(node1, node2));

        client.refreshSchema();

        assertThat(client.getCircle().size(), is(2));
    }

    @Test
    void testGetNodeWhenCircleIsEmpty() {
        when(hashFunction.hash("key4")).thenReturn(101112);

        Node result = client.getNode("key4");

        assertThat(result, is(nullValue()));
    }

    @Test
    void testGetValueWhenNodeIsNull() {
        when(hashFunction.hash("key5")).thenReturn(131415);

        String result = client.getValue("key5");

        assertThat(result, is(nullValue()));
    }*/
}
