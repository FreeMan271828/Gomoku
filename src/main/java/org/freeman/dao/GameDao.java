package org.freeman.dao;

import org.freeman.object.Border;
import org.freeman.object.Game;
import org.freeman.object.Player;

import java.sql.SQLException;
import java.util.List;

public interface GameDao {

    /**
     * 新建游戏
     * @param border 游戏使用的棋盘
     * @param player1 游戏玩家1
     * @param player2 游戏玩家2
     * @return 新建的棋盘实例
     */
    public Game newGame(Border border, Player player1,Player player2) throws SQLException;

    /**
     * 获取全部的游戏
     * @return 获取游戏
     */
    public List<Game> GetGames();

    /**
     * 根据game查询游戏
     * @param game
     * @return
     */
    public List<Game>GetGames(Game game);
}
