package Game.Plugins;

import Core.Objects.DynamicElement;

import java.util.ArrayList;

public class Gravity extends Thread{
    private ArrayList<DynamicElement> elements;

    public void setElements(ArrayList<DynamicElement> elements) {
        this.elements = elements;
    }

    private boolean killed;

    public void run() {
        while (!killed) {
            try {
                int size = elements.size();
                for(int i=0;i<size;i++) {
                    DynamicElement element = elements.get(i);
                    if (!element.isHidden() && !element.getType().equals("Plant")) {
                        element.setSpeedY(element.getSpeedY() + 1);
                    }
                }
                Thread.sleep(100);
            } catch (Exception ignored) {}
        }
    }

    public void apply() {
        killed = false;
        super.start();
    }

    public void remove() {killed = true;}
}
