package org.freeman.control;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.freeman.object.Cell;
import org.freeman.object.Game;
import org.freeman.object.Player;
import org.freeman.service.*;

import java.sql.SQLException;

public class AIChessFrame {

    private final Display display;
    private final Shell shell;
    private final BeforeGameService beforeGameService;
    private final GameService gameService;
    private final  GomokuAI aiService;  // 添加 AIService
    private boolean isBlack = true;
    private boolean canPlay = true;
    private String message = "黑方先行";
    private int maxTime = 0;
    private int blackTime = 0;
    private int whiteTime = 0;
    private String blackMessage = "无限制";
    private String whiteMessage = "无限制";
    private int borderHeight;
    private int borderWidth;
    private int[][] allChess;
    private int isSave;

    public AIChessFrame(Display display, Shell shell, BeforeGameService beforeGameService, GameService gameService) {  // 修改构造函数
        this.beforeGameService = beforeGameService;
        this.borderHeight = gameService.getCurrentGame().getBorder().getLength();
        this.borderWidth = gameService.getCurrentGame().getBorder().getWidth();
        this.gameService = gameService;
        this.aiService = new GomokuAI();  // 初始化 AIService
        this.display = display;
        this.shell = shell;
        this.isSave = 0;
        shell.setText("五子棋");
        shell.setSize(1800, 1200); // 增大窗口尺寸

        allChess = new int[borderHeight][borderWidth];

        shell.addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent e) {
                drawGame(e);
            }
        });

        shell.addMouseListener(new MouseListener() {
            @Override
            public void mouseDoubleClick(MouseEvent e) {
            }

            @Override
            public void mouseDown(MouseEvent e) {
                try {
                    handleMouseClick(e);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public void mouseUp(MouseEvent e) {
            }
        });

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

    // 渲染棋盘
    private void drawGame(PaintEvent e) {
        int cellSize = Math.min((1000 - 200) / borderWidth, (950 - 175) / borderHeight); // 动态计算每个格子的大小
        int xOffset = 200; // 调整棋盘偏移量
        int yOffset = 175; // 调整棋盘偏移量

        FontData fontData = new FontData("黑体", 50, SWT.BOLD);
        Font font = new Font(display, fontData);
        e.gc.setFont(font);
        e.gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
        e.gc.drawText("游戏信息: " + message, 100, 70); // 调整文本位置

        fontData = new FontData("宋体", 20, SWT.NONE); // 调整字体大小
        font = new Font(display, fontData);
        e.gc.setFont(font);
        e.gc.drawText("黑方时间: " + blackMessage, 150, 1000); // 调整文本位置
        e.gc.drawText("白方时间: " + whiteMessage, 550, 1000); // 调整文本位置

        fontData = new FontData("黑体", 30, SWT.BOLD); // 调整按钮字体大小
        font = new Font(display, fontData);
        e.gc.setFont(font);
        e.gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
        e.gc.drawText("开始游戏", 1200, 200); // 调整按钮位置
        e.gc.drawText("保存游戏", 1200, 350); // 调整按钮位置
        e.gc.drawText("退出游戏", 1200, 750); // 调整按钮位置
        e.gc.drawText("认输", 1230, 600); // 调整按钮位置
        e.gc.drawText("关于", 1230, 450); // 调整按钮位置
        e.gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
        e.gc.fillOval(1180, 800, 200, 150); // 调整按钮形状和位置
        e.gc.fillOval(1150, 790, 60, 60); // 调整按钮形状和位置
        e.gc.fillOval(1310, 790, 60, 60); // 调整按钮形状和位置
        e.gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
        e.gc.drawText("游戏设置", 1200, 900); // 调整按钮位置

        e.gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
        e.gc.setFont(new Font(display, "黑体", 15, SWT.BOLD)); // 调整字体大小

        // 绘制棋盘
        for (int i = 0; i <= borderWidth; i++) {
            e.gc.drawLine(xOffset + i * cellSize, yOffset, xOffset + i * cellSize,
                    yOffset + cellSize * (borderHeight - 1));
        }
        for (int i = 0; i <= borderHeight; i++) {
            e.gc.drawLine(xOffset, yOffset + i * cellSize, xOffset + cellSize * (borderWidth - 1),
                    yOffset + i * cellSize);
        }

        // 绘制所有棋子
        for (int i = 0; i < borderWidth; i++) {
            for (int j = 0; j < borderHeight; j++) {
                if (allChess[i][j] == 1) {
                    // 黑棋
                    int tempx = xOffset + i * cellSize;
                    int tempy = yOffset + j * cellSize;
                    e.gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
                    e.gc.fillOval(tempx - cellSize / 2, tempy - cellSize / 2, cellSize, cellSize);
                }
                if (allChess[i][j] == 2) {
                    // 白棋
                    int tempx = xOffset + i * cellSize;
                    int tempy = yOffset + j * cellSize;
                    e.gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
                    e.gc.fillOval(tempx - cellSize / 2, tempy - cellSize / 2, cellSize, cellSize);
                    e.gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
                    e.gc.drawOval(tempx - cellSize / 2, tempy - cellSize / 2, cellSize, cellSize);
                }
            }
        }
    }

    // 处理鼠标点击
    private void handleMouseClick(MouseEvent e) throws SQLException {
        int cellSize = Math.min((1000 - 200) / borderWidth, (950 - 175) / borderHeight);
        int xOffset = 200;
        int yOffset = 175;

        if (canPlay) {
            int nx = e.x;
            int ny = e.y;
            if (nx >= xOffset && nx <= xOffset + cellSize * (borderWidth - 1) && ny >= yOffset
                    && ny <= yOffset + cellSize * (borderHeight - 1)) {
                int x = (nx - xOffset + cellSize / 2) / cellSize;
                int y = (ny - yOffset + cellSize / 2) / cellSize;
                Cell cell = new Cell();
                if (allChess[x][y] == 0) {
                    if (isBlack) {
                        allChess[x][y] = 1;
                        gameService.setAllChess(allChess);
                        this.initCell(cell, x, y, gameService.getCurrentGame().getPlayer1(),
                                gameService.getCurrentGame());
                        gameService.registerChess(cell);
                        isBlack = false;
                        message = "轮到白方";
                        shell.redraw();
                        checkWin(x,y,1);
                        handleAIMove(); // 处理AI落子
                    }
                } else {
                    MessageBox messageBox = new MessageBox(shell, SWT.OK);
                    messageBox.setMessage("此处已有棋子，请换个地方下!");
                    messageBox.open();
                }
            }
        }
        // 判断是否单击了开始游戏
        if (e.x >= 1180 && e.x <= 1380 && e.y >= 200 && e.y <= 260) {
            isSaveBox();
            MessageBox messageBox = new MessageBox(shell, SWT.YES | SWT.NO);
            messageBox.setMessage("是否重新开始游戏？");
            int result = messageBox.open();
            if (result == SWT.YES) {
                resetGame();  // 调用重置游戏方法
            }
        }
        // 判断是否单击了保存
        if (e.x >= 1180 && e.x <= 1380 && e.y >= 350 && e.y <= 410) {
            isSaveBox();
            shell.dispose();
        }
        // 判断是否单击了退出游戏
        if (e.x >= 1180 && e.x <= 1380 && e.y >= 750 && e.y <= 810) {
            isSaveBox();
            MessageBox messageBox = new MessageBox(shell, SWT.OK);
            messageBox.setMessage("欢迎下次再来!");
            messageBox.open();
            shell.dispose();
        }
        // 判断是否单击了认输
        if (e.x >= 1180 && e.x <= 1380 && e.y >= 600 && e.y <= 660) {
            MessageBox messageBox = new MessageBox(shell, SWT.YES | SWT.NO);
            messageBox.setMessage("是否确定认输?");
            int result = messageBox.open();
            if (result == SWT.YES) {
                MessageBox winBox = new MessageBox(shell, SWT.OK);
                winBox.setMessage((isBlack ? "白方" : "黑方") + "胜利!");
                if (isBlack) {
                    gameService.setWinner(gameService.getCurrentGame().getPlayer1());
                } else {
                    gameService.setWinner(gameService.getCurrentGame().getPlayer2());
                }
                winBox.open();
                canPlay = false;
            }
            isSaveBox();
        }
        // 判断是否单击了关于
        if (e.x >= 1180 && e.x <= 1380 && e.y >= 450 && e.y <= 510) {
            MessageBox messageBox = new MessageBox(shell, SWT.OK);
            messageBox.setMessage("五子棋游戏由 Gomoku 开发.");
            messageBox.open();
        }
    }

    private void handleAIMove() throws SQLException {
        if (!isBlack && canPlay) {
            int[] aiMove = aiService.findBestMove(allChess, borderHeight);
            int x = aiMove[0];
            int y = aiMove[1];
            if (allChess[x][y] == 0) {
                allChess[x][y] = 2;
                Cell cell = new Cell();
                gameService.setAllChess(allChess);
                this.initCell(cell, x, y, gameService.getCurrentGame().getPlayer2(), gameService.getCurrentGame());
                gameService.registerChess(cell);
                isBlack = true;
                message = "轮到黑方";
                shell.redraw();
                checkWin(x, y, 2);
            }
        }
    }

    private void resetGame() {
        for (int i = 0; i < borderWidth; i++) {
            for (int j = 0; j < borderHeight; j++) {
                allChess[i][j] = 0;
            }
        }
        isBlack = true;
        message = "黑方先行";
        blackTime = 0;
        whiteTime = 0;
        blackMessage = "无限制";
        whiteMessage = "无限制";
        canPlay = true;
        shell.redraw();
    }

    private void isSaveBox() throws SQLException {
        if (isSave == 0) {
            // 确认是否保存的弹窗
            MessageBox saveBox = new MessageBox(shell, SWT.YES | SWT.NO);
            saveBox.setMessage("是否保存当前对局？");
            int result = saveBox.open();
            if (result == SWT.YES) {
                isSave = 1;
                saveGame();
            }
        }
    }

    private void saveGame() throws SQLException {
        gameService.saveAllResigter();
    }

    // 落子时进行初始化cell
    private void initCell(Cell cell, int x, int y, Player player, Game game) {
        cell.setX(x);
        cell.setY(y);
        cell.setPlayer(player);
        cell.setGame(game);
    }

    // 判断胜利条件
    private void checkWin(int x, int y, int player) throws SQLException {
        // 检查横向、纵向、斜向的胜利条件
        if (checkWinDirection(x, y, 1, 0, player) || checkWinDirection(x, y, 0, 1, player)
                || checkWinDirection(x, y, 1, 1, player) || checkWinDirection(x, y, 1, -1, player)) {
            canPlay = false;
            if (player == 1) {
                gameService.setWinner(gameService.getCurrentGame().getPlayer1());
            } else {
                gameService.setWinner(gameService.getCurrentGame().getPlayer2());
            }
            MessageBox messageBox = new MessageBox(shell, SWT.OK);
            messageBox.setMessage("游戏结束 " + (player == 1 ? "黑方" : "白方") + " 胜利!");
            messageBox.open();
            isSaveBox();
        }
    }

    // 检查单个方向上的胜利条件
    private boolean checkWinDirection(int x, int y, int dx, int dy, int player) {
        int count = 1;
        for (int i = 1; i < 5; i++) {
            if (x + i * dx >= 0 && x + i * dx < borderWidth && y + i * dy >= 0 && y + i * dy < borderHeight
                    && allChess[x + i * dx][y + i * dy] == player) {
                count++;
            } else {
                break;
            }
        }
        for (int i = 1; i < 5; i++) {
            if (x - i * dx >= 0 && x - i * dx < borderWidth && y - i * dy >= 0 && y - i * dy < borderHeight
                    && allChess[x - i * dx][y - i * dy] == player) {
                count++;
            } else {
                break;
            }
        }
        return count >= 5;
    }
}
