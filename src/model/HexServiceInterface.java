package model;

public interface HexServiceInterface {
	
	//interface for ID stuff FOR NOW
	public int[] getFieldCoordinates(String field_id);
	public int[] getCornerCoordinates(String field_ids);
	public int[] getEdgeCoordinates(String field_ids);
	
	public String getSpiral(String starting_point_id);
	public String getRing(int radius);
}
