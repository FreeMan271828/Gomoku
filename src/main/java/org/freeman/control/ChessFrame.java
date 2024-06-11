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
import org.freeman.service.BeforeGameService;
import org.freeman.service.GameService;

public class ChessFrame {

    private Display display;
    private Shell shell;
    private BeforeGameService beforeGameService;
    private GameService gameService;
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

    public ChessFrame(Display display, Shell shell, BeforeGameService beforeGameService, GameService gameService) {

        this.beforeGameService = beforeGameService;
        this.borderHeight = gameService.getCurrentGame().getBorder().getLength();
        this.borderWidth = gameService.getCurrentGame().getBorder().getWidth();
        this.gameService = gameService;
        this.display = display;
        this.shell = shell;
        shell.setText("五子棋");
        shell.setSize(1080, 850);
        shell.setLayout(new FillLayout());
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
                handleMouseClick(e);
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

    private void drawGame(PaintEvent e) {
        int cellSize = Math.min((800 - 150) / borderWidth, (775 - 125) / borderHeight); // 动态计算每个格子的大小
        int xOffset = 150;
        int yOffset = 125;

        FontData fontData = new FontData("黑体", 50, SWT.BOLD);
        Font font = new Font(display, fontData);
        e.gc.setFont(font);
        e.gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
        e.gc.drawText("游戏信息:" + message, 70, 90);

        fontData = new FontData("宋体", 10, SWT.NONE);
        font = new Font(display, fontData);
        e.gc.setFont(font);
        e.gc.drawText("黑方时间:" + blackMessage, 75, 813);
        e.gc.drawText("白方时间:" + whiteMessage, 440, 813);

        fontData = new FontData("黑体", 20, SWT.BOLD);
        font = new Font(display, fontData);
        e.gc.setFont(font);
        e.gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
        e.gc.drawText("开始游戏", 910, 130);
        e.gc.drawText("游戏说明", 910, 240);
        e.gc.drawText("退出游戏", 910, 590);
        e.gc.drawText("认输", 940, 470);
        e.gc.drawText("关于", 940, 360);
        e.gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
        e.gc.fillOval(900, 650, 150, 100);
        e.gc.fillOval(880, 640, 50, 50);
        e.gc.fillOval(1020, 640, 50, 50);
        e.gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
        e.gc.drawText("游戏设置", 910, 710);

        e.gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
        e.gc.setFont(new Font(display, "黑体", 10, SWT.BOLD));

        // 绘制棋盘
        for (int i = 0; i <= borderWidth; i++) {
            e.gc.drawLine(xOffset + i * cellSize, yOffset, xOffset + i * cellSize, yOffset + cellSize * (borderHeight - 1));
        }
        for (int i = 0; i <= borderHeight; i++) {
            e.gc.drawLine(xOffset, yOffset + i * cellSize, xOffset + cellSize * (borderWidth - 1), yOffset + i * cellSize);
        }

        // 绘制所有棋子
        for (int i = 0; i < borderWidth; i++) {
            for (int j = 0; j < borderHeight; j++) {
                if (allChess[i][j] == 1) {
                    // 黑棋
                    // 黑棋子
                    int tempx = xOffset + i * cellSize;
                    int tempy = yOffset + j * cellSize;
                    e.gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
                    e.gc.fillOval(tempx - cellSize / 2, tempy - cellSize / 2, cellSize, cellSize);
                }
                if (allChess[i][j] == 2) {
                    // 白棋子
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

    private void handleMouseClick(MouseEvent e) {
        int cellSize = Math.min((800 - 150) / borderWidth, (775 - 125) / borderHeight);
        int xOffset = 150;
        int yOffset = 125;

        if (canPlay) {
            int nx = e.x;
            int ny = e.y;
            if (nx >= xOffset && nx <= xOffset + cellSize * (borderWidth - 1) && ny >= yOffset && ny <= yOffset + cellSize * (borderHeight - 1)) {
                int x = (nx - xOffset + cellSize / 2) / cellSize;
                int y = (ny - yOffset + cellSize / 2) / cellSize;
                if (allChess[x][y] == 0) {
                    if (isBlack) {
                        allChess[x][y] = 1;
                        gameService.setAllChess(allChess);
                        isBlack = false;
                        message = "轮到白方";
                    } else {
                        allChess[x][y] = 2;
                        gameService.setAllChess(allChess);
                        isBlack = true;
                        message = "轮到黑方";
                    }
                    if (gameService.checkWin(x, y)) {
                        MessageBox messageBox = new MessageBox(shell, SWT.OK);
                        messageBox.setMessage("游戏结束" + (isBlack ? "白方" : "黑方") + "胜利!");
                        messageBox.open();
                        canPlay = false;
                    }
                    shell.redraw();
                } else {
                    MessageBox messageBox = new MessageBox(shell, SWT.OK);
                    messageBox.setMessage("此处已有棋子，请换个地方下!");
                    messageBox.open();
                }
            }
        }
        // 判断是否单击了开始游戏
        if (e.x >= 875 && e.x <= 1060 && e.y >= 110 && e.y <= 170) {
            MessageBox messageBox = new MessageBox(shell, SWT.YES | SWT.NO);
            messageBox.setMessage("是否重新开始游戏？");
            int result = messageBox.open();
            if (result == SWT.YES) {
                reStart();
            }
        }
        // 判断是否单击了游戏说明
        if (e.x >= 900 && e.x <= 1050 && e.y >= 220 && e.y <= 270) {
            MessageBox messageBox = new MessageBox(shell, SWT.OK);
            messageBox.setMessage("五子棋游戏说明:\n游戏开始黑方先行,轮流下子.黑白双方在横,竖,斜任意方向连续排列五枚相同颜色棋子则胜利.");
            messageBox.open();
        }
        // 判断是否单击了退出游戏
        if (e.x >= 900 && e.x <= 1050 && e.y >= 570 && e.y <= 620) {
            MessageBox messageBox = new MessageBox(shell, SWT.OK);
            messageBox.setMessage("欢迎下次再来!");
            messageBox.open();
            shell.dispose();
        }
        // 判断是否单击了认输
        if (e.x >= 900 && e.x <= 1050 && e.y >= 450 && e.y <= 500) {
            MessageBox messageBox = new MessageBox(shell, SWT.YES | SWT.NO);
            messageBox.setMessage("是否确定认输?");
            int result = messageBox.open();
            if (result == SWT.YES) {
                MessageBox winBox = new MessageBox(shell, SWT.OK);
                winBox.setMessage((isBlack ? "白方" : "黑方") + "胜利!");
                winBox.open();
                canPlay = false;
            }
        }
        // 判断是否单击了关于
        if (e.x >= 900 && e.x <= 1050 && e.y >= 340 && e.y <= 390) {
            MessageBox messageBox = new MessageBox(shell, SWT.OK);
            messageBox.setMessage("五子棋游戏由Your Name开发.");
            messageBox.open();
        }
    }

    private void reStart() {
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
}
