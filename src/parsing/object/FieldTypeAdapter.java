package parsing.object;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
*
* <b>4.1: "Felder"</b>
* <p>
* Determines field's ID, type and dice index.
* </p>
*
* @version 0.1
*/
public class FieldTypeAdapter extends TypeAdapter<String>{

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
