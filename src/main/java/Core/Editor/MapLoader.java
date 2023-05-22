package Core.Editor;

import Core.Objects.DynamicElement;
import Core.Objects.Layer;
import Core.Objects.Map;
import Core.Objects.StaticElement;
import Game.Util.SpriteLoader;
import com.google.gson.Gson;

import java.io.FileReader;
import java.util.ArrayList;

public class MapLoader extends SpriteLoader {
    public MapLoader(String PathToMaps) {
        super(PathToMaps);
    }

    @Override
    public boolean isLocked(String type) {
        return super.isLocked(type);
    }

    @Override
    public boolean isDynamic(String type) {
        return super.isDynamic(type);
    }
    public void loadMap(int ID) {
        Gson gson = new Gson();
        try {
            FileReader reader = new FileReader(PathToMaps + ID + ".map");
            Map map = gson.fromJson(reader, Map.class);
            ArrayList<Layer> layers = map.getLAYERS();
            for (Layer layer : layers) {
                ArrayList<StaticElement> elements = layer.getStaticElements();
                for (StaticElement element : elements) {
                    element.setImages(getSprite(element.getType()));
                }
                ArrayList<DynamicElement> dynamicElements = layer.getDynamicElements();
                for (DynamicElement de : dynamicElements) {
                    de.setImages(getSprite(de.getType()));
                    if (de.isLockedCharacter()) {
                        map.setLockedCharacter(de);
                    }
                }
            }
            map.init();
        } catch (Exception e) {
            System.out.println("Error reading Maps");
        }
    }
}
