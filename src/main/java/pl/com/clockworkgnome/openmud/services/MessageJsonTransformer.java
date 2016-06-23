package pl.com.clockworkgnome.openmud.services;


import org.springframework.stereotype.Service;
import pl.com.clockworkgnome.openmud.domain.Exit;
import pl.com.clockworkgnome.openmud.domain.Location;
import pl.com.clockworkgnome.openmud.domain.Player;
import pl.com.clockworkgnome.openmud.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.Collection;
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
        addCollectionValue(sb,"players",playersWithourPlayer,"name","name");
        next(sb);
        addCollectionValue(sb,"exits",currentLocation.getExits().keySet(),"exit","exitString");
        return wrap(sb);
    }

    private void addCollectionValue(StringBuffer sb, String key, Collection valuesCollection, String collectionElementKeyValue, String collectionElementParamName) {
        sb.append("\""+key+"\":[");

        int size = valuesCollection.size();
        int i = 0;
        for(Object o : valuesCollection) {
            i++;
            sb.append("{\""+collectionElementKeyValue+"\":\""+String.valueOf(ReflectionUtils.runGetter(collectionElementParamName,o))+"\"}");
            if(i<size) {
                sb.append(",");
            }
        }
        sb.append("]");
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
