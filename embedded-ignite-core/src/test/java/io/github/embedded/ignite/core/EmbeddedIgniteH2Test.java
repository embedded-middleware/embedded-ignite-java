package io.github.embedded.ignite.core;

import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class EmbeddedIgniteH2Test {
    @Test
    public void testH2Sql() throws Exception {
        String createTableSql = "CREATE TABLE IF NOT EXISTS Person (id LONG PRIMARY KEY, name VARCHAR)";
        String insertSql = "INSERT INTO Person (id, name) VALUES (?, ?)";
        String selectSql = "SELECT name FROM Person WHERE id = ?";
        String updateSql = "UPDATE Person SET name = ? WHERE id = ?";
        String deleteSql = "DELETE FROM Person WHERE id = ?";
        String deleteTableSql = "DROP TABLE Person";

        EmbeddedIgniteServer server = new EmbeddedIgniteServer();
        server.start();
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setAddresses(String.format("localhost:%d", server.clientConnectorPort()));
        IgniteClient igniteClient = Ignition.startClient(clientConfiguration);

        igniteClient.query(new SqlFieldsQuery(createTableSql)).getAll();

        igniteClient.query(new SqlFieldsQuery(insertSql).setArgs(1L, "John Doe")).getAll();

        List<List<?>> results = igniteClient.query(new SqlFieldsQuery(selectSql).setArgs(1L)).getAll();
        Assertions.assertEquals("John Doe", results.get(0).get(0));

        igniteClient.query(new SqlFieldsQuery(updateSql).setArgs("Jane Doe", 1L)).getAll();

        results = igniteClient.query(new SqlFieldsQuery(selectSql).setArgs(1L)).getAll();
        Assertions.assertEquals("Jane Doe", results.get(0).get(0));

        igniteClient.query(new SqlFieldsQuery(deleteSql).setArgs(1L)).getAll();

        results = igniteClient.query(new SqlFieldsQuery(selectSql).setArgs(1L)).getAll();
        Assertions.assertTrue(results.isEmpty());

        igniteClient.query(new SqlFieldsQuery(deleteTableSql)).getAll();
        server.close();
    }
}
