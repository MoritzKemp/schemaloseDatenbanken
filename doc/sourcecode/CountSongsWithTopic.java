package com.studium.millionsong.mapreduce;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
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
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class CountSongsWithTopic extends Configured implements Tool{
    
    public static class SongTopicMapper extends TableMapper<Text, IntWritable>{
        private CharSequence topic = "love";
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

            if(!title.toLowerCase().contains(this.topic)) return;
            
            artist_name.set(name);
            context.write(artist_name, ONE);
        }
        
        @Override
        protected void setup(Context context) throws IOException, InterruptedException{
            Configuration config = context.getConfiguration();
            this.topic = config.get("searchFor.topic");
        }
    }
    
    public static class SongTopicReducer extends TableReducer<Text, IntWritable, ImmutableBytesWritable>{
        private static final byte[] CF_COMMON = "com".getBytes();
        private byte[] attr_topic;
        
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) 
                throws IOException, InterruptedException {
            int i=0;
            for(IntWritable val : values){
                i += val.get();
            }
            
            Put put = new Put(Bytes.toBytes(key.toString()));
            put.add(CF_COMMON, this.attr_topic, Bytes.toBytes(String.valueOf(i)));
            context.write(null, put);
        }
        
        protected void setup(Context context) throws IOException, InterruptedException{
            Configuration config = context.getConfiguration();
            this.attr_topic = config.get("searchFor.topic").getBytes();
        }
    
    }
    
    public static Job configureJob(Configuration conf, String[] args) 
            throws IOException{
        conf.setInt("timeout", 120000);
        conf.set("hbase.master", "*10.20.110.61:16006*");
        conf.set("hbase.zookeeper.quorum","10.20.110.61");
        conf.set("hbase.zookeeper.property.clientPort", "2186");

        //Set topic by given argument
        conf.set("searchFor.topic", args[0]);
        
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
        return job;
    }
    
    public int run(String[] args) throws Exception {
        // Create Hbase-specific configuration
        Configuration conf = HBaseConfiguration.create(getConf());
        Job job = configureJob(conf, args);
        
        return (job.waitForCompletion(true) ? 0 : 1);
    }
    
    public static void main(String[] args) throws Exception{
        int result = ToolRunner.run(
                HBaseConfiguration.create(), new CountSongsWithTopic(), args
        );
        
        System.exit(result);
    }
}
