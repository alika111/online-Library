/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlers;

import DB.database;
import beans.Book;
import enums.SearchType;
import java.io.Serializable;
import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

/**
 *
 * @author alex
 */
@ManagedBean(eager = true)
@SessionScoped
public class BookListController implements Serializable {

    private boolean requestFromPager;
    private int books_on_page = 1;
    private int selected_genre_id;
    private int selected_page_number = 1;
    private int count_of_books;
    private ArrayList<Integer> pagenumbers = new ArrayList<>();
    private String searchString;
    private SearchType searchType;
    private ArrayList<Book> bookList;
    private String sql;

    public ArrayList<Book> getBookList() {
        return bookList;
    }

    public void setBookList(ArrayList<Book> BookList) {
        this.bookList = BookList;
    }

    public void fillBooksBySearch() {
        if (searchString.trim().length() == 0) {
            fillallBooks();
            return;
        }
        StringBuilder sb = new StringBuilder("select book.BookID, book.name, book.book_image, book.page, book.year, book.descr, a.author, g.genre, p.publisher  from dbo.Book_Author "
                + "inner join dbo.Book book on dbo.Book_Author.BookID=book.BookID "
                + "                inner join dbo.Author a on dbo.Book_Author.AuthorID=a.AuthorID "
                + "                inner join dbo.Genre g on book.GenreID=g.GenreID  "
                + "               inner join dbo.Publisher p on book.PublisherID=p.PublisherID ");
        if (searchType == SearchType.AUTHOR) {
            sb.append("where LOWER(a.author) like '%" + searchString.toLowerCase() + "%' order by book.name");
        } else if (searchType == SearchType.TITLE) {
            sb.append("where LOWER(book.name) like '%" + searchString.toLowerCase() + "%' order by book.name");
        }
        fillbysqlquery(sb.toString());
        selected_genre_id = -1;
        selected_page_number = 1;
    }

    public void fillbysqlquery(String query) {
        StringBuilder sb = new StringBuilder(query);
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        sql = query;

        try {
            conn = database.getConnection();
            stmt = conn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            if (!requestFromPager) {
                rs = stmt.executeQuery(sb.toString());
                rs.last();

                count_of_books = rs.getRow();
                pagenumbers(count_of_books, books_on_page);
            }
            if (count_of_books > books_on_page) {
                sb.append(" OFFSET ").append(selected_page_number * books_on_page - books_on_page).append(" ROWS FETCH NEXT ").append(books_on_page).append(" ROWS ONLY");
            }
            rs = stmt.executeQuery(sb.toString());
            bookList = new ArrayList<Book>();
            while (rs.next()) {
                Book book = new Book();
                book.setID(rs.getInt("BookID"));
                book.setName(rs.getString("name"));
                book.setPage(rs.getInt("page"));
                book.setYear(rs.getInt("year"));
                book.setAuthor(rs.getString("author"));
                book.setGenre(rs.getString("genre"));
                book.setPublisher(rs.getString("publisher"));
                book.setDescription(rs.getString("descr"));
                bookList.add(book);
            }

        } catch (SQLException ex) {
            Logger.getLogger(BookListController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(BookListController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void pagenumbers(int count, int b_onpage) {
        int pages = count > 0 ? ((count / b_onpage) + 1) : 0;
        pagenumbers.clear();
        for (int i = 1; i <= pages; i++) {
            pagenumbers.add(i);
        }
    }

    private void fillallBooks() {
        fillbysqlquery("select book.BookID, book.name, book.book_image, book.page, book.year, book.descr, a.author, g.genre, p.publisher  from dbo.Book_Author "
                + "inner join dbo.Book book on dbo.Book_Author.BookID=book.BookID "
                + "                inner join dbo.Author a on dbo.Book_Author.AuthorID=a.AuthorID "
                + "                inner join dbo.Genre g on book.GenreID=g.GenreID  "
                + "               inner join dbo.Publisher p on book.PublisherID=p.PublisherID order by book.name");
    }

    public void fillBooksbyGenre() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        submitValues(1, Integer.valueOf(params.get("Genre_id")), false);

        fillbysqlquery("select book.BookID, book.name, book.book_image, book.page, book.year, book.descr, a.author, g.genre, p.publisher from dbo.Book_Author "
                + "inner join dbo.Book book on dbo.Book_Author.BookID=book.BookID "
                + "inner join dbo.Author a on dbo.Book_Author.AuthorID=a.AuthorID "
                + "inner join dbo.Genre g on book.GenreID=g.GenreID "
                + "inner join dbo.Publisher p on book.PublisherID=p.PublisherID "
                + "where g.GenreID=" + selected_genre_id + " order by book.name");

    }

    public byte[] getImage(int id) {
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        byte[] image = null;

        try {
            conn = database.getConnection();
            stmt = conn.createStatement();

            rs = stmt.executeQuery("select book_image from Book where BookID=" + id);
            while (rs.next()) {
                image = rs.getBytes("book_image");
            }

        } catch (SQLException ex) {
            Logger.getLogger(Book.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Book.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return image;
    }

    public byte[] getContent(int id) {
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        byte[] content = null;
        try {
            conn = database.getConnection();
            stmt = conn.createStatement();

            rs = stmt.executeQuery("select contentfile from Book where BookID=" + id);
            while (rs.next()) {
                content = rs.getBytes("contentfile");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Book.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Book.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }

        return content;
    }

    public void selectpage() {
        Map<String, String> par = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        selected_page_number = Integer.valueOf(par.get("page_number"));
        requestFromPager = true;
        fillbysqlquery(sql);
    }

    private void submitValues(int selected_page_number, int selected_genre_id, boolean requestFromPager) {
        this.selected_page_number = selected_page_number;
        this.selected_genre_id = selected_genre_id;
        this.requestFromPager = requestFromPager;

    }
public void searchStringChanged(ValueChangeEvent e) {
        searchString = e.getNewValue().toString();
    }

    public void searchTypeChanged(ValueChangeEvent e) {
        searchType = (SearchType) e.getNewValue();
    }
    public SearchType getSearchType() {
        return searchType;
    }

    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }

    public ArrayList<Book> getCurrentBookList() {
        return bookList;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public int getBooks_on_page() {
        return books_on_page;
    }

    public void setBooks_on_page(int books_on_page) {
        this.books_on_page = books_on_page;
    }

    public int getSelected_genre_id() {
        return selected_genre_id;
    }

    public void setSelected_genre_id(int selected_genre_id) {
        this.selected_genre_id = selected_genre_id;
    }

    public int getSelected_page_number() {
        return selected_page_number;
    }

    public void setSelected_page_number(int selected_page_number) {
        this.selected_page_number = selected_page_number;
    }

    public int getCount_of_books() {
        return count_of_books;
    }

    public void setCount_of_books(int count_of_books) {
        this.count_of_books = count_of_books;
    }

    public ArrayList<Integer> getPagenumbers() {
        return pagenumbers;
    }

    public void setPagenumbers(ArrayList<Integer> pagenumbers) {
        this.pagenumbers = pagenumbers;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

}
