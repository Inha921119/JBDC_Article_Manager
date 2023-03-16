package com.KoreaIT.example.JAM.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Util {
	public static String changeDateToString(LocalDateTime dateTime) {
		return DateTimeFormatter.ofPattern("yyyy-MM-DD HH:mm:ss").format(dateTime);
	}
}
