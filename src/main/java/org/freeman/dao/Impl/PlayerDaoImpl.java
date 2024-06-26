package org.freeman.dao.Impl;

import myUtils.MyDate;
import myUtils.MyLog;
import myUtils.MyUuid;
import myUtils.MyConnnect;
import org.freeman.dao.PlayerDao;
import org.freeman.object.Player;

import java.sql.*;
import java.util.*;

public class PlayerDaoImpl implements PlayerDao {

    //数据库连接类
    private final Connection connection = MyConnnect.getConnection();

    //注入日志类
    private final MyLog LOG = MyLog.getInstance();

    @Override
    public Player AddPlayer(Player p) throws SQLException {
        if(p==null){
            LOG.error("用户名称为空，dao层创建玩家失败");return null;}
        else if(connection==null){
            LOG.error("连接失败，请重新连接");}
        else{
            String sql = "INSERT INTO PLAYER VALUES(?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, MyUuid.getUuid());
            stmt.setString(2, p.getName());
            stmt.setString(3, MyDate.getNowInDateTime());
            stmt.setString(4, MyDate.getNowInDateTime());
            int affectedRow = stmt.executeUpdate();
            if(affectedRow>0){
                return GetPlayers(p).getFirst();
            }
        }
        return null;
    }

    @Override
    public List<Player> GetPlayers() {
        assert connection != null;
        String sql = "select * from player";
        return getPlayerBySql(sql);
    }

    @Override
    public List<Player> GetPlayers(String name) {
        assert connection != null;
        String sql = "select * from player where name like '%"+name+"%'";
        return getPlayerBySql(sql);
    }

    @Override
    public List<Player> GetPlayers(Player p) {
        StringBuilder sb = new StringBuilder("SELECT * FROM Player WHERE 1=1");
        if (p.getId() != null) {
            sb.append(" AND id = '").append(p.getId()).append("'");
        }
        if (p.getName() != null) {
            sb.append(" AND name = '").append(p.getName()).append("'");
        }
        BaseMethod.SetTimeParam(sb, p.getGmtCreated(), p.getGmtModified());
        String sql = sb.toString();
        List<Player> players = getPlayerBySql(sql);
        if (players.isEmpty()) { LOG.error("查询失败"); return null;}
        return players;
    }

    @Override
    public Player GetPlayer(UUID id) {
        assert connection != null;
        if(id == null){ LOG.error("未找到参数"); return null;}
        String sql = "select * from player where id = '"+id+"'";
        return getPlayerBySql(sql).getFirst();
    }

    /**
     * 基本方法
     * 根据sql查询语句返回对应的User
     */
    private List<Player> getPlayerBySql(String sql){
        List<Player> players = new ArrayList<>();
        assert connection != null;
        try (ResultSet rs = connection.prepareStatement(sql).executeQuery()) {
            while (rs.next()) {
                Player player = new Player();
                player.setId(UUID.fromString(rs.getString("id")));
                player.setName(rs.getString("name"));
                player.setGmtCreated(rs.getTimestamp("gmt_created"));
                player.setGmtModified(rs.getTimestamp("gmt_modified"));
                players.add(player);
            }
        }catch (Exception e){
            LOG.info("getPlayerBySql Exception: " + e.getMessage());
            e.fillInStackTrace();
        }
        return players;
    }

}
