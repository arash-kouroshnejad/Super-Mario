package core.editor;

import core.objects.Map;
import core.util.Loader;
import com.google.gson.Gson;


import java.io.FileWriter;

public abstract class MapCreator extends Loader {
    public MapCreator(String PathToMaps) {
        super(null, PathToMaps);
    }

    @Override
    public void saveMap(Map map, int ID) {
        Gson gson = new Gson();
        try {
            FileWriter writer = new FileWriter(PathToMaps + ID + ".map");
            gson.toJson(map, writer);
            writer.close();
        } catch(Exception e) {
            System.out.println("Error Saving Map " + ID + " At " + PathToMaps);
        }
    }
}
