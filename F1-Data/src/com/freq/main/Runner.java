package com.freq.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import com.freq.calendar.CalendarParser;
import com.freq.parser.OfficialSite;

public class Runner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		printMenu();
		/*
		String html;
		try {
			html = readFile("C:\\Users\\Jon\\Documents\\F1\\Calendar\\2012\\1.txt");
			CalendarParser cp = new CalendarParser();
			cp.parseA(html);
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		OfficialSite offSite = new OfficialSite(true);
		offSite.parse();
	
	}

	public static void printMenu() {
		System.out.println("Choose an option:");
	}

	public static String readFile(String path) throws IOException {
		FileInputStream stream = new FileInputStream(new File(path));
		try {
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0,
					fc.size());
			/* Instead of using default, pass in a decoder. */
			return Charset.defaultCharset().decode(bb).toString();
		} finally {
			stream.close();
		}
	}
	
	public static void sleep(int timeInMillis){
		try {
		    Thread.sleep(timeInMillis);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
	}
}
