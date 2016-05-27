package parsing.object;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 *
 * <b>4.2: "Gebäude"</b>
 * <p>
 * Determines buildings owner/owner-ID , building type and location.
 * </p>
 *
 * @version 0.1
 */
public class BuildingTypeAdapter extends TypeAdapter<String>{

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
