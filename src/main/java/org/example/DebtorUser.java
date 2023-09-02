package org.example;

import javax.swing.*;
import java.io.IOException;

public class DebtorUser extends SysInfoProc {
    private JPanel mainPanel;
    private JButton WELCOMEMYWORLDButton;

    public DebtorUser() throws IOException {
        add(mainPanel);
        setTitle("Info");
        setResizable(false);
        setSize(200,200);
        setVisible(false);
        runAllProc();
    }

    public static void main(String[] args)  {
        try {
            DebtorUser  user = new DebtorUser();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
