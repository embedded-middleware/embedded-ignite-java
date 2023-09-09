package io.github.embedded.ignite.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EmbeddedIgniteServerTest {
    @Test
    public void testIgniteServerBoot() throws Exception {
        EmbeddedIgniteServer server = new EmbeddedIgniteServer();
        server.start();
        Assertions.assertTrue(server.clientConnectorPort() > 0);
        Assertions.assertTrue(server.jdbcPort() > 0);
        Assertions.assertTrue(server.httpPort() > 0);
        server.close();
    }

    @Test
    public void testIgniteServerBootSpecify1Port() throws Exception {
        int[] serverPorts = SocketUtil.getFreeServerPorts(1);
        EmbeddedIgniteConfig embeddedIgniteConfig = new EmbeddedIgniteConfig();
        embeddedIgniteConfig.setClientConnectorPort(serverPorts[0]);
        EmbeddedIgniteServer server = new EmbeddedIgniteServer(embeddedIgniteConfig);
        server.start();
        Assertions.assertEquals(serverPorts[0], server.clientConnectorPort());
        server.close();
    }

    @Test
    public void testIgniteServerBootSpecify2Port() throws Exception {
        int[] serverPorts = SocketUtil.getFreeServerPorts(2);
        EmbeddedIgniteConfig embeddedIgniteConfig = new EmbeddedIgniteConfig();
        embeddedIgniteConfig.setClientConnectorPort(serverPorts[0]);
        embeddedIgniteConfig.setJdbcPort(serverPorts[1]);
        EmbeddedIgniteServer server = new EmbeddedIgniteServer(embeddedIgniteConfig);
        server.start();
        Assertions.assertEquals(serverPorts[0], server.clientConnectorPort());
        Assertions.assertEquals(serverPorts[1], server.jdbcPort());
        server.close();
    }

    @Test
    public void testIgniteServerBootSpecify3Port() throws Exception {
        int[] serverPorts = SocketUtil.getFreeServerPorts(3);
        EmbeddedIgniteConfig embeddedIgniteConfig = new EmbeddedIgniteConfig();
        embeddedIgniteConfig.setClientConnectorPort(serverPorts[0]);
        embeddedIgniteConfig.setJdbcPort(serverPorts[1]);
        embeddedIgniteConfig.setHttpPort(serverPorts[2]);
        EmbeddedIgniteServer server = new EmbeddedIgniteServer(embeddedIgniteConfig);
        server.start();
        Assertions.assertEquals(serverPorts[0], server.clientConnectorPort());
        Assertions.assertEquals(serverPorts[1], server.jdbcPort());
        Assertions.assertEquals(serverPorts[2], server.httpPort());
        server.close();
    }
}
