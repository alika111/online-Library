/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

/**
 *
 * @author alex
 */
public class Genre {
    private long ID;
    private String name;

    public Genre() {
    }

    public long getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Genre(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
