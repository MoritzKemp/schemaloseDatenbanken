package de.repository;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.filter.ValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eugenbesel on 04.12.16.
 */
@Component
public class SongFinderDao {
    Connection connection = null;
    Table table = null;

   // @PostConstruct
    public void init() throws IOException {
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
            table =  connection.getTable(TableName.valueOf("music"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Exception" + e.getMessage());

        }
    }

    //@PreDestroy
    public void closeConnection() throws IOException {
        table.close();
        connection.close();
    }

    public List<String> getSongNamesWithEquals(String columnName, String value) throws IOException {

        Scan scan = new Scan();
        scan.setFilter(new SingleColumnValueFilter("song".getBytes(), columnName.getBytes(), CompareFilter.CompareOp.EQUAL, value.getBytes()));
        // Getting the scan result
        ResultScanner scanner = table.getScanner(scan);
        // Reading values from scan result
        boolean completed = false;
        List<String> resultList = new ArrayList<>();
        while(!completed) {
            Result result = scanner.next();
            if(result == null){
                completed = true;
                continue;
            }
            byte[] r = result.getValue("song".getBytes(), "SongName".getBytes());
            System.out.println(new String(r,"UTF-8"));
            resultList.add(new String(r,"UTF-8"));
        }
        //closing the scanner
        scanner.close();
        return resultList;
    }

    public List<String> getSongNamesWithGreaterOperator(String columnName, String value) throws IOException {

        Scan scan = new Scan();
        scan.setFilter(new SingleColumnValueFilter("song".getBytes(), columnName.getBytes(), CompareFilter.CompareOp.GREATER, value.getBytes()));
        // Getting the scan result
        ResultScanner scanner = table.getScanner(scan);
        // Reading values from scan result
        boolean completed = false;
        List<String> resultList = new ArrayList<>();
        while(!completed) {
            Result result = scanner.next();
            if(result == null){
                completed = true;
                continue;
            }
            byte[] r = result.getValue("song".getBytes(), "SongName".getBytes());
            System.out.println(new String(r,"UTF-8"));
            resultList.add(new String(r,"UTF-8"));
        }
        //closing the scanner
        scanner.close();
        return resultList;
    }

    public List<String> getSongNamesWithLikeOperator(String columnName, String value) throws IOException {

        Scan scan = new Scan();
        scan.setFilter(new ValueFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator(value)));
        scan.addColumn("song".getBytes(), columnName.getBytes());
        // Getting the scan result
        ResultScanner scanner = table.getScanner(scan);
        // Reading values from scan result
        boolean completed = false;
        List<String> resultList = new ArrayList<>();
        while(!completed) {
            Result result = scanner.next();
            if(result == null){
                completed = true;
                continue;
            }
            byte[] r = result.getValue("song".getBytes(), "SongName".getBytes());
            System.out.println(new String(r,"UTF-8"));
            resultList.add(new String(r,"UTF-8"));
        }
        //closing the scanner
        scanner.close();
        return resultList;
    }
}
