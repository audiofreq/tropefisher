package com.freq.calendar;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class CalendarParser {
	private String filePath;
	
	public CalendarParser(){
	}
	
	public void parseA(String html){	
		Document doc = Jsoup.parse(html);	
		Element e;
		for(int i = 1; i <=20; i++){
			String location;
			String first, second, third, qualy;
			
			e = doc.getElementById("GP" + i + "_2012");
			location = e.getElementsByClass("summary").first().text();
			
			Element firstPractice = e.getElementsByClass("first-practice").first();
			
			first = getSession(firstPractice);
			second = getSession(e.getElementsByClass("second-practice").first());
			third = getSession(e.getElementsByClass("third-practice").first());
			qualy = getSession(e.getElementsByClass("qualifying").first());

			
			System.out.println(location);
			System.out.println("\t" + first);
			System.out.println("\t" + second);
			System.out.println("\t" + third);
			System.out.println("\t" + qualy);
		}
	}
	
	private String getSession(Element element){
		String val;
		val = element.getElementsByClass("dtstart").first().text() + " 2012";
		val = val + " " + element.getElementsByClass("dtend").text();	
		return val;
	}
	

}
