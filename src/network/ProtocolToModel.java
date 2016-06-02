package network;

import model.HexService;

public final class ProtocolToModel {
	public int[] getFieldCoordinates(String field_id) {
		return HexService.getFieldCoordinates(field_id);
	}

}
