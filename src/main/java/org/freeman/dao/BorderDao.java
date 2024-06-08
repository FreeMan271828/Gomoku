package org.freeman.dao;

import org.freeman.dao.Impl.BorderDaoImpl;
import org.freeman.object.Border;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface BorderDao {
    /**
     * 创建一个新的棋盘
     * @param border 要求输入长度宽度
     * @return 返回棋盘实例（id，长，宽，创建时间，修改时间）
     */
    public Border AddBorder(Border border) throws SQLException;

    /**
     * 获取目前的所有棋盘
     * @return 所有的棋盘
     */
    public List<Border> GetBorders();

    /**
     * 获取符合条件的棋盘
     * @param border 棋盘条件，包括长，宽等
     * @return 符合的棋盘条件
     */
    public List<Border> GetBorders(Border border);

    /**
     * 根据棋盘的长宽获取棋盘
     * @param length 棋盘的长
     * @param width 棋盘的宽
     * @return 棋盘信息
     */
    public List<Border> GetBorders(int length,int width) throws SQLException;

    /**
     * 根据id 获取棋盘
     * @param id 棋盘的id
     * @return 棋盘实例
     */
    public Border GetBorder(UUID id);
}
