package studentrentals.repository;

import studentrentals.domain.Property;
import studentrentals.domain.Room;
import studentrentals.domain.RoomSearch;
import studentrentals.util.IndexUtil;

import java.util.*;


public class PropertyRoomRepo {

// In-memory storage of properties and rooms
    private final Map<String, Property> property_by_ID = new HashMap<>();
    private final Map<String, Room> room_by_ID = new HashMap<>();
    private final Map<String, List<String>> property_ID_by_owner = new HashMap<>();


// Methods for Property
    public Optional<Property> findPropertyByID(String id) {
        Optional<Property> property = Optional.ofNullable(property_by_ID.get(id));
        return property;
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

    public Optional<Property> findPropertyByRoomID (String roomID) {
        Room room = room_by_ID.get(roomID);
        if (room == null) {
            return Optional.empty();
        }
        Property property = property_by_ID.get(room.getPropertyID());
        return Optional.ofNullable(property);
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

        IndexUtil.addToIndex(property_ID_by_owner, p.getHomeownerID(), p.getPropertyId()); // Index property by homeowner
    }


// Methods for Rooms
    public Optional<Room> findRoomByID(String id) {
        Optional<Room> room = Optional.ofNullable(room_by_ID.get(id));
        return room;
    }

    public void addRoomToProperty(Room room) {
        Objects.requireNonNull(room);
        Property property = property_by_ID.get(room.getPropertyID());
        if (property == null) {
            throw new IllegalArgumentException("Property not found for this room");
        }

        roomIndex(room);
        property.addRoom(room);
    }

    public void roomIndex (Room room) {
        Objects.requireNonNull(room);
        if (room_by_ID.containsKey(room.getRoomID())) {
            throw new IllegalArgumentException("Room with given ID already exists");
        }
        room_by_ID.put(room.getRoomID(), room);
    }

    public List<RoomSearch> getAllActiveRooms() {
        List<RoomSearch> active_rooms = new ArrayList<>();
        
        for (Property property : property_by_ID.values()){
            if (property.isActive()){
                for (Room room : property.getRooms()){
                    if (room.isActive()){
                        RoomSearch room_search = new RoomSearch(property, room);
                        active_rooms.add(room_search);
                    }
                }
            }
        }
        return active_rooms;
    }

    public List<RoomSearch> linearRoomSearch(String keyword) { // linear search that checks all properties and their rooms foor any mathces
        String k;

        if (keyword != null) {
            k = keyword.trim().toLowerCase();
        } else {
            k = "";
        }

        List<RoomSearch> matching_rooms = new ArrayList<>();

        for (Property property : property_by_ID.values()) {
            String address = property.getAddress().toLowerCase();
            String description = property.getDescription().toLowerCase();
            boolean matches;

            if (k.isEmpty()){
                matches = true; //if keyword was blank, match all
            } else {
                matches = address.contains(k) || description.contains(k);
            }

            if (matches && property.isActive()){
                for (Room room : property.getRooms()){
                    if (room.isActive()){
                        matching_rooms.add(new RoomSearch(property, room));
                    }
                }
            }
        }
        return matching_rooms;
    }


}
