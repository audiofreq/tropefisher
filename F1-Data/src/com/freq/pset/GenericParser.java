package com.freq.pset;

import org.jsoup.nodes.Document;

public abstract class GenericParser {

	public GenericParser(){
		
	}
	
	public abstract void parsePage(Document page);
	
}
