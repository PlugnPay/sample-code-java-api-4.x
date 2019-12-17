/*
 * testTransaction.java
 *
 * Created on October 19, 2004, 4:02 PM
 */

import pnp4.*;
import java.util.*;

/**
 * An example class for using the pnp java api.
 * @author  PlugnPay Technologies Inc.
 */
public class testTransaction {
    
    /**
     * main shows how to run a transaction
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TransactionEngine pnp = new TransactionEngine();

        Properties pairs = new Properties();
        pairs.put("publisher-name","pnpdemo");
        pairs.put("card-name","John Smith");
        pairs.put("shipname","test & test & . , - _ + = ; : [ { ( ' ` %26 %");
        pairs.put("card-number","4111111111111111");
        pairs.put("card-address1","123 Main St.");
        pairs.put("card-city","NYC");
        pairs.put("card-state","NY");
        pairs.put("card-zip","11788");
        pairs.put("card-country","US");
        pairs.put("card-amount","30.00");
        pairs.put("card-exp","12/08");
        pairs.put("email","customer@you.com");
        pairs.put("card-type","Mastercard");
        pairs.put("publisher-email","me@mystoreaddress.com");

	// run the transaction and handle any exceptions 
        try {  

                System.out.println("API VERSION: " + pnp.apiVersion());

        	Properties results = pnp.doTransaction(pairs);
		// this just prints out the results you would do
                // something else
		for (Enumeration e = results.propertyNames(); e.hasMoreElements() ;) {
			String key = (String) e.nextElement();
			System.out.println(key + "\t" + results.getProperty(key));
		}
	}
	catch (TransactionEngineException e) {
		// You should handle the exception here some how
		// the messages are fairly meaningful and can be found
		// in TransactionEngine.java
		System.out.println(e.getMessage());
	}
    }
}
