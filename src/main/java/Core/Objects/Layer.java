package Core.Objects;


import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;


public class Layer {
    private final int index;
    private final CopyOnWriteArrayList<StaticElement> StaticElements = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<DynamicElement> DynamicElements = new CopyOnWriteArrayList<>();

    public Layer(ArrayList<StaticElement> staticElements, ArrayList<DynamicElement> dynamicElements, int index) {
        this.StaticElements.addAll(staticElements);
        DynamicElements.addAll(dynamicElements);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public CopyOnWriteArrayList<StaticElement> getStaticElements() {
        return StaticElements;
    }

    public CopyOnWriteArrayList<DynamicElement> getDynamicElements() {
        return DynamicElements;
    }

    public void addStaticElement(StaticElement element) {
        StaticElements.add(element);
    }

    public void addDynamicElement(DynamicElement de) {DynamicElements.add(de);}

    public void addStaticElement(StaticElement element, int index) {StaticElements.add(index, element);}

    public void addDynamicElement(DynamicElement de, int index) {DynamicElements.add(index, de);}
}
