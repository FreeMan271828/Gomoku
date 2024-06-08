package org.freeman.dao.Impl;

import MyUtils.*;
import org.freeman.dao.BorderDao;
import org.freeman.dao.GameDao;
import org.freeman.dao.PlayerDao;
import org.freeman.object.Border;
import org.freeman.object.Game;
import org.freeman.object.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameDaoImpl implements GameDao {

    private static final MyLog LOG = MyLog.getInstance();

    private static final Connection connection = MyConnnect.getConnection();

    private static final BorderDao borderDao = DependencyContainer.get(BorderDao.class);

    private static final PlayerDao playerDao = DependencyContainer.get(PlayerDao.class);

    @Override
    public Game newGame(Border border, Player player1, Player player2) throws SQLException {
        if(border==null || player1==null || player2==null){ LOG.error("信息不全，新建游戏错误"); return null; }
        String sql = "INSERT INTO game VALUES(?,?,?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, MyUuid.getUuid());          ps.setString(2,border.getId().toString());
        ps.setString(3,player1.getId().toString()); ps.setString(4,player2.getId().toString());
        ps.setString(5, String.valueOf(0));      ps.setString(6, MyDate.getNowInDateTime());
        ps.setString(7, MyDate.getNowInDateTime());
        ps.executeUpdate();

        return null;
    }

    @Override
    public List<Game> GetGames() {
        assert connection!=null;
        String sql = "SELECT * FROM game ORDER BY gmt_modified DESC";
        return getGameBySql(sql);
    }

    @Override
    public List<Game> GetGames(Game game) {
        return List.of();
    }

    private static List<Game> getGameBySql(String sql){
        assert connection != null;
        List<Game> games = new ArrayList<>();
        try (ResultSet rs = connection.prepareStatement(sql).executeQuery()) {
            while (rs.next()) {
                Game game = new Game();
                game.setId(UUID.fromString(rs.getString("id")));
                game.setBorder(borderDao.GetBorder(UUID.fromString(rs.getString("border_id"))));
                game.setPlayer1(playerDao.GetPlayer(UUID.fromString(rs.getString("player1_id"))));
                game.setPlayer2(playerDao.GetPlayer(UUID.fromString(rs.getString("player2_id"))));
                game.setStatus(rs.getInt("status"));
                game.setGmtCreated(rs.getTimestamp("gmt_created"));
                game.setGmtModified(rs.getTimestamp("gmt_modified"));
                games.add(game);
            }
        }catch (Exception e){
            LOG.info("getGameBySql Exception: " + e.getMessage());
            e.fillInStackTrace();
        }
        return games;
    }
}