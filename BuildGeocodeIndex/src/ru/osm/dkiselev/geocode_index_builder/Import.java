package ru.osm.dkiselev.geocode_index_builder;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.time.DurationFormatUtils;

public class Import {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		List<DBTask> tasks = new ArrayList<DBTask>();
		tasks.add(new GetBuildingsQuerry());

		long start = System.currentTimeMillis();
		
		DBTaskManager.execute(tasks);

		DurationFormatUtils.formatDurationHMS(System.currentTimeMillis() - start);
	}

}
