package dm;

/**
 * Domain class representing a book
 */
public class Book implements DomainObject {
    // Variables
    private String ID;
    private String title;
    private String author;
    private float price;

    // Constructor
    public Book(String ID, String p_title, String p_author, float p_price) throws IllegalArgumentException {
        if (ID != null)
            this.ID = ID;
        else
            throw new IllegalArgumentException("Cannot use null ID");

        this.title = p_title;
        this.author = p_author;
        this.price = p_price;
    }

    public Book(Book p_book) {
        this(p_book.getId().toString(), p_book.getTitle().toString(), p_book.getAuthor().toString(), Float.parseFloat(p_book.getPrice().toString()));
    }

    // Accessors
    @Override
    public Object getId() {
        return this.ID;
    }

    public Object getTitle() { return title; }

    public Object getAuthor() { return author; }

    public Object getPrice() { return this.price; }

    @Override
    public void setId (Object p_ID) throws IllegalArgumentException {
        if (ID != null)
            this.ID = ID;
        else
            throw new IllegalArgumentException("Cannot use null ID");
    }

    public void setTitle(String title) { this.title = title; }

    public void setAuthor(String author) { this.author = author; }

    public void setPrice (float p_price) { this.price = p_price; }

    @Override
    public String toString() {
        return "Book{" +
                "ID='" + ID + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                '}';
    }
}
