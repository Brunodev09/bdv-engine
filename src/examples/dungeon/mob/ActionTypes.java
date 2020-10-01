package examples.dungeon.mob;

public enum ActionTypes {

    MOVE("move"),
    WAIT("wait");

    private final String description;

    ActionTypes(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
