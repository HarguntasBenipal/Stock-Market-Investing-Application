/* Robby Sodhi
 * J.Bains
 * 2023
 * REST_client wraps all of the headers for the rest api server (buy stock, sell stock,etc)  
 * Parent class to user_manager
 * Notes:
 * add json-simple library (included in the folder) to classpaths (edit -> preferences -> resource_locations -> classpaths) 
*/


import java.net.URL;
import java.net.HttpURLConnection;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.simple.parser.JSONParser;




public class REST_client{
  String target; //i.e http://192.168.190.246:58034
  JSONParser parser = new JSONParser(); //used to parse a JSON string to object
  
  //constructor, takes the url and stores it (meant to come from the ssdp client)
  REST_client(String url){
    target = url;
  }
  
  //private as the children of this class aren't meant to use this (parent will do the request handling and the child will manage the paramters needed, ie. the id or date range)
  //allows us to make a request to the server (takes the paramstring[header + parameters] i.e buy_stock?id=ID&ticker=TICKER&amount=AMOUNT and combines it with the target url (i.e http://192.168.190.246:58034[found from the ssdp client])
  private JSONObject makeRequest(String paramString) throws java.net.MalformedURLException, java.io.IOException{
    
    //generate the url 
    URL url = new URL(target + "/" + paramString);
    //attempt the connection
    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
    
    //if the server doesn't respond 200, assume failure. (the server is configured to send 200 on ok, 422 on illegal arguments and then 500 on completely invalid request/server-crash)
    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK){
      System.out.println("response code failed");
      System.out.println("response code was " + connection.getResponseCode());
      return null;
    }
    
    //create a stream to read through all of the received data
    BufferedReader read = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();
    
    //loop through all of the lines of received data and add it to a single string
    while ((inputLine = read.readLine()) != null) {
      response.append(inputLine);
    }
    
    read.close(); 
    
    //we know the data we are going to receive from the server is going to  be a stringified JSON object so parse it as such
    try {
      JSONObject responseJSON = (JSONObject)(parser.parse((String)response.toString()));
           //the response object from the server is usually {"valid": boolean, "data": data}, where if valid is false, the server wasn't able to complete the request (not logged in, invalid amount/balance, etc.)
            if (((String)responseJSON.get("valid")).equals("false")){
              System.out.println("valid = false"); 
              return null;
            }
    
            return responseJSON;
            
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
            return null;
        }
    
    
   
  }
  //using protected so people with access to child class can't access these methods
  //login user takes the username and password and creates the corresponding login param string (header + params) to send to the server
  //wraps the rest server's login_user header
  protected String login_user(String username, String password) throws java.net.MalformedURLException, java.io.IOException, org.json.simple.parser.ParseException, javax.naming.AuthenticationException {
    String paramString = "login_user?username=" + username + "&password=" + password;
    JSONObject data = this.makeRequest(paramString);
    //if the login fails (response from server is either "valid": false or response not 200)
    if (data == null){
      //AuthenticationException is used to note that it is most likely an error with the user credentials(user id)
     throw new javax.naming.AuthenticationException("login failed");  
    }
    
    //sessionKey is a unique id associate with the user (server generates this and sends it back if the login succeeds), allows us to re-log users in without needing to store password/username on the client -> security (this technically should expire every few weeks, but im lazy)
    return (String)data.get("sessionKey");
    
  }
  
  //takes the username and password creates the corresponding param string to send to the rest server
  //wraps the create_user header from the rest server
   protected String create_user(String username, String password) throws java.net.MalformedURLException, java.io.IOException, org.json.simple.parser.ParseException, javax.naming.AuthenticationException {
    String paramString = "create_user?username=" + username + "&password=" + password;
    JSONObject data = this.makeRequest(paramString);
    if (data == null){
     throw new javax.naming.AuthenticationException("create user failed");  
    }
   
    //the create_user header automatically logs the user in (thus returning the unique userid (sessionKey))
    return (String)data.get("sessionKey");
    
  }
  
   //takes the users id and retrieves their balance
   //wraps the get_balance header from the rest server
   protected Double get_balance(String id) throws java.net.MalformedURLException, java.io.IOException, org.json.simple.parser.ParseException, javax.naming.AuthenticationException {
    String paramString = "get_balance?id=" + id;
    JSONObject data = this.makeRequest(paramString);
    if (data == null){
     throw new javax.naming.AuthenticationException("user(id) doesn't exist");  
    }
    
    return ((Number)data.get("balance")).doubleValue();
   }
   
   //takes the users id and receivers all of the tickers that they own( in the format [[username,ticker, amount]
   //returns 2D string array of the tickers the user owns
   //wraps the get_user_ticker_data header on the rest server
   protected String[][] get_user_ticker_data(String id) throws java.net.MalformedURLException, java.io.IOException, org.json.simple.parser.ParseException, javax.naming.AuthenticationException {
    String paramString = "get_user_ticker_data?id=" + id;
    JSONObject data = this.makeRequest(paramString);
    if (data == null){
     throw new javax.naming.AuthenticationException("user(id) doesn't exist");  
    }
    
    //loop through the 2D JSONArray object and convert it into a 2D string array
    JSONArray outer = (JSONArray)data.get("user_ticker_data");
    //create String[][] of matching size to JSONArray
    String[][] new_data_array = new String[outer.size()][3];
    //loop through it
    for (int i = 0; i < outer.size(); i++){
      //grab the inner array and its contents
      JSONArray inner = (JSONArray)outer.get(i);
      String username = (String)inner.get(0);
      String ticker = (String)inner.get(1);
      String amount = String.valueOf(((Number)inner.get(2)).doubleValue()); //the amount is an integer and thus not compatible with String[][] so we convert it to a string
      //create new inner array
      String[] arr = {username, ticker, amount};
      new_data_array[i] = arr; //add it to the new outer array
    }
    return new_data_array;
   }
   
   //takes a ticker and returns the current price of that ticker (throws illegalArgument if fails)
   //wraps the get_current_stock_price header on the server
   protected Double get_current_stock_price(String ticker) throws java.net.MalformedURLException, java.io.IOException, org.json.simple.parser.ParseException, IllegalArgumentException{
    String paramString = "get_current_stock_price?ticker=" + ticker;
    JSONObject data = this.makeRequest(paramString);
    if (data == null){
     throw new IllegalArgumentException("ticker doesn't exist");  
    }
    
    return ((Number)data.get("price")).doubleValue();
    
    
   }
   
   //takes the users id, ticker and the amount and creates and sends the request to the server
    //this adds the specified amount to the users portfolio and subsequently decreased their balance by its current price * amount
   //wraps the buy_stock header on the rest server
    protected void buy_stock(String id, String ticker, String amount) throws java.net.MalformedURLException, java.io.IOException, org.json.simple.parser.ParseException, javax.naming.AuthenticationException{
    String paramString = "buy_stock?ticker=" + ticker + "&id=" + id + "&amount=" + amount;
    JSONObject data = this.makeRequest(paramString);
    if (data == null){
      //throwing IllegalArgumentException to denote that the reason the server didin't response is most likely because the user doesn't have enough balance and or ticker is invalid
      //technically, it is possible for this to be becuase the id(user) is invalid, however, I did not configure the server to differentiate between these two errors and thus cannot really tell
     throw new IllegalArgumentException("buy_stock failed (amount might be invalid, balance might be invalid, ticker might be invalid");
    }
    
   }
    
   //takes the users id, ticker and the amount and creates and sends the sell_stock request to the server
    //this removes the specifies amount from the users portfolio and subsequently increases their balance by its current price * amount
   //wraps the sell_stock header on the rest server
   protected void sell_stock(String id, String ticker, String amount) throws java.net.MalformedURLException, java.io.IOException, org.json.simple.parser.ParseException, javax.naming.AuthenticationException{
    String paramString = "sell_stock?ticker=" + ticker + "&id=" + id + "&amount=" + amount;
    JSONObject data = this.makeRequest(paramString);
    if (data == null){
     throw new IllegalArgumentException("sell_stock failed (amount might be invalid, ticker might be invalid)");
    }
   }
   
   //takes the ticker, start date (in format yyyy-mm-dd) and end date and returns the price history of a single stock
   //returns the stock history as 2D string array
   //wraps the get_stock_history_by_ticker header on the rest server
   protected String[][] get_stock_history_by_ticker(String ticker, String start, String end) throws java.net.MalformedURLException, java.io.IOException, org.json.simple.parser.ParseException, javax.naming.AuthenticationException{
    //format:
    /*
     * "[[\"2022-01-03\", \"AAPL\", 176.803859425408, 181.8247221621082, 176.68455672275658, 180.95973205566406, 104487900, 0, 0], [\"2022-01-04\", \"AAPL\", 181.5761715334321, 181.88438030728767, 178.08641564312208, 178.66307067871094, 99310400, 0, 0]]";
     *                 (Inner array) -> date, ticker, open, high, low, close, volume, dividends, stock-splits
     */
     String paramString = "get_stock_history_by_ticker?ticker=" + ticker + "&start=" + start + "&end=" + end;
    JSONObject data = this.makeRequest(paramString);
    if (data == null){
     throw new IllegalArgumentException("get stock history failed, start date might be wrong, end date might be wrong(cant be today) or ticker invalid");
    }
    
    //convert the 2D JSONArray into a 2D string array 
    
    //pull the inner array from the outer jsonObject format {ticker, [][]}
    JSONArray arr = (JSONArray)data.get(ticker);
    
    //create the new 2D array of the correct size
    String[][] result = new String[arr.size()][9];
    //loop through all of the inner arrays in the original JSONArray, pull their data and create a new String array to put into our outer array
    for (int i = 0; i < arr.size(); i++) {
      JSONArray inner = (JSONArray) arr.get(i);
      String[] inner_str_arr = new String[inner.size()]; 
      for (int x = 0; x < inner.size(); x++){
        inner_str_arr[x] = String.valueOf(inner.get(x)); //the elements can be int, double, or string, use String.vaueOf(Obj) to convert whatever it is into a string (this is safe aslong as the object is a String, Int, Double, Long, etc. If its some custom object or something weird itll give us some garbage data, so not really a good way to handle this
      }
      result[i] = inner_str_arr;
    }
    return result;
    
   }
   
   
   
   

   

  
}
