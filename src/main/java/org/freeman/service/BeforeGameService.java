package org.freeman.service;

import Factory.DaoFactory;
import lombok.Data;
import org.freeman.dao.BorderDao;
import org.freeman.dao.GameDao;
import org.freeman.dao.PlayerDao;
import org.freeman.dao.WinnerDao;
import org.freeman.object.Border;
import org.freeman.object.Game;
import org.freeman.object.Player;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


@Data
public class BeforeGameService {

    private final BorderDao borderDao = DaoFactory.createDao(BorderDao.class);
    private final GameDao gameDao = DaoFactory.createDao(GameDao.class);
    private final PlayerDao playerDao = DaoFactory.createDao(PlayerDao.class);
    private final WinnerDao winnerDao = DaoFactory.createDao(WinnerDao.class);
    private int BorderWidth;
    private int BorderHeight;
    private List<Player> allPlayers;
    private List<Game> allGames;
    private List<Border> allBorders;
    private Border currentBorder;
    private Map<UUID,List<Game>> playersGames;
    //获取所有棋盘
    public void get_allBorders(){
        this.allBorders = borderDao.GetBorders();
    }

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

    public Player get_winner(Game game) throws SQLException {
        Player player = winnerDao.GetWinner(game.getId());
        return  player;
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

    //根据玩家id获取这该玩家所赢的局数
    public List<Game> getWinGames(Player player) throws SQLException {
        List<Game> winGames = winnerDao.GetWinGames(player.getId());
        return  winGames;
    }

    //根据游戏获取游戏赢家
    public Player getWinner(Game game) throws SQLException {
        Player player = winnerDao.GetWinner(game.getId());
        return player;
    }

}
