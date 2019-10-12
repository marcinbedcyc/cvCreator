package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AbilityModel {
    private StringProperty ability = new SimpleStringProperty();

    public String getAbility() {
        return ability.get();
    }

    public StringProperty abilityProperty() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability.set(ability);
    }
}
