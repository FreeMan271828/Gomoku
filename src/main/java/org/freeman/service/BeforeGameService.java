package org.freeman.service;

import Factory.DaoFactory;
import Factory.DaoFactoryImpl;
import org.freeman.dao.BorderDao;
import org.freeman.dao.GameDao;
import org.freeman.dao.PlayerDao;
import org.freeman.object.Border;
import org.freeman.object.Game;
import org.freeman.object.Player;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;



public class BeforeGameService {

    private final DaoFactory daoFactory = new DaoFactoryImpl();
    private final BorderDao borderDao = daoFactory.createDao(BorderDao.class);
    private final GameDao gameDao = daoFactory.createDao(GameDao.class);
    private final PlayerDao playerDao = daoFactory.createDao(PlayerDao.class);
    private int BorderWidth;
    private int BorderHeight;
    private List<Player> allPlayers;
    private List<Game> allGames;
    private List<Border> allBorders;
    private Border currentBorder;
    private Map<UUID,List<Game>> playersGames;
    //获取所有棋盘
    public void get_allBorders(){
        List<Border> allBorder = borderDao.GetBorders();
        this.allBorders = allBorder;
    }

    //设置棋盘大小
    public void setSize(int width,int height){
        this.BorderWidth = width;
        this.BorderHeight = height;
    }

    //清空棋盘大小,返回时调用
    public void clearSize(){
        this.BorderHeight = 0;
        this.BorderWidth = 0;
    }

    //选择当下已有的棋盘
    //默认值为0，在setSize()后调用
    public void chooseBorder() throws SQLException {
        Border border = borderDao.GetBorders(this.BorderHeight,this.BorderWidth).getFirst();
        this.currentBorder = border;
    }

    //获取玩家信息
    public void  get_AllPlayers() throws SQLException {
        List<Player> players = playerDao.GetPlayers();
        this.allPlayers = players;
    }
    public void get_AllGames(){
        List<Game> games = gameDao.GetGames();
        this.allGames = games;
    }

    //根据id获取玩家参与的所有的游戏信息,渲染玩家列表调用
    public void get_PlayersGames(List<Player> allPlayers, List<Game> allGames){

            Map<UUID, List<Game>> playerGamesMap = new HashMap<>();

            for (Player player : allPlayers) {
                UUID playerId = player.getId();
                List<Game> playerGames = allGames.stream()
                        .filter(game -> playerId.equals(game.getPlayer1()) || playerId.equals(game.getPlayer2()))
                        .collect(Collectors.toList());
                playerGamesMap.put(playerId, playerGames);
            }

            this.playersGames = playerGamesMap;
        }

    //根据



}
