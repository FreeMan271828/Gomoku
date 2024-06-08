package org.freeman.dao.Impl;

import myUtils.DependencyContainer;
import myUtils.MyConnnect;
import myUtils.MyDate;
import myUtils.MyLog;
import org.eclipse.swt.internal.win32.LOGBRUSH;
import org.freeman.dao.GameDao;
import org.freeman.dao.PlayerDao;
import org.freeman.dao.WinnerDao;
import org.freeman.object.Game;
import org.freeman.object.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WinnerDaoImpl implements WinnerDao {

    private final Connection connection = MyConnnect.getConnection();

    private static final MyLog LOG = MyLog.getInstance();

    @Override
    public boolean AddWinner(Player player, Game game) throws SQLException {
        assert connection != null;
        if(player == null || game == null){
            LOG.error("信息不全");
            return false;
        }
        String sql = "insert into winner values(?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, player.getId().toString());
        ps.setString(2, game.getId().toString());
        ps.setString(3, MyDate.getNowInDateTime());
        ps.setString(4, MyDate.getNowInDateTime());
        int affectedRow = ps.executeUpdate();
        return affectedRow > 0;
    }

    @Override
    public Player GetWinner(UUID gameId) throws SQLException {
        assert connection != null;
        PlayerDao playerDao = DependencyContainer.get(PlayerDao.class);
        String sql = "select * from winner where game_id=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, String.valueOf(gameId));
        ResultSet rs = ps.executeQuery();
        List<Player> players = new ArrayList<>();
        while (rs.next()) {
            String playerId = rs.getString("player_id");
            players.add(playerDao.GetPlayer(UUID.fromString(playerId)));
        }
        return players.getFirst();
    }

    @Override
    public List<Game> GetWinGames(UUID playerId) throws SQLException {
        assert connection != null;
        GameDao gameDao = DependencyContainer.get(GameDao.class);
        String sql = "select * from winner where player_id=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, String.valueOf(playerId));
        ResultSet rs = ps.executeQuery();
        List<Game> games = new ArrayList<Game>();
        while (rs.next()) {
            String gameId = rs.getString("game_id");
            games.add(gameDao.GetGame(UUID.fromString(gameId)));
        }
        return games;
    }
}
