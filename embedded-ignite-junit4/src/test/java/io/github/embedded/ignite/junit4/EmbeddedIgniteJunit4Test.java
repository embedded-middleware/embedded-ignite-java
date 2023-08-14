package io.github.embedded.ignite.junit4;

import org.junit.ClassRule;
import org.junit.Test;

public class EmbeddedIgniteJunit4Test {
    @ClassRule
    public static EmbeddedIgniteClassRule embeddedIgniteClassRule = new EmbeddedIgniteClassRule();

    @Test
    public void testIgniteServer() {
    }
}
