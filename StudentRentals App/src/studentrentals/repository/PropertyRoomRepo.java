package studentrentals.repository;

import studentrentals.domain.Property;
import studentrentals.domain.Room;
import java.util.*;


public class PropertyRoomRepo {

    // In-memory storage of properties and rooms
    private final Map<String, Property> property_by_ID = new HashMap<>();
    private final Map<String, Room> room_by_ID = new HashMap<>();
    private final Map<String, List<String>> property_ID_by_owner = new HashMap<>();

    public Optional<Property> findPropertyByID(String id) {
        Optional<Property> property = Optional.ofNullable(property_by_ID.get(id));
        return property;
    }

    public Optional<Room> findRoomByID(String id) {
        Optional<Room> room = Optional.ofNullable(room_by_ID.get(id));
        return room;
    }

    public List<Property> findPropertyByOwnerID (String ownnerID) {
        List<String> propertyIDs = property_ID_by_owner.get(ownnerID);
        if (propertyIDs == null) {
            return Collections.emptyList();
        }
        List<Property> properties = new ArrayList<>();
        for (String propertyID : propertyIDs) {
            Property property = property_by_ID.get(propertyID);
            if (property != null) {
                properties.add(property);
            }
        }
        return properties;
    }

    public Collection<Property> getAllProperties() {
        Collection<Property> all_properties = Collections.unmodifiableCollection(property_by_ID.values());
        return all_properties;
    }

    public void saveProperty(Property p) {
        Objects.requireNonNull(p);
        if (property_by_ID.containsKey(p.getPropertyId())) {
            throw new IllegalArgumentException("Property with given ID already exists");
        }
        property_by_ID.put(p.getPropertyId(), p);

        String ownerID = p.getHomeownerID(); // Index property by owner
        List<String> owner_properties = property_ID_by_owner.get(ownerID);

        if (owner_properties == null) { // First property for this owner
            owner_properties = new ArrayList<>();
            property_ID_by_owner.put(ownerID, owner_properties);
        }
        owner_properties.add(p.getPropertyId());
    }

    public void roomIndex (Room room) {
        Objects.requireNonNull(room);
        if (room_by_ID.containsKey(room.getRoomID())) {
            throw new IllegalArgumentException("Room with given ID already exists");
        }
        room_by_ID.put(room.getRoomID(), room);
    }
}
