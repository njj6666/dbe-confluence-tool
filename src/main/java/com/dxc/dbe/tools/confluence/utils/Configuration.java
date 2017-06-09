package com.dxc.dbe.tools.confluence.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Configuration extends Properties {
	
	static private Configuration config;
	
	private Configuration(){}
	
	public static Configuration getInstance(String path){
		if(config == null){
			try {
				loadConf(path);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return config;
	}
	
	public static Configuration getInstance(){
		if(config == null){
			try {
				loadConf("./config");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return config;
	}
	
	
	
	public static void loadConf(String path) throws Exception {
		config = new Configuration();
		InputStream in = new FileInputStream(path);
		config.load(in);
	}
}
