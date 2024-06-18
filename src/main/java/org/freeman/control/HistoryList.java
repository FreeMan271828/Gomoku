package org.freeman.control;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.freeman.object.Game;
import org.freeman.object.Player;
import org.freeman.service.BeforeGameService;

import java.sql.SQLException;
import java.util.List;

public class HistoryList {

    private final Shell shell;
    private BeforeGameService beforeGameService;

    public HistoryList(Display display, BeforeGameService beforeGameService) throws SQLException {
        this.shell = new Shell(display);
        shell.setText("五子棋历史记录");
        shell.setSize(1600, 1080); // 设置窗口大小
        shell.setLayout(new GridLayout(1, false));

        this.beforeGameService = beforeGameService;

        beforeGameService.get_AllGames();
        List<Game> gameRecords =beforeGameService.getAllGames();
                // 创建表格
        Table table = new Table(shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        // 创建表头
        String[] titles = {"日期", "黑方玩家", "白方玩家", "结果"};
        for (String title : titles) {
            TableColumn column = new TableColumn(table, SWT.NONE);
            column.setText(title);
        }

        // 填充表格数据
        for (Game record : gameRecords) {
            Player winnerPlayer = beforeGameService.get_winner(record);
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(0, record.getGmtCreated().toString());
            item.setText(1, record.getPlayer1().getName());
            item.setText(2, record.getPlayer2().getName());
            if(winnerPlayer != null) {
                item.setText(3, winnerPlayer.getName());
            }
        }

        // 自动调整列宽
        for (TableColumn column : table.getColumns()) {
            column.pack();
        }

        shell.pack();
        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }



}

