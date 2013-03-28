package dominoes.gui;

import dominoes.gui.proxy.PlayerProxy;
import dominoes.players.DominoPlayer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Player builder
 *
 * @author Hisham Khalifa
 */
public class PlayerBuilder {

    public static final String LOCAL_PLAYER = "dominoes.players.LocalPlayer";
    public static final String ADVANCED_AI_PLAYER = "dominoes.players.AIPlayer";
    public static final String SIMPLE_AI_PLAYER = "dominoes.players.RuleBaseAIPlayer";

    static PlayerProxy BuildPlayer(String typeString, DominoUIImp delegate) {
        Class classForPlayer;
        PlayerProxy playerProxy = null;

        try {
            classForPlayer = Class.forName(typeString);
            try {

                playerProxy = new PlayerProxy((DominoPlayer) classForPlayer.newInstance());
                playerProxy.setIsHuman(playerIsHumanType(typeString));
                playerProxy.setControl(delegate);

            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(PlayerBuilder.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PlayerBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }

        return playerProxy;
    }
    
    
    static boolean playerIsHumanType (String typeString) {
        if (typeString.equals(LOCAL_PLAYER)) {
            return true;
        } else {
            return false;
        }
    }
}
