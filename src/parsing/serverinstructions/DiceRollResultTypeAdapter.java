package parsing.serverinstructions;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import parsing.JSONInterface;

/**
 * 
 * <b>7.2: "Würfeln"</b>
 * <p>
 * Determines value of the dice roll.
 * </p>
 * 
 * @version 0.1
 */
public class DiceRollResultTypeAdapter extends TypeAdapter<String>{

	@Override
	public void write(JsonWriter out, String value) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String read(JsonReader in) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}



}
