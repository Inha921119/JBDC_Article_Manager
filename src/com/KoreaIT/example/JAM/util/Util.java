package com.KoreaIT.example.JAM.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class Util {
	public static String changeDateToString(LocalDateTime dateTime) {
		LocalDateTime DateTime = dateTime;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String dateString = format.format(DateTime);

		return dateString;
	}
}
