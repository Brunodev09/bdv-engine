package examples.dungeon.mob;

public enum FactionTypes {

    HUMANS("humans"),
    ELVES("elves"),
    ORCS("orcs"),
    MONSTER("monster"),
    ANIMAL("animal"),
    OBJECT("object");

    private final String faction;

    FactionTypes(String description) {
        this.faction = description;
    }

    public String getFaction() {
        return faction;
    }
}
