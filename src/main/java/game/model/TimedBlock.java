package game.model;

import core.editor.LevelEditor;
import core.objects.StaticElement;

import java.util.HashMap;
import java.util.Map;

public class TimedBlock{
    private static final Map<StaticElement, TimedBlock> blocks = new HashMap<>();
    public static boolean isTimed(StaticElement element) {
        return blocks.containsKey(element);
    }
    public static void addBlock(StaticElement element, long capacity) {
        blocks.put(element, new TimedBlock(capacity, element));
    }
    public static TimedBlock getBlock(StaticElement block) {
        return blocks.get(block);
    }
    public static void reset() {
        for (var block : blocks.keySet())
            blocks.get(block).capacity = blocks.get(block).initCapacity;
    }

    private long capacity;
    private final long initCapacity;
    private final StaticElement element;

    private TimedBlock(long capacity, StaticElement element) {
        this.capacity = capacity;
        this.initCapacity = capacity;
        this.element = element;
    }

    public void check (long time) {
        capacity -= time;
        if (isFinished()) {
            var editor = LevelEditor.getInstance();
            editor.removeElement(element);
            blocks.remove(element);
        }
    }

    private boolean isFinished() {return capacity <= 0;}
}
