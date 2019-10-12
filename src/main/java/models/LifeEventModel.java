package models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class LifeEventModel {
    private StringProperty title = new SimpleStringProperty();
    private StringProperty description = new SimpleStringProperty();
    private ObjectProperty<LocalDate> begin = new SimpleObjectProperty<>();
    private ObjectProperty<LocalDate> end = new SimpleObjectProperty<>();

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public LocalDate getBegin() {
        return begin.get();
    }

    public ObjectProperty<LocalDate> beginProperty() {
        return begin;
    }

    public void setBegin(LocalDate begin) {
        this.begin.set(begin);
    }

    public LocalDate getEnd() {
        return end.get();
    }

    public ObjectProperty<LocalDate> endProperty() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end.set(end);
    }
}
