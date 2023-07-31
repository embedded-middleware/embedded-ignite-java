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
}
