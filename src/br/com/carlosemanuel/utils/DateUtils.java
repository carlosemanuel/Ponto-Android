package br.com.carlosemanuel.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.util.Log;

public class DateUtils {

	public static Date getFirstHourOfDay() {
		GregorianCalendar gc = new GregorianCalendar();
		Log.d("PONTO", gc.getTime().toString());
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		Log.d("PONTO", gc.getTime().toString());

		return gc.getTime();
	}

	public static Date getLastHourOfDay() {
		GregorianCalendar gc = new GregorianCalendar();
		Log.d("PONTO", gc.getTime().toString());
		gc.set(Calendar.HOUR_OF_DAY, 23);
		gc.set(Calendar.MINUTE, 59);
		gc.set(Calendar.SECOND, 59);
		gc.set(Calendar.MILLISECOND, 59);
		Log.d("PONTO", gc.getTime().toString());

		return gc.getTime();
	}

	public static Date getFirstDayOfWeek() {
		GregorianCalendar gc = new GregorianCalendar();
		int dayOfWeek = gc.get(Calendar.DAY_OF_WEEK);
		gc.add(Calendar.DAY_OF_MONTH, -1 * (dayOfWeek - Calendar.SUNDAY));

		return gc.getTime();
	}

	public static Date getFirstDayOfMonth() {
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(Calendar.DAY_OF_MONTH, gc.getMinimum(Calendar.DAY_OF_MONTH));

		return gc.getTime();
	}
}
