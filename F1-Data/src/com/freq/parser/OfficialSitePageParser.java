package com.freq.parser;

import java.util.HashMap;
import java.util.Iterator;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.freq.main.Driver;

public class OfficialSitePageParser {
	
	private HashMap<String, Driver> driverMap;

	public OfficialSitePageParser() {
		driverMap = new HashMap<String, Driver>();

	}

	public void parsePage(Document page, int session, int type) {
		//System.out.println("");

		String header = page.getElementsByClass("raceResultsHeading").first()
				.getElementsByTag("h2").first().text();
		String year = header.substring(0, 4);

		Elements results = page.getElementsByClass("raceResults").first()
				.getElementsByTag("tr");
		results.remove(0);

		for (Element result : results) {
			Elements values = result.getElementsByTag("td");

			
			if(session == OfficialSite.SESSION_PRACTICE){
				
				if(type == OfficialSite.TYPE_RESULT){
					HashMap<String,String> data = practiceResult(values);	
					Driver driver = getOrPut(data.get("drivername"));
				}

			}
					
			//System.out.println(pos + ":" + name + ":" + laps);
		}

	
	}
	
	public void printResultSummary(){
		Iterator<Driver> driveIT = driverMap.values().iterator();
		while(driveIT.hasNext()){
			Driver d = driveIT.next();
			System.out.println(d.toString());
		}
		 System.out.println(driverMap.size());
	}

	public HashMap<String,String> practiceResult(Elements values){

		HashMap<String,String> result = new HashMap<String,String>();
		
		result.put("carno", values.get(1).text());
		result.put("drivername", values.get(2).text());
		result.put("team", values.get(3).text());
		result.put("time", values.get(4).text());
		result.put("gap", values.get(5).text());
		result.put("laps", values.get(6).text());
		
		return result;
		
	}
	
	private Driver getOrPut(String name) {
		Driver val;
		if(!driverMap.containsKey(name)){
			val = new Driver(name);
			driverMap.put(name,val);
		}else
			val = driverMap.get(name);
		
		return val;
	}
	
	
	

}
