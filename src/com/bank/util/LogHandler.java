package com.bank.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogHandler {
	
	public static Logger getLogger(String name,String fileName){
		Logger logger = null;
		try {
			logger = Logger.getLogger(name);
			FileHandler handler = new FileHandler(fileName,true);
			java.util.logging.Formatter formatter = new SimpleFormatter();
			handler.setFormatter(formatter);
			logger.setUseParentHandlers(false);
			logger.addHandler(handler);
		}
		catch(IOException | SecurityException exception) {
			exception.printStackTrace();
		}
		return logger;
	}
	
}
