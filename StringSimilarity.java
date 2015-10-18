//package com.ii.stringsimilarity;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;
import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.JaccardSimilarity;
import uk.ac.shef.wit.simmetrics.similaritymetrics.JaroWinkler;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;
import uk.ac.shef.wit.simmetrics.similaritymetrics.NeedlemanWunch;
import uk.ac.shef.wit.simmetrics.similaritymetrics.SmithWaterman;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Soundex;

public class StringSimilarity {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<String> source1 = new ArrayList<String>();
		if (args.length<3){
			System.out.println("Invalid number of arguments! Please specify arguments as: source1 source2 output");
			System.exit(1);
		}
		String source1_filename = args[0];
		
		Scanner s1=null;
		try {
			s1 = new Scanner(new File(source1_filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while (s1.hasNextLine()){
		    source1.add(s1.nextLine());
		}
		s1.close();
		/*
		for(String line:source1)
		{
			System.out.println(line);
		}*/
		String source2_filename = args[1];
		Scanner s2=null;
		try {
			s2 = new Scanner(new File(source2_filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<String> source2 = new ArrayList<String>();
		while (s2.hasNextLine()){
		    source2.add(s2.nextLine());
		}
		s2.close();
		/*
		for(String line:source2)
		{
			System.out.println(line);
		}*/
		AbstractStringMetric sw = new SmithWaterman();
		AbstractStringMetric jw = new JaroWinkler();
		AbstractStringMetric nw = new NeedlemanWunch();
		
		float distance_sw=0,distance_jw=0,distance_nw=0;
		float max_so_far,max_so_far_sw,max_so_far_jw,max_so_far_nw;
		String output_filename=args[2];
		PrintWriter writer=null;
		try {
			writer = new PrintWriter(output_filename, "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(String str1:source1){
			max_so_far_sw=max_so_far_jw=max_so_far_nw=-99;
			String string1=str1.substring(str1.indexOf('-')+3);
			String best_str2=null,best_str2_sw=null,best_str2_jw=null,best_str2_nw=null;
			for(String str2:source2){
				String string2=str2.substring(str2.indexOf('-')+3);
				distance_sw=sw.getSimilarity(string1, string2);
				if (distance_sw > max_so_far_sw){
					max_so_far_sw=distance_sw;
					best_str2_sw=str2;
				}
				distance_jw=jw.getSimilarity(string1, string2);
				if (distance_jw > max_so_far_jw){
					max_so_far_jw=distance_jw;
					best_str2_jw=str2;
				}
				distance_nw=nw.getSimilarity(string1, string2);
				if (distance_nw > max_so_far_nw){
					max_so_far_nw=distance_nw;
					best_str2_nw=str2;
				}
			}
			if(max_so_far_sw>=max_so_far_jw){
				if(max_so_far_sw>=max_so_far_nw){
					max_so_far=max_so_far_sw;
					best_str2=best_str2_sw;
				}
				else{
					max_so_far=max_so_far_nw;
					best_str2=best_str2_nw;
				}
			}else{
				if(max_so_far_jw>=max_so_far_nw){
					max_so_far=max_so_far_jw;
					best_str2=best_str2_jw;
				}else{
					max_so_far=max_so_far_nw;
					best_str2=best_str2_nw;
				}
			}
			//Empirical analysis of threshold shows 0.88 to be better threshold to cover all records in groundtruth
			if(max_so_far>0.88){
				writer.println(source1_filename+":"+str1.substring(0, str1.indexOf('-'))+"\t"+source2_filename+":"+best_str2.substring(0, best_str2.indexOf('-')));
				//writer.println("s1: "+str1+"\ts2: "+best_str2+" dist: "+max_so_far);
			}
			
			//writer.println("s1: "+str1+"\ts2: "+best_str2_jw+" dist: "+max_so_far_jw);
			//writer.println("s1: "+str1+"\ts2: "+best_str2_sw+" dist: "+max_so_far_sw);
			//writer.println("s1: "+str1+"\ts2: "+best_str2_nw+" dist: "+max_so_far_nw);
		}
		
		
		writer.close();
	}

}
