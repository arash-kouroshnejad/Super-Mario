package Core.Util;

import Core.Objects.DynamicElement;
import Core.Objects.Layer;
import Core.Objects.Map;
import Core.Objects.StaticElement;
import com.google.gson.Gson;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Loader {

    protected final String PathToSprites;
    protected final String PathToMaps;

    protected final HashMap<String, ArrayList<Image>> IMAGES = new HashMap<>();

    protected final HashMap<String, String[]> references = new HashMap<>();

    protected final HashMap<String, Dimension> dimensions = new HashMap<>();

    protected String[] TYPES;

    public Loader(String pathToSprites, String pathToMaps) {
        PathToSprites = pathToSprites;
        PathToMaps = pathToMaps;
    }

    public ArrayList<Image> getSprite(String type) {
        if (IMAGES.containsKey(type)) {
            return IMAGES.get(type);
        }
        return null;
    }

    protected void loadSprites() {
        for (String type : TYPES) {
            ArrayList<Image> arr = new ArrayList<>();
            for (String str : references.get(type)) {
                try {
                    arr.add(ImageIO.read(new File(PathToSprites + str + ".png")));
                } catch (Exception e) {
                    System.out.println("Error Reading " + type + ":" + str);
                }
            }
            IMAGES.put(type, arr);
        }
    }

    public Map getMap(int ID, int level) {
        Gson gson = new Gson();
        try {
            FileReader reader = new FileReader(PathToMaps + ID + "/" + level + ".map");
            return gson.fromJson(reader, Map.class);
        } catch (Exception e) {
            System.out.println("Error reading Map " + ID + "/" + level);
        }
        return null;
    }

    public void saveMap(Map map, int ID) {
        Gson gson = new Gson();
        try {
            FileWriter writer = new FileWriter(PathToMaps + ID + "/" + map.getID() + ".map");
            gson.toJson(map, writer);
            writer.close();
        }
        catch(IOException e) {
            System.out.println("Error writing Map " + map.getID());
        }
    }

    public void loadMap(int ID, int level) {
        Map map = getMap(ID, level);
        if (map != null) {
            ArrayList<Layer> layers = map.getLAYERS();
            for (Layer layer : layers) {
                ArrayList<StaticElement> elements = layer.getStaticElements();
                for (StaticElement element : elements) {
                    element.setImages(getSprite(element.getType()));
                    element.setWidth(getDimension(element.getType()).width);
                    element.setHeight(getDimension(element.getType()).height);
                }
                ArrayList<DynamicElement> dynamicElements = layer.getDynamicElements();
                for (DynamicElement de : dynamicElements) {
                    de.setImages(getSprite(de.getType()));
                    de.setWidth(getDimension(de.getType()).width);
                    de.setHeight(getDimension(de.getType()).height);
                    if (de.isLockedCharacter()) {
                        map.setLockedCharacter(de);
                    }
                }
            }
            map.init();
        }
    }

    public abstract boolean isDynamic(String type);

    public String[] getTYPES() {
        return TYPES;
    }

    public abstract boolean isLocked(String type);

    public Dimension getDimension(String type) {
        if (dimensions.containsKey(type)) {
            return dimensions.get(type);
        }
        return null;
    }
}
