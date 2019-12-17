/*
 * transactionEngine.java
 *
 */

package pnp4;

import java.util.*;
import javax.net.ssl.*;
import java.net.*;
import java.io.*;

/**
 * Class to send a transactions for processing to PlugNpay.
 *
 * Please refer to the Remote Client Specifications for details on
 * what fields are required for each type of transaction.  It can be
 * found in the admin area under documentation.
 *
 * @author Plugnpay Technologies inc. 
 * @version 4.0.20070222 02/22/2007
 */
public class TransactionEngine {
   
    // CGI script to send transactions to. 
    private String transactionScript = "https://pay1.plugnpay.com/payment/pnpremote.cgi";

    // Method for sending transactions
    // Please do not use a GET 
    private final String REQUESTMETHOD = "POST";

    // api version
    private final String JAVAAPIVERSION = "4.0.20070222";

    // text encoding for url encoding and decoding
    private final String STRINGENCODING = "UTF-8";

    // results are stored here
    private Properties results;

    // raw response string
    private String stringResponse = "";
    
    /*
     * Creates a new instance of transactionEngine
     * initializes results
     * @param newTransactionURL URL to send transactions to
     */
    public TransactionEngine(String newTransactionURL) {
        transactionScript = newTransactionURL;
        results = new Properties();
    }

    /*
     * Creates new instance of transactionEngine
     * initialzes results
     */
    public TransactionEngine() {
        results = new Properties();
    }

    /*
     * Method to retrieve result Properties in case you need them again
     * @return Properties result list
     */
    public Properties result() {
        return results;
    }

    /*
     * Method to retrieve the raw response string from the server
     * @return String raw transaction response.
     */
    public String rawResult() {
        return stringResponse;
    }
 
    /*
     *  Method that returns the version of your current api
     *  @return String
     */ 
    public String apiVersion() {
        return JAVAAPIVERSION;
    }
 
    /*
     * Runs the transaction
     * @param data a Properties list containing transaction data
     * @return Properties a list of the results
     * @throws TransactionEngineException for problems check the message
     */ 
    public Properties doTransaction(Properties data) throws TransactionEngineException {
        // SSL connection
        HttpsURLConnection secureConnection;

        // string to send to server
        String requestString = encodeString(data);

        // connect send transaction and setup the results
        try {
            secureConnection = (HttpsURLConnection) (new URL(transactionScript)).openConnection();    
            secureConnection.setRequestMethod(REQUESTMETHOD);
            secureConnection.setDoOutput(true);
            secureConnection.setRequestProperty("Connection","close");
            secureConnection.setRequestProperty("Content-type","text/html");
            secureConnection.setRequestProperty("Accept","text/html");
            
            BufferedWriter secureOut = new BufferedWriter(new OutputStreamWriter(secureConnection.getOutputStream()));
            secureOut.write(requestString);
            secureOut.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(secureConnection.getInputStream()));

            stringResponse = reader.readLine();
            secureConnection.disconnect();
        }
        catch (MalformedURLException e) {
            // bad URL handle it
            throw(new TransactionEngineException("Bad TransactionURL set"));
        }
        catch (ProtocolException e) {
            // Request Method error
            throw(new TransactionEngineException("Bad request method try POST"));
        }
        catch (UnknownServiceException e) {
            // thrown when reading or writing 
            throw(new TransactionEngineException("Bad service " + e.getMessage()));
        }
        catch (IOException e) {
            // IO Exception maybe network problem
            throw(new TransactionEngineException("IO Exception thrown " + e.getMessage()));
        }
        catch (IllegalStateException e) {
            // thrown by setDoOutput
            // thrown by setRequestProperty
            throw(new TransactionEngineException("Bad state set " + e.getMessage()));
        }

        // if we have a response return it's Properties
        if (stringResponse != null) {
            results = decodeString(stringResponse);

            return results;
        }
        // something went wrong so return null most likely an Exception was thrown 
        return null;
    }

    /*
     * Private method used to decode a URL encoded string into it's pairs
     * @param encodedString of data to decode
     * @return Properties a list of the results
     * @throws TransactionEngineException for problems check the message
     */
    private Properties decodeString (String encodedString) throws TransactionEngineException {
        // result to be returned
        Properties decodeResult = new Properties();

        String[] pairs = encodedString.split("&");
        for (int pos=0;pos<pairs.length;pos++) {
            String[] pair = pairs[pos].split("=",2);
            if (pair[0] != null) {
                try {
                    pair[0] = URLDecoder.decode(pair[0], STRINGENCODING);
                    pair[1] = URLDecoder.decode(pair[1], STRINGENCODING);
                    decodeResult.setProperty(pair[0], pair[1]);
                }
                catch (UnsupportedEncodingException e) {
                    throw(new TransactionEngineException("bad UTF-8 encoding " + e.getMessage()));
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    throw(new TransactionEngineException("result decoding problem " + e.getMessage() + " STRING: " + encodedString));
                }
            } 
        }

        return(decodeResult);
    }

    /*
     * Private method used to encode a list of properties into a string
     * a variable named javaapiversion is added to your pairs.  Do not
     * use this variable it will be overwritten.
     * @param args Properties list
     * @return String URL encoded
     * @throws TransactionEngineException for problems check the message
     */
    private String encodeString (Properties args) throws TransactionEngineException {

        /* add a variable so that the processor knows the api version */
        args.setProperty("javaapiversion",JAVAAPIVERSION);

        StringBuffer buffer = new StringBuffer();
        Enumeration names = args.propertyNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            String value = args.getProperty(name);
            try {
                buffer.append(URLEncoder.encode(name, STRINGENCODING)+"="+URLEncoder.encode(value, STRINGENCODING));
                if (names.hasMoreElements()) {
                    buffer.append("&");
                }
             }
             catch (UnsupportedEncodingException e) {
                 throw(new TransactionEngineException("bad UTF-8 encoding " + e.getMessage()));
             }
        }

        return (buffer.toString());
    }
}
