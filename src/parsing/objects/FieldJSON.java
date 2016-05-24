package parsing.objects;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import enums.ResourceType;
import model.Field;

/**
 * 
 * <b>4.1: "Felder"</b>
 * <p>
 * Determines field's ID (location), resource type and chip/dice index.
 * </p>
 * 
 * @version 0.1
 */
public class FieldJSON {
	public Object toFieldJson(Field f) {
		Field field = new Field();
		// Ort
		field.setFieldID("A");
		// Typ
		field.setResourceType(ResourceType.CORN);
		// Zahl
		field.setDiceIndex(2);

		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting().serializeNulls();
		Gson gson = builder.create();
		return gson.toJson(field);
	}

	public void writeFieldJson(Field f) throws IOException {
		try (Writer writer = new OutputStreamWriter(new FileOutputStream("SS.json"), "UTF-8")) {

			Gson gson = new GsonBuilder().create();
			gson.toJson(toFieldJson(f), writer);
		}

	}

	public static void writeFieldJson2() throws IOException {

		Field field = new Field();
		field.setFieldID("A");
		field.setResourceType(ResourceType.CORN);
		field.setDiceIndex(2);

		try (Writer writer = new OutputStreamWriter(new FileOutputStream("SS.json"), "UTF-8")) {
			GsonBuilder builder = new GsonBuilder();
			builder.setPrettyPrinting().serializeNulls();
			Gson gson = builder.create();
			gson.toJson(field, writer);
		}

	}
}
