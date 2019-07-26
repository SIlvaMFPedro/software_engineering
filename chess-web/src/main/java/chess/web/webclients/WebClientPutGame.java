/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.web.webclients;

import chess.web.GameStateBean;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



/**
 *
 * @author pedro
 */
public class WebClientPutGame {
//    // http://localhost:8080/api/chess/game
//    @SuppressWarnings("CallToPrintStackTrace")
//    public static void main(String[] args) {
//
//      try {
//
//            URL url = new URL("http://localhost:8080/api/chess/game");
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setDoOutput(true);
//            conn.setRequestMethod("PUT");
//            conn.setRequestProperty("Content-Type", "application/json");
//            
//            GameStateBean gameInfo = (GameStateBean) conn.getContent();
//
//            String input = "{\"game\":\"1\",\"name\":\"Game 1\", \"state\":\"Still going\", \"info\": " + gameInfo.toString() + "\"}";
//
//            OutputStream os = conn.getOutputStream();
//            os.write(input.getBytes());
//            os.flush();
//
//            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
//                    throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
//            }
//
//            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
//
//            String output;
//            System.out.println("Output from Server .... \n");
//            while ((output = br.readLine()) != null) {
//                    System.out.println(output);
//            }
//
//            conn.disconnect();
//
//      } catch (MalformedURLException e) {
//
//            e.printStackTrace();
//
//      } catch (IOException e) {
//
//            e.printStackTrace();
//
//     }
//
//    }
        //http://localhost:8080/api/chess/game?id=1
       @SuppressWarnings("CallToPrintStackTrace")
       public static void main(String[] args) {

         try {

               URL url = new URL("http://localhost:8080/api/chess/game?id=1");
               HttpURLConnection conn = (HttpURLConnection) url.openConnection();
               conn.setDoOutput(true);
               conn.setRequestMethod("PUT");
               conn.setRequestProperty("Content-Type", "application/json");

               if (conn.getResponseCode() != 200) {
                       throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
               }

               BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

               String output;
               System.out.println("Output from Server .... \n");
               while ((output = br.readLine()) != null) {
                       System.out.println(output);
               }

               conn.disconnect();

   //            
   //            String input = "{\"new\":\"game\",\"name\":\"New Game\", \"Command\":\"Reset\", \"info\": \"" + conn.getContent().toString() + "\"}";
   //            
   //            /* Output info */
   //            //OutputStream os = conn.getOutputStream();
   //            DataOutputStream os = new java.io.DataOutputStream(conn.getOutputStream());
   //            os.write(input.getBytes());
   //            //os.flush();
   //
   //            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
   //                    throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
   //            }
   //            
   //            /* Input info */
   //            //BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
   //            DataInputStream in = new java.io.DataInputStream(conn.getInputStream());
   //            BufferedReader br = new BufferedReader(new InputStreamReader((in)));
   //
   //            String output;
   //            System.out.println("Output from Server .... \n");
   //            while ((output = br.readLine()) != null) {
   //                    System.out.println(output);
   //            }
   //            os.flush();
   //            conn.disconnect();

         } catch (MalformedURLException e) {

               e.printStackTrace();

         } catch (IOException e) {

               e.printStackTrace();

        }
    }

}
