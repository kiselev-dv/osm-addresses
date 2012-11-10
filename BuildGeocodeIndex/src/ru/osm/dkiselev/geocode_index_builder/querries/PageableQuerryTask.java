package ru.osm.dkiselev.geocode_index_builder.querries;

import java.sql.ResultSet;
import java.sql.SQLException;


public abstract class PageableQuerryTask implements DBTask {

	private final int pageSize;
	private int batchCounter = 0;
	
	private int counter = 0;
	
	public PageableQuerryTask(){
		pageSize = 1000;
	}
	
	public PageableQuerryTask(int pageSize){
		this.pageSize = pageSize;
	}
	
	@Override
	public String getQuerry() {
		return getQuerryNotPaged() + " limit " + pageSize + " offset " + counter; 
	}
	
	public abstract String getQuerryNotPaged();

	@Override
	public final void handleRow(ResultSet rs) throws SQLException {
		counter++;
		batchCounter++;
		
		handleRsRow(rs);
	}
	
	protected abstract void handleRsRow(ResultSet rs) throws SQLException;

	@Override
	public boolean doAgain() {
		if(batchCounter == pageSize || batchCounter == 0){
			batchCounter = 0;
			return true;
		}
		return false;
	}

	public int getCounter(){
		return this.counter;
	}
}
