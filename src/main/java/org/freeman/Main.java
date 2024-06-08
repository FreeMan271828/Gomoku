package org.freeman;

import launcher.RegisterDC;
import myUtils.DependencyContainer;
import org.freeman.dao.*;
import org.freeman.object.Border;
import org.freeman.object.Game;
import org.freeman.object.Player;

import java.util.UUID;


public class Main {

    private static BorderDao borderDao;

    private static PlayerDao playerDao;

    private static GameDao gameDao;

    private static WinnerDao winnerDao;

    public static void main(String[] args) throws Exception {
        init();
        Game game = gameDao.GetGame(UUID.fromString("52074090-cf6d-4525-9cfd-0868b7e642d2"));
        Player player = playerDao.GetPlayer(UUID.fromString("7ad4c86d-4c57-4cd5-99f3-2d67b04b99ad"));
        System.out.println(winnerDao.GetWinner(UUID.fromString("52074090-cf6d-4525-9cfd-0868b7e642d2")));
    }

    public static void init(){
        borderDao = DependencyContainer.get(BorderDao.class);
        playerDao = DependencyContainer.get(PlayerDao.class);
        gameDao = DependencyContainer.get(GameDao.class);
        winnerDao = DependencyContainer.get(WinnerDao.class);
    }

    static {
        try {
            RegisterDC.register();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}