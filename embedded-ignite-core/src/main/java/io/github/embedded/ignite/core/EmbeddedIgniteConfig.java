package io.github.embedded.ignite.core;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmbeddedIgniteConfig {
    public int clientConnectorPort;

    public int jdbcPort;

    public int httpPort;

    public SqlEngineType sqlEngineType;

    public EmbeddedIgniteConfig() {
    }

    public EmbeddedIgniteConfig clientConnectorPort(int clientConnectorPort) {
        this.clientConnectorPort = clientConnectorPort;
        return this;
    }

    public EmbeddedIgniteConfig jdbcPort(int jdbcPort) {
        this.jdbcPort = jdbcPort;
        return this;
    }

    public EmbeddedIgniteConfig httpPort(int httpPort) {
        this.httpPort = httpPort;
        return this;
    }

    public EmbeddedIgniteConfig sqlEngineType(SqlEngineType sqlEngineType) {
        this.sqlEngineType = sqlEngineType;
        return this;
    }
}
