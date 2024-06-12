package org.freeman.control;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.freeman.object.Border;
import org.freeman.object.Player;
import org.freeman.service.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GomokuApp {
    private Display display;
    private Shell shell;
    private final BeforeGameService beforeGameService = new BeforeGameService();
    private final GameService gameService = new GameService();
    private Map<String, UUID> players;

//    public static void main(String[] args) {
//        new GomokuApp().open();
//    }

    public void open() {
        display = new Display();
        shell = new Shell(display);
        shell.setText("五子棋游戏");
        shell.setSize(1080, 850);
        shell.setLayout(new GridLayout(1, false));

        createInitialScreen();

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

    //初始化界面
    private void createInitialScreen() {
        shell.setLayout(new GridLayout(1, false));

        // 设置标题
        Label titleLabel = new Label(shell, SWT.CENTER);
        titleLabel.setText("五子棋游戏");
        FontData fontData = new FontData("隶书", 24, SWT.BOLD);  // 设置字体为隶书，大小24，加粗
        Font titleFont = new Font(display, fontData);
        titleLabel.setFont(titleFont);
        GridData titleGridData = new GridData(SWT.CENTER, SWT.CENTER, true, false);
        titleGridData.verticalIndent = 120;  // 设置标题与按钮之间的间距
        titleLabel.setLayoutData(titleGridData);

        GridData gridData = new GridData(SWT.CENTER, SWT.CENTER, true, false);
        gridData.widthHint = 200;  // 设置按钮宽度
        gridData.heightHint = 50;  // 设置按钮高度

        Button startGameButton = new Button(shell, SWT.PUSH);
        startGameButton.setText("开始游戏");
        startGameButton.setLayoutData(gridData);
        startGameButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                try {
                    showGameSettingsScreen();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        Button viewHistoryButton = new Button(shell, SWT.PUSH);
        viewHistoryButton.setText("查看历史记录");
        viewHistoryButton.setLayoutData(gridData);
        viewHistoryButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                showHistoryScreen();
            }
        });

        Button exitButton = new Button(shell, SWT.PUSH);
        exitButton.setText("退出");
        exitButton.setLayoutData(gridData);
        exitButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                shell.dispose();
            }
        });
    }

    //设置对局配置的选项
    private void showGameSettingsScreen() throws SQLException {

        //清空所有组件
        clearComponents();

        Label boardSizeLabel = new Label(shell, SWT.NONE);
        boardSizeLabel.setText("选择棋盘大小:");

        Combo boardSizeCombo = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
        boardSizeCombo.setItems(getAllBorderType());

        Label player1Label = new Label(shell, SWT.NONE);
        player1Label.setText("选择玩家1:");

        Combo player1Combo = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
        player1Combo.setItems(getAllPlayersName());

        Label player2Label = new Label(shell, SWT.NONE);
        player2Label.setText("选择玩家2:");

        Combo player2Combo = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
        player2Combo.setItems(getAllPlayersName());

        // 注册按钮
        Button registerButton = new Button(shell, SWT.PUSH);
        registerButton.setText("注册用户");

        // 注册按钮监听事件
        registerButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                new RegisterFrame(display,shell);
            }
        });

        Button backButton = new Button(shell, SWT.PUSH);
        backButton.setText("返回");
        backButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                for (org.eclipse.swt.widgets.Control control : shell.getChildren()) {
                    control.dispose();
                }
                createInitialScreen();
                shell.layout();
            }
        });

        Button confirmButton = new Button(shell, SWT.PUSH);
        confirmButton.setText("确认");
        confirmButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                String boardSize = boardSizeCombo.getText();
                String player1 = player1Combo.getText();
                String player2 = player2Combo.getText();
                Border border = getBorderByName(boardSize);
                gameService.setBorderToGame(border);
                try {
                    gameService.setPlayerIdToGame(getPlayerIdByName(player1),getPlayerIdByName(player2));
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                if (!boardSize.isEmpty() && !player1.isEmpty() && !player2.isEmpty()) {
                    startGame();
                } else {
                    //TODO 显示错误信息
                }
            }
        });

        shell.layout();
    }

    //历史记录的查看代码
    private void showHistoryScreen() {

        clearComponents();

        // 历史记录查看代码在这里

        shell.layout();
    }
    private void startGame() {

        clearComponents();


        //重新渲染一个棋盘，
        new ChessFrame(display,shell,beforeGameService,gameService);
//        Button backButton = new   Button(shell, SWT.PUSH);
//        backButton.setText("返回主菜单");
//        backButton.addSelectionListener(new SelectionAdapter() {
//            public void widgetSelected(SelectionEvent e) {
//                for (org.eclipse.swt.widgets.Control control : shell.getChildren()) {
//                    control.dispose();
//                }
//                createInitialScreen();
//                shell.layout();
//            }
//        });

//        shell.layout();
    }



    //初始化界面
    private void clearComponents(){
        for (org.eclipse.swt.widgets.Control control : shell.getChildren()) {
            control.dispose();
        }
        shell.setLayout(new GridLayout(2, false));
    }

    /**
     * 获取玩家id和名称的对应
     * @return
     */
    public Map<UUID,String> getPlayerNameByIdMap() throws SQLException {
        beforeGameService.get_allBorders();
        beforeGameService.get_AllPlayers();
        Map<UUID,String> Players = new HashMap<>();
        for (Player player : beforeGameService.getAllPlayers()) {
            UUID ID = player.getId();
            Players.put(ID,player.getName());
        }
        return Players;
    }

    public Map<String,UUID> getPlayerIdByNameMap() throws SQLException {
        beforeGameService.get_AllPlayers();
        Map<String,UUID> Players = new HashMap<>();
        for (Player player : beforeGameService.getAllPlayers()) {
            UUID ID = player.getId();
            Players.put(player.getName(),ID);
        }
        return Players;
    }

    public String[] getAllPlayersName() throws SQLException {
        Map<UUID, String> players = getPlayerNameByIdMap();
        return players.values().toArray(new String[0]);
    }

    public String getPlayerNameById(String ID) throws SQLException {
        Map<UUID, String> players = getPlayerNameByIdMap();
        return players.get(ID);
    }

    public UUID getPlayerIdByName(String name) throws SQLException {
        Map<String,UUID> players = getPlayerIdByNameMap();
        return players.get(name);

    }

    public String[] getAllBorderType(){
        beforeGameService.get_allBorders();
        return beforeGameService.getAllBorders().stream()
                .map(border -> border.getLength() + "*" + border.getWidth())
                .distinct()
                .toArray(String[]::new);
    }

    public Border getBorderByName(String name) {
        // 从name中解析出长度和宽度
        String[] dimensions = name.split("\\*");
        if (dimensions.length != 2) {
            throw new IllegalArgumentException("Invalid border name format. Expected 'length*width'.");
        }
        int length = Integer.parseInt(dimensions[0]);
        int width = Integer.parseInt(dimensions[1]);

        // 遍历所有边界对象，找到匹配的
        return beforeGameService.getAllBorders().stream()
                .filter(border -> border.getLength() == length && border.getWidth() == width)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No border found with the specified dimensions."));
    }

}
