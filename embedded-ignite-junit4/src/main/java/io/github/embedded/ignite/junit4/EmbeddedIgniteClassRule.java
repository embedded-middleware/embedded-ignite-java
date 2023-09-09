package io.github.embedded.ignite.junit4;

import io.github.embedded.ignite.core.AnnotationUtil;
import io.github.embedded.ignite.core.EmbeddedIgniteConfig;
import io.github.embedded.ignite.core.EmbeddedIgnitePorts;
import io.github.embedded.ignite.core.EmbeddedIgniteServer;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class EmbeddedIgniteClassRule implements TestRule {

    private EmbeddedIgniteServer embeddedIgniteServer;

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                setUp(description);
                try {
                    base.evaluate(); // Executes the test
                } finally {
                    tearDown();
                }
            }
        };
    }

    private void setUp(Description description) throws Exception {
        EmbeddedIgniteConfig igniteConfig = new EmbeddedIgniteConfig();

        EmbeddedIgnitePorts portsAnnotation = AnnotationUtil.findPorts(description.getTestClass());

        if (portsAnnotation != null) {
            igniteConfig.setClientConnectorPort(portsAnnotation.clientConnectorPort());
            igniteConfig.setJdbcPort(portsAnnotation.jdbcPort());
            igniteConfig.setHttpPort(portsAnnotation.httpPort());
        }

        embeddedIgniteServer = new EmbeddedIgniteServer(igniteConfig);
        embeddedIgniteServer.start();
    }

    private void tearDown() throws Exception {
        embeddedIgniteServer.close();
    }
}
