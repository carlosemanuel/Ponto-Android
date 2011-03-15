package br.com.carlosemanuel.ponto.repository;

import android.content.Context;
import br.com.carlosemanuel.sql.SQLiteHelper;

public class PontoScriptRepository extends PontoRepository {

	private static final String SCRIPT_DROP_TABLE = "DROP TABLE IF EXISTS ponto";

	private static final String[] SCRIPT_CREATE_TABLE = new String[] { "CREATE"
			+ " TABLE ponto (_id integer primary key autoincrement,"
			+ " start_time date, end_time date, reference_date date);" };

	private static final int DATABASE_VERISON = 1;

	private SQLiteHelper dbHelper;

	public PontoScriptRepository(Context ctx) {
		dbHelper = new SQLiteHelper(ctx, PontoRepository.NOME_BANCO,
				DATABASE_VERISON, SCRIPT_CREATE_TABLE, SCRIPT_DROP_TABLE);

		db = dbHelper.getWritableDatabase();
	}

	@Override
	public void close() {
		super.close();
		if (dbHelper != null) {
			dbHelper.close();
		}
	}

}
