package pl.com.clockworkgnome.openmud.domain;

public enum Exit {
    DOOR("door","door"), NORTH("n","north"), SOUTH("s","south"), UP("u","up"), DOWN("d","down"), EAST("e","east"), WEST("w","west");

    public String exitString;
    public String shortString;

    Exit(String shortString, String exitString) {
        this.exitString = exitString;
        this.shortString = shortString;
    }

    public String getExitString() {
        return exitString;
    }

    public String getShortString() {
        return shortString;
    }
}
