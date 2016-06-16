package pl.com.clockworkgnome.openmud.services;


import org.springframework.stereotype.Service;
import pl.com.clockworkgnome.openmud.domain.Exit;
import pl.com.clockworkgnome.openmud.domain.Location;
import pl.com.clockworkgnome.openmud.domain.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class MessageJsonTransformer {


    public String locationLook(Player player) {
        final Location currentLocation = player.getCurrentLocation();
        StringBuffer sb = new StringBuffer("{");
        sb.append("\"type\": \"Location\"");
        sb.append(",");
        sb.append("\"short\": \""+currentLocation.getShortDescription()+"\"");
        sb.append(",");
        sb.append("\"long\": \""+currentLocation.getLongDescription()+"\"");
        sb.append(",");
        List<Player> playersWithourPlayer = new ArrayList<>(currentLocation.getPlayers());
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
        int noExits = currentLocation.getExits().size();
        Set<Exit> exits = currentLocation.getExits().keySet();
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

    public String getSayResponse(String text) {
        StringBuffer sb = new StringBuffer("{");
        sb.append("\"type\": \"Say\"");
        sb.append(",");
        sb.append("\"message\": \"You say: "+text+"\"");
        sb.append("}");
        return sb.toString();
    }

    public String getSayOtherResponse(String text, String playerName) {
        StringBuffer sb = new StringBuffer("{");
        sb.append("\"type\": \"Say\"");
        sb.append(",");
        sb.append("\"message\":\""+ playerName + " says: "+ text +"\"");
        sb.append("}");
        return sb.toString();
    }

    public String playerLeaves(String name, String where) {
        StringBuffer sb = new StringBuffer("{");
        sb.append("\"type\": \"Leaves\"");
        sb.append(",");
        sb.append("\"message\":\""+ name + " goes " + where + "\"");
        sb.append("}");
        return sb.toString();
    }
}
