package org.freeman.dao;

import org.freeman.object.Border;
import org.freeman.object.Game;
import org.freeman.object.Player;

import java.sql.SQLException;
import java.util.List;

public interface GameDao {

    /**
     * 新建游戏
     * @param border
     * @param player1
     * @param player2
     * @return
     * @throws SQLException
     */
    public Game newGame(Border border, Player player1,Player player2) throws SQLException;

    public List<Game> GetGames();

    public List<Game>GetGames(Game game);
}
