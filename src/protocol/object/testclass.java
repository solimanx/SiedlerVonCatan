package protocol.object;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

public class testclass {
	public static void main(String[] args){

		ProtocolField xx = new ProtocolField();
		xx.setField_id("A");
		xx.setField_type(null);
		xx.setDice_index(2);
		Gson gson = new GsonBuilder().serializeNulls().create();



	}

}
