package entity;

public enum UserRole {
    CLIENT("client"),
    ADMIN("admin");
    private final String type;

    UserRole(String type){
        this.type = type;
    }
}
