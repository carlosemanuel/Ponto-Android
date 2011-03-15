package br.com.carlosemanuel.ponto;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PontoUtil {
	private static SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
	private static SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yy");
	private static SimpleDateFormat sdfDateTime = new SimpleDateFormat(
			"dd/MM/yy HH:mm");

	public static String formatDate(Date date) {
		if (date != null) {
			return sdfDate.format(date);
		}
		return "";
	}

	public static String formatTime(Date date) {
		if (date != null) {
			return sdfTime.format(date);
		}
		return "";
	}

	public static String formatDateTime(Date date) {
		if (date != null) {
			return sdfDateTime.format(date);
		}
		return "";
	}
}
