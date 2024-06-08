package org.freeman;

import MyUtils.DependencyContainer;
import org.freeman.dao.BorderDao;
import org.freeman.dao.GameDao;
import org.freeman.dao.Impl.BorderDaoImpl;
import org.freeman.dao.Impl.GameDaoImpl;
import org.freeman.dao.Impl.PlayerDaoImpl;
import org.freeman.dao.PlayerDao;
import org.freeman.object.Border;
import org.freeman.object.Player;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        registerDC();

        BorderDao borderDao = DependencyContainer.get(BorderDao.class);
        PlayerDao playerDao = DependencyContainer.get(PlayerDao.class);
        GameDao gameDao = DependencyContainer.get(GameDao.class);

        Border border = borderDao.GetBorders(6,6).getFirst();
        Player player1 = playerDao.GetPlayers("player1").getFirst();
        Player player2 = playerDao.GetPlayers("player2").getFirst();
        gameDao.newGame(border, player1, player2);

    }

    public static void registerDC(){
        DependencyContainer.register(BorderDao.class,new BorderDaoImpl());
        DependencyContainer.register(GameDao.class,new GameDaoImpl());
        DependencyContainer.register(PlayerDao.class,new PlayerDaoImpl());
    }
}