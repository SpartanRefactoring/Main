package metatester.aux_layer;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

import static metatester.aux_layer.FileUtils.FS;

/**
 * JSON files reading utilities.
 * @author Oren Afek
 * @since 04-Jul-17.
 */

class JSONReader {

    private final JSONParser parser;
    private static final String jsonFileName = "metatester.json";
    private final String fileName;

    private JSONReader(String fileName) {
        this.parser = new JSONParser();
        this.fileName = fileName;
    }

    static JSONReader create(String jsonPath) {
        return new JSONReader(jsonPath + FS + jsonFileName);
    }

    String read(String key) {
        try {
            JSONObject obj = (JSONObject) parser.parse(new FileReader(fileName));
            return obj.get(key).toString();

        } catch (IOException | ParseException e) {
            return "";
        }
    }
}
