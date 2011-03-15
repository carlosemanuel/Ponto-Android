package br.com.carlosemanuel.ponto;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import br.com.carlosemanuel.ponto.beans.Ponto;
import br.com.carlosemanuel.ponto.repository.PontoRepository;

public class EditPontoActivity extends Activity {

	private SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");

	private Ponto ponto;
	private PontoRepository repository = PontoActivity.repository;
	// private TextView date;
	private TextView startTime;
	// private TextView endTime;

	private Button btnEnterOut;
	private TextView startDate;
	private TextView finishDate;
	private TextView finishTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ponto = (Ponto) this.getIntent().getExtras().get("ponto");

		startDate = (TextView) findViewById(R.id.start_date);
		startTime = (TextView) findViewById(R.id.start_time);
		finishDate = (TextView) findViewById(R.id.end_date);
		finishTime = (TextView) findViewById(R.id.end_time);
		btnEnterOut = (Button) findViewById(R.id.btnEnterOut);

		btnEnterOut.setVisibility(View.INVISIBLE);

		startDate.setText(sdfDate.format(ponto.getStartDate()));
		startTime.setText(sdfTime.format(ponto.getStartDate()));

		if (ponto.getFinishDate() != null) {
			finishDate.setText(sdfDate.format(ponto.getFinishDate()));
			finishTime.setText(sdfTime.format(ponto.getFinishDate()));
		} else {
			finishDate.setText("");
			finishTime.setText("");
		}

		startDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTime(ponto.getStartDate());
				int year = gc.get(Calendar.YEAR);
				int month = gc.get(Calendar.MONTH);
				int dayOfMonth = gc.get(Calendar.DAY_OF_MONTH);

				createDateEditDialog(year, month, dayOfMonth, ponto
						.getStartDate(), true);
			}
		});

		startTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				createTimePickerDialog(ponto.getStartDate(), true);
			}
		});

		finishDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTime(ponto.getFinishDate());
				int year = gc.get(Calendar.YEAR);
				int month = gc.get(Calendar.MONTH);
				int dayOfMonth = gc.get(Calendar.DAY_OF_MONTH);

				createDateEditDialog(year, month, dayOfMonth, ponto
						.getFinishDate(), false);
			}
		});

		finishTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				createTimePickerDialog(ponto.getFinishDate(), false);
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.setResult(1);
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.setResult(1, new Intent());
		this.finish();
	}

	private void createDateEditDialog(int year, int month, int dayOfMonth,
			final Date date, final boolean isStartDate) {

		final GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);

		DatePickerDialog dialog = new DatePickerDialog(this,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {

						gc.set(Calendar.YEAR, year);
						gc.set(Calendar.MONTH, monthOfYear);
						gc.set(Calendar.DAY_OF_MONTH, dayOfMonth);

						if (isStartDate) {
							ponto.setStartDate(gc.getTime());
							startDate.setText(sdfDate.format(gc.getTime()));
						} else {
							ponto.setFinishDate(gc.getTime());
							finishDate.setText(sdfDate.format(gc.getTime()));
						}
						repository.saveOrUpdate(ponto);
					}
				}, year, month, dayOfMonth);
		dialog.show();
	}

	private void createTimePickerDialog(final Date time,
			final boolean isStartDate) {

		final GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(time);
		int hourOfDay = gc.get(Calendar.HOUR_OF_DAY);
		int minute = gc.get(Calendar.MINUTE);

		TimePickerDialog dialog = new TimePickerDialog(this,
				new TimePickerDialog.OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker view, int hourOfDay,
							int minute) {

						if (isStartDate) {
							gc.set(Calendar.HOUR_OF_DAY, hourOfDay);
							gc.set(Calendar.MINUTE, minute);
							ponto.setStartDate(gc.getTime());
							startTime.setText(sdfTime.format(gc.getTime()));
						} else {
							gc.setTime(ponto.getStartDate());
							gc.set(Calendar.HOUR_OF_DAY, hourOfDay);
							gc.set(Calendar.MINUTE, minute);
							ponto.setFinishDate(gc.getTime());
							finishTime.setText(sdfTime.format(gc.getTime()));
						}

						repository.saveOrUpdate(ponto);
					}
				}, hourOfDay, minute, true);
		dialog.show();
	}

}
