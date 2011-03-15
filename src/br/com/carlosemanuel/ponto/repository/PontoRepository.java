package br.com.carlosemanuel.ponto.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import br.com.carlosemanuel.ponto.beans.Ponto;
import br.com.carlosemanuel.utils.DateUtils;

public class PontoRepository {

	public static final String ID = "_id";

	public static final String START_DATE = "start_time";

	public static final String FINISH_DATE = "end_time";

	public static final String[] COLUMNS = new String[] { ID, START_DATE,
			FINISH_DATE };

	protected static final String NOME_BANCO = "ponto";

	protected static final String NOME_TABELA = "ponto";

	protected SQLiteDatabase db;

	public PontoRepository() {
	}

	public PontoRepository(Context ctx) {
		db = ctx.openOrCreateDatabase(NOME_BANCO, Context.MODE_PRIVATE, null);
	}

	public long save(Ponto ponto) {
		return db.insert(NOME_TABELA, "", convert(ponto));
	}

	public void update(Ponto ponto) {

		String _id = String.valueOf(ponto.getId());

		String where = ID + "=?";
		String[] whereArgs = new String[] { _id };

		int updateds = db.update(NOME_TABELA, convert(ponto), where, whereArgs);
		Log.d("PONTO", "Atualizado [" + updateds + "] registros");
	}

	public void saveOrUpdate(Ponto ponto) {
		if (ponto.getId() == null) {
			long id = save(ponto);
			ponto.setId(id);
		} else {
			update(ponto);
		}
	}

	public Ponto getLast() {
		Cursor cursor = db.query(NOME_TABELA, COLUMNS, null, null, null, null,
				START_DATE + " DESC");

		Ponto p = null;
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			p = convert(cursor);
		}
		cursor.close();
		return p;
	}

	public List<Ponto> getLasts(int n) {
		Cursor cursor = db.query(NOME_TABELA, COLUMNS, null, null, null, null,
				START_DATE + " DESC");

		List<Ponto> pontos = new ArrayList<Ponto>();
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			for (int i = 0; i < n && !cursor.isLast(); i++) {
				pontos.add(convert(cursor));
				cursor.moveToNext();
			}
		}

		cursor.close();
		return pontos;
	}

	public List<Ponto> listAll() {
		Cursor cursor = db.query(NOME_TABELA, COLUMNS, null, null, null, null,
				START_DATE + " DESC");

		List<Ponto> pontos = new ArrayList<Ponto>();
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isLast()) {
				pontos.add(convert(cursor));
				cursor.moveToNext();
			}
		}

		cursor.close();
		return pontos;
	}

	public int remove(Ponto ponto) {
		String where = ID + "=?";
		String _id = ponto.getId().toString();
		String[] whereArgs = new String[] { _id };
		return db.delete(NOME_TABELA, where, whereArgs);
	}

	public List<Ponto> getPontosOfWeek() {
		return getPontosBetween(DateUtils.getFirstDayOfWeek(), new Date());
	}

	public List<Ponto> getPontosOfMonth() {
		return getPontosBetween(DateUtils.getFirstDayOfMonth(), new Date());
	}

	public List<Ponto> getPontosOfDay() {
		return getPontosBetween(DateUtils.getFirstHourOfDay(), DateUtils
				.getLastHourOfDay());
	}

	public List<Ponto> getPontosBetween(Date date1, Date date2) {
		List<Ponto> pontos = new ArrayList<Ponto>();

		String where = START_DATE + " >= ? and " + START_DATE + " <= ?";
		String[] whereArgs = new String[] { Long.toString(date1.getTime()),
				Long.toString(date2.getTime()) };

		Cursor cursor = db.query(NOME_TABELA, COLUMNS, where, whereArgs, null,
				null, START_DATE + " DESC", null);

		cursor.moveToFirst();
		while (cursor.isAfterLast() == false) {
			pontos.add(convert(cursor));
			cursor.moveToNext();
		}
		cursor.close();

		return pontos;

	}

	private Ponto convert(Cursor cursor) {
		Ponto p = null;

		p = new Ponto();
		p.setId(cursor.getLong(0));
		long init = cursor.getLong(1);
		if (init > 0) {
			p.setStartDate(new Date(init));
		} else {
			p.setStartDate(null);
		}
		long end = cursor.getLong(2);
		if (end > 0) {
			p.setFinishDate(new Date(end));
		} else {
			p.setFinishDate(null);
		}

		return p;
	}

	private ContentValues convert(Ponto ponto) {
		ContentValues values = new ContentValues();

		if (ponto.getId() != null) {
			values.put(ID, ponto.getId());
		}

		long startTime = -1L;
		if (ponto.getStartDate() != null) {
			startTime = ponto.getStartDate().getTime();
		}
		values.put(START_DATE, startTime);

		long finishTime = -1L;
		if (ponto.getFinishDate() != null) {
			finishTime = ponto.getFinishDate().getTime();
		}
		values.put(FINISH_DATE, finishTime);

		return values;
	}

	// Fecha o banco
	public void close() {
		// fecha o banco de dados
		if (db != null) {
			db.close();
		}
	}
}
