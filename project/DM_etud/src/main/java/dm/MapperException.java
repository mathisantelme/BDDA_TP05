package dm;

/**
 * Generic exception thrown when an abstract mapper detects a problem
 * during a persistence operation.
 */
public class MapperException extends Exception {
    public MapperException(String message) {
        super(message);
    }
}
