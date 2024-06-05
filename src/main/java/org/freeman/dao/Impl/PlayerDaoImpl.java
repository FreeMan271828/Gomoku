package org.freeman.dao.Impl;

import org.freeman.dao.PlayerDao;
import org.freeman.object.Player;

import java.util.List;

public class PlayerDaoImpl implements PlayerDao {

    @Override
    public Player AddPlayer(Player p) {
        if(p==null){ return null; }
        return null;
    }

    @Override
    public List<Player> GetPlayers(Player p) {
        return List.of();
    }
}
