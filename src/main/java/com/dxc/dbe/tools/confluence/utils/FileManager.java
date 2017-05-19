package com.dxc.dbe.tools.confluence.utils;

import java.io.File;
import java.io.PrintWriter;

public class FileManager{
	
	public PrintWriter pw;
	
	public FileManager(PrintWriter pw){
		this.pw = pw;
	}
	
	public void insertRow(String row){
		this.pw.write(row);
	}
	

}
