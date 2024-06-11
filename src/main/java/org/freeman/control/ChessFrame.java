package org.freeman.control;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
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
    private Image bgimage;
    private boolean isBlack = true;
    private boolean canPlay = true;
    private String message = "黑方先行";
    private int maxTime = 0;
    private int blackTime = 0;
    private int whiteTime = 0;
    private String blackMessage = "无限制";
    private String whiteMessage = "无限制";
    private int borderHeight = gameService.getCurrentGame().getBorder().getLength();
    private int borderWidth = gameService.getCurrentGame().getBorder().getWidth();
    private int [][] allChess = new int[borderHeight][borderHeight];

    public ChessFrame(Display display,Shell shell,BeforeGameService beforeGameService,GameService gameService) {

        this.beforeGameService = beforeGameService;
        this.gameService = gameService;
        this.display = display;
        this.shell = shell;
        shell.setText("五子棋");
        shell.setSize(1080, 850);
        shell.setLayout(new FillLayout());


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

        // Load background image
//        try {
//            ImageData imageData = new ImageData("path/to/your/background/image.jpg");
//            bgimage = new Image(display, imageData);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

    private void drawGame(PaintEvent e) {
//        display.dispose()

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
        for (int i = 0; i <= borderHeight-3; i++) {
            e.gc.drawLine(150 + i * 50, 125, 150 + i * 50, 775);
            e.gc.drawLine(150, 125 + i * 50, 800, 125 + i * 50);
        }



        // 绘制所有棋子
        for (int i = 1; i <= borderHeight-2; i++) {
            for (int j = 1; j <= borderHeight-2; j++) {
                if (allChess[i][j] == 1) {
                    // 黑棋子
                    int tempx = (i - 1) * 50 + 150;
                    int tempy = (j - 1) * 50 + 125;
                    e.gc.fillOval(tempx - 20, tempy - 20, 40, 40);
                }
                if (allChess[i][j] == 2) {
                    // 白棋子
                    int tempx = (i - 1) * 50 + 150;
                    int tempy = (j - 1) * 50 + 125;
                    e.gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
                    e.gc.fillOval(tempx - 20, tempy - 20, 40, 40);
                    e.gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
                    e.gc.drawOval(tempx - 20, tempy - 20, 40, 40);
                }
            }
        }
    }

    private void handleMouseClick(MouseEvent e) {
        if (canPlay) {
            int nx = e.x;
            int ny = e.y;
            if (nx >= 150 && nx <= 800 && ny >= 125 && ny <= 775) {
                int x = Math.round((nx - 150) / 50) + 1;
                int y = Math.round((ny - 125) / 50) + 1;
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

        for (int i = 0; i <= borderHeight-1; i++) {
            for (int j = 0; j <= 15; j++) {
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
