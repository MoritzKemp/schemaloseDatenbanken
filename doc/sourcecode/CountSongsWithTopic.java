package com.studium.millionsong.mapreduce;

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


public class CountSongsWithTopic {
    
    public static class SongTopicMapper extends TableMapper<Text, IntWritable>{
        private Text artist_name = new Text();
        private final IntWritable ONE = new IntWritable(1);
        private static final byte[] CF_SONG = "song".getBytes();
        private static final byte[] ATTR_NAME = "ArtistName".getBytes();
        private static final byte[] ATTR_TITLE = "SongName".getBytes();
        
        @Override
        protected void map(ImmutableBytesWritable rowkey, Result value, Context context) 
                throws IOException, InterruptedException {
            
            String name = new String(value.getValue(CF_SONG, ATTR_NAME));
            String title = new String(value.getValue(CF_SONG, ATTR_TITLE));
            
            CharSequence topic = "love";
            
            // Return without writing intermediate result 
            // if title doesnt contain the topic
            if(!title.toLowerCase().contains(topic)) return;
            
            artist_name.set(name);
            context.write(artist_name, ONE);
        }
    }
    
    public static class SongTopicReducer extends TableReducer<Text, IntWritable, ImmutableBytesWritable>{
        private static final byte[] CF_COMMON = "com".getBytes();
        private static final byte[] ATTR_TOPIC = "love".getBytes();
        
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) 
                throws IOException, InterruptedException {
            int i=0;
            for(IntWritable val : values){
                i += val.get();
            }
            
            Put put = new Put(Bytes.toBytes(key.toString()));
            put.add(CF_COMMON, ATTR_TOPIC, Bytes.toBytes(String.valueOf(i)));
            context.write(null, put);
        }
    
    }
    
     public static void main(String[] args) throws Exception{
    
        // Create Hbase-specific configuration
        Configuration conf = HBaseConfiguration.create();
        conf.setInt("timeout", 120000);
        conf.set("hbase.master", "*10.20.110.61:16006*");
        conf.set("hbase.zookeeper.quorum","10.20.110.61");
        conf.set("hbase.zookeeper.property.clientPort", "2186");


        Job job = Job.getInstance(conf, "Count_topic_songs");
        job.setJarByClass(CountSongsWithTopic.class);
        //job.setMapperClass(SongTopicMapper.class);
        job.setNumReduceTasks(1);

        Scan scan = new Scan();
        scan.setCaching(500);
        scan.setCacheBlocks(false);
        
        //Set Hbase-specific config for map task
        TableMapReduceUtil.initTableMapperJob(
                "music", 
                scan, 
                SongTopicMapper.class, 
                Text.class, 
                IntWritable.class, 
                job
        );
        
        //Set Hbase-specific config for reduce task
        TableMapReduceUtil.initTableReducerJob(
                "song_topic", 
                SongTopicReducer.class, 
                job
        );
        
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
