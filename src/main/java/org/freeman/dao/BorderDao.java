package org.freeman.dao;

import org.freeman.object.Border;

import java.sql.SQLException;
import java.util.List;

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
}
