package org.freeman.service;



public class GameService {
    private int[][] allChess = new int[16][16];  // 棋盘，0表示空位置
    private boolean isBlack = true;  // 当前是否轮到黑方
    private boolean canPlay = true;  // 游戏是否可以继续
    private String message = "黑方先行";  // 游戏状态信息
    private int maxTime = 0;  // 每方的最大下棋时间
    private int blackTime = 0;  // 黑方剩余时间
    private int whiteTime = 0;  // 白方剩余时间
    private String blackMessage = "无限制";  // 黑方时间信息
    private String whiteMessage = "无限制";  // 白方时间信息

    // 获取棋盘状态
    public int[][] getAllChess() {
        return allChess;
    }

    // 获取当前是否轮到黑方
    public boolean isBlack() {
        return isBlack;
    }

    // 设置当前是否轮到黑方
    public void setBlack(boolean isBlack) {
        this.isBlack = isBlack;
    }

    // 获取游戏是否可以继续
    public boolean isCanPlay() {
        return canPlay;
    }

    // 设置游戏是否可以继续
    public void setCanPlay(boolean canPlay) {
        this.canPlay = canPlay;
    }

    // 获取游戏状态信息
    public String getMessage() {
        return message;
    }

    // 获取黑方时间信息
    public String getBlackMessage() {
        return blackMessage;
    }

    // 获取白方时间信息
    public String getWhiteMessage() {
        return whiteMessage;
    }

    // 获取每方的最大下棋时间
    public int getMaxTime() {
        return maxTime;
    }

    // 设置每方的最大下棋时间
    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }

    // 获取黑方剩余时间
    public int getBlackTime() {
        return blackTime;
    }

    // 设置黑方剩余时间
    public void setBlackTime(int blackTime) {
        this.blackTime = blackTime;
    }

    // 获取白方剩余时间
    public int getWhiteTime() {
        return whiteTime;
    }

    // 设置白方剩余时间
    public void setWhiteTime(int whiteTime) {
        this.whiteTime = whiteTime;
    }

    // 检查是否胜利
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
        int tempX = xChange, tempY = yChange;
        // 检查正方向
        while (color == allChess[x + xChange][y + yChange]) {
            count++;
            if (xChange != 0) xChange++;
            if (yChange != 0) yChange = yChange > 0 ? yChange + 1 : yChange - 1;
        }
        xChange = tempX; yChange = tempY;
        // 检查反方向
        while (color == allChess[x - xChange][y - yChange]) {
            count++;
            if (xChange != 0) xChange++;
            if (yChange != 0) yChange = yChange > 0 ? yChange + 1 : yChange - 1;
        }
        return count;
    }

    // 重置游戏状态
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
    }

    // 将时间格式化为 "小时:分钟:秒" 的字符串
    private String formatTime(int time) {
        return time / 3600 + ":" + (time / 60 - time / 3600 * 60) + ":" + (time - time / 60 * 60);
    }
}

