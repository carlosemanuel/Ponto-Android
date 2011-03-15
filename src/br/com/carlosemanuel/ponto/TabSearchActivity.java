package br.com.carlosemanuel.ponto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import br.com.carlosemanuel.utils.DateUtils;

public class TabSearchActivity extends Activity {

	private Spinner spinner;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private Date startDt;
	private Date finishDt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);

		startDt = DateUtils.getFirstDayOfMonth();
		finishDt = new Date();

		createView();
	}

	private void createView() {
		spinner = (Spinner) findViewById(R.id.periodo);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, getResources()
						.getStringArray(R.array.select_search_items));

		final LinearLayout layout = (LinearLayout) findViewById(R.id.search_by_date);

		adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		final TextView startDate = (TextView) findViewById(R.id.initial_date);
		final TextView finishDate = (TextView) findViewById(R.id.finish_date);

		startDate.setText(sdf.format(startDt));
		finishDate.setText(sdf.format(finishDt));

		Button search = (Button) findViewById(R.id.btnSearch);
		search.setText("Procurar");

		spinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {

						if (id == SearchItensOption.POR_DATA) {
							layout.setVisibility(View.VISIBLE);
						} else {
							layout.setVisibility(View.GONE);
						}

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
		});
		
		search.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				long itemId = spinner.getSelectedItemId();
				Intent intent = new Intent(TabSearchActivity.this,
						ResultSearchActivity.class);

				intent.putExtra("key", itemId);

				if (spinner.getSelectedItemId() == SearchItensOption.POR_DATA) {
					try {
						intent.putExtra("initialDate", sdf.parse(startDate
								.getText().toString()));
						intent.putExtra("finishDate", sdf.parse(finishDate
								.getText().toString()));
					} catch (ParseException e) {

					}
				}

				startActivityForResult(intent, 1);
			}
		});

		startDate.setOnClickListener(new DateClickListener(startDt, startDate));

		finishDate.setOnClickListener(new DateClickListener(finishDt,
				finishDate));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 1) {
			Toast.makeText(this, "Ponto Alterado", Toast.LENGTH_SHORT).show();
		}
		if (resultCode == 2) {
			Toast.makeText(this, "Ponto Removido", Toast.LENGTH_SHORT).show();
		}
	}

	private void createDateEditDialog(int year, int month, int dayOfMonth,
			final TextView textView) {


		DatePickerDialog dialog = new DatePickerDialog(this,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {

						final GregorianCalendar gc = new GregorianCalendar();
						gc.set(Calendar.YEAR, year);
						gc.set(Calendar.MONTH, monthOfYear);
						gc.set(Calendar.DAY_OF_MONTH, dayOfMonth);

						textView.setText(sdf.format(gc.getTime()));
					}
				}, year, month, dayOfMonth);
		dialog.show();
	}

	private class DateClickListener implements View.OnClickListener {

		private Date date;
		private TextView textView;

		public DateClickListener(Date date, TextView textView) {
			this.date = date;
			this.textView = textView;
		}

		@Override
		public void onClick(View v) {
			Calendar gc = Calendar.getInstance();
			gc.setTime(date);

			int year = gc.get(Calendar.YEAR);
			int month = gc.get(Calendar.MONTH);
			int dayOfMonth = gc.get(Calendar.DAY_OF_MONTH);

			createDateEditDialog(year, month, dayOfMonth, textView);
		}

	}
}
