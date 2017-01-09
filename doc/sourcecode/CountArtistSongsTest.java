package com.studium.millionsong.mapreduce.junit;

import com.studium.millionsong.mapreduce.CountArtistSongs;

import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.mapreduce.MutationSerialization;
import org.apache.hadoop.hbase.mapreduce.ResultSerialization;
import org.apache.hadoop.hbase.client.Mutation;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Row;

import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.apache.hadoop.mrunit.types.Pair;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.conf.Configuration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class CountArtistSongsTest {
    
    public MapDriver<ImmutableBytesWritable, Result, Text, IntWritable> mapDriver;
    public ReduceDriver<Text, IntWritable, ImmutableBytesWritable, Mutation> reduceDriver;
    public static final byte[] CF_SONG = "song".getBytes();
    public static final byte[] ATTR_ARTIST_NAME = "ArtistName".getBytes();
    public static final byte[] ATTR_DURATION = "Duration".getBytes();
    public static final byte[] ATTR_SAM_RATE = "SampleRate".getBytes();
    public static final byte[] ATTR_SONG_NAME = "SongName".getBytes();
    public static final byte[] ATTR_TRACK_ID = "TrackId".getBytes();
    public static final byte[] ATTR_YEAR = "Year".getBytes();
    
    public static final byte[] CF_COM = "com".getBytes();
    public static final byte[] ATTR_NUM_SONGS = "NumOfSongs".getBytes();
    
    @Before
    public void setup(){
        CountArtistSongs.ArtistSongMapper mapper = new CountArtistSongs.ArtistSongMapper();
        CountArtistSongs.ArtistSongReducer reducer = new CountArtistSongs.ArtistSongReducer();
        mapDriver = MapDriver.newMapDriver(mapper);
        reduceDriver = ReduceDriver.newReduceDriver(reducer);
        
        // Tell hadoop how to serialize the Mutation and Result-IOFormat
        Configuration conf_map = mapDriver.getConfiguration();
        conf_map.setStrings(
                "io.serializations", 
                new String[]{
                    conf_map.get("io.serializations"), 
                    MutationSerialization.class.getName(), 
                    ResultSerialization.class.getName()
                }
        );
        
        Configuration conf_reduce = reduceDriver.getConfiguration();
        conf_reduce.setStrings(
                "io.serializations", 
                new String[]{
                    conf_reduce.get("io.serializations"), 
                    MutationSerialization.class.getName(), 
                    ResultSerialization.class.getName()
                }
        );
    }
    
    @Test
    public void testMapper() throws Exception {
        // prepare test data
        List<Cell> cells = new ArrayList<>();
        cells.add(CellUtil.createCell(
                "t1s1".getBytes(), 
                CF_SONG, 
                ATTR_ARTIST_NAME,
                20122L,
                KeyValue.Type.Put.getCode(),
                "Artist_1".getBytes()
        ));
        
        cells.add(CellUtil.createCell(
                "t1s1".getBytes(), 
                CF_SONG, 
                ATTR_DURATION,
                20122L,
                KeyValue.Type.Put.getCode(),
                "218".getBytes()
        ));
        
        cells.add(CellUtil.createCell(
                "t1s1".getBytes(), 
                CF_SONG, 
                ATTR_SAM_RATE,
                20122L,
                KeyValue.Type.Put.getCode(),
                "2205".getBytes()
        ));
        
        cells.add(CellUtil.createCell(
                "t1s1".getBytes(), 
                CF_SONG, 
                ATTR_SONG_NAME,
                20122L,
                KeyValue.Type.Put.getCode(),
                "Test_Song_Name_1".getBytes()
        ));
        
        cells.add(CellUtil.createCell(
                "t1s1".getBytes(), 
                CF_SONG, 
                ATTR_TRACK_ID,
                20122L,
                KeyValue.Type.Put.getCode(),
                "TRAAAWAA".getBytes()
        ));
        
        cells.add(CellUtil.createCell(
                "t1s1".getBytes(), 
                CF_SONG, 
                ATTR_YEAR,
                20122L,
                KeyValue.Type.Put.getCode(),
                "0".getBytes()
        ));
        
        Result hbaseInput = Result.create(cells);
        
        //set test data as input to mapper
        mapDriver.withInput(new ImmutableBytesWritable("row1".getBytes()), hbaseInput);
        
        //expected output
        mapDriver.withOutput(new Text("Artist_1"), new IntWritable(1));
        
        mapDriver.runTest();
    }
    
//    @Test
//    public void testReducer() throws Exception{
//        List<IntWritable> values = new ArrayList<>();
//        values.add(new IntWritable(1));
//        values.add(new IntWritable(1));
//        values.add(new IntWritable(1));
//        
//        Text key = new Text("Artist_1");
//        reduceDriver.withInput(key, values);
//        List<Pair<ImmutableBytesWritable, Mutation>> output = reduceDriver.run();
//        
//        Mutation outputMutation = output.get(0).getSecond();
//        Row expectedRow = new Put("Artist_1".getBytes()).addColumn(CF_COM, ATTR_NUM_SONGS, 211L, "3".getBytes());
//        Assert.assertEquals(expectedRow, outputMutation.getRow());
//        
//    }
}
