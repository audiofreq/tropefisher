package com.freq.main;

public class Driver {

	private String mName;
	private String mKey;

	public Driver(String name){
		this.mName = name;
		mKey = guessKey();
	}

	public String getName(){
		return mName;
	}
	
	public void setName(String name){
		this.mName = name;
	}
	
	private String guessKey() {
		String[] split = mName.split(" ");
		
		return split[split.length - 1].toUpperCase();
	}

	public String getKey() {
		return this.mKey;
	}
	
	public String toString(){
		String val = "";
		val = val + mName;
		
		//int keyPos = 80 - mKey.length();
		
		for(int i = mName.length(); i<65; i++){
			val += " ";
		}
		return val + mKey;
	}

}
