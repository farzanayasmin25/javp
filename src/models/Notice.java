package models;

import javafx.beans.property.*;

import java.sql.Timestamp;

public class Notice {
    private final IntegerProperty id;
    private final StringProperty title;
    private final StringProperty notice;
    private final ObjectProperty<Timestamp> timestamp;

    public Notice(int id, String title, String notice, Timestamp timestamp) {
        this.id = new SimpleIntegerProperty(id);
        this.title = new SimpleStringProperty(title);
        this.notice = new SimpleStringProperty(notice);
        this.timestamp = new SimpleObjectProperty<>(timestamp);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public String getNotice() {
        return notice.get();
    }

    public StringProperty noticeProperty() {
        return notice;
    }

    public Timestamp getTimestamp() {
        return timestamp.get();
    }

    public ObjectProperty<Timestamp> timestampProperty() {
        return timestamp;
    }
}
