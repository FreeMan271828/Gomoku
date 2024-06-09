package org.freeman.dao;

import org.freeman.object.Game;
import org.freeman.object.Player;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface WinnerDao {

    /**
     * 为一场游戏添加赢家
     * @param player 玩家（id必需）
     * @param game 游戏(id必需)
     * @return 是否成功
     */
    boolean AddWinner(Player player,Game game) throws SQLException;

    /**
     * 返回一局游戏的赢家
     * @param gameId 游戏Id
     * @return 赢家
     */
    Player GetWinner(UUID gameId) throws SQLException;

    /**
     * 获取玩家赢的局数
     * @param playerId 玩家信息
     * @return Game集合
     */
    List<Game> GetWinGames(UUID playerId) throws SQLException;
}
