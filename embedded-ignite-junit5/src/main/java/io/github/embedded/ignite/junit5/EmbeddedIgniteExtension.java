package io.github.embedded.ignite.junit5;

import io.github.embedded.ignite.core.EmbeddedIgniteConfig;
import io.github.embedded.ignite.core.EmbeddedIgniteServer;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class EmbeddedIgniteExtension implements BeforeAllCallback, AfterAllCallback {
    private static EmbeddedIgniteServer embeddedIgniteServer;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        EmbeddedIgniteConfig igniteConfig = new EmbeddedIgniteConfig();

        // Retrieve annotation from the test class
        EmbeddedIgnitePorts portsAnnotation = context.getRequiredTestClass().getAnnotation(EmbeddedIgnitePorts.class);

        if (portsAnnotation != null) {
            igniteConfig.setClientConnectorPort(portsAnnotation.clientConnectorPort());
            igniteConfig.setJdbcPort(portsAnnotation.jdbcPort());
            igniteConfig.setHttpPort(portsAnnotation.httpPort());
        }

        embeddedIgniteServer = new EmbeddedIgniteServer(igniteConfig);
        embeddedIgniteServer.start();
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        embeddedIgniteServer.close();
    }
}
