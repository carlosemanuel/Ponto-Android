package br.com.carlosemanuel.ponto;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import br.com.carlosemanuel.ponto.beans.Ponto;
import br.com.carlosemanuel.ponto.repository.PontoRepository;

public class ResultSearchActivity extends ListActivity {

	private PontoRepository repository;
	private ArrayList<Ponto> pontos;
	private PontoListAdapter adapter;
	private static final int HOURS_PER_DAY = 24;
	private static final int MINUTES_PER_HOUR = 60;
	private static final int SECONDS_PER_MINUTE = 60;
	private static final int MILLISECONDS_PER_SECOND = 1000;
	private static final int SEND_EMAIL = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		super.getListView().setBackgroundResource(R.drawable.carbon_fiber);

		repository = PontoActivity.repository;

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		long itemId = (Long) extras.get("key");

		if (itemId == SearchItensOption.POR_DIA) {
			pontos = (ArrayList<Ponto>) repository.getPontosOfDay();
		} else if (itemId == SearchItensOption.POR_SEMANA) {
			pontos = (ArrayList<Ponto>) repository.getPontosOfWeek();
		} else if (itemId == SearchItensOption.POR_MES) {
			pontos = (ArrayList<Ponto>) repository.getPontosOfMonth();
		} else if (itemId == SearchItensOption.POR_DATA) {
			Date initialDate = (Date) extras.get("initialDate");
			Date finishDate = (Date) extras.get("finishDate");
			pontos = (ArrayList<Ponto>) repository.getPontosBetween(
					initialDate, finishDate);
		} else if (itemId == SearchItensOption.TODOS) {
			pontos = (ArrayList<Ponto>) repository.listAll();
		}


		long times = 0L;
		for (Ponto p : pontos) {
			if (p.getFinishDate() != null) {
				times += (p.getFinishDate().getTime() - p.getStartDate()
						.getTime());
			} else {
				times += (new Date().getTime() - p.getStartDate().getTime());
			}
		}

		adapter = new PontoListAdapter(this, R.layout.result_search, pontos);
		this.setTitle(format(times));
		setListAdapter(adapter);
		getListView().setOnItemLongClickListener(
				new ListView.OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> adapter,
							View v, final int position, long id) {

						AlertDialog dialog = new AlertDialog.Builder(
								ResultSearchActivity.this).setTitle(
								R.string.choose).setItems(
								R.array.select_dialog_items,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {

										if (which == 0) {
											Intent intent = new Intent(
													ResultSearchActivity.this,
													EditPontoActivity.class);
											intent.putExtra("ponto", pontos
													.get(position));

											startActivityForResult(intent, 1);
										} else {
											Dialog remove = createConfirmRemoveDialog(position);
											remove.show();
										}
									}
								}).create();
						dialog.show();
						return true;
					}
				});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		this.setResult(resultCode);
		this.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuItem item = menu.add(0, SEND_EMAIL, 0, this.getResources().getText(
				R.string.send_pontos));
		item.setIcon(R.drawable.send48);

		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case SEND_EMAIL:
			writeFile();
			sendEmail();
			return true;
		}
		return false;
	}

	private Dialog createConfirmRemoveDialog(final int position) {
		AlertDialog dialog = new AlertDialog.Builder(ResultSearchActivity.this)
				.setTitle(R.string.alert_confirm_ponto_remove)
				.setPositiveButton(R.string.alert_dialog_ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								Ponto ponto = pontos.get(position);
								pontos.remove(position);
								repository.remove(ponto);
								ResultSearchActivity.this.setResult(2);
								ResultSearchActivity.this.finish();
							}
						}).setNegativeButton(R.string.alert_dialog_cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

							}
						}).create();

		return dialog;
	}

	private String format(long time) {
		@SuppressWarnings("unused")
		int millis = (int) (time % MILLISECONDS_PER_SECOND);

		/* /= is just shorthand for togo = togo / 1000 */
		time /= MILLISECONDS_PER_SECOND;

		int seconds = (int) (time % SECONDS_PER_MINUTE);
		time /= SECONDS_PER_MINUTE;

		int minutes = (int) (time % MINUTES_PER_HOUR);
		time /= MINUTES_PER_HOUR;

		int hours = (int) (time % HOURS_PER_DAY);
		int days = (int) (time / HOURS_PER_DAY);

		return String.format("Total de %d horas, %d min", (24 * days)
				+ hours, minutes, seconds);
	}


	protected void sendEmail() {
		File root = Environment.getExternalStorageDirectory();
		File file = new File(root, "Ponto/ponto.csv");
		if (file.exists()) {
			ArrayList<Uri> uris = new ArrayList<Uri>();
			uris.add(Uri.fromFile(file));

			// Create a new Intent to send messages
			Intent sendIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
			sendIntent.putExtra(Intent.EXTRA_SUBJECT, this.getResources()
					.getString(R.string.send_subject));
			sendIntent
					.putExtra(Intent.EXTRA_TEXT, this.getResources().getString(
							R.string.send_body,
							PontoUtil.formatDate(pontos.get(pontos.size() - 1)
									.getFinishDate()),
							PontoUtil.formatDate(pontos.get(0).getStartDate())));
			sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
			sendIntent.setType("application/csv");
			startActivity(Intent.createChooser(sendIntent, "MySendMail"));
		} else {
			Toast.makeText(this, "Erro ao enviar arquivo", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void writeFile() {
		try {
			File root = Environment.getExternalStorageDirectory();
			String state = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(state)) {
				File dirPonto = new File(root, "Ponto");
				if (!dirPonto.exists()) {
					dirPonto.mkdir();
				}
				File gpxfile = new File(dirPonto, "ponto.csv");
				FileWriter gpxwriter = new FileWriter(gpxfile);
				BufferedWriter out = new BufferedWriter(gpxwriter);
				for (Ponto p : pontos) {
					out.append(PontoUtil.formatDateTime(p.getStartDate()));
					out.append(";");
					out.append(PontoUtil.formatDateTime(p.getFinishDate()));
					out.newLine();
				}
				out.close();
			}
		} catch (IOException e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
}
