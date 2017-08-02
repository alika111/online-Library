/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlers;

import DB.database;
import beans.Genre;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author alex
 */
@ApplicationScoped
@ManagedBean(eager = true)
public class GenreController implements Serializable{

    private ArrayList<Genre> genreList;

    public ArrayList<Genre> getGenreList() {
        return genreList;
    }

    public void setGenreList(ArrayList<Genre> genreList) {
        this.genreList = genreList;
    }

    public GenreController() {
        getgenres();
    }
    

    private void getgenres() {
        Statement st = null;
        ResultSet rs = null;
        Connection con = null;
        genreList=new ArrayList<>();
        try {
            con = database.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("select * from dbo.Genre");
            while (rs.next()) {
                Genre genre = new Genre();
                genre.setID(rs.getInt("GenreID"));
                genre.setName(rs.getString("genre"));
                genreList.add(genre);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GenreController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GenreController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public ArrayList<Genre> getgenrelist() {

            return genreList;
        }
}
