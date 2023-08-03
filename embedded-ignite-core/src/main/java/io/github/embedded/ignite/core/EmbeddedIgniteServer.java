package io.github.embedded.ignite.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.ClientConnectorConfiguration;
import org.apache.ignite.configuration.ConnectorConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.internal.processors.cache.persistence.wal.reader.StandaloneNoopCommunicationSpi;
import org.apache.ignite.logger.slf4j.Slf4jLogger;
import org.apache.ignite.spi.discovery.isolated.IsolatedDiscoverySpi;
import org.assertj.core.util.Files;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;

@Slf4j
public class EmbeddedIgniteServer {
    private final EmbeddedIgniteConfig embeddedIgniteConfig;

    private final IgniteConfiguration igniteConfiguration;

    private Ignite ignite;

    private int clientConnectorPort;

    private int jdbcPort;

    private int httpPort;

    public EmbeddedIgniteServer() {
        this(new EmbeddedIgniteConfig());
    }

    public EmbeddedIgniteServer(EmbeddedIgniteConfig embeddedIgniteConfig) {
        this.embeddedIgniteConfig = embeddedIgniteConfig;
        this.igniteConfiguration = new IgniteConfiguration();
    }

    public void start() throws Exception {
        String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        int index = jvmName.indexOf('@');
        if (index > 1) {
            String pid = jvmName.substring(0, index);
            log.info("pid is {}", pid);
        }
        int[] ports = SocketUtil.getFreeServerPorts(3);
        this.clientConnectorPort = ports[0];
        this.jdbcPort = ports[1];
        this.httpPort = ports[2];
        log.info("clientConnectorPort is {} jdbcPort is {} httpPort is {}", clientConnectorPort, jdbcPort, httpPort);
        igniteConfiguration.setDiscoverySpi(new IsolatedDiscoverySpi());
        igniteConfiguration.setCommunicationSpi(new StandaloneNoopCommunicationSpi());
        igniteConfiguration.setIgniteInstanceName("local-ignite-server");
        igniteConfiguration.setConsistentId("local-ignite-server");
        igniteConfiguration.setGridLogger(new Slf4jLogger());
        ConnectorConfiguration connectorConfiguration = new ConnectorConfiguration();
        connectorConfiguration.setPort(jdbcPort);
        File jettyConfigFolder = Files.newTemporaryFolder();
        jettyConfigFolder.deleteOnExit();
        createJettyConfigFile(jettyConfigFolder, httpPort);
        connectorConfiguration.setJettyPath(jettyConfigFolder.getAbsolutePath() + File.separator + "jetty.xml");
        igniteConfiguration.setConnectorConfiguration(connectorConfiguration);
        ClientConnectorConfiguration clientConnectorConfiguration = new ClientConnectorConfiguration();
        clientConnectorConfiguration.setPort(clientConnectorPort);
        igniteConfiguration.setClientConnectorConfiguration(clientConnectorConfiguration);
        this.ignite = Ignition.start(igniteConfiguration);
    }

    public int clientConnectorPort() {
        return this.clientConnectorPort;
    }

    public int jdbcPort() {
        return this.jdbcPort;
    }

    public int httpPort() {
        return this.httpPort;
    }

    private static void createJettyConfigFile(File folder, int port) throws IOException {
        String xmlContent = "<?xml version=\"1.0\"?>\n"
                + "<!DOCTYPE Configure PUBLIC \"-//Jetty//Configure//EN\""
                + " \"http://www.eclipse.org/jetty/configure.dtd\">\n"
                + "<Configure id=\"Server\" class=\"org.eclipse.jetty.server.Server\">\n"
                + "    <Call name=\"addConnector\">\n"
                + "        <Arg>\n"
                + "            <New class=\"org.eclipse.jetty.server.ServerConnector\">\n"
                + "                <Arg name=\"server\"><Ref refid=\"Server\"/></Arg>\n"
                + "                <Arg name=\"factories\">\n"
                + "                    <Array type=\"org.eclipse.jetty.server.ConnectionFactory\">\n"
                + "                        <Item>\n"
                + "                            <New class=\"org.eclipse.jetty.server.HttpConnectionFactory\">\n"
                + "                                <Ref refid=\"httpCfg\"/>\n"
                + "                            </New>\n"
                + "                        </Item>\n"
                + "                    </Array>\n"
                + "                </Arg>\n"
                + "                <Set name=\"host\">localhost</Set>\n"
                + "                <Set name=\"port\">" + port + "</Set>\n"
                + "                <Set name=\"reuseAddress\">true</Set>\n"
                + "            </New>\n"
                + "        </Arg>\n"
                + "    </Call>\n"
                + "</Configure>";
        File jettyConfigFile = new File(folder, "jetty.xml");
        try (OutputStream os = java.nio.file.Files.newOutputStream(jettyConfigFile.toPath());
             OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
             BufferedWriter writer = new BufferedWriter(osw)) {
            writer.write(xmlContent);
        }
    }

    public void close() throws Exception {
        this.ignite.close();
    }
}
