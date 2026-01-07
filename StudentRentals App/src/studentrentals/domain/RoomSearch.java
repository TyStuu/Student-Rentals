package studentrentals.domain;

public class RoomSearch {
    private final Property property;
    private final Room room;

    public RoomSearch(Property property, Room room) {
        this.property = property;
        this.room = room;
    }

    public Property getProperty() {
        return property;
    }

    public Room getRoom() {
        return room;
    }
    
}
