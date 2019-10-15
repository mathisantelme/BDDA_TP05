package dm;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public class BookMapper extends AbstractMapper implements BookMapperOps {

    private static BookMapper instance = null;

    // Constructor
    private BookMapper(String dbName) {
        super(dbName);
    }

    // Accessor
    public static BookMapper getMapper () {
        if (instance == null) {
            instance = new BookMapper("bookstore");
        }

        return instance;
    }

    // AbstractMapper inherited methods override
    @Override
    protected String insertStatement() {
        return "INSERT INTO BOOK (isbn, title, author, price) VALUES (?, ?, ?, ?)";
    }

    @Override
    protected String findStatement() {
        return "SELECT * from BOOK where isbn = ?";
    }

    @Override
    protected String updateStatement() { return "UPDATE BOOK VALUES isbn=?, title=?, author=?, price=? WHERE isbn=?"; }

    @Override
    protected String deleteStatement() {
        return "DELETE FROM BOOK WHERE isbn=?";
    }

    @Override
    protected String deleteAllStatement() {
        return "DELETE FROM BOOK WHERE 1=1";
    }

    @Override
    protected void doInsert(DomainObject subject, PreparedStatement ps) throws MapperException {
        try {
            ps.setString(1, (String) subject.getId());
            ps.setString(2, (String) ((Book) subject).getTitle());
            ps.setString(3, (String) ((Book) subject).getAuthor());
            ps.setFloat(4, (float) ((Book) subject).getPrice());
        } catch (SQLException e) {
            throw new BookMapperException(e.getMessage());
        }
    }

    @Override
    protected DomainObject doLoad(ResultSet rs) throws MapperException {
        try {
            String ID = rs.getString(1);
            String title = rs.getString(2);
            String author = rs.getString(3);
            float price = rs.getFloat(4);

            return new Book(ID, title, author, price);
        } catch (SQLException e) {
            throw new MapperException(e.getMessage());
        }
    }

    @Override
    protected void doUpdate(DomainObject subject, PreparedStatement ps) throws MapperException {
        try {
            if (subject == null || ps == null)
                throw new BookMapperException("BookMapper: Update Failed because subject and/or ps is/are null");

            ps.setString(1, (String) subject.getId());
            ps.setString(2, (String) ((Book) subject).getTitle());
            ps.setString(3, (String) ((Book) subject).getAuthor());
            ps.setFloat(4, (float) ((Book) subject).getPrice());
            ps.setString(5, (String) subject.getId());
        } catch (SQLException e) {
            throw new BookMapperException(e.getMessage());
        }
    }

    // Implementation of the methods from BookMapperOps

    @Override
    public String insert(Book obj) throws BookMapperException {
        try {
            super.abstractInsert(obj);
        } catch (MapperException e) {
            throw new BookMapperException(e.getMessage());
        }
        return (String) obj.getId();
    }

    @Override
    public Book find(String isbn) throws BookMapperException {
        try {
            return (Book) super.abstractFind(isbn);
        } catch (MapperException e) {
            throw new BookMapperException(e.getMessage());
        }
    }

    @Override
    public Set<Book> findManyByAuthor(String authorName) throws BookMapperException {
        try {
            return (Set) super.abstractFindMany(authorName, "SELECT * FROM BOOK WHERE AUTHOR=?");
        } catch (MapperException e) {
            throw new BookMapperException(e.getMessage());
        }
    }

    @Override
    public void update(Book updatedBook) throws BookMapperException {
        try {
            super.abstractUpdate(updatedBook);
        } catch (MapperException e) {
            throw new BookMapperException(e.getMessage());
        }
    }

    @Override
    public void delete(Book book) throws BookMapperException {
        try {
            super.abstractDelete(book);
        } catch (MapperException e) {
            throw new BookMapperException(e.getMessage());
        }
    }

    @Override
    public void deleteAll() throws BookMapperException {
        try {
            super.abstractDeleteAll();
        } catch (MapperException e) {
            throw new BookMapperException(e.getMessage());
        }
    }
}
