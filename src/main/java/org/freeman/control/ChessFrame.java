package org.freeman.control;

import org.freeman.object.Game;
import org.freeman.service.BeforeGameService;
import org.freeman.service.GameService;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ChessFrame extends JFrame implements MouseListener, Runnable {

    private BeforeGameService beforeGameService;
    private GameService gameService;
    int width = Toolkit.getDefaultToolkit().getScreenSize().width; // 获取屏幕宽度和宽度
    int height = Toolkit.getDefaultToolkit().getScreenSize().height;
    BufferedImage bgimage = null;
    // 保存棋子的坐标
    int x = 0;
    int y = 0;
    float nx = 0;
    float ny = 0;
    // 保存之前所有下过棋子的坐标,0表示这个点没有棋子,1这个点有黑棋子,2这个点有白棋子
    int[][] allChess = new int[16][16];
    // 表识当前是黑棋还是白棋
    boolean isBlack = true;
    // 标识当前游戏是否可以继续
    boolean canPlay = true;
    String message = "黑方先行";
    // 保存最多拥有多少时间(秒)线程操作
    int maxTime = 0;
    // 做倒计时线程类
    Thread t = new Thread(this);
    // 保存黑方与白方的剩余时间
    int blackTime = 0;
    int whiteTime = 0;
    // 保存双方剩余时间的信息
    String blackMessage = "无限制";
    String whiteMessage = "无限制";

    // 线程控制标志
    private boolean isRunning = false;

    public ChessFrame() {
        // 设置标题
        this.setTitle("五子棋");
        // 设置窗口大小
        this.setSize(1080, 850);
        // 设置窗口位置
        this.setLocation((width - 1000) / 2, (height - 1000) / 2);// 设置窗口位置
        // 将窗口设置为大小不改变
        this.setResizable(false);
        // 将窗体的关闭方式设置为默认关闭程序结束
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 为窗体加入监听
        this.addMouseListener(this);
         //导入图片
//        try {
//            bgimage = ImageIO.read(getClass().getResource("https://img.51miz.com/Element/01/06/19/55/cb5ec09f_E1061955_7bed678b.jpg"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        this.setVisible(true);
        t.start();
        pauseThread(); // 启动后暂停线程
        this.repaint(); // 刷新屏幕防止开始游戏时无法显示
    }

    public synchronized void pauseThread() {
        isRunning = false;
    }

    public synchronized void resumeThread() {
        isRunning = true;
        notify();
    }

    @Override
    public void paint(Graphics g) {
        // 双缓冲技术防止屏幕闪烁
        BufferedImage bi = new BufferedImage(1080, 850, BufferedImage.TYPE_INT_ARGB);
        Graphics g2 = bi.createGraphics();

        // 绘制背景
        g2.drawImage(bgimage, 0, 0, this);
        // 输出标题信息
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("黑体", Font.BOLD, 50));
        g2.drawString("游戏信息:" + message, 70, 90);
        g2.setFont(new Font("宋体", 30, 30));
        g2.drawString("黑方时间:" + blackMessage, 75, 813);
        g2.drawString("白方时间:" + whiteMessage, 440, 813);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("黑体", Font.BOLD, 30));
        g2.drawString("开始游戏", 910, 130);
        g2.drawString("游戏说明", 910, 240);
        g2.drawString("退出游戏", 910, 590);
        g2.drawString("认输", 940, 470);
        g2.drawString("关于", 940, 360);
        g2.setColor(Color.ORANGE);
        g2.fillOval(900, 650, 150, 100);
        g2.fillOval(880, 640, 50, 50);
        g2.fillOval(1020, 640, 50, 50);
        g2.setColor(Color.WHITE);
        g2.drawString("游戏设置", 910, 710);
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("黑体", Font.BOLD, 20));
        // 绘制棋盘
        for (int i = 0; i <= 13; i++) {
            g2.drawLine(150 + i * 50, 125, 150 + i * 50, 775);
            g2.drawLine(150, 125 + i * 50, 800, 125 + i * 50);
        }
        // 绘制所有棋子
        for (int i = 1; i <= 14; i++) {
            for (int j = 1; j <= 14; j++) {
                if (allChess[i][j] == 1) {
                    // 黑棋子
                    int tempx = (i - 1) * 50 + 150;
                    int tempy = (j - 1) * 50 + 125;
                    g2.fillOval(tempx - 20, tempy - 20, 40, 40);
                }
                if (allChess[i][j] == 2) {
                    // 白棋子
                    int tempx = (i - 1) * 50 + 150;
                    int tempy = (j - 1) * 50 + 125;
                    g2.setColor(Color.WHITE);
                    g2.fillOval(tempx - 20, tempy - 20, 40, 40);
                    g2.setColor(Color.BLACK);
                    g2.drawOval(tempx - 20, tempy - 20, 40, 40);
                }
            }
        }
        g.drawImage(bi, 0, 0, this);
    }

    @Override
    public synchronized void run() {
        // 判断是否有时间的限制
        if (maxTime > 0) {
            while (true) {
                while (!isRunning) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (canPlay) {
                    if (isBlack) {
                        blackTime--;
                        if (blackTime == 0) {
                            JOptionPane.showMessageDialog(this, "黑方超时游戏结束!");
                            canPlay = false;
                        }
                    } else {
                        whiteTime--;
                        if (whiteTime == 0) {
                            JOptionPane.showMessageDialog(this, "白方超时游戏结束!");
                            canPlay = false;
                        }
                    }
                    blackMessage = blackTime / 3600 + ":" + (blackTime / 60 - blackTime / 3600 * 60) + ":"
                            + (blackTime - blackTime / 60 * 60);
                    whiteMessage = whiteTime / 3600 + ":" + (whiteTime / 60 - whiteTime / 3600 * 60) + ":"
                            + (whiteTime - whiteTime / 60 * 60);
                    this.repaint();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        if (canPlay) {
            nx = e.getX();
            ny = e.getY();
            if (nx >= 150 && nx <= 800 && ny >= 125 && ny <= 775) {
                x = Math.round((nx - 150) / 50) + 1;
                y = Math.round((ny - 125) / 50) + 1;
                // 判断当前位置是否有棋子
                if (allChess[x][y] == 0) {
                    // 判断当前下的是什么颜色的棋子
                    if (isBlack) {
                        allChess[x][y] = 1;
                        isBlack = false;
                        message = "轮到白方";
                    } else {
                        allChess[x][y] = 2;
                        isBlack = true;
                        message = "轮到黑方";
                    }
                    // 判断当前胜负
                    boolean winFlag = this.checkWin();
                    if (winFlag) {
                        JOptionPane.showMessageDialog(this, "游戏结束" + (isBlack ? "白方" : "黑方") + "胜利!");
                        canPlay = false;
                    }
                    this.repaint();
                } else {
                    JOptionPane.showMessageDialog(this, "此处已有棋子，请换个地方下!");
                }
            }
        }
        // 判断是否单击了开始游戏
        if (nx >= 875 && nx <= 1060 && ny >= 110 && ny <= 170) {
            int result = JOptionPane.showConfirmDialog(this, "是否重新开始游戏？");
            if (result == 0) {
                // 重新开始游戏
                reStart();
            }
        }
        // 判断是否单击了游戏说明
        if (nx >= 900 && nx <= 1050 && ny >= 220 && ny <= 270) {
            JOptionPane.showMessageDialog(this,
                    "五子棋游戏说明:\n游戏开始黑方先行,轮流下子.黑白双方在横,竖,斜任意方向连续排列五枚相同颜色棋子则胜利.");
        }
        // 判断是否单击了退出游戏
        if (nx >= 900 && nx <= 1050 && ny >= 570 && ny <= 620) {
            JOptionPane.showMessageDialog(this, "欢迎下次再来!");
            System.exit(0);
        }
        // 判断是否单击了认输
        if (nx >= 900 && nx <= 1050 && ny >= 450 && ny <= 500) {
            int result = JOptionPane.showConfirmDialog(this, "是否确定认输?");
            if (result == 0) {
                JOptionPane.showMessageDialog(this, (isBlack ? "白方" : "黑方") + "胜利!");
                canPlay = false;
            }
        }
        // 判断是否单击了关于
        if (nx >= 900 && nx <= 1050 && ny >= 340 && ny <= 390) {
            JOptionPane.showMessageDialog(this, "五子棋游戏v1.0\n制作人:某某某\n制作时间:2021.2.18");
        }
        // 判断是否单击了游戏设置
        if (nx >= 880 && nx <= 1030 && ny >= 640 && ny <= 750) {
            String input = JOptionPane.showInputDialog("请输入游戏每一方最多的思考时间(单位:分钟),如果输入0表示没有时间限制:");
            try {
                maxTime = Integer.parseInt(input) * 60;
                if (maxTime < 0) {
                    JOptionPane.showMessageDialog(this, "请输入正数或者0");
                    return;
                }
                if (maxTime == 0) {
                    int result = JOptionPane.showConfirmDialog(this, "设置完成,是否重新开始游戏?");
                    if (result == 0) {
                        reStart();
                    }
                }
                if (maxTime > 0) {
                    int result = JOptionPane.showConfirmDialog(this, "设置完成,是否重新开始游戏?");
                    if (result == 0) {
                        blackTime = maxTime;
                        whiteTime = maxTime;
                        blackMessage = blackTime / 3600 + ":" + (blackTime / 60 - blackTime / 3600 * 60) + ":"
                                + (blackTime - blackTime / 60 * 60);
                        whiteMessage = whiteTime / 3600 + ":" + (whiteTime / 60 - whiteTime / 3600 * 60) + ":"
                                + (whiteTime - whiteTime / 60 * 60);
                        reStart();
                    }
                }
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(this, "请正确输入以上数字");
            }
        }
    }

    // 判断胜负方法
    public boolean checkWin() {
        // 定义当前颜色
        int color = isBlack ? 2 : 1;
        // 判断横向是否有5个相同棋子
        int count = 1;
        // 横向
        for (int i = 1; i < 5; i++) {
            if (x - i > 0 && allChess[x - i][y] == color) {
                count++;
            } else {
                break;
            }
        }
        for (int i = 1; i < 5; i++) {
            if (x + i < 15 && allChess[x + i][y] == color) {
                count++;
            } else {
                break;
            }
        }
        if (count >= 5) {
            return true;
        }
        // 竖向
        count = 1;
        for (int i = 1; i < 5; i++) {
            if (y - i > 0 && allChess[x][y - i] == color) {
                count++;
            } else {
                break;
            }
        }
        for (int i = 1; i < 5; i++) {
            if (y + i < 15 && allChess[x][y + i] == color) {
                count++;
            } else {
                break;
            }
        }
        if (count >= 5) {
            return true;
        }
        // 判断右上左下
        count = 1;
        for (int i = 1; i < 5; i++) {
            if (x - i > 0 && y - i > 0 && allChess[x - i][y - i] == color) {
                count++;
            } else {
                break;
            }
        }
        for (int i = 1; i < 5; i++) {
            if (x + i < 15 && y + i < 15 && allChess[x + i][y + i] == color) {
                count++;
            } else {
                break;
            }
        }
        if (count >= 5) {
            return true;
        }
        // 判断右下左上
        count = 1;
        for (int i = 1; i < 5; i++) {
            if (x + i < 15 && y - i > 0 && allChess[x + i][y - i] == color) {
                count++;
            } else {
                break;
            }
        }
        for (int i = 1; i < 5; i++) {
            if (x - i > 0 && y + i < 15 && allChess[x - i][y + i] == color) {
                count++;
            } else {
                break;
            }
        }
        if (count >= 5) {
            return true;
        }
        return false;
    }

    // 重新开始
    public void reStart() {
        for (int i = 0; i < allChess.length; i++) {
            for (int j = 0; j < allChess[i].length; j++) {
                allChess[i][j] = 0;
            }
        }
        message = "黑方先行";
        isBlack = true;
        canPlay = true;
        this.repaint();
        if (maxTime > 0) {
            blackTime = maxTime;
            whiteTime = maxTime;
            blackMessage = blackTime / 3600 + ":" + (blackTime / 60 - blackTime / 3600 * 60) + ":"
                    + (blackTime - blackTime / 60 * 60);
            whiteMessage = whiteTime / 3600 + ":" + (whiteTime / 60 - whiteTime / 3600 * 60) + ":"
                    + (whiteTime - whiteTime / 60 * 60);
            resumeThread(); // 重新开始后恢复线程
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }
}
