package br.com.carlosemanuel.ponto;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import br.com.carlosemanuel.ponto.beans.Ponto;
import br.com.carlosemanuel.ponto.beans.PontoStatus;
import br.com.carlosemanuel.ponto.repository.PontoRepository;
import br.com.carlosemanuel.ponto.service.PontoService;

public class UpdateWidget {

	public static void update(Context context) {

		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.widget_layout);

		PontoRepository repository = new PontoRepository(context);
		PontoService service = new PontoService(repository);

		Ponto ponto = service.getLastPonto();
		PontoStatus status = service.getPontoStatus(ponto);

		if (status == PontoStatus.PONTO_ENTRADA) {
			views.setImageViewResource(R.id.widget_button,
					R.drawable.entrar_48x48);
		} else {
			views.setImageViewResource(R.id.widget_button,
					R.drawable.sair_48x48);
		}

		Intent intent2 = new Intent(context, SmallWidget.class);
		intent2.setAction(SmallWidget.ACTION_CLIQUE);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intent2, 0);

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
