package org.freeman.dao;

import org.freeman.object.Player;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface PlayerDao {

    /**
     * 通过p新增用户
     * @param p 用户姓名（name）
     * @return 完整用户(id,name,创建时间，修改时间)
     */
    public Player AddPlayer(Player p) throws SQLException;

    /**
     * 获取全部用户
     * @return 返回全部用户
     */
    public List<Player>GetPlayers() throws SQLException;

    /**
     * 根据姓名进行模糊查询
     * @return 返回全部用户
     */
    public List<Player>GetPlayers(String name);

    /**
     * 通过信息获取用户
     * @param p 用户信息（name,id）
     * @return 匹配的用户信息
     */
    public List<Player> GetPlayers(Player p);

}
