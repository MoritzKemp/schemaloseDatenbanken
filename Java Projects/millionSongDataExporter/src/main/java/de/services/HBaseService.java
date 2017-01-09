package de.services;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * Created by eugenbesel on 30.11.16.
 */
public class HBaseService {


    public void test() throws IOException {
        Table table = null;
        Connection connection = null;
        String hbaseHost = "10.20.110.61";
        try {
            System.out.println("erzeuge die HBase Konfiguration");
            Configuration config = HBaseConfiguration.create();
            config.setInt("timeout", 120000);
            config.set("hbase.master", "*" + hbaseHost + ":16006*");
            config.set("hbase.zookeeper.quorum","10.20.110.61");
            config.set("hbase.zookeeper.property.clientPort", "2186");

            System.out.println("erzeuge Connection zu HBase");
            connection = ConnectionFactory.createConnection(config);

            System.out.println("Hole die tabelle music");
            table = connection.getTable(TableName.valueOf("music"));

            System.out.println("Erzeuge die Reihe Song1");
            Put p = new Put(Bytes.toBytes("Song1"));

            System.out.println("Erzeuge eine Spalte für die Spaltenfamilei song");
            p.addColumn(Bytes.toBytes("song"), Bytes.toBytes("Author"),Bytes.toBytes("Amazing grace"));

            System.out.println("Füge die Spalte in die Tabelle");
            table.put(p);

            System.out.println("Schliese die Tabelle");
            table.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            // Use the table as needed, for a single operation and a single thread
        } finally {
            table.close();
            connection.close();
        }
    }
}
