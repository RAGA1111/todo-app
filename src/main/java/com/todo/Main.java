
package com.todo;
import java.sql.Connection;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.todo.gui.TodoAppGUI;
import com.todo.util.DatabaseConnection;

public class Main{

    public static void main(String[] args) {
        DatabaseConnection db_Connection = new DatabaseConnection();
        try {
            Connection cn = db_Connection.getDBConnection();
            System.out.println("CONNECTED SUCCESSFULLY");
        }
        catch (Exception e) {
            System.out.println("CONNECTION FAILED");
            System.exit(1);
        }
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e){
            System.err.println("Could not set look and feel"+e.getMessage());
        }

        SwingUtilities.invokeLater(
            () ->
            {
                try{
                    new TodoAppGUI().setVisible(true);

                }
                catch(Exception e){
                    System.out.println("Error Starting the application"+e.getLocalizedMessage());

                }
            }
        );
    }
}