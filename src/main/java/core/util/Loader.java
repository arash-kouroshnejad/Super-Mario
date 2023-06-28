package core.util;

import core.objects.DynamicElement;
import core.objects.Layer;
import core.objects.Map;
import core.objects.StaticElement;
import com.google.gson.Gson;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Loader {

    protected final String PathToSprites;
    protected final String PathToMaps;

    protected final HashMap<String, ArrayList<Image>> IMAGES = new HashMap<>();

    protected final HashMap<String, String[]> references = new HashMap<>();

    protected final HashMap<String, Dimension> dimensions = new HashMap<>();

    protected String[] TYPES;

    protected String[] lockedElements = new String[0];

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
                    arr.add(ImageIO.read(new File(PathToSprites + str + ".png")).getScaledInstance(
                            getDimension(type).width, getDimension(type).height, Image.SCALE_SMOOTH));
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
            List<Layer> layers = map.getLAYERS();
            for (Layer layer : layers) {
                for (StaticElement element : layer.getStaticElements()) {
                    element.setImages(getSprite(element.getType()));
                    element.setWidth(getDimension(element.getType()).width);
                    element.setHeight(getDimension(element.getType()).height);
                }
                for (DynamicElement de : layer.getDynamicElements()) {
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

    public boolean isLocked(String type) {
        for (String str : lockedElements)
            if (str.equals(type))
                return true;
        return false;
    }

    public Dimension getDimension(String type) {
        if (dimensions.containsKey(type)) {
            return dimensions.get(type);
        }
        return null;
    }
}
