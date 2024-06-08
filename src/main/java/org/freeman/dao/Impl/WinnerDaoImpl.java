package org.freeman.dao.Impl;

import myUtils.MyConnnect;
import org.freeman.dao.WinnerDao;
import org.freeman.object.Game;
import org.freeman.object.Player;

import java.sql.Connection;
import java.util.List;

public class WinnerDaoImpl implements WinnerDao {

    private final Connection connection = MyConnnect.getConnection();

    @Override
    public boolean AddWinner(Player player, Game game) {

        return false;
    }

    @Override
    public Player GetWinner(String gameId) {
        return null;
    }

    @Override
    public List<Game> GetWinGames(Player player) {
        return List.of();
    }
}
