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
        StringBuffer sb = new StringBuffer("");
        addKeyValue(sb, "type","Location");
        next(sb);
        addKeyValue(sb, "short",currentLocation.getShortDescription());
        next(sb);
        addKeyValue(sb, "long",currentLocation.getLongDescription());
        next(sb);

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

        next(sb);

        sb.append("\"exits\":[");
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

        return wrap(sb);
    }

    public String getSayResponse(String text) {
        StringBuffer sb = new StringBuffer("");
        addKeyValue(sb, "type","Say");
        next(sb);
        addKeyValue(sb,"message","You say: "+text);
        return wrap(sb);
    }

    public String getSayOtherResponse(String text, String playerName) {
        StringBuffer sb = new StringBuffer("");
        addKeyValue(sb, "type","Say");
        next(sb);
        addKeyValue(sb,"message",playerName + " says: "+ text);
        return wrap(sb);
    }

    public String playerLeaves(String name, String where) {
        StringBuffer sb = new StringBuffer("");
        addKeyValue(sb,"type","Leaves");
        next(sb);
        addKeyValue(sb,"message",name+" goes "+where);
        return wrap(sb);
    }

    private void next(StringBuffer sb) {
        sb.append(",");
    }

    private String wrap(StringBuffer sb) {
        return "{"+sb.toString()+"}";
    }

    private void addKeyValue(StringBuffer sb, String key, String value) {
        sb.append("\""+key+"\":\""+value+"\"");
    }
}
