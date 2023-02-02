public class Main{
  public static void main(String[] args) throws java.net.MalformedURLException, java.io.IOException, org.json.simple.parser.ParseException, javax.naming.AuthenticationException {
   user_manager t = new user_manager("http://192.168.0.41:56515", "robby", "test", false);
   System.out.println(t.get_balance());
//   String[][] arr = t.get_user_ticker_data();
//   for (int i = 0; i < arr.length; i++){
//     for (int x = 0; x < 3; x++){
//      System.out.print(arr[i][x]); 
//      System.out.print(" ");
//     }
//     System.out.println("");
//   }
//   System.out.println(t.get_current_stock_price("AAPL"));
//   
   //t.buy_stock("AAPL", "5");
   //t.sell_stock("AAPL", "2");
//   String[][] data = t.get_stock_history_by_ticker("AAPL", "30");
//    for (int i = 0; i < data.length; i++) {
//        for (int j = 0; j < data[i].length; j++) {
//            System.out.print(data[i][j] + " ");
//        }
//    }
//    System.out.println();
//   
  }
  
}