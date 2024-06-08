package org.freeman;

import launcher.RegisterDC;
import myUtils.DependencyContainer;
import org.freeman.dao.BorderDao;
import org.freeman.dao.CellDao;
import org.freeman.dao.GameDao;
import org.freeman.dao.PlayerDao;
import org.freeman.object.Border;
import org.freeman.object.Game;
import org.freeman.object.Player;


public class Main {

    private static final BorderDao borderDao;

    private static final PlayerDao playerDao;

    private static final GameDao gameDao;

    public static void main(String[] args) throws Exception {
        Border border = borderDao.GetBorders(6,6).getFirst();
        Player player1 = playerDao.GetPlayers("player1").getFirst();
        Player player2 = playerDao.GetPlayers("player2").getFirst();

        Game game = gameDao.GetGames(border.getId().toString(),player1.getId().toString(),player2.getId().toString()).getFirst();
        System.out.println(game.getId());
    }

    static {
        try {
            RegisterDC.register();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        borderDao = DependencyContainer.get(BorderDao.class);
        playerDao = DependencyContainer.get(PlayerDao.class);
        gameDao = DependencyContainer.get(GameDao.class);
    }
}