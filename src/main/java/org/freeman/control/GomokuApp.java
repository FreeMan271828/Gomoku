package org.freeman.control;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.freeman.object.Border;
import org.freeman.object.Player;
import org.freeman.service.*;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GomokuApp {
    private Display display;
    private Shell shell;
    private final BeforeGameService beforeGameService = new BeforeGameService();
    private final GameService gameService = new GameService();
    private Map<String, UUID> players;

    // Main method commented out for testing purposes
//    public static void main(String[] args) {
//        new GomokuApp().open();
//    }

    public void open() {
        display = new Display();
        shell = new Shell(display);
        shell.setText("五子棋游戏");
        shell.setSize(1600, 1080);
        shell.setLayout(new GridLayout(1, false));
        // 设置背景颜色
        Color background = new Color(display, 240, 240, 240);
        shell.setBackground(background);

        createInitialScreen();

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

    // 设置对局配置的选项
    private void showGameSettingsScreen(boolean isSinglePlayer) throws SQLException {
        // 清空所有组件
        clearComponents();

        // 设置布局
        shell.setLayout(new GridLayout(1, false));

        // 设置标题
        Label titleLabel = new Label(shell, SWT.CENTER);
        titleLabel.setText("对局设置");
        FontData fontData = new FontData("隶书", 24, SWT.BOLD);  // 设置字体为隶书，大小24，加粗
        Font titleFont = new Font(display, fontData);
        titleLabel.setFont(titleFont);
        GridData titleGridData = new GridData(SWT.CENTER, SWT.CENTER, true, false);
        titleGridData.verticalIndent = 80;  // 设置标题与其他组件之间的间距
        titleLabel.setLayoutData(titleGridData);

        // 设置组件的通用 GridData
        GridData gridData = new GridData(SWT.CENTER, SWT.CENTER, true, false);
        gridData.widthHint = 200;  // 设置组件宽度
        gridData.heightHint = 50;  // 设置组件高度

        Label boardSizeLabel = new Label(shell, SWT.NONE);
        boardSizeLabel.setText("选择棋盘大小:");
        boardSizeLabel.setLayoutData(gridData);

        Combo boardSizeCombo = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
        boardSizeCombo.setItems(getAllBorderType());
        boardSizeCombo.setLayoutData(gridData);

        Label player1Label = new Label(shell, SWT.NONE);
        player1Label.setText("选择玩家1:");
        player1Label.setLayoutData(gridData);

        Combo player1Combo = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
        player1Combo.setItems(getAllPlayersName());
        player1Combo.setLayoutData(gridData);

        Label player2Label = new Label(shell, SWT.NONE);
        player2Label.setText("选择玩家2:");
        player2Label.setLayoutData(gridData);

        Combo player2Combo = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
        if (isSinglePlayer) {
            player2Combo.setItems(new String[]{"AI"});
            player2Combo.select(0);  // 选择AI
            player2Combo.setEnabled(false);  // 禁用选择
        } else {
            player2Combo.setItems(getAllPlayersName());
        }
        player2Combo.setLayoutData(gridData);

        Button confirmButton = new Button(shell, SWT.PUSH);
        confirmButton.setText("确认");
        confirmButton.setLayoutData(gridData);
        confirmButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                String boardSize = boardSizeCombo.getText();
                String player1 = player1Combo.getText();
                String player2 = player2Combo.getText();
                Border border = getBorderByName(boardSize);
                gameService.setBorderToGame(border);
                try {
                    gameService.setPlayerIdToGame(getPlayerIdByName(player1), getPlayerIdByName(player2));
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                if (!boardSize.isEmpty() && !player1.isEmpty() && !player2.isEmpty()) {
                    startGame(isSinglePlayer);
                } else {
                    // TODO 显示错误信息
                }
            }
        });



        Button backButton = new Button(shell, SWT.PUSH);
        backButton.setText("返回");
        backButton.setLayoutData(gridData);
        backButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                for (Control control : shell.getChildren()) {
                    control.dispose();
                }
                createInitialScreen();
                shell.layout();
            }
        });

        Button registerButton = new Button(shell, SWT.PUSH);
        registerButton.setText("注册用户");
        registerButton.setLayoutData(gridData);
        // 注册按钮监听事件
        registerButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                new RegisterFrame(display);
            }
        });

        Button addBorderButton = new Button(shell, SWT.PUSH);
        addBorderButton.setText("添加棋盘");
        addBorderButton.setLayoutData(gridData);
        addBorderButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                new AddBorderFrame(display);
            }
        });

        shell.layout();
    }


    // 游戏模式选择界面
    private void showGameModeSelectionScreen() {
        clearComponents();

        shell.setLayout(new GridLayout(1,false));
        // 设置标题
        Label titleLabel = new Label(shell, SWT.CENTER);
        titleLabel.setText("选择游戏模式");
        FontData fontData = new FontData("隶书", 24, SWT.BOLD);  // 设置字体为隶书，大小24，加粗
        Font titleFont = new Font(display, fontData);
        titleLabel.setFont(titleFont);
        GridData titleGridData = new GridData(SWT.CENTER, SWT.CENTER, true, false);
        titleGridData.verticalIndent = 120;  // 设置标题与按钮之间的间距
        titleLabel.setLayoutData(titleGridData);

        GridData gridData = new GridData(SWT.CENTER, SWT.CENTER, true, false);
        gridData.widthHint = 200;  // 设置按钮宽度
        gridData.heightHint = 50;  // 设置按钮高度

        Button singlePlayerButton = new Button(shell, SWT.PUSH);
        singlePlayerButton.setText("单人游戏");
        singlePlayerButton.setLayoutData(gridData);
        singlePlayerButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                try {
                    showGameSettingsScreen(true);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        Button multiPlayerButton = new Button(shell, SWT.PUSH);
        multiPlayerButton.setText("双人游戏");
        multiPlayerButton.setLayoutData(gridData);
        multiPlayerButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                try {
                    showGameSettingsScreen(false);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        Button backButton = new Button(shell, SWT.PUSH);
        backButton.setText("返回");
        backButton.setLayoutData(gridData);
        backButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                createInitialScreen();
                shell.layout();
            }
        });

        shell.layout();
    }


    // 历史记录的查看代码
    private void showHistoryScreen() throws SQLException {
        new HistoryList(this.display, beforeGameService);
        shell.layout();
    }

    private void startGame(boolean isSinglePlayer) {
        clearComponents();
        if (isSinglePlayer) {
            new AIChessFrame(display, shell, beforeGameService, gameService);
        } else {
            new ChessFrame(display, shell, beforeGameService, gameService);
        }
    }

    // 初始化界面
    private void clearComponents() {
        for (Control control : shell.getChildren()) {
            control.dispose();
        }
        shell.setLayout(new GridLayout(2, false));
    }

    /**
     * 获取玩家id和名称的对应
     * @return
     */
    public Map<UUID, String> getPlayerNameByIdMap() throws SQLException {
        beforeGameService.get_allBorders();
        beforeGameService.get_AllPlayers();
        Map<UUID, String> Players = new HashMap<>();
        for (Player player : beforeGameService.getAllPlayers()) {
            UUID ID = player.getId();
            Players.put(ID, player.getName());
        }
        return Players;
    }

    public Map<String, UUID> getPlayerIdByNameMap() throws SQLException {
        beforeGameService.get_AllPlayers();
        Map<String, UUID> Players = new HashMap<>();
        for (Player player : beforeGameService.getAllPlayers()) {
            UUID ID = player.getId();
            Players.put(player.getName(), ID);
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
        Map<String, UUID> players = getPlayerIdByNameMap();
        return players.get(name);
    }

    public String[] getAllBorderType() {
        beforeGameService.get_allBorders();
        return beforeGameService.getAllBorders().stream()
                .map(border -> border.getLength() + "*" + border.getWidth())
                .distinct()
                .sorted(Comparator.comparingInt(String::length))
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

    // 初始化界面
    public void createInitialScreen() {
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
                showGameModeSelectionScreen();
            }
        });

        Button viewHistoryButton = new Button(shell, SWT.PUSH);
        viewHistoryButton.setText("查看历史记录");
        viewHistoryButton.setLayoutData(gridData);
        viewHistoryButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                try {
                    showHistoryScreen();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
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
}
