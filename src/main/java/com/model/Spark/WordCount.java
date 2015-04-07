package com.model.Spark;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;

import com.model.FileReaders.ExcelfileReader;
import com.model.FileReaders.PDFFileReader;
import com.model.FileReaders.ReadDocFile;

public class WordCount {

	static ArrayList<String> result = new ArrayList<String>();
	static StringBuffer resultString = new StringBuffer();
	static String query;

	public List<String> search(String word, String directory) {
		query = word;
		result.clear();
		SparkConf conf = new SparkConf().setAppName("Hackethon").setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		File dir = new File(directory);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				resultString.delete(0, resultString.length());
				JavaRDD<String> lines = getContentRDD(child.getAbsolutePath(),
						sc);
				if (lines != null) {
					JavaRDD<Integer> putEl = lines.map(new PutIntoElastic());
					int count = putEl.reduce(new AddWordCount());
					if (count >= 1) {
						System.out.println("FINAL count = " + count);
						resultString.append("|:|" + child.getName());
						resultString.append("||:" + count);
						result.add(resultString.toString());
					}
				}
				resultString.delete(0, resultString.length());
			}
		} else {
			System.out.println("Directory not found");
		}
		return result;
	}

	public static JavaRDD<String> getContentRDD(String path, JavaSparkContext sc) {
		String extension = path.substring(path.length() - 4);
		JavaRDD<String> content = null;

		if (extension.contains("doc") && !extension.contains("docx")) {
			// its a doc file
			ReadDocFile a = new ReadDocFile();
			content = a.readFile(path, sc);
		} else if (extension.contains("xls")) {
			// its an excel file
			ExcelfileReader a = new ExcelfileReader();
			try {
				content = a.readFile(path, sc);
			} catch (Exception e) {
			}
		} else if (extension.contains("txt")) {
			content = sc.textFile(path);
		} else if (extension.contains("pdf")) {
			PDFFileReader a = new PDFFileReader();
			try {
				content = a.readFile(path, sc);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return content;
	}

	public static int count(String sentence, String word) {
		String[] words = sentence.split(" ");
		int splitcount = 0;
		for (int i = 0; i < words.length; i++) {
			if (words[i].compareToIgnoreCase(word) == 0)
				splitcount++;
		}
		return splitcount;
	}
}

class PutIntoElastic implements Function<String, Integer> {
	public Integer call(String s) {
		if (s.contains(WordCount.query)) {
			WordCount.resultString.append("<line>" + s + "</line>");
			System.out.println("found in s=" + s);
			return WordCount.count(s, WordCount.query);
		} else
			return 0;
	}
}

class AddWordCount implements Function2<Integer, Integer, Integer> {
	public Integer call(Integer a, Integer b) {
		return a + b;
	}
}