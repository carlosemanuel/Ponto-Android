package br.com.carlosemanuel.ponto;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import br.com.carlosemanuel.ponto.beans.Ponto;
import br.com.carlosemanuel.ponto.beans.PontoStatus;
import br.com.carlosemanuel.ponto.service.PontoService;

public class TabPontoActivity extends Activity {

	private static final int INFO = 1;

	private Button btnEnterOut;
	private TextView startDate;
	private TextView startTime;
	private TextView finishDate;
	private TextView finishTime;
	private Ponto ponto;
	private PontoService pontoService;

	public Context context;

	public TabPontoActivity() {
		pontoService = new PontoService();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		createView();
		updateView();
		this.context = this;
	}

	private void createView() {

		startDate = (TextView) findViewById(R.id.start_date);
		startTime = (TextView) findViewById(R.id.start_time);
		finishDate = (TextView) findViewById(R.id.end_date);
		finishTime = (TextView) findViewById(R.id.end_time);

		startDate.setText("");
		startTime.setText("");
		finishDate.setText("");
		finishTime.setText("");

		btnEnterOut = (Button) findViewById(R.id.btnEnterOut);

		ponto = PontoActivity.repository.getLast();
		ponto = checkPonto(ponto);

		final PontoStatus status = pontoService.getPontoStatus(ponto);
		
		if (status == PontoStatus.PONTO_SAIDA) {
			btnEnterOut.setText(R.string.out);
		} else {
			btnEnterOut.setText(R.string.enter);
		}

		btnEnterOut.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				ponto = pontoService.baterPonto();
				checkPonto(ponto);
				updateView();
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuItem item = menu.add(0, INFO, 0, this.getResources().getText(
				R.string.info));
		item.setIcon(R.drawable.info_details);

		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case INFO:
			Log.d("PONTO", "menu info");
			Dialog dialog = infoDialog();
			dialog.show();
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private void updateView() {
		if (ponto != null) {
			if (ponto.getStartDate() != null) {
				startDate.setText(PontoUtil.formatDate(ponto.getStartDate()));
				startTime.setText(PontoUtil.formatTime(ponto.getStartDate()));
				finishDate.setText("");
				finishTime.setText("");
			}

			if (ponto.getFinishDate() != null) {
				finishDate.setText(PontoUtil.formatDate(ponto.getFinishDate()));
				finishTime.setText(PontoUtil.formatTime(ponto.getFinishDate()));
			}
		}
	}

	private Ponto checkPonto(Ponto ponto) {
		if (ponto == null) {
			ponto = new Ponto();
		} else {
			if (ponto.getStartDate() != null && ponto.getFinishDate() != null) {
				ponto = new Ponto();
			}
		}

		return ponto;
	}

	private Dialog infoDialog() {


		PackageManager manager = context.getPackageManager();
		String version = "";

		try {
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			version = info.versionName;
		} catch (NameNotFoundException e) {
			Log.d("PONTO", e.getMessage());
		}
		String t = "Versão: " + version
				+ "\nDesenvolvido por: Carlos Emanuel\nDesigner: Arthur Lucena";

		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.info,
				(ViewGroup) findViewById(R.id.layout_root));

		TextView text2 = (TextView) layout.findViewById(R.id.info_text);
		text2.setText(t);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setView(layout);

		AlertDialog alert = builder.create();

		alert.setTitle("Sobre");
		alert.setIcon(R.drawable.clock36);

		return alert;
	}
}
