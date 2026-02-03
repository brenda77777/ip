package candy;

/**
 * Represents exceptions specific to the Candy application.
 * <p>
 * This exception is used to signal invalid user input or logical errors
 * during command processing.
 */
public class CandyException extends Exception {

    /**
     * Constructs a CandyException with the specified error message.
     *
     * @param message The detail message describing the error.
     */
    public CandyException(String message) {
        super(message);
    }
}