/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class database {

    private static Connection conn;
    private static InitialContext ic;
    private static DataSource ds;

    public static Connection getConnection() {
        try {
            ic = new InitialContext();
            ds = (DataSource) ic.lookup("jdbc/biblioteka");
            conn = ds.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
        }

        return conn;
    }
}
