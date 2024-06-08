package org.freeman.dao;

import org.freeman.object.Cell;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface CellDao {

    /**
     * 添加棋子类
     * @param cell 棋子（包括player_id,game_id,x,y）
     * @return 棋子实例
     */
    Cell AddCell(Cell cell) throws SQLException;

    /**
     * 获取一局游戏中的全部棋子
     * @param game_id 游戏id
     * @return 棋子集合
     */
    List<Cell>GetCells(String game_id);

    /**
     * 获取一局游戏中一个玩家的全部棋子
     * @param game_id 游戏id
     * @param player_id 玩家id
     * @return 棋子集合
     */
    List<Cell>GetCells(String game_id,String player_id);
}