
package com.studium.millionsong.mapreduce;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

public class CompleteToStripped {
    
    public static class CompleteStrippedMapper extends Mapper<Object, Text, Text, Text>{
        
        private static int untilGenreAttr = 13;
        private static int fromGenreAttr = 147;
        private static int maxNumOfAttr = 166;
        private static int songIdColumn = 6;
        
        private Text strippedSongAttr = new Text();
        private Text songId = new Text();
        
        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            // Extract attributes to single strings but limit array length for memories sake.
            // That means only the first maxNumOfAttr are analysed
            String[] allAttributes = value.toString().split(",", maxNumOfAttr);
            
            String newStrippedValue = new String();
            
            //Key is song id, its found in the 159. column of the csv-file
            songId.set(allAttributes[songIdColumn-1]);
            
            //Run until the genre attributes
            for(int i=0; i<untilGenreAttr; i++){
                newStrippedValue += ","+allAttributes[i];
            }
            
            // Skip genre attributes and carry on till end
            for(int k=fromGenreAttr-1; k<maxNumOfAttr; k++){
                newStrippedValue += ","+allAttributes[k];
            }
            
            //Set String to output format
            strippedSongAttr.set(newStrippedValue);
            
            //Write out intermediate result
            context.write(songId, strippedSongAttr);
        }
        
    }
    
    public static class StrippedReducer extends Reducer<Text, Text, Text, Text>{

        private Text resultValue = new Text();
        
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            // Just take the first intermediate result because
            // all results with the same key are assumed to be equal
            resultValue = values.iterator().next();
            context.write(key, resultValue);
        }
        
    }
    
    public static void main(String[] args) throws Exception{
        
        Path inputPath = new Path("/millionSong/complete.csv");
        Path outputPath = new Path("/millionSong/result/run1");
        
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "From complete to stripped dataset");
        
        // Job configuration:
        // 0. Set har which contains this classes
        job.setJarByClass(CompleteToStripped.class);
        
        // 1. Which Mapper and Reduce should be used
        job.setMapperClass(CompleteStrippedMapper.class);
        job.setReducerClass(StrippedReducer.class);
        
        // 2. Which are the output datatypes of the mapper- and reduce-functions
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        // 3. Set local combiner for data reduction
        job.setCombinerClass(StrippedReducer.class);
        
        // 4. Where are the input file(s)
        // Default FileInputFormat is TextInputFormat, so its using
        // the correct implementation automatically.
        FileInputFormat.addInputPath(job, inputPath);
        FileOutputFormat.setOutputPath(job, outputPath);
        
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}

