package dm;

/**
 * Domain class representing a book
 */
public class Book implements DomainObject {
    // Variables
    private Object ID;

    // Constructor
    public Book(Object ID) {
        this.ID = ID;
    }

    // Methods
    @Override
    public Object getId() {
        return this.ID;
    }

    @Override
    public void setId(Object p_ID) {
        this.ID = p_ID;
    }
}
