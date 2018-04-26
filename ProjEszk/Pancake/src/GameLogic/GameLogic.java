/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

import Client.Client;
import Database.DataSource;
import Database.Entities.Question;
import GUI.Modell;
import Server.Server;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Barish Arianna
 */
public class GameLogic {



    public static void startGame() {
        server.startGame();
    }
    
    Modell modell;
    Client client;
    static Server server;
    boolean first = true;
    ArrayList<Player> playersResults;
    
    public static void main(String[] args) throws Exception{ GameLogic g = new GameLogic();}
    
    public GameLogic() throws Exception{
        modell = new Modell(this);
    }
    

    
    public static void statusChange(int status){
        System.out.println(status);
    }
    

    
    public void statusZero(){
        //itt csatalozik uj jatekos
        modell.newPlayer();
    }
    
    public void statusOne(){
        try {
          if(first){
              modell.GamePanelCreate(10);
              first = false;
          }else{
              modell.refreshQuestion();
          }
          
            
        } catch (Exception ex) {
            Logger.getLogger(GameLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void statusTwo(){    
        System.out.println("Status 2");
    }

    public void statusThree(){
           playersResults = client.getPlayers();
           modell.endOfQuestion(playersResults);
    }
    
    public void startCommunication(String playerName, String ip, String port) {
        
        Thread c = new Thread( ()-> {
            client = new Client(Integer.parseInt(port), ip, playerName,this);
        });
        
        c.start();
    }
    
  
    


    public String[] getQuestion() throws SQLException {

        
        int index = client.getQuestionID();
        Question myQuestion = DataSource.getInstance().getQuestionController().getEntities().get(index);        
        List<String> answers = myQuestion.getAnswers();
        String[] questionWithAnswers = {myQuestion.getQuestionString(), answers.get(0), answers.get(1), answers.get(2), answers.get(3)};
        return questionWithAnswers;
    }

    public void sendAnswer(String answer) {
          client.setSelectedAnswer(answer);    
    }
    

    public ArrayList getResult() {
        System.out.println("meret"+client.getPlayers().size());
        return client.getPlayers();
    }

    public void startSzerver(int parseInt) {
        
        Thread s = new Thread(  ()-> {
            server = new Server(parseInt, 10,5);
            modell.serverAddress(server.getPORT());
        });
        
        s.start();
    }
        
    
}
