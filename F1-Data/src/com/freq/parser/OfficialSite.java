package com.freq.parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.freq.main.Driver;
import com.freq.main.Runner;

public class OfficialSite {
	/** CONSTANTS **/
	public static final int SESSION_PRACTICE 			= 101;
	
	
	public static final int TYPE_RESULT					= 901;
	public static final int TYPE_BEST_SECTOR_TIMES 		= 902;
	
	/***************/
	
	
	private String urlTemplate = "http://www.formula1.com/results/season/";
	private String urlBase = "http://www.formula1.com";
	private String base_file = "C:\\Users\\Jon\\Documents\\F1\\Calendar\\2012\\2.txt";
	private String pathLocalStore = "C:\\Users\\Jon\\Documents\\F1\\Calendar\\Store\\";

	private boolean debug;
	private OfficialSitePageParser parser;

	public OfficialSite(boolean debug) {
		this.debug = debug;
		parser = new OfficialSitePageParser();
	}

	public OfficialSite() {
		this(false);
	}

	public void parse() {
		try {
			if (debug) {
				writeToLog("", false);
				debug("*****Starting Process*****\n", 0);
			}

			/**
			 * GET THE YEAR LIST FROM A BASE FILE
			 */
			ArrayList<String> yearList = new ArrayList<String>();

			String html = Runner.readFile(base_file);

			Document doc = Jsoup.parse(html);

			Elements data_store = doc.select("div#tertiaryNav");
			data_store = data_store.select("li.listheader");

			Elements years = data_store.first().getElementsByTag("ul").first()
					.getElementsByTag("li");

			for (Element year : years) {
				int yearInt = Integer.parseInt(year.text());

				switch (year.text().trim()) {
				// case "2012": break;
				// case "2011":
				// case "2010":
				// case "2009":
				// case "2008":
				// case "2007":
				// case "2006":
				// case "2005":
				// case "2004": break;
				// case "2003": break;
				// case "2002": break;
				default:
					boolean x = (yearInt > 2001) ? yearList.add(year.text())
							: false;
				}
			}

			/**
			 * GET REAL DATA
			 */

			for (String currentYear : yearList) {

				/** YEAR **/
				String pathYear = pathLocalStore + currentYear + "/";
				if (debug)
					debug("Starting Year " + currentYear, 1);
				createFolder(pathYear);
				if (debug)
					debugDone();

				/** SEASON **/
				Document year_doc = Jsoup.parse(new URL(urlTemplate
						+ currentYear), 1000);
				String pathYearOverview = pathLocalStore + currentYear
						+ "\\overview.html";
				if (debug)
					debug("Writing year overview...", 2);
				writeFile(year_doc.toString(), pathYearOverview);
				if (debug)
					debugDone();

				Elements elmsEvents = year_doc.select("div#tertiaryNav")
						.select("li.listheader").get(1).getElementsByTag("ul")
						.first().getElementsByTag("li");
				elmsEvents.remove(0);

				for (Element event : elmsEvents) {
					String event_link = event.getElementsByTag("a").first()
							.attr("href");
					String event_name = event.getElementsByTag("a").first()
							.text();
					String pathEventBaseFolder = pathYear + event_name + "/";

					String pathEventRaceFolder = pathEventBaseFolder + "/Race/";

					if (debug)
						debug("Starting race " + event_name, 2);
					createFolder(pathEventBaseFolder);
					if (debug)
						debugDone();

					// results
					Document docEventBase = getAndWrite(new URL(urlBase
							+ event_link), pathEventRaceFolder + "results"
							+ ".html", false);

					// System.out.println("Base:" + event_link);

					// Sessions
					try {
						Elements sessions = docEventBase
								.select("div#tertiaryNav")
								.select("li.listheader").get(2)
								.getElementsByTag("ul").first()
								.getElementsByTag("li");

						for (Element session : sessions) {
							// extras
							int session_type = -1;
							int page_type;
							
							try {

								String folder = pathEventBaseFolder;
								// System.out.println("extras:" +
								// extras.text());
								switch (session.text().trim()) {
								case "RACE":
									folder = pathEventRaceFolder;
									break;
								case "PRACTICE 1":
									folder = pathEventBaseFolder + "P1/";
									session_type = this.SESSION_PRACTICE;
									break;
								case "PRACTICE 2":
									folder = pathEventBaseFolder + "P2/";
									session_type = this.SESSION_PRACTICE;
									break;
								case "PRACTICE 3":
									folder = pathEventBaseFolder + "P3/";
									session_type = this.SESSION_PRACTICE;
									break;
								case "QUALIFYING":
									folder = pathEventBaseFolder + "Qualy/";
									break;
								case "THURSDAY PRACTICE":
									folder = pathEventBaseFolder
											+ "Thursday Practice/";
									session_type = this.SESSION_PRACTICE;
								case "FRIDAY PRACTICE":
									folder = pathEventBaseFolder
											+ "Friday Practice/";
									session_type = this.SESSION_PRACTICE;
								case "FRIDAY PRACTICE 1":
									folder = pathEventBaseFolder
											+ "Friday Practice 1/";
									session_type = this.SESSION_PRACTICE;
									break;
								case "FRIDAY PRACTICE 2":
									folder = pathEventBaseFolder
											+ "Friday Practice 2/";
									session_type = this.SESSION_PRACTICE;
									break;
								case "SATURDAY PRACTICE":
									folder = pathEventBaseFolder
											+ "Saturday Practice/";
									session_type = this.SESSION_PRACTICE;
									break;
								case "THURSDAY PRACTICE 1":
									folder = pathEventBaseFolder
											+ "Thursday Practice 1/";
									session_type = this.SESSION_PRACTICE;
									break;
								case "THURSDAY PRACTICE 2":
									folder = pathEventBaseFolder
											+ "Thursday Practice 2/";
									session_type = this.SESSION_PRACTICE;
									break;
								case "SATURDAY PRACTICE 1":
									folder = pathEventBaseFolder
											+ "Saturday Practice 1/";
									session_type = this.SESSION_PRACTICE;
									break;
								case "SATURDAY PRACTICE 2":
									folder = pathEventBaseFolder
											+ "Saturday Practice 2/";
									session_type = this.SESSION_PRACTICE;
									break;
								case "THURSDAY QUALIFYING":
									folder = pathEventBaseFolder
											+ "Thursday Qualy/";
									break;
								case "FRIDAY QUALIFYING":
									folder = pathEventBaseFolder
											+ "Friday Qualy/";
									break;
								case "SATURDAY QUALIFYING":
									folder = pathEventBaseFolder
											+ "Saturday Qualy/";
									break;
								case "SUNDAY QUALIFYING":
									folder = pathEventBaseFolder
											+ "Sunday Qualy/";
									break;
								case "SATURDAY PRE-QUAL":
									folder = pathEventBaseFolder
											+ "Saturday Pre-Qualy/";
									break;
								default:
									System.out.println("Unknown");
									System.exit(1);
									break;

								}

								if (debug) {
									// String tabs = e.text().equals("RACE") ?
									// "\t\t":"\t";
									debug("Creating Folder: " + session.text(),
											3);
								}
								createFolder(folder);
								if (debug)
									debugDone();

								String session_link = session
										.getElementsByTag("a").first()
										.attr("href");

								// System.out.println(folder);
								Document docSession = getAndWrite(new URL(
										urlBase + session_link), folder
										+ "results.html", true);

								Elements extras = docSession
										.select("div#tertiaryNav")
										.select("li.listheader").get(3)
										.getElementsByTag("ul").first()
										.getElementsByTag("li");

								// System.out.println(urlBase + session_link);
								// System.out.println("");

								for (Element extra : extras) {
									String eLink = extra.getElementsByTag("a")
											.first().attr("href");
									String eName = extra.getElementsByTag("a")
											.first().text();
									String eName2 = eLink.substring(
											eLink.lastIndexOf("/") + 1,
											eLink.length());
									

									if (debug)
										debug("Writing file " + eName2, 4);
									Document docExtra = getAndWrite(
											new URL(urlBase + eLink),
											folder
													+ eLink.substring(
															eLink.lastIndexOf("/") + 1,
															eLink.length()),
											true);

									// System.out.println(eLink);

									if (debug)
										debugDone();

									try {
										//System.out.println(eName);

										parser.parsePage(
													docExtra
												   ,session_type
												   ,determineDocType(eName)
										);	
										
									} catch (Exception exp) {
										System.out
												.println("Error parsing page");
									}

							
									

								}
							} catch (Exception exception) {

								String folder = "";
								String filename = "results.html";
								// System.out.println(e.text());
								switch (session.text().trim()) {
								case "RACE":
									folder = pathEventRaceFolder;
									break;
								case "FRIDAY PRACTICE":
									folder = pathEventBaseFolder
											+ "Friday Practice/";
									break;
								case "SATURDAY PRACTICE 1":
									folder = pathEventBaseFolder
											+ "Saturday Practice 1/";
									break;
								case "SATURDAY PRACTICE 2":
									folder = pathEventBaseFolder
											+ "Saturday Practice 2/";
									break;
								case "FRIDAY QUALIFYING":
									folder = pathEventBaseFolder
											+ "Friday Qualy/";
									break;
								case "SATURDAY QUALIFYING":
									folder = pathEventBaseFolder
											+ "Saturday Qualy/";
									break;
								case "THURSDAY PRACTICE":
									folder = pathEventBaseFolder
											+ "Thursday Practice/";
									break;
								case "THURSDAY QUALIFYING":
									folder = pathEventBaseFolder
											+ "Thursday Qualy/";
									break;
								default:
									System.out.println("Fatal Error");
									System.exit(0);

								}
								String eLink = session.getElementsByTag("a")
										.first().attr("href");

								if (debug) {
									debug("Creating Folder: " + filename, 3);
								}
								createFolder(folder);
								if (debug)
									debugDone();

								if (debug)
									debug("Writing file " + filename, 4);
								getAndWrite(new URL(urlBase + eLink), folder
										+ filename, true);
								if (debug)
									debugDone();
							}

						}

					} catch (Exception e) {
						System.out.println("Woah");
						// always 1 event at least
						System.exit(0);

					}

				}
				parser.printResultSummary();
				System.exit(0);

			}//END SEASON
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Document getAndWrite(URL url, String file, boolean write) {
		File f = new File(file);
		if (f.exists()) {
			try {
				Document doc = Jsoup.parse(f, "UTF-8");
				return doc;
			} catch (IOException e) {
				f.delete();
				return getAndWrite(url, file, write);
			}
		} else {
			int timeToWait = new Random().nextInt(500) + 500;
			Runner.sleep(timeToWait);

			try {
				Document doc = Jsoup.parse(url, 1000);
				if (write)
					writeFile(doc.toString(), file);
				return doc;
			} catch (IOException e) {

				System.out
						.println("Problem reading link. Waiting 10 seconds...\n"
								+ e.toString());
				System.out.println(url.toString());
				Runner.sleep(5000);

				return getAndWrite(url, file, write);
			}
		}

	}

	public void writeFile(String text, String path) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path)));
		bw.write(text);
		bw.flush();
		bw.close();
	}

	public void writeFile(Document doc, String path) throws IOException {
		writeFile(doc.toString(), path);
	}

	public boolean createFolder(String folder) {
		// System.out.println("Creating Folder: " + folder);
		return new File(folder).mkdir();
	}

	private int last = 0;
	private String flush = "";

	private void debug(String message, int level) {

		level = level * 2;
		String val = "";
		for (int i = 0; i < level; i++) {
			val += " ";
		}
		val += message;
		System.out.print(val);
		last = val.length();
		flush = val;
	}

	private void debugDone() {
		int pos = last;
		String val = "";
		for (int i = last; i < 56; i++) {
			val += " ";
		}
		val = val + "Done\n";
		System.out.print(val);

		if (flush != "") {
			writeToLog(flush + val, true);
			flush = "";
		} else
			writeToLog(val, true);
	}

	private void writeToLog(String data, boolean append) {
		try {

			FileWriter fileWriter = new FileWriter(pathLocalStore + "log.txt",
					append);
			BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
			bufferWriter.write(data);
			bufferWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private int determineDocType(String name){
		int type = -1;
		switch (name){
		case "RESULTS":
			type = TYPE_RESULT;
		default:
			break;
		}
		
		return type;
	}

}
