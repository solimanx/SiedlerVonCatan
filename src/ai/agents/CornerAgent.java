package ai.agents;

import enums.CornerStatus;
import enums.HarbourStatus;
import model.Index;
import model.objects.Edge;
import model.objects.Field;

/**
 * Calculates the advantage of occupying the corner, by taking into account what the
 * surrounding fields, their dice index is, and connected edges.
 */
public class CornerAgent {
	//Whether the corner is occupied, blocked
	private CornerStatus state;
	//Whether the corner is also a harbor
	private HarbourStatus harbour_state;
	//Location of the corner
	private Index[] location = new Index[3];
	
	//0th Field
	private Field fZero;
	//1st Field
	private Field fOne;
	//2nd Field
	private Field fTwo;
	
	//0th Edge
	private Edge eZero;
	//1st Edge
	private Edge eOne;
	//2nd Edge
	private Edge eTwo;
	
	
	private int utility;
	

}
