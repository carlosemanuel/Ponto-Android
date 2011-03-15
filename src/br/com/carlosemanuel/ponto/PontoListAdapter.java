package br.com.carlosemanuel.ponto;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import br.com.carlosemanuel.ponto.beans.Ponto;

public class PontoListAdapter extends ArrayAdapter<Ponto> {

	private Context context;
	private List<Ponto> pontos;

	public PontoListAdapter(Context context, int textViewResourceId,
			ArrayList<Ponto> pontos) {
		super(context, textViewResourceId);
		this.pontos = pontos;
		this.context = context;
	}


	@Override
	public int getCount() {
		return pontos.size();
	}

	@Override
	public Ponto getItem(int position) {
		return pontos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Ponto ponto = pontos.get(position);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View view = inflater.inflate(R.layout.result_search, null);

		TextView line = (TextView) view.findViewById(R.id.search_line);

		String text = "";

		if (ponto.getStartDate() != null) {
			text = PontoUtil.formatDateTime(ponto.getStartDate());
		}

		text += " às ";

		if (ponto.getFinishDate() != null) {
			text += PontoUtil.formatDateTime(ponto.getFinishDate());
		}

		line.setText(text);

		return view;
	}

}
