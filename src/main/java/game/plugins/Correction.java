package game.plugins;

import core.objects.DynamicElement;
import core.objects.Layers;
import core.objects.StaticElement;
import core.util.Routine;
import persistence.Config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Correction extends Routine {

    public Correction() {
        super(0.5, new Placement());
    }

    private static class Placement implements Runnable{
        Set<String> staticPlatforms = new HashSet<>();
        Set<String> strictDynamics = new HashSet<>();
        Set<DynamicElement> rectifiable = new HashSet<>();
        Set<StaticElement> platforms = new HashSet<>();
        public Placement() {
            Config c = Config.getInstance();
            staticPlatforms.addAll(List.of(c.getProperty("StaticPlatforms").split(",")));
            strictDynamics.addAll(List.of(c.getProperty("StrictElements").split(",")));
        }
        @Override
        public void run() {
            query();
            for (var dynamic : rectifiable)
                for (var platform : platforms)
                    if (dynamic.collidesWith(platform) && !dynamic.collidesHorizontally(platform) &&
                            !dynamic.beneath(platform))
                        while (dynamic.collidesWith(platform))
                            dynamic.setY(dynamic.getY() - 1);
        }

        private void query() {
            var dynamics = Layers.getInstance().getALL_LAYERS().get(1).getDynamicElements();
            var statics = Layers.getInstance().getALL_LAYERS().get(2).getStaticElements();
            for (var dynamic : dynamics)
                if (strictDynamics.contains(dynamic.getType()) || dynamic.isLockedCharacter())
                    rectifiable.add(dynamic);
            for (var element : statics)
                if (staticPlatforms.contains(element.getType()))
                    platforms.add(element);
        }
    }
}
