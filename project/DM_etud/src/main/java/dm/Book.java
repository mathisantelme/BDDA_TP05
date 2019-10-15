package dm;

/**
 * Domain class representing a book
 */
public class Book implements DomainObject {
    // Variables
    private String ID;
    private String title;
    private String author;

    // Constructor
    public Book(String ID, String p_title, String p_author) throws IllegalArgumentException {
        if (ID != null)
            this.ID = ID;
        else
            throw new IllegalArgumentException("Cannot use null ID");

        this.title = p_title;
        this.author = p_author;
    }

    // Accessors
    @Override
    public Object getId() {
        return this.ID;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public void setId (Object p_ID) throws IllegalArgumentException {
        if (ID != null)
            this.ID = ID;
        else
            throw new IllegalArgumentException("Cannot use null ID");
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
