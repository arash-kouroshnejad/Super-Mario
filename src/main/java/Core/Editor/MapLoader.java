package Core.Editor;

import Core.Objects.DynamicElement;
import Core.Objects.Layer;
import Core.Objects.Map;
import Core.Objects.StaticElement;
import Game.Util.Loaders.SpriteLoader;
import com.google.gson.Gson;

import java.awt.geom.Line2D;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class MapLoader extends SpriteLoader {
    public MapLoader(String PathToMaps) {
        super(PathToMaps);
    }

    public void loadMap(int ID) {
        Gson gson = new Gson();
        try {
            FileReader reader = new FileReader(PathToMaps + ID + ".map");
            Map map = gson.fromJson(reader, Map.class);
            List<Layer> layers = map.getLAYERS();
            for (Layer layer : layers) {
                for (StaticElement element : layer.getStaticElements()) {
                    element.setImages(getSprite(element.getType()));
                }
                for (DynamicElement de : layer.getDynamicElements()) {
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
