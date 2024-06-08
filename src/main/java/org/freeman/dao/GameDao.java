package org.freeman.dao;

import org.freeman.object.Border;
import org.freeman.object.Game;
import org.freeman.object.Player;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface GameDao {

    /**
     * 新建游戏
     * @param border 游戏使用的棋盘
     * @param player1 游戏玩家1
     * @param player2 游戏玩家2
     * @return 新建的棋盘实例
     */
    Game newGame(Border border, Player player1,Player player2) throws SQLException;

    /**
     * 获取全部的游戏
     * @return 获取游戏
     */
    List<Game> GetGames();

    /**
     * 根据id查询游戏
     * @param id 游戏的id
     * @return 游戏实例
     */
    Game GetGame(UUID id);

    /**
     * 根据game查询游戏
     * @param game 查询细腻系
     * @return Game实例
     */
    List<Game>GetGames(Game game);

    /**
     * 根据borderId playerId查询游戏
     * @param borderId 棋盘Id
     * @param player1Id 玩家1Id
     * @param player2Id 玩家2Id
     * @return 游戏实例
     */
    List<Game>GetGames(String borderId, String player1Id, String player2Id);
}
