package org.freeman;

import Factory.DaoFactory;
import Factory.DaoFactoryImpl;
import org.freeman.dao.*;
import org.freeman.object.Game;
import org.freeman.object.Player;

import java.util.UUID;


public class Main {

    private static final DaoFactory daoFactory = new DaoFactoryImpl();
    private final BorderDao borderDao = daoFactory.createDao(BorderDao.class);
    private static final PlayerDao playerDao = daoFactory.createDao(PlayerDao.class);
    private static final GameDao gameDao = daoFactory.createDao(GameDao.class);
    private static final WinnerDao winnerDao = daoFactory.createDao(WinnerDao.class);

    public static void main(String[] args) throws Exception {
        Game game = gameDao.GetGame(UUID.fromString("52074090-cf6d-4525-9cfd-0868b7e642d2"));
        Player player = playerDao.GetPlayer(UUID.fromString("7ad4c86d-4c57-4cd5-99f3-2d67b04b99ad"));
        System.out.println(winnerDao.GetWinner(UUID.fromString("52074090-cf6d-4525-9cfd-0868b7e642d2")));
    }
}