package com.model.FileReaders;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;


public class ReadDocFile {
	String fileName;

	public String getName() {
		return fileName;
	}

	public void setName(String name) {
		this.fileName = name;
	}
	public static void main(String[] args) {

		File file = null;
		WordExtractor extractor = null;
		try {

			file = new File("c:\\files\\wordfile_doc.doc");
			FileInputStream fis = new FileInputStream(file.getAbsolutePath());
			HWPFDocument document = new HWPFDocument(fis);
			extractor = new WordExtractor(document);
			String[] fileData = extractor.getParagraphText();
			for (int i = 0; i < fileData.length; i++) {
				if (fileData[i] != null)
					System.out.println(fileData[i]);
			}
		} catch (Exception exep) {
			System.out.print("error in reading doc file");
		}
	}
	public JavaRDD<String> readFile(String path,JavaSparkContext sc) {
		File file = null;
		WordExtractor extractor = null;
		List<String> content = new ArrayList();
		try {

			file = new File(path);
			FileInputStream fis = new FileInputStream(file.getAbsolutePath());
			HWPFDocument document = new HWPFDocument(fis);
			extractor = new WordExtractor(document);
			String[] fileData = extractor.getParagraphText();
			for (int i = 0; i < fileData.length; i++) {
				if (fileData[i] != null)
				{
					content.add(fileData[i]);
				}
			}
		} catch (Exception exep) {
			System.out.print("error in reading doc file");
		}
		return sc.parallelize(content);
	}
}
