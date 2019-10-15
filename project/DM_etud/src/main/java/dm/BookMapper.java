package dm;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Set;

public class BookMapper extends AbstractMapper implements BookMapperOps {

    // Constructor
    public BookMapper(String dbName) {
        super(dbName);
    }

    // Accessor
    public static AbstractMapper getMapper () {

    }

    // AbstractMapper inherited methods override
    @Override
    protected String insertStatement() {
        return null;
    }

    @Override
    protected String findStatement() {
        return null;
    }

    @Override
    protected String updateStatement() {
        return null;
    }

    @Override
    protected String deleteStatement() {
        return null;
    }

    @Override
    protected String deleteAllStatement() {
        return null;
    }

    @Override
    protected void doInsert(DomainObject subject, PreparedStatement ps) throws MapperException {

    }

    @Override
    protected DomainObject doLoad(ResultSet rs) throws MapperException {
        return null;
    }

    @Override
    protected void doUpdate(DomainObject subject, PreparedStatement ps) throws MapperException {

    }

    // Implementation of the methods from BookMapperOps

    @Override
    public String insert(Book newObject) throws BookMapperException {
        return null;
    }

    @Override
    public Book find(String isbn) throws BookMapperException {
        return null;
    }

    @Override
    public Set<Book> findManyByAuthor(String authorName) throws BookMapperException {
        return null;
    }

    @Override
    public void update(Book updatedBook) throws BookMapperException {

    }

    @Override
    public void delete(Book bookToDelete) throws BookMapperException {

    }

    @Override
    public void deleteAll() throws BookMapperException {

    }
}
