package ru.osm.dkiselev.geocode_index_builder;

import java.util.ArrayList;
import java.util.List;

public class Import {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		List<DBTask> tasks = new ArrayList<DBTask>();
		tasks.add(new GetBuildingsQuerry());
		
		DBTaskManager.execute(tasks);
	}

}
