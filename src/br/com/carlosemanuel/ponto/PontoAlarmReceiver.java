package br.com.carlosemanuel.ponto;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PontoAlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		Log.d("PONTO", "Alarme recebido");

		UpdateWidget.update(context);
	}

}
