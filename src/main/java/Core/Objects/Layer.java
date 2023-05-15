package Core.Objects;

import java.util.ArrayList;

public class Layer {
    private final int index;
    private final ArrayList<StaticElement> StaticElements = new ArrayList<>();
    private final ArrayList<DynamicElement> DynamicElements = new ArrayList<>();

    public Layer(ArrayList<StaticElement> staticElements, ArrayList<DynamicElement> dynamicElements, int index) {
        this.StaticElements.addAll(staticElements);
        DynamicElements.addAll(dynamicElements);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public ArrayList<StaticElement> getStaticElements() {
        return StaticElements;
    }

    public ArrayList<DynamicElement> getDynamicElements() {
        return DynamicElements;
    }

    public void addStaticElement(StaticElement element) {
        StaticElements.add(element);
    }

    public void addDynamicElement(DynamicElement de) {DynamicElements.add(de);}
}
