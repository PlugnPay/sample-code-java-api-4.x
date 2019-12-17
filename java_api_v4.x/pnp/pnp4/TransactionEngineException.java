
package pnp4;

/*
 * TransactionEngineException.java
 *
 * Class for TransactionEngine Exceptions.  In most cases you
 * should check these messages because they will be meaningful.
 *
 * @author Plugnpay Technologies Inc.
 * @version 4.0.20070222 02/22/2007
 */
public class TransactionEngineException extends Exception {
  public TransactionEngineException() {
    super("General Transaction Engine Exception.");
  }
  
  public TransactionEngineException(String message) {
    super(message);
  }
}
