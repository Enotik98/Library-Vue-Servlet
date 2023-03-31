package entity;

public enum StatusType {
    WAITING("waiting"),
    ISSUED("issued"),
    RETURNED("returned");
    private String status;

    StatusType(String status) {
        this.status = status;
    }
}
