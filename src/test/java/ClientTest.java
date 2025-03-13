import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import ru.itmo.vk.client.Client;
import ru.itmo.vk.client.MasterNode;
import ru.itmo.vk.client.Node;
import ru.itmo.vk.hash.HashFunction;
import ru.itmo.vk.hash.MD5HashFunction;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClientTest {

//    Client client;
//    HashMap<Node, Integer> values;
//
//    @BeforeAll
//    void init () {
//        client = new Client(new MD5HashFunction(), new MasterNode("test"));
//        values = new HashMap<>();
//    }
//    @Test
//    void collectData() {
//        client.refreshSchema();
//
//        for (int i = 0; i < 100000; i++) {
//            var value = values.getOrDefault(client.getNode(RandomStringUtils.random(100)), 0);
//            values.put(client.getNode(RandomStringUtils.random(100)), ++value);
//        }
//
//        System.out.println(values);
//    }

    private Client client;
    private HashFunction hashFunction;
    private MasterNode masterNode;

    @BeforeEach
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

        assertNotNull(result);
    }

    @Test
    void testRefreshSchema() {
        Node node1 = new Node("192.168.1.147:5001");
        Node node2 = new Node("192.168.1.147:5002");
        when(masterNode.refreshSchema()).thenReturn(List.of(node1, node2));

        client.refreshSchema();

        assertEquals(2, client.getCircle().size());
    }

    @Test
    void testGetNodeWhenCircleIsEmpty() {
        when(hashFunction.hash("key4")).thenReturn(101112);

        Node result = client.getNode("key4");

        assertNull(result);
    }

    @Test
    void testGetValueWhenNodeIsNull() {
        when(hashFunction.hash("key5")).thenReturn(131415);

        String result = client.getValue("key5");

        assertThat(result, is(nullValue()));
    }
}
