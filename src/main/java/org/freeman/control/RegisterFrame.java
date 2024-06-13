package org.freeman.control;

import Factory.DaoFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.freeman.dao.PlayerDao;
import org.freeman.object.Player;

import java.sql.SQLException;

public class RegisterFrame{


    private final PlayerDao playerDao;

    private final Shell shell;

    public RegisterFrame(Display display) {
        this.shell = new Shell(display);
        this.playerDao = DaoFactory.createDao(PlayerDao.class);

        //清空所有组件
        clearComponents();

        shell.setText("添加用户");
        shell.setLayout(new GridLayout(2, false));

        // 姓名标签
        Label nameLabel = new Label(shell, SWT.NONE);
        nameLabel.setText("姓名：");

        // 姓名输入框
        Text nameText = new Text(shell, SWT.BORDER);
        GridData nameData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        nameText.setLayoutData(nameData);

        // 添加按钮
        Button addButton = new Button(shell, SWT.NONE);
        addButton.setText("添加");
        GridData buttonData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
        addButton.setLayoutData(buttonData);

        Button backButton = new Button(shell, SWT.PUSH);
        backButton.setText("返回");
        backButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                for (org.eclipse.swt.widgets.Control control : shell.getChildren()) {
                    control.dispose();
                }
                shell.close();
            }
        });

        // 添加按钮监听事件
        addButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String name = nameText.getText();

                if (name.isEmpty() ) {
                    MessageBox messageBox = new MessageBox(shell, SWT.OK);
                    messageBox.setMessage("请填写完整信息！");
                    messageBox.open();
                    return;
                }

                // 进行添加用户的逻辑
                Player player = new Player();
                player.setName(name);
                Player result = null;
                try {
                    result = playerDao.AddPlayer(player);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                if (result!=null) {
                    MessageBox messageBox = new MessageBox(shell, SWT.OK);
                    messageBox.setMessage("添加用户成功！");
                    messageBox.open();
                    shell.dispose(); // 关闭添加用户窗口
                } else {
                    MessageBox messageBox = new MessageBox(shell, SWT.OK);
                    messageBox.setMessage("添加用户失败，可能账号已存在！");
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

    //初始化界面
    private void clearComponents(){
        for (org.eclipse.swt.widgets.Control control : shell.getChildren()) {
            control.dispose();
        }
        shell.setLayout(new GridLayout(2, false));
    }

}