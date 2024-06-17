package org.freeman.service;
import Factory.DaoFactory;
import lombok.Data;
import myUtils.MyUuid;
import org.freeman.dao.*;
import org.freeman.object.Border;
import org.freeman.object.Cell;
import org.freeman.object.Game;
import org.freeman.object.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class GameService {

    private Game currentGame = new Game();
    private Player winner = new Player();
    private int[][] allChess ;  // 棋盘，0表示空位置
    private boolean isBlack = true;  // 当前是否轮到黑方
    private boolean canPlay = true;  // 游戏是否可以继续
    private String message = "黑方先行";  // 游戏状态信息
    private int maxTime = 0;  // 每方的最大下棋时间
    private int blackTime = 0;  // 黑方剩余时间
    private int whiteTime = 0;  // 白方剩余时间
    private String blackMessage = "无限制";  // 黑方时间信息
    private String whiteMessage = "无限制";  // 白方时间信息
    private List<Cell> registerCells = new ArrayList<>();
    private final BorderDao borderDao = DaoFactory.createDao(BorderDao.class);
    private final GameDao gameDao = DaoFactory.createDao(GameDao.class);
    private final PlayerDao playerDao = DaoFactory.createDao(PlayerDao.class);
    private final CellDao cellDao = DaoFactory.createDao(CellDao.class);
    private final WinnerDao winnerDao = DaoFactory.createDao(WinnerDao.class);

    // 选择对局玩家
    public void setPlayerIdToGame(UUID player1Id, UUID player2Id){
        Player player1 = playerDao.GetPlayer(player1Id);
        Player player2 = playerDao.GetPlayer(player2Id);
        this.currentGame.setPlayer1(player1);
        this.currentGame.setPlayer2(player2);
    }
    //初始化游戏棋盘
    public void setBorderToGame(Border currentBorder){
        this.currentGame.setBorder(currentBorder);
        int[][] chessAll = new int[currentBorder.getWidth()][currentBorder.getLength()];
        this.allChess = chessAll;
    }
    //分配uuid
    public void setUUID(){
        this.currentGame.setId(UUID.fromString(MyUuid.getUuid()));
    }

    //落子功能
//    public void putChess(int x,int y,boolean isBlack){
//        if(isBlack){
//            //黑子为1
//            allChess[x][y] = 1;
//        }else{
//            //白子为2
//            allChess[x][y] = 1;
//        }
//    }

    //悔棋，玩家可以在本方回合结束之前任意悔棋，在确认结束本回合后将无法悔棋
    public void regretChess(int x,int y){
        allChess[x][y] = 0;
    }
    //保存棋子缓存记录,在确认确认结束本方回合，进入对方回合时调用，cell在control层实例化
    public void registerChess(Cell cell) throws SQLException {
       this.registerCells.add(cell);
    }


    // 检查是否胜利，在保存棋子记录后调用
    public boolean checkWin(int x, int y) {
        int color = allChess[x][y];  // 获取当前位置的棋子颜色
        // 检查四个方向是否有五个连续的相同颜色的棋子
        if (checkCount(x, y, 1, 0, color) >= 5 ||
                checkCount(x, y, 0, 1, color) >= 5 ||
                checkCount(x, y, 1, -1, color) >= 5 ||
                checkCount(x, y, 1, 1, color) >= 5) {
            return true;
        }
        return false;
    }


    // 辅助方法，用于计算指定方向上连续相同颜色的棋子数
    private int checkCount(int x, int y, int xChange, int yChange, int color) {
        int count = 1;  // 计数器

        // 检查正方向
        int tempX = x + xChange;
        int tempY = y + yChange;
        while (isValidPosition(tempX, tempY) && color == allChess[tempX][tempY]) {
            count++;
            tempX += xChange;
            tempY += yChange;
        }

        // 检查反方向
        tempX = x - xChange;
        tempY = y - yChange;
        while (isValidPosition(tempX, tempY) && color == allChess[tempX][tempY]) {
            count++;
            tempX -= xChange;
            tempY -= yChange;
        }

        return count;
    }

    // 检查坐标是否在棋盘范围内
    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < allChess.length && y >= 0 && y < allChess[0].length;
    }

    // 重置游戏状态，单独实现一个按扭
    public void resetGame() {
        for (int i = 1; i <= 14; i++) {
            for (int j = 1; j <= 14; j++) {
                allChess[i][j] = 0;  // 清空棋盘
            }
        }
        message = "黑方先行";  // 重置游戏状态信息
        isBlack = true;  // 设置为黑方先行
        blackTime = maxTime;  // 重置黑方时间
        whiteTime = maxTime;  // 重置白方时间
        blackMessage = maxTime > 0 ? formatTime(maxTime) : "无限制";  // 更新黑方时间信息
        whiteMessage = maxTime > 0 ? formatTime(maxTime) : "无限制";  // 更新白方时间信息
        canPlay = true;  // 设置游戏可以继续
        winner = new Player();
        registerCells = new ArrayList<>();
    }

    //在决出胜负后，同保存缓存记录一块调用
    public void setWinner(Player player) throws SQLException {
        winner = player;
    }
    // 将时间格式化为 "小时:分钟:秒" 的字符串
    private String formatTime(int time) {
        return time / 3600 + ":" + (time / 60 - time / 3600 * 60) + ":" + (time - time / 60 * 60);
    }

    //保存所有缓存记录，确认保存时调用，一般是一方胜利或者主动结束游戏。
    public void saveAllResigter() throws SQLException {
        currentGame.setBorder(borderDao.AddBorder(currentGame.getBorder()));
        currentGame = gameDao.newGame(currentGame.getBorder(), currentGame.getPlayer1(),currentGame.getPlayer2());
        for (Cell registerCell : registerCells) {
            cellDao.AddCell(registerCell);
        }
        winnerDao.AddWinner(winner,currentGame);
    }

}

