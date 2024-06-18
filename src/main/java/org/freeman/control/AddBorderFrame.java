package org.freeman.control;

import Factory.DaoFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.freeman.dao.BorderDao;
import org.freeman.dao.PlayerDao;
import org.freeman.object.Border;
import org.freeman.object.Player;

import java.sql.SQLException;

public class AddBorderFrame {

    private final BorderDao borderDao;
    private  Shell shell;

    public AddBorderFrame(Display display) {
        this.shell = new Shell(display);
        shell.setText("添加棋盘");
        shell.setSize(1080, 850); // 设置窗口大小


        // 居中窗口
        shell.setLocation((display.getBounds().width - shell.getSize().x) / 2,
                (display.getBounds().height - shell.getSize().y) / 2);

        this.borderDao = DaoFactory.createDao(BorderDao.class);

        // 清空所有组件
        clearComponents();

        shell.setLayout(new GridLayout(2, false));

        // 姓名标签
        Label heightLabel = new Label(shell, SWT.NONE);
        heightLabel.setText("高：");
        heightLabel.setFont(display.getSystemFont());

        // 姓名输入框
        Text heightText = new Text(shell, SWT.BORDER);
        GridData heightData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        heightData.widthHint = 200; // 设置宽度
        heightText.setLayoutData(heightData);
        heightText.setFont(display.getSystemFont());

        Label widthLabel = new Label(shell, SWT.NONE);
        widthLabel.setText("宽：");
        widthLabel.setFont(display.getSystemFont());

        Text widthText = new Text(shell, SWT.BORDER);
        GridData widthData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        widthData.widthHint = 200; // 设置宽度
        widthText.setLayoutData(widthData);
        widthText.setFont(display.getSystemFont());

        // 添加按钮
        Button addButton = new Button(shell, SWT.PUSH);
        addButton.setText("添加");
        GridData buttonData = new GridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1);
        buttonData.widthHint = 100; // 设置按钮宽度
        buttonData.heightHint = 40; // 设置按钮高度
        addButton.setLayoutData(buttonData);
        addButton.setFont(display.getSystemFont());

        // 返回按钮
        Button backButton = new Button(shell, SWT.PUSH);
        backButton.setText("返回");
        GridData backButtonData = new GridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1);
        backButtonData.widthHint = 100; // 设置按钮宽度
        backButtonData.heightHint = 40; // 设置按钮高度
        backButton.setLayoutData(backButtonData);
        backButton.setFont(display.getSystemFont());

        backButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                shell.close();
            }
        });

        // 添加按钮监听事件
        addButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String height = heightText.getText();
                String width = widthText.getText();

                if (height.isEmpty()||width.isEmpty()) {
                    MessageBox messageBox = new MessageBox(shell, SWT.OK);
                    messageBox.setMessage("请填写完整信息！");
                    messageBox.open();
                    return;
                }

                // 进行添加棋盘的逻辑
                int Width = Integer.parseInt(width);
                int Height = Integer.parseInt(height);
                Border border = new Border();
                border.setLength(Height);
                border.setWidth(Width);

                Border result = null;
                try {
                    result = borderDao.AddBorder(border);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                if (result != null) {
                    MessageBox messageBox = new MessageBox(shell, SWT.OK);
                    messageBox.setMessage("添加成功！");
                    messageBox.open();
                    shell.dispose(); // 关闭添加用户窗口
                } else {
                    MessageBox messageBox = new MessageBox(shell, SWT.OK);
                    messageBox.setMessage("添加失败，可能棋盘已存在！");
                    messageBox.open();
                }
            }
        });

        shell.pack();
        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    // 初始化界面
    private void clearComponents() {
        for (org.eclipse.swt.widgets.Control control : shell.getChildren()) {
            control.dispose();
        }
        shell.setLayout(new GridLayout(2, false));
    }

}
