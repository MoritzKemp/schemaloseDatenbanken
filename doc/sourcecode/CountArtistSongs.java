package com.studium.millionsong.mapreduce;

// Use new "mapreduce" API, not "mapred"!
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;


/*
Counts songs of every artist of music table in hbase. Result is written
into a text file.
*/
public class CountArtistSongs {
    
    public static class ArtistSongMapper extends TableMapper<Text, IntWritable>{
        
        private Text artist_name = new Text();
        private final IntWritable ONE = new IntWritable(1);
        private static final byte[] CF_SONG = "song".getBytes();
        private static final byte[] ATTR_NAME = "ArtistName".getBytes();
        
        @Override
        protected void map(ImmutableBytesWritable rowkey, Result value, Context context) 
                throws IOException, InterruptedException {
            
            String val = new String(value.getValue(CF_SONG, ATTR_NAME));
            artist_name.set(val);
            context.write(artist_name, ONE);
        } 
    }
    
    public static class ArtistSongReducer extends TableReducer<Text, IntWritable, ImmutableBytesWritable>{

        private static final byte[] CF_COMMON = "com".getBytes();
        private static final byte[] ATTR_SONGNUM = "NumOfSongs".getBytes();
        
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) 
                throws IOException, InterruptedException {
            int i=0;
            for(IntWritable val : values){
                i += val.get();
            }
            
            Put put = new Put(Bytes.toBytes(key.toString()));
            put.addColumn(CF_COMMON, ATTR_SONGNUM, String.valueOf(i).getBytes());
            context.write(new ImmutableBytesWritable(key.getBytes()), put);
        }
    }
    
    
    public static void main(String[] args) throws Exception{
    
        // Create Hbase-specific configuration
        Configuration conf = HBaseConfiguration.create();
        conf.setInt("timeout", 120000);
        conf.set("hbase.master", "*10.20.110.61:16006*");
        conf.set("hbase.zookeeper.quorum","10.20.110.61");
        conf.set("hbase.zookeeper.property.clientPort", "2186");


        Job job = Job.getInstance(conf, "Count_artist_songs");
        job.setJarByClass(CountArtistSongs.class);
        job.setMapperClass(ArtistSongMapper.class);
        job.setNumReduceTasks(1);

        Scan scan = new Scan();
        scan.setCaching(500);
        scan.setCacheBlocks(false);
        
        //Set Hbase-specific config for map task
        TableMapReduceUtil.initTableMapperJob(
                "music", 
                scan, 
                ArtistSongMapper.class, 
                Text.class, 
                IntWritable.class, 
                job
        );
        
        //Set Hbase-specific config for reduce task
        TableMapReduceUtil.initTableReducerJob(
                "statistic", 
                ArtistSongReducer.class, 
                job
        );
        
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
