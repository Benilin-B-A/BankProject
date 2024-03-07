package com.bank.db.implementation;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConnectionHandler {
		
		private static Properties conProp = new Properties();
		
		static
		{
			try(FileReader reader=new FileReader(new File(System.getProperty("user.dir"),"ConnProp"))){
				conProp.load(reader);
				dataBase = conProp.getProperty("dBMS");
				serverName = conProp.getProperty("serverName");
				portNum = conProp.getProperty("portNum");
				user = conProp.getProperty("user");
				password = conProp.getProperty("password");	
				dB = conProp.getProperty("dB");
			}
			catch(IOException exception) {
				exception.printStackTrace();
			}
		}
		public static void setdB(String dB) {
			ConnectionHandler.dB = dB;
		}

		public static void setdBMS(String dBMS) {
			ConnectionHandler.dataBase = dBMS;
		}

		public static void setServerName(String serverName) {
			ConnectionHandler.serverName = serverName;
		}

		public static void setPortNum(String portNum) {
			ConnectionHandler.portNum = portNum;
		}

		public static void setUser(String user) {
			ConnectionHandler.user = user;
		}

		public static void setPassword(String password) {
			ConnectionHandler.password = password;
		}
		private static String dB;

		private static String dataBase;
		
		private static String serverName;
		
		private static String portNum;
		
		private static String user;
		
		private static String password;
		
		public static String getUser() {
			return user;
		}
		
		public static String getPassword() {
			return password;
		}
		
		public static String getURL() {
			return "jdbc:" + dataBase + "://" + serverName +":" + portNum + "/" + dB;
		}
}
