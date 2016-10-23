package hoot;

import db.DB;
import db.DBTable;
import db.IDType;

class HootDB {

	private static final String HOOT_DESTINATIONS_TABLE_NAME = "HootDestinations";
	private static final String HOOT_MESSAGES_TABLE_NAME = "HootMessages";

	static DBTable DESTs = new DBTable(HOOT_DESTINATIONS_TABLE_NAME, "id", null, false);
	static DBTable MSGs = new DBTable(HOOT_MESSAGES_TABLE_NAME, "id", "seq", true);

	public static void init(int dbLocalPort) throws Exception {
		DB.init(dbLocalPort, DESTs, MSGs);
		DB.createTable(HOOT_DESTINATIONS_TABLE_NAME, "id", null, null);
		DB.createTable(HOOT_MESSAGES_TABLE_NAME, "id", "seq", IDType.STRING);
	}

}
