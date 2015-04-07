package com.model.FileReaders;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class PDFFileReader {
	public JavaRDD<String> readFile(String path, JavaSparkContext sc) throws IOException
	{
		PDFTextStripper stripper;
		PDDocument doc=null;          
		try{
		  doc = PDDocument.load(path);
		  stripper = new PDFTextStripper();
		  stripper.setStartPage( 1 );
		  stripper.setEndPage( Integer.MAX_VALUE );
		  String x1= stripper.getText(doc);
		  //break up the file content returned as a string into individual lines
		  List<String> ans= Arrays.asList(x1.split("\r\n"));
		  return sc.parallelize(ans);
		}
		catch(Exception e)
		{
			
		}
		finally{
		   if(doc!=null)
		   doc.close();
		}
		return null;
	}
}
