package ru.osm.dkiselev.geocode_index_builder;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.time.DurationFormatUtils;

import ru.osm.dkiselev.geocode_index_builder.querries.DBTask;
import ru.osm.dkiselev.geocode_index_builder.querries.GetPlacesQuerry;
import ru.osm.dkiselev.geocode_index_builder.writers.PSQLWriter;

public class Import {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		List<DBTask> tasks = new ArrayList<DBTask>();
		tasks.add(new GetPlacesQuerry());

		long start = System.currentTimeMillis();
		
		DBTaskManager.execute(tasks);
		PSQLWriter.getInstance().close();

		String durationString = DurationFormatUtils.formatDurationHMS(System.currentTimeMillis() - start);
		System.out.println("Done in " + durationString);
	}

}
