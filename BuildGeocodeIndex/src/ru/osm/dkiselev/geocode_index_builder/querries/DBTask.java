package ru.osm.dkiselev.geocode_index_builder.querries;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DBTask {

	public String getQuerry();

	public void handleRow(ResultSet rs) throws SQLException;

	public boolean doAgain();

	public void done();

}
