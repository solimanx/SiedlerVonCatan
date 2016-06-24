package ai.agents;

import java.util.ResourceBundle;

import ai.AdvancedAI;
import enums.CornerStatus;
import enums.HarbourStatus;
import enums.ResourceType;
import model.Board;
import model.Index;
import model.objects.Corner;
import model.objects.Edge;
import model.objects.Field;
import network.ModelToProtocol;
import network.ProtocolToModel;

/**
 * Calculates the advantage of occupying the corner, by taking into account what
 * the surrounding fields, their dice index is, and connected edges.
 */
public class CornerAgent {
	// Whether the corner is occupied, blocked
	private CornerStatus state;
	// Whether the corner is also a harbor
	private HarbourStatus harbour_state;
	// Location of the corner
	private int[] location = new int[3];
	// Fields around it
	private Field[] f = new Field[3];
	// Edge around it
	private Edge[] e = new Edge[3];
	// Corners around it
	private Corner[] c = new Corner[3];
	// utility of edges surrounding the corner
	private int[] edgeUtility = { 0, 0, 0 };
	// total utility of corner
	private int netUtility;
	// difference level of field resources
	private int difference;
	// id of corner/corneragent
	private String id;

	private ResourceBundle rb;
	private Board board;
	private AdvancedAI aai;

	public CornerAgent(int[] loc, Board board, AdvancedAI aai) {
		this.aai = aai;
		this.board = board;
		Corner c = board.getCornerAt(loc[0], loc[1], loc[2]);
		this.id = c.getCornerID();
		this.state = c.getStatus();
		if (!isBlocked()) {

			this.harbour_state = c.getHarbourStatus();
			this.location = loc;
			this.f = board.getTouchingFields(loc[0], loc[1], loc[2]);
			this.e = board.getProjectingEdges(loc[0], loc[1], loc[2]);
			this.c = board.getAdjacentCorners(loc[0], loc[1], loc[2]);
			this.netUtility = 0;
			this.rb = ResourceBundle.getBundle("ai.bundle.AIProperties");
		}
	}

	/**
	 * Used to calculate the best direction for the initial roads, by checking
	 * the corners on the adjacent edges of the adjacent edges to this corner.
	 */
	public void calculateInitialRoad() {
		for (int i = 0; i < 3; i++) {
			if (c[i] != null) {
				int[] coords = ProtocolToModel.getCornerCoordinates(c[i].getCornerID());
				Edge[] neighbouringEdges = board.getProjectingEdges(coords[0], coords[1], coords[2]);
				for (int j = 0; j < neighbouringEdges.length; j++) {
					if (neighbouringEdges[j] != null && neighbouringEdges[j] != e[i]) {
						if (!neighbouringEdges[j].isHasStreet()) {
							int[] coords2 = ProtocolToModel.getEdgeCoordinates(neighbouringEdges[j].getEdgeID());
							Corner[] neighbouringCorners = board.getAttachedCorners(coords2[0], coords2[1], coords2[2]);
							for (int k = 0; k < neighbouringCorners.length; k++) {
								if (neighbouringCorners[k] != c[i]) {
									int util = aai.getCornerAgentByID(neighbouringCorners[k].getCornerID())
											.calculateInitialVillageUtility();
									edgeUtility[i] += util;
								}
							}
						}
					}
				}
			}
		}

	}

	/**
	 * Returns the coordinates of the highest utility road.
	 */
	public int[] getBestRoad() {
		int max = edgeUtility[0];
		int c = 0;
		for (int i = 0; i < 3; i++) {
			if (edgeUtility[i] > max) {
				max = edgeUtility[i];
				c = i;
			}
		}

		return ProtocolToModel.getEdgeCoordinates(e[c].getEdgeID());
	}

	/**
	 * Adds all the factors together to calculate the utility for placing the
	 * initial settlement, the higher the utility, the higher the chance to be
	 * picked by the AI as a spot to build on.
	 */
	public int calculateInitialVillageUtility() {
		if (isBlocked()) {
			// Random negative number to avoid building here.
			return -10000;
		} else {
			int ld = landDiversity();
			int hd = harbourDiversity();
			int rp = rollProbability();
			int rb = resourceBonus();
			int bonus = 0;
			int[] utilities = { ld, hd, rp };

			if (difference == 0) {
				// change nothing
			} else {
				// get greatest and second greatest utility
				int high1 = Integer.MIN_VALUE;
				int high2 = Integer.MIN_VALUE;
				for (int num : utilities) {
					if (num >= high1) {
						high2 = high1;
						high1 = num;
					} else if (num > high2) {
						high2 = num;
					}

				}
				if (difference == 1)
					bonus = high2;
				else if (difference == 2)
					bonus = high1;

			}
			netUtility = ld + hd + rp + bonus + rb;

			return netUtility;

		}

	}

	/**
	 * Returns false if the corner can be build upon, otherwise true and
	 * blocked. Blocked corners aren't taken into account.
	 */
	protected boolean isBlocked() {
		return (!state.equals(CornerStatus.EMPTY));
	}

	/**
	 * Returns a utility number depending on the difference of the surrounding
	 * fields; and their type. Difference since we want to prioritize fields
	 * that have different types over fields that have the same type, and then
	 * since sea and desert fields are not benefitial they deduct value
	 */
	protected int landDiversity() {
		int utility = 0;
		final int DESERT_PENALTY = Integer.parseInt(rb.getString("DESERT_PENALTY"));
		final int SEA_PENALTY = Integer.parseInt(rb.getString("SEA_PENALTY"));
		for (int i = 0; i < 3; i++) {
			// If land exists
			if (f[i] != null) {
				switch (f[i].getResourceType()) {
				case NOTHING:
					utility += DESERT_PENALTY;
				case SEA:
					utility += SEA_PENALTY;
				default: // nothing
				}
			}
		}
		// TODO separate to a different method
		// If all 3 fields they have the same resource type
		if (f[0].getResourceType().equals(f[1].getResourceType())
				&& f[1].getResourceType().equals(f[2].getResourceType())
				&& f[0].getResourceType().equals(f[2].getResourceType())) {
			// Add 0 to utility
			setDifference(0);
		}
		// If None of them are the same
		else if (!f[0].getResourceType().equals(f[1].getResourceType())
				&& !f[1].getResourceType().equals(f[2].getResourceType())
				&& !f[0].getResourceType().equals(f[2].getResourceType())) {
			setDifference(2);

		}
		// If two of them are the same
		else {
			setDifference(1);
		}
		return utility;
	}

	/**
	 * Returns a utility number depending on the type of harbour surrounds the
	 * corner; 2to1 harbours preferred over 3to1 harbours.
	 */
	protected int harbourDiversity() {
		int utility = 0;
		final int THREE_HARBOUR_BENEFIT = Integer.parseInt(rb.getString("THREE_HARBOUR_BENEFIT"));
		final int TWO_HARBOUR_BENEFIT = Integer.parseInt(rb.getString("TWO_HARBOUR_BENEFIT"));
		for (int i = 0; i < 3; i++) {
			if (c[i] != null) {
				// If it's a harbour
				if (!c[i].getHarbourStatus().equals(HarbourStatus.NULL)) {
					switch (c[i].getHarbourStatus()) {
					// If it's a 3-1 harbour
					case THREE_TO_ONE:
						utility += THREE_HARBOUR_BENEFIT;
						break;
					// Any other harbour type which is 2:1
					default:
						utility += TWO_HARBOUR_BENEFIT;
						break;
					}
				}
			}
		}

		return utility;
	}

	/**
	 * Adds or deducts utility depending on the probability to roll the number
	 * set on the field.
	 */
	protected int rollProbability() {
		Double diversity = 0.0;
		Integer f0p = f[0] != null ? f[0].getDiceIndex() != null ? f[0].getDiceIndex() : 0 : 0;
		Integer f1p = f[1] != null ? f[1].getDiceIndex() != null ? f[1].getDiceIndex() : 0 : 0;
		Integer f2p = f[2] != null ? f[2].getDiceIndex() != null ? f[2].getDiceIndex() : 0 : 0;
		Double roll0 = aai.getDiceRollProbabilities().get(f0p) != null ? aai.getDiceRollProbabilities().get(f0p) : 0.0;
		Double roll1 = aai.getDiceRollProbabilities().get(f1p) != null ? aai.getDiceRollProbabilities().get(f1p) : 0.0;
		Double roll2 = aai.getDiceRollProbabilities().get(f2p) != null ? aai.getDiceRollProbabilities().get(f2p) : 0.0;
		diversity += roll0 + roll1 + roll2;
		// round up
		final int HEX_DICE_WEIGHT = Integer.parseInt(rb.getString("HEX_DICE_WEIGHT"));
		return (int) (HEX_DICE_WEIGHT * diversity);

	}

	/**
	 * Calculates whether there is an ore/corn bonus.
	 */
	protected int resourceBonus() {
		int bonus = 0;
		int woodB = aai.getSingleResourceWeight(ResourceType.WOOD);
		int clayB = aai.getSingleResourceWeight(ResourceType.CLAY);
		int oreB = aai.getSingleResourceWeight(ResourceType.ORE);
		int woolB = aai.getSingleResourceWeight(ResourceType.SHEEP);
		int cornB = aai.getSingleResourceWeight(ResourceType.CORN);
		for (int i = 0; i < 3; i++) {
			if (f[i] != null) {
				switch (f[i].getResourceType()) {
				case WOOD:
					bonus += woodB;
					break;
				case CLAY:
					bonus += clayB;
					break;
				case ORE:
					bonus += oreB;
					break;
				case SHEEP:
					bonus += woolB;
					break;
				case CORN:
					bonus += cornB;
					break;
				default:
					break;
				}
			}
		}
		return bonus;
	}

	private void setDifference(int i) {
		difference = i;

	}

	public int[] getLocation() {
		return location;
	}

	// DEBUG
	public String getLocationString() {
		return "[" + location[0] + "," + location[1] + "," + location[2] + "]";
	}

	public String getID() {
		return id;
	}

	public Field[] getFields() {
		return f;
	}

}
