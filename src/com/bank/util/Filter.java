package com.bank.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bank.pojo.Transaction;

public class Filter {

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


	public static List<Transaction> filterByDate(List<Transaction> list, Date filterDate) {
		List<Transaction> rList = new ArrayList<>();
		list.forEach(e -> {
			long time = e.getTime();
			String date = dateFormat.format(TimeUtil.getDateTime(time));
			if (date.equals(filterDate)) {
				rList.add(e);
			}
		});
		return rList;
	}

	public static List<Transaction> filterByType(List<Transaction> list, String filterType) {
		List<Transaction> rList = new ArrayList<>();
		list.forEach(e -> {
			String type = e.getType();
			if (type.equals(filterType)) {
				rList.add(e);
			}
		});
		return rList;
	}
}
