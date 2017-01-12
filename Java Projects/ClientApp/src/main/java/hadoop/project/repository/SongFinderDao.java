package hadoop.project.repository;

import hadoop.project.model.ArtistResult;
import hadoop.project.model.Song;
import hadoop.project.model.Statistic;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.client.coprocessor.AggregationClient;
import org.apache.hadoop.hbase.client.coprocessor.LongColumnInterpreter;
import org.apache.hadoop.hbase.coprocessor.ColumnInterpreter;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eugenbesel on 04.12.16.
 */
@Component
public class SongFinderDao {
    private Connection connection = null;
    private String hbaseHost = "10.20.110.61";
    private AggregationClient aggregationClient;


    // @PostConstruct
    public void init() throws IOException {
        if(connection == null || connection.isClosed()){
            try {
                System.out.println("erzeuge die HBase Konfiguration");
                Configuration config = HBaseConfiguration.create();
                config.setInt("timeout", 120000);
                config.set("hbase.master", "*" + hbaseHost + ":16006*");
                config.set("hbase.zookeeper.quorum","10.20.110.61");
                config.set("hbase.zookeeper.property.clientPort", "2186");

                System.out.println("erzeuge Connection zu HBase");
                connection = ConnectionFactory.createConnection(config);
                aggregationClient = new AggregationClient(config);
                HTable t = new HTable(config, TableName.valueOf("".getBytes()));
                t.setAutoFlush(true);
               // AggregationClient aClient = new AggregationClient(config);
                //aClient.avg(TableName.valueOf("music".getBytes()),);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Exception" + e.getMessage());
            }
        }
    }

    public Table getTable(String tableName) throws IOException {
        System.out.println("Hole die tabelle: " + tableName);

        return connection.getTable(TableName.valueOf(tableName));
    }

    @PreDestroy
    public void closeConnection() throws IOException {
        connection.close();
    }

    //@PreDestroy
    public void closeTable(Table table) throws IOException {
        table.close();
    }

    public List<Song> getSongsWithEquals(String columnName, String value) throws IOException {
        Table table = getTable("music");
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
        closeTable(table);

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
        Song song = new Song(songKey == null ? "123" : new String(songKey,"UTF-8"),
                songName == null ? "" : new String(songName,"UTF-8"),
                artistName == null ? "" : new String(artistName,"UTF-8"),
                duration == null ? "" : new String(duration,"UTF-8"),
                year == null ? "" : new String(year,"UTF-8"),
                sampleRate == null ? "" : new String(sampleRate,"UTF-8"),
                trackId == null ? "" : new String(trackId,"UTF-8"));
        resultList.add(song);
    }

    public List<Song> getSongNamesWithGreaterOperator(String columnName, String value) throws IOException {
        Table table = getTable("music");

        Scan scan = new Scan();
        scan.setFilter(new SingleColumnValueFilter("song".getBytes(), columnName.getBytes(), CompareFilter.CompareOp.GREATER, value.getBytes()));
        // Getting the scan result
        ResultScanner scanner = table.getScanner(scan);
        List<Song> resultList = new ArrayList<>();

        for (Result result : scanner){
            mapResultSet(resultList, result);
        }
        //closing the scanner
        scanner.close();
        closeTable(table);

        return resultList;
    }

    public List<Song> getSongNamesWithLikeOperator(String columnName, String value) throws IOException {
        Table table = getTable("music");

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
        closeTable(table);

        return resultList;
    }

    public String getArtistforKey(String songKey) throws IOException {
        Table table = getTable("music");

        Get get = new Get(Bytes.toBytes(songKey));
        get.addColumn(Bytes.toBytes("song"), Bytes.toBytes("ArtistName"));

        // Getting the scan result
        Result result = table.get(get);
        // Reading values from scan result
        byte[] artistName = result.getValue("song".getBytes(), "ArtistName".getBytes());

        closeTable(table);
        return new String(artistName,"UTF-8");

    }

    public void addNewSong(Song song) throws IOException {
        Table table = getTable("music");

        Put p = new Put(Bytes.toBytes(song.getSongKey()));

        //System.out.println("Erzeuge eine Spalte für die Spaltenfamilei song");
        p.addColumn(Bytes.toBytes("song"), Bytes.toBytes("ArtistName"),Bytes.toBytes(song.getArtistName()));
        p.addColumn(Bytes.toBytes("song"), Bytes.toBytes("SongName"),Bytes.toBytes(song.getSongName()));
        p.addColumn(Bytes.toBytes("song"), Bytes.toBytes("Duration"),Bytes.toBytes(song.getDuration()));
        p.addColumn(Bytes.toBytes("song"), Bytes.toBytes("Year"),Bytes.toBytes(song.getYear()));
        p.addColumn(Bytes.toBytes("song"), Bytes.toBytes("SampleRate"),Bytes.toBytes(song.getSampleRate()));
      //  p.addColumn(Bytes.toBytes("song"), Bytes.toBytes("TrackId"),Bytes.toBytes(song.getTrackId()));

        //System.out.println("Füge die Spalte in die Tabelle: " + value);
        table.put(p);
        closeTable(table);
    }
    public void deleteSong(Song song) throws IOException {
        Table table = getTable("music");

        Delete delete = new Delete(Bytes.toBytes(song.getSongKey()));
        table.delete(delete);
        closeTable(table);

    }

    public List<ArtistResult> getAllArtists(String searchSubstring) throws IOException {
        Table table = getTable("music");

        List<ArtistResult> results = new ArrayList<>();

        Scan scan = new Scan();
        scan.setFilter(new ValueFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator(searchSubstring)));
        scan.addColumn("song".getBytes(), "ArtistName".getBytes());
        // Getting the scan result
        try {
            ResultScanner scanner = table.getScanner(scan);
            int index = 0;
            for(Result scannerResult : scanner){
                byte[] artistName = scannerResult.getValue("song".getBytes(), "ArtistName".getBytes());
                results.add(new ArtistResult(index++, new String(artistName,"UTF-8")));
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        closeTable(table);
        return results;
    }


    public List<Statistic> getStatistics() throws IOException {
        Table table = getTable("statistic");

        List<Statistic> results = new ArrayList<>();

        Scan scan = new Scan();
        scan.setMaxResultSize(20);
        scan.addColumn("com".getBytes(), "NumOfSongs".getBytes());
        PageFilter pf = new PageFilter(20);
        scan.setFilter(pf);
        // Getting the scan result
        try {
            ResultScanner scanner = table.getScanner(scan);
            for(Result scannerResult : scanner){
                String countOfSongs = new String(scannerResult.getValue("com".getBytes(), "NumOfSongs".getBytes()),"UTF-8");
                System.out.println("Artist:" + new String(scannerResult.getRow(),"UTF-8") + "; songCount:" + countOfSongs);
                if(countOfSongs != null && !countOfSongs.isEmpty()){
                    try{
                        results.add(new Statistic(new String(scannerResult.getRow(),"UTF-8"), Integer.parseInt(countOfSongs)));
                    }catch (NumberFormatException nfe){
                        System.out.print("");
                    }
                }
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        closeTable(table);
        return results;
    }


    public void getMaxValue() throws IOException {

        Table table = getTable("statistic");
        Scan scan = new Scan();
        scan.addColumn("com".getBytes(), "NumOfSongs".getBytes());
        Object maxValue = null;
        try {
            maxValue = aggregationClient.max(table, new LongColumnInterpreter(), scan);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.print("MaxValue:" + maxValue);
        if(maxValue != null){
            Scan scan2 = new Scan();
            String maxValueAsString = (String)maxValue;
            scan2.setFilter(new SingleColumnValueFilter("song".getBytes(), "NumOfSongs".getBytes(), CompareFilter.CompareOp.EQUAL, maxValueAsString.getBytes()));
            scan2.addColumn("com".getBytes(), "NumOfSongs".getBytes());
            ResultScanner scanner = table.getScanner(scan);
        }

    }
}
