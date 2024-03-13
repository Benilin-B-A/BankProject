package com.bank.adapter;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import com.bank.custom.exceptions.BankingException;
import com.bank.util.LogHandler;

public class JSONAdapter {
	
	private static Logger logger = LogHandler.getLogger(JSONAdapter.class.getName(), "AdapterLogs.txt");

	public static <E> JSONArray transactionTOJson(List<E> list) throws BankingException{
		JSONArray arr = new JSONArray();
		for (E element : list) {
			JSONObject obj;
			try {
				obj = new JSONObject(element);
				arr.put(obj);
			} catch (InvocationTargetException | IllegalAccessException | IntrospectionException exception) {
				logger.log(Level.SEVERE, "Couldn't convert to JSON",exception);
				throw new BankingException("cannot convert to JSON");
			}
		}
		return arr;
	}
	
	public static JSONObject objToJSONObject(Object obj) throws BankingException{
		try {
			return new JSONObject(obj);
		} catch (InvocationTargetException | IllegalAccessException | IntrospectionException exception) {
			logger.log(Level.SEVERE, "Couldn't convert to JSON",exception);
			throw new BankingException("cannot convert to JSON");
		}
	}

	public static <K,V> JSONObject mapToJSON(Map<K, V> map) {
		return new JSONObject(map);
	}
}