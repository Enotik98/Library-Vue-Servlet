package entity;

public enum TicketType {
    SUBSCRIPTION("subscription"),
    ROOM("room");

    private final String type;
    TicketType(String type){
        this.type = type;
    }
}
