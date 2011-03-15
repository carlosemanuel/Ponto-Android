package br.com.carlosemanuel.ponto;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import br.com.carlosemanuel.ponto.repository.PontoRepository;
import br.com.carlosemanuel.ponto.repository.PontoScriptRepository;

import com.admob.android.ads.AdManager;

public class PontoActivity extends TabActivity {

	public static PontoRepository repository;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		repository = new PontoScriptRepository(this);

		TabHost tabHost = getTabHost();
		TabSpec tabPonto = tabHost.newTabSpec("Ponto");
		tabPonto.setIndicator("Ponto", getResources().getDrawable(
				R.drawable.clock36));
		tabPonto.setContent(new Intent(this, TabPontoActivity.class));
		tabHost.addTab(tabPonto);

		TabSpec tabSearch = tabHost.newTabSpec("Busca");
		tabSearch.setIndicator("Busca", getResources().getDrawable(
				R.drawable.search36));
		tabSearch.setContent(new Intent(this, TabSearchActivity.class));
		tabHost.addTab(tabSearch);

		AdManager.setAllowUseOfLocation(true);
		AdManager.setTestDevices(new String[] { AdManager.TEST_EMULATOR });
		AdManager.setPublisherId("a14ca66077af2b4");

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// Fecha o banco
		repository.close();
	}

}