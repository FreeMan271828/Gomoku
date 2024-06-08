package org.freeman.dao.Impl;

import myUtils.*;
import org.freeman.dao.CellDao;
import org.freeman.dao.GameDao;
import org.freeman.dao.PlayerDao;
import org.freeman.object.Cell;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CellDaoImpl implements CellDao {

    private static final MyLog LOG = MyLog.getInstance();

    private static final Connection connection = MyConnnect.getConnection();

    private static final PlayerDao playerDao = DependencyContainer.get(PlayerDao.class);

    private static final GameDao gameDao = DependencyContainer.get(GameDao.class);

    @Override
    public Cell AddCell(Cell cell) throws SQLException {
        assert connection != null;
        if(cell.getGame()==null) { LOG.error("棋子未指定游戏"); return null; }
        if(cell.getPlayer()==null) {LOG.error("棋子未指定玩家"); return null; }
        if (cell.getX()==null) {LOG.error("棋子未指定横坐标"); return null;}
        if(cell.getY()==null) { LOG.error("棋子未指定纵坐标"); return null; }

        String sql = String.format("INSERT INTO CELL VALUES(" +
                "'%s','%s','%d','%d','%s','%s');",
                MyUuid.getUuid(),cell.getGame().getId(),
                cell.getX(),cell.getY(), MyDate.getNowInDateTime(),MyDate.getNowInDateTime());
        int affectedRow = connection.createStatement().executeUpdate(sql);

        return null;
    }

    @Override
    public List<Cell> GetCells(String game_id) {
        assert connection != null;
        if(game_id.isEmpty()) { LOG.error("gameId未发现");return null;}
        String sql = String.format("SELECT * FROM CELL WHERE game_id='%s';", game_id);
        return getCellBySql(sql);
    }

    @Override
    public List<Cell> GetCells(String game_id, String player_id) {
        assert connection != null;
        if(game_id.isEmpty()) { LOG.error("gameId未发现");return null;}
        String sql = String.format("SELECT * FROM CELL WHERE game_id='%s' AND player_id='%s';", game_id, player_id);
        return getCellBySql(sql);
    }

    private static List<Cell> getCellBySql(String sql){
        assert connection != null;
        List<Cell> cells = new ArrayList<>();
        try (ResultSet rs = connection.prepareStatement(sql).executeQuery()) {
            while (rs.next()) {
                Cell cell = new Cell();
                cell.setPlayer(playerDao.GetPlayer(UUID.fromString(rs.getString("player_id"))));
                cell.setGame(gameDao.GetGame(UUID.fromString(rs.getString("game_id"))));
                cell.setX(rs.getInt("x"));
                cell.setY(rs.getInt("y"));
                cell.setGmtCreated(rs.getTimestamp("gmt_created"));
                cell.setGmtModified(rs.getTimestamp("gmt_modified"));
                cells.add(cell);
            }
        }catch (Exception e){
            LOG.info("getGameBySql Exception: " + e.getMessage());
            e.fillInStackTrace();
        }
        return cells;
    }
}
