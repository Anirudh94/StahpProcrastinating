package stahpprocrastinating.example.stahpprocrastinating;

/**
 * Created by Chen on 2015-11-07.
 */
public class Charity {

    String name = null;
    boolean selected = false;

    public Charity(String name, boolean selected) {
        super();
        this.name = name;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
