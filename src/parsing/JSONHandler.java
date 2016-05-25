package parsing;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import model.Field;
import parsing.objects.FieldTypeAdapter;

/**
 * Java to JSON writer
 */
public class JSONHandler {
	final GsonBuilder gsonBuilder = new GsonBuilder();
	Gson gson;

	public JSONHandler() {
		gsonBuilder.setVersion(0.1).registerTypeAdapter(Field.class, new FieldTypeAdapter())
				.registerTypeAdapter(Field.class, new FieldTypeAdapter()).setPrettyPrinting();

		gson = gsonBuilder.create();
	}

	public void write(Object o, String wrap) throws IOException {
		try (Writer writer = new OutputStreamWriter(new FileOutputStream("Output.json"), "UTF-8")) {
			gson.toJson(o, writer);
			// TODO logging
			// TODO should we even wrap?

		}
	}
	
	//TODO NOT WORKING
	public void read(Object o) throws IOException{
		try (final Reader data = new InputStreamReader(new FileInputStream("E:\\Output.json"), "UTF-8")) {
			o = gson.fromJson(data,o.getClass());
		}
	}

}
