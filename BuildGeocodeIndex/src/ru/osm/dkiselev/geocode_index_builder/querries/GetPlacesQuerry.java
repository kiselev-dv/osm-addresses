package ru.osm.dkiselev.geocode_index_builder.querries;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.poly2tri.geometry.polygon.Polygon;
import org.poly2tri.geometry.polygon.PolygonPoint;
import org.poly2tri.triangulation.TriangulationContext;
import org.poly2tri.triangulation.TriangulationPoint;
import org.poly2tri.triangulation.delaunay.DelaunayTriangle;
import org.poly2tri.triangulation.delaunay.sweep.DTSweep;
import org.poly2tri.triangulation.delaunay.sweep.DTSweepContext;
import org.postgresql.jdbc2.EscapedFunctions;

import ru.osm.dkiselev.geocode_index_builder.writers.PSQLWriter;


public class GetPlacesQuerry extends PageableQuerryTask {

	@Override
	public String getQuerryNotPaged() {
		return "SELECT osm_id, \"NAME\", \"NAME_EN\", \"NAME_RU\", \"PLACE\", \"A_CNTR\", \"A_RGN\", " +  
					"\"A_DSTRCT\", ST_ASText(geom) " + 
				"FROM layer.\"RU settlement-polygon\"";
	}

	@Override
	protected void handleRsRow(ResultSet rs) throws SQLException {
		
		BigDecimal osm_id = rs.getBigDecimal(1);
		
		if(osm_id == null){
			return;
		}
		
		String name = rs.getString(2);
		String nameEN = rs.getString(3);
		String nameRU = rs.getString(4);
		String place = rs.getString(5);
		String contry = rs.getString(6);
		String region = rs.getString(7);
		String district = rs.getString(8);
		String geometry = rs.getString(9);
		
		List multipolygon = MultiPolygonParser.parse(geometry);
				
		StringBuilder citySQL = new StringBuilder();
		citySQL.append("insert into cityes values(").append(osm_id).append(",");
		
		if(StringUtils.stripToNull(name) == null){
			citySQL.append(" NULL,");
		}else{
			citySQL.append(" '").append(StringEscapeUtils.escapeSql(name)).append("', ");
		}

		if(StringUtils.stripToNull(nameEN) == null){
			citySQL.append(" NULL,");
		}else{
			citySQL.append(" '").append(StringEscapeUtils.escapeSql(nameEN)).append("', ");
		}

		if(StringUtils.stripToNull(nameRU) == null){
			citySQL.append(" NULL,");
		}else{
			citySQL.append(" '").append(StringEscapeUtils.escapeSql(nameRU)).append("', ");
		}
		
		//if(StringUtils.stripToNull(region) == null){
			citySQL.append(" NULL,");
		//}else{
		//	citySQL.append(" '").append(StringEscapeUtils.escapeSql(region)).append("', ");
		//}
		
		if(StringUtils.stripToNull(district) == null){
			citySQL.append(" NULL,");
		}else{
			citySQL.append(" '").append(StringEscapeUtils.escapeSql(district)).append("',");
		}
		
		citySQL.append("ST_GeomFromText('").append(geometry).append("')");
		
		citySQL.append(")");
		
		PSQLWriter.getInstance().writeString(citySQL.toString());
		
		List<List<double[]>> triangles = triangulate(multipolygon);
		for(List<double[]> triangle : triangles){

			StringBuilder sql = new StringBuilder();
			
			String wkt = asWKT(triangle);
			sql.append("insert into city_triangles values(").append(osm_id).append(", ST_GeometryFromText('").append(wkt).append("'))");
			
			PSQLWriter.getInstance().writeString(sql.toString());
		}
	}

	private String asWKT(List<double[]> triangle) {
		StringBuilder wkt = new StringBuilder();
		wkt.append("POLYGON((");
		
		for(double[] p : triangle){
			wkt.append(p[0]).append(" ").append(p[1]);
			
			wkt.append(", ");
		}
		
		wkt.append(triangle.get(0)[0]).append(" ").append(triangle.get(0)[1]);
		
		wkt.append("))");
		
		return wkt.toString();
	}

	private List<List<double[]>> triangulate(List multipolygon) {
		DTSweepContext tcx = new DTSweepContext();
		
		Polygon p = new Polygon(getPointsList((double[][]) multipolygon.get(0)));
		
		for(int i = 1; i < multipolygon.size(); i++){
			p.addHole(new Polygon(getPointsList((double[][]) multipolygon.get(i))));
		}
		
		tcx.prepareTriangulation(p);
		
		DTSweep.triangulate(tcx);
		
		List<DelaunayTriangle> triangles = tcx.getTriangles();
		
		List<List<double[]>> result = new ArrayList<List<double[]>>(triangles.size());
		
		for(DelaunayTriangle t : triangles){
			
			ArrayList<double[]> tps = new ArrayList<double[]>(3);
			result.add(tps);

			for(TriangulationPoint tp : t.points){
				tps.add(new double[]{tp.getX(), tp.getY()});
			}
		}
		
		return result;
	}

	private List<PolygonPoint> getPointsList(double[][] line) {
		
		List<PolygonPoint> result = new ArrayList<PolygonPoint>();
		
		for(double[] p : line){
			result.add(new PolygonPoint(p[0], p[1]));
		}
		
		return result;
	}

	@Override
	public void done() {
		
	}

}
