package org.freeman.dao;

import org.freeman.object.Player;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface PlayerDao {

    /**
     * 通过p新增用户
     * @param p 用户姓名（name）
     * @return 完整用户(id,name,创建时间，修改时间)
     */
    Player AddPlayer(Player p) throws SQLException;

    /**
     * 获取全部用户
     * @return 返回全部用户
     */
    List<Player>GetPlayers() throws SQLException;

    /**
     * 根据姓名进行模糊查询
     * @return 返回全部用户
     */
    List<Player>GetPlayers(String name);

    /**
     * 通过信息获取用户
     * @param p 用户信息（name,id）
     * @return 匹配的用户信息
     */
    List<Player> GetPlayers(Player p);

    /**
     * 根据id获取用户
     * @param id 用户id
     * @return 用户实例
     */
    Player GetPlayer(UUID id);
}
