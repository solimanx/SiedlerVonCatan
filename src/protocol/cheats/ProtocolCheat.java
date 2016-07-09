package protocol.cheats;

import com.google.gson.annotations.SerializedName;

import enums.CheatCode;
/**
 * Cheatcode sent by Client to Server
 * @author Roxana
 * 
 *
 */
public class ProtocolCheat {

	@SerializedName("Code")
	private CheatCode cc;

	public ProtocolCheat(CheatCode cc) {
		this.cc = cc;
	}

	public CheatCode getCheatCode() {
		return cc;
	}
	
}
