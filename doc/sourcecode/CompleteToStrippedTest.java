package com.studium.millionsong.mapreduce.junit;

import com.studium.millionsong.mapreduce.CompleteToStripped;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;

import org.junit.Before;
import org.junit.Test;


public class CompleteToStrippedTest {
    
    public MapDriver<Object, Text, Text, Text> mapDriver;
    public ReduceDriver<Text, Text, Text, Text> reduceDriver;
    
    public Text testDataMapInput = new Text();
    public Text testDataMapOutput = new Text();
    public List<Text> testDataReduceInput = new ArrayList<>();
    public Text testDataReduceOutput = new Text();
    
    @Before
    public void setup(){
        CompleteToStripped.CompleteStrippedMapper mapper = new CompleteToStripped.CompleteStrippedMapper();
        CompleteToStripped.StrippedReducer reducer = new CompleteToStripped.StrippedReducer();
        mapDriver = MapDriver.newMapDriver(mapper);
        reduceDriver = ReduceDriver.newReduceDriver(reducer);
    
        initTestData();
    }
    
    @Test
    public void testMap() throws Exception{
        
        mapDriver.clearInput();
        mapDriver.withInput(new Text("row1"), testDataMapInput);
        mapDriver.withOutput(new Text("intrestingAttr".getBytes()), testDataMapOutput);
        
        mapDriver.runTest();
    }
    
    public void testReduce() throws Exception{
    
        reduceDriver.clearInput();
        reduceDriver.withInput(new Text("intrestingAttr".getBytes()), testDataReduceInput);
        reduceDriver.withOutput(new Text("intrestingAttr".getBytes()), testDataReduceOutput);
        
        reduceDriver.runTest();
    }
    
    
    // Create a test data set, with 200 attributes encoded as a 
    // comma seperated list.
    private void initTestData(){
    
        String[] sInput = new String[200];  
        String wholeRowInput;
        
        for(int i=0; i<200; i++) 
            sInput[i] = "trash";
        
        for(int k=0; k<13; k++)
            sInput[k] = "intrestingAttr";
        
        for(int j=146; j<166; j++)
            sInput[j] = "intrestingAttr";
        
        wholeRowInput = Arrays.toString(sInput);
        
        testDataMapInput.set(wholeRowInput);
        
        String wholeRowOutput = new String();
        
        for(int m=0; m<32; m++)
            wholeRowOutput = wholeRowOutput.concat("intrestingAttr, ");
        
        testDataMapOutput.set(wholeRowOutput);
        
        testDataReduceInput.add(testDataMapOutput);
        testDataReduceInput.add(testDataMapOutput);
        testDataReduceInput.add(testDataMapOutput);
        
        testDataReduceOutput.set(wholeRowOutput);
    } 
}
