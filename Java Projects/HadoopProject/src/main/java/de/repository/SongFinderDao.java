package de.repository;

import de.model.Song;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.filter.ValueFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

    public List<Song> getSongNamesWithEquals(String columnName, String value) throws IOException {
        Scan scan = new Scan();
        scan.setFilter(new SingleColumnValueFilter("song".getBytes(), columnName.getBytes(), CompareFilter.CompareOp.EQUAL, value.getBytes()));
        // Getting the scan result
        ResultScanner scanner = table.getScanner(scan);
        // Reading values from scan result
        boolean completed = false;
        List<Song> resultList = new ArrayList<>();
        while(!completed) {
            Result scannerResult = scanner.next();
            if(scannerResult == null){
                completed = true;
                continue;
            }
            mapResultSet(resultList, scannerResult);
        }
        //closing the scanner
        scanner.close();
        return resultList;
    }

    private void mapResultSet(List<Song> resultList, Result result) throws UnsupportedEncodingException {
        byte[] songKey = result.getRow();
        byte[] songName = result.getValue("song".getBytes(), "SongName".getBytes());
        byte[] duration = result.getValue("song".getBytes(), "Duration".getBytes());
        byte[] artistName = result.getValue("song".getBytes(), "ArtistName".getBytes());
        byte[] year = result.getValue("song".getBytes(), "Year".getBytes());
        byte[] sampleRate = result.getValue("song".getBytes(), "SampleRate".getBytes());
        byte[] trackId = result.getValue("song".getBytes(), "TrackId".getBytes());
        Song song = new Song(new String(songKey,"UTF-8"), new String(songName,"UTF-8"), new String(artistName,"UTF-8"), new String(duration,"UTF-8"), new String(year,"UTF-8"), new String(sampleRate,"UTF-8"), new String(trackId,"UTF-8"));
        resultList.add(song);
    }

    public List<Song> getSongNamesWithGreaterOperator(String columnName, String value) throws IOException {

        Scan scan = new Scan();
        scan.setFilter(new SingleColumnValueFilter("song".getBytes(), columnName.getBytes(), CompareFilter.CompareOp.GREATER, value.getBytes()));
        // Getting the scan result
        ResultScanner scanner = table.getScanner(scan);
        // Reading values from scan result
        boolean completed = false;
        List<Song> resultList = new ArrayList<>();
        while(!completed) {
            Result scannerResult = scanner.next();
            if(scannerResult == null){
                completed = true;
                continue;
            }
            mapResultSet(resultList, scannerResult);
        }
        //closing the scanner
        scanner.close();
        return resultList;
    }

    public List<Song> getSongNamesWithLikeOperator(String columnName, String value) throws IOException {

        Scan scan = new Scan();
        scan.setFilter(new ValueFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator(value)));
        scan.addColumn("song".getBytes(), columnName.getBytes());
        // Getting the scan result
        ResultScanner scanner = table.getScanner(scan);
        // Reading values from scan result
        boolean completed = false;
        List<Song> resultList = new ArrayList<>();
        while(!completed) {
            Result scannerResult = scanner.next();
            if(scannerResult == null){
                completed = true;
                continue;
            }
            mapResultSet(resultList, scannerResult);
        }
        //closing the scanner
        scanner.close();
        return resultList;
    }
}
