package pl.com.clockworkgnome.openmud.domain;

import com.google.gson.Gson;

import java.util.*;

public class Location {
    private int id;
    private String shortDescription;
    private String longDescription;
    private Map<Exit, Location> exits = new EnumMap<>(Exit.class);
    private List<Player> players = new ArrayList<>();

    public Location(int id, String shortDescription, String longDescription) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
    }

    public int getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void addExit(Exit exit, Location location) {
        this.exits.put(exit, location);
    }

    public Map<Exit, Location> getExits() {
        return exits;
    }

    public void addPlayer(Player player) {
        this.players.add(player);
        player.setCurrentLocation(this);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public String getResponse(Player player) {
        StringBuffer sb = new StringBuffer("{");
        sb.append("\"type\": \"Location\"");
        sb.append(",");
        sb.append("\"short\": \""+this.shortDescription+"\"");
        sb.append(",");
        sb.append("\"long\": \""+this.longDescription+"\"");
        sb.append(",");
        List<Player> playersWithourPlayer = new ArrayList<>(players);
        playersWithourPlayer.remove(player);
        sb.append("\"players\":[");
        int size = playersWithourPlayer.size();
        if(size==1) {
            sb.append("{\"name\":\""+playersWithourPlayer.get(0).getName()+"\"}");
        } else if(size>1) {
            for(int i=0;i<size-1;i++) {
                sb.append("{\"name\":\""+playersWithourPlayer.get(i).getName()+"\"},");
            }
            sb.append("{\"name\":\""+playersWithourPlayer.get(size-1).getName()+"\"}");
        }
        sb.append("]");
        sb.append(",");
        sb.append("\"exits\":[");
        int noExits = exits.size();
        Set<Exit> exits = this.exits.keySet();
        int i = 0;
        for(Exit e : exits) {
            i++;
            sb.append("{\"exit\":\""+e.exitString+"\"}");
            if(i<size) {
                sb.append(",");
            }
        }
        sb.append("]");
        sb.append("}");
        return sb.toString();
    }

    public boolean doesExitExists(String exit) {
        for(Exit e : exits.keySet()) {
            if(exit.equalsIgnoreCase(e.exitString) || exit.equalsIgnoreCase(e.shortString)) {
               return true;
            }
        }
        return false;
    }

    public Location getNextLocation(String exit) {
        for(Exit e : exits.keySet()) {
            if(exit.equalsIgnoreCase(e.exitString) || exit.equalsIgnoreCase(e.shortString)) {
                return exits.get(e);
            }
        }
        return null;
    }
}
