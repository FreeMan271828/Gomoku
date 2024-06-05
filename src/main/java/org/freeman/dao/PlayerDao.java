package org.freeman.dao;

import org.freeman.object.Player;

import java.util.List;

public interface PlayerDao {

    /**
     * 通过p新增用户
     * @param p 部分用户信息（name）
     * @return 完整用户(id,name,创建时间，修改时间)
     */
    public Player AddPlayer(Player p);

    /**
     * 通过信息获取用户
     * @param p 部分用户信息（name,id,创建时间，修改时间）
     * @return 匹配的用户信息
     */
    public List<Player> GetPlayers(Player p);

}
