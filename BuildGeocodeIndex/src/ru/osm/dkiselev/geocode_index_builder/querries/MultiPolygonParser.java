package ru.osm.dkiselev.geocode_index_builder.querries;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class MultiPolygonParser {

	public static List parse(String geometry) {
		String g1 = geometry.substring("MULTIPOLYGON".length()).trim();

		String g2 = g1.substring(1, g1.length() - 2);
		
		// ((0 0,4 0,4 4,0 4,0 0),(1 1,2 1,2 2,1 2,1 1)), ((-1 -1,-1 -2,-2 -2,-2 -1,-1 -1))
		int pointer = 0;
		
		List<String> outers = new ArrayList<String>();
		List<String> inners = new ArrayList<String>();
		
		boolean stillOuters = true;
		for(String s : StringUtils.split(g2, "(")){
			
			if(stillOuters){
				outers.add(s);
				stillOuters = StringUtils.endsWith(s, "))");
			}else{
				inners.add(s);
			}
		}
		
		List result = new ArrayList();
		
		for(String s : outers){
			
			String[] points_s = StringUtils.remove(s, ")").split(",");
			
			double[][] outer = new double[points_s.length][2];
			
			int pi = 0;
			for(String pair : points_s){
				String[] xy = pair.split(" ");
				double[] p = new double[]{Double.parseDouble(xy[0]), Double.parseDouble(xy[1])};
				outer[pi++] = p;
			}
			
			result.add(outer);
			
			break;
		}

		for(String s : inners){
			
			String[] points_s = StringUtils.remove(s, ")").split(",");
			
			double[][] inner = new double[points_s.length][2];
			
			int pi = 0;
			for(String pair : points_s){
				String[] xy = pair.split(" ");
				double[] p = new double[]{Double.parseDouble(xy[0]), Double.parseDouble(xy[1])};
				inner[pi++] = p;
			}
			
			result.add(inner);
		}
		
		return result;
	}

}
