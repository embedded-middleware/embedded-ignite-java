package io.github.embedded.ignite.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

public class SocketUtil {

    public static int[] getFreeServerPorts(int number) {
        return getFreePorts(number, 10000, 49151);
    }

    public static int[] getFreeServerPorts(int number, Set<Integer> exclusivePorts) {
        return getFreePorts(number, 10000, 49151, exclusivePorts);
    }

    public static int[] getFreePorts(int number, int start, int end) {
        return getFreePorts(number, start, end, new HashSet<>());
    }

    public static int[] getFreePorts(int number, int start, int end, Set<Integer> exclusivePorts) {
        if (start < 0 || end > 65535 || start > end) {
            throw new IllegalArgumentException("Invalid port range");
        }

        int[] ports = new int[number];
        ServerSocket[] sockets = new ServerSocket[number];
        int found = 0;

        try {
            for (int i = start; i <= end && found < number; i++) {
                if (exclusivePorts.contains(i)) {
                    continue;
                }

                try {
                    sockets[found] = new ServerSocket(i);
                    ports[found] = sockets[found].getLocalPort();
                    found++;
                } catch (IOException e) {
                    // This port is not available, try the next one
                }
            }

            if (found < number) {
                throw new IllegalStateException("Could not find enough free ports in the range");
            }

            return ports;
        } finally {
            // Close all sockets to release the ports
            for (int i = 0; i < found; i++) {
                try {
                    sockets[i].close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }

}
