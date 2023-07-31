package io.github.embedded.ignite.core;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmbeddedIgniteConfig {
    private int port;

    public EmbeddedIgniteConfig() {
    }

    public EmbeddedIgniteConfig port(int port) {
        this.port = port;
        return this;
    }
}
