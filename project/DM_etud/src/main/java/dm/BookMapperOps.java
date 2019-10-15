package dm;

import java.util.Set;

/**
 *  Define operations must be implemented by a subclass of AbstractMapper
 */
public interface BookMapperOps {
    /**
     * Insert a book object in database
     *
     * @param newObject
     * @return oid
     * @throws BookMapperException if something goes wrong...
     */
    String insert(Book newObject) throws BookMapperException;

    /**
     * Find a book object with isbn
     *
     * @param isbn oid
     * @return a Book object matching oid
     * @throws BookMapperException if something goes wrong...
     */
    Book find(String isbn) throws BookMapperException;

    /**
     * Find an author's books
     *
     * @param authorName author (person) oid
     * @return a set of Book objects matching author name
     * @throws BookMapperException if something goes wrong...
     */
    Set<Book> findManyByAuthor(String authorName) throws BookMapperException;

    /**
     * Update a book
     *
     * @param updatedBook book to update
     * @throws BookMapperException if something goes wrong...
     */
    void update(Book updatedBook) throws BookMapperException;

    /**
     * Delete a book
     *
     * @param bookToDelete book to delete
     * @throws BookMapperException if something goes wrong...
     */
    void delete(Book bookToDelete) throws BookMapperException;

    /**
     * Delete all books
     *
     * @throws BookMapperException if something goes wrong...
     */
    void deleteAll() throws BookMapperException;
}
