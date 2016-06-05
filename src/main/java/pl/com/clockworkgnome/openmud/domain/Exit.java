package pl.com.clockworkgnome.openmud.domain;

public enum Exit {
    DOOR("door","door"), NORTH("n","north"), SOUTH("s","south"), UP("u","up"), DOWN("d","down"), EAST("e","east"), WEST("w","west");

    public String exitString;
    public String shortString;

    Exit(String exitString, String shortString) {
        this.exitString = exitString;
        this.shortString = shortString;
    }
}
