package br.com.carlosemanuel.ponto;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import br.com.carlosemanuel.ponto.beans.Ponto;
import br.com.carlosemanuel.ponto.beans.PontoStatus;
import br.com.carlosemanuel.ponto.repository.PontoRepository;
import br.com.carlosemanuel.ponto.service.PontoService;

public class SmallWidget extends AppWidgetProvider {

	public static final String ACTION_CLIQUE = "ACTION_CLIQUE";
	public static final long PERIOD = 5 * 60 * 1000L;

	static final String PNAME = SmallWidget.class.getPackage().toString();

	public static final String ACTION_CLICK = PNAME + ".ACTION_CLICK";

    public static final String EXTRA_APPWIDGET_ID = PNAME + ".APPWIDGET_ID";
	public static final String EXTRA_VIEW_ID = PNAME + ".VIEW_ID";

	public SmallWidget() {

	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Log.d("PONTO", "update realizado");

		Intent alarmIntent = new Intent(context, PontoAlarmReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, alarmIntent,
				0);

		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System
				.currentTimeMillis(), PERIOD, sender);


		for (int appWidgetId : appWidgetIds) {

			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.widget_layout);

			setOnClickListener(context, appWidgetId, views, R.id.widget);
			setOnClickListener(context, appWidgetId, views, R.id.widget_button);
			setOnClickListener(context, appWidgetId, views, R.id.widget_text);

			appWidgetManager.updateAppWidget(appWidgetId, views);
		}

		UpdateWidget.update(context);
	}

	/**
	 * Set pending intent in remote views
	 * 
	 * @param context
	 * @param views
	 *            Content remote views
	 * @param viewId
	 *            The view listening to click
	 */
	void setOnClickListener(Context context, int appWidgetId,
			RemoteViews views, int viewId) {
		Intent active = new Intent(context.getApplicationContext(),
				SmallWidget.class);

		active.setAction(ACTION_CLIQUE);

		active.putExtra(EXTRA_APPWIDGET_ID, appWidgetId);
		active.putExtra(EXTRA_VIEW_ID, viewId);

		// This is tricky, be aware that you can only have one set of extras for
		// any given PendingIntent action+data+category+component pair.
		active.setData(Uri.parse(appWidgetId + ":" + viewId));

		PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context
				.getApplicationContext(), 0, active,
				PendingIntent.FLAG_CANCEL_CURRENT);
		views.setOnClickPendingIntent(viewId, actionPendingIntent);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		if (intent.getAction().equals(ACTION_CLIQUE)) {

			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.widget_layout);

			PontoRepository repository = new PontoRepository(context);
			PontoService service = new PontoService(repository);

			Ponto ponto = service.baterPonto();
			PontoStatus status = service.getPontoStatus(ponto);

			if (status == PontoStatus.PONTO_ENTRADA) {
				views.setImageViewResource(R.id.widget_button,
						R.drawable.entrar_48x48);
				Toast.makeText(context, R.string.widget_out_msg,
						Toast.LENGTH_SHORT).show();
			} else {
				views.setImageViewResource(R.id.widget_button,
						R.drawable.sair_48x48);
				Toast.makeText(context, R.string.widget_enter_msg,
						Toast.LENGTH_SHORT).show();
			}

			Intent intent2 = new Intent(context, SmallWidget.class);
			intent2.setAction(ACTION_CLIQUE);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
					0, intent2, 0);

			views.setOnClickPendingIntent(R.id.widget, pendingIntent);
			views.setOnClickPendingIntent(R.id.widget_button, pendingIntent);
			views.setOnClickPendingIntent(R.id.widget_text, pendingIntent);

			ComponentName me = new ComponentName(context, SmallWidget.class);
			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(context);
			appWidgetManager.updateAppWidget(me, views);
			repository.close();

		}
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {

		super.onDeleted(context, appWidgetIds);

		Log.d("PONTO", "destroy widget");

		Intent alarmIntent = new Intent(context, PontoAlarmReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0,
				alarmIntent, 0);

		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
	}


}
