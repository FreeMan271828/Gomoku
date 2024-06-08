package org.freeman.dao.Impl;

import MyUtils.MyConnnect;
import MyUtils.MyDate;
import MyUtils.MyLog;
import MyUtils.MyUuid;
import org.freeman.dao.GameDao;
import org.freeman.object.Border;
import org.freeman.object.Game;
import org.freeman.object.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class GameDaoImpl implements GameDao {

    private static final MyLog LOG = MyLog.getInstance();

    private static final Connection coon = MyConnnect.getConnection();

    @Override
    public Game newGame(Border border, Player player1, Player player2) throws SQLException {
        if(border==null || player1==null || player2==null){ LOG.error("信息不全，新建游戏错误"); return null; }
        String sql = "INSERT INTO game VALUES(?,?,?,?,?,?,?)";
        PreparedStatement ps = coon.prepareStatement(sql);
        ps.setString(1, MyUuid.getUuid());          ps.setString(2,border.getId().toString());
        ps.setString(3,player1.getId().toString()); ps.setString(4,player2.getId().toString());
        ps.setString(5, String.valueOf(0));      ps.setString(6, MyDate.getNowInDateTime());
        ps.setString(7, MyDate.getNowInDateTime());
        ps.executeUpdate();

        return null;
    }

    @Override
    public List<Game> GetGames() {
        return List.of();
    }

    @Override
    public List<Game> GetGames(Game game) {
        return List.of();
    }
}
