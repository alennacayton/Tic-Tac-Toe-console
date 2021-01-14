

import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class TicTacToe {

    static Scanner sc = new Scanner(System.in);
    static Tile [] gameBoard;
    static int playerTurn = 1; // -1 for human, 1 for computer
    private static Random random = new Random();

    static HashMap<String, Integer> scores = new HashMap<>();




    public static void displayBoard()
    {


        for(int i = 0; i < gameBoard.length; i++)
        {
            if (i == 0 || i == 3 || i == 6)
            System.out.print("\t\t\t\t\t\t\t\t" + gameBoard[i].getState() + "   " + "|");

            else if (i == 1 || i == 4 || i == 7)
                System.out.print("    " + gameBoard[i].getState() + "    " + "|");
            else
                System.out.print("    " + gameBoard[i].getState());


            if( (i + 1) % 3 == 0)
            {
                System.out.print("\n");
                if(i != 8)
                System.out.println("\t\t\t\t\t\t\t----------------------------");
            }



        }
    }




    // ths method initializes the board
    public static void initBoard ()
    {
        gameBoard = new Tile [9];

        for(int i = 0; i < gameBoard.length; i++)
        {
            gameBoard[i] = new Tile();


        }


    }



    // this method resets the board

    public static void resetBoard()
    {

        for (Tile tile : gameBoard) {
            tile.setState(' ');
        }

    }



    public static void updateBoard(int move)
    {
        // if human's turn
        if(playerTurn == -1)
        {
            gameBoard[move].setState('O');
        }
        // if computer's turn
        else if (playerTurn == 1)
        {
            gameBoard[move].setState('X');
        }
        else
        {
            gameBoard[move].setState(' ');
            System.out.println("this should never print");
        }
    }


    // this method returns the marker of the winner (if there is already), "TIE" if its a tie , returns "-" if game is not yet over

    public static String checkBoard( )
    {

        // checks if theres a winner already
        for(int i = 0; i < 8; i++)
        {
            String line = switch (i) {
                case 0 -> String.valueOf(gameBoard[0].getState()) + gameBoard[3].getState() + gameBoard[6].getState();
                case 1 -> String.valueOf(gameBoard[1].getState()) + gameBoard[4].getState() + gameBoard[7].getState();
                case 2 -> String.valueOf(gameBoard[2].getState()) + gameBoard[5].getState() + gameBoard[8].getState();
                case 3 -> String.valueOf(gameBoard[0].getState()) + gameBoard[1].getState() + gameBoard[2].getState();
                case 4 -> String.valueOf(gameBoard[3].getState()) + gameBoard[4].getState() + gameBoard[5].getState();
                case 5 -> String.valueOf(gameBoard[6].getState()) + gameBoard[7].getState() + gameBoard[8].getState();
                case 6 -> String.valueOf(gameBoard[0].getState()) + gameBoard[4].getState() + gameBoard[8].getState();
                case 7 -> String.valueOf(gameBoard[2].getState()) + gameBoard[4].getState() + gameBoard[6].getState();
                default -> null;
            };

            if(line.equals("XXX"))
                return "X";

            if(line.equals("OOO"))
                return "O";



        }

        // if all boards are filled and no one won
        if(gameOver())
        {
            return "TIE";
        }

        // game not yet done
        return "-";



    }

    // this method returns true if game is over, false otherwise
    public static boolean gameOver()
    {
        int count = 0;

        for(int j = 0; j < 9; j++)
        {
            if(gameBoard[j].getState() != ' ')
            {
                count += 1;
            }

        }

        // if all boards are filled
        return count == 9;
    }



    // this method returns a random valid move
    public static int computerTurnRandom()
    {
        int min = 0;
        int max = 8;
        int num;


        do{
            num = random.nextInt(max - min + 1) + min;

        } while(gameBoard[num].getState() != ' ');

        return num;
    }



    // returns a hard coded move

    public static int computerTurnHuman()
    {


      //  System.out.println("HELLO ARE U ENTERING THIS FUNCTION");
        boolean winPossibleComputer = false;

        // checks for possible win for x if x is temporarily put in all empty spots
        for(int i = 0; i < gameBoard.length; i++)
        {


            if(gameBoard[i].getState() == ' ')
            {
                updateBoard(i);
                if(checkBoard().equals("X"))
                {
                   // System.out.println("ENTERED POSSIBLE WIN FOR COMPUTER");
                    winPossibleComputer = true;
                }
                gameBoard[i].setState(' ');

            }

            if(winPossibleComputer)
            {
                return i;
            }


        }



        boolean winPossibleHuman = false;

        // changing player marker
        playerTurn = -1;

        // if no possible wins in all moves, just block player O if player O can win in the next move
        for(int i = 0; i < gameBoard.length; i++)
        {
            if(gameBoard[i].getState() == ' ')
            {

                updateBoard(i);
                if(checkBoard().equals("O"))
                {
                   // System.out.println("ENTERED POSSIBLE WIN FOR HUMAN");
                    winPossibleHuman = true;
                }
                gameBoard[i].setState(' ');

            }

            if(winPossibleHuman)
            {
                playerTurn = 1;  // changing it back to computer's turn

                return i;
            }


        }

        // changing player marker back to computer
        playerTurn = 1;


        // if no possible wins for both parties, choose computer's next best move - which is the next empty spot

        for(int i = 0; i < gameBoard.length; i++)
        {
            if(gameBoard[i].getState() == ' ')
            {
              return i;

            }


        }


    return -1;



    }



    // computers turn AI
    public static int computerTurnMinimax()
    {
        // initial best score
        int tempScore = Integer.MIN_VALUE; // temporary best score
        int move = -1;

        /*

        X : +10  --- computer
        O : -10  --- human
        TIE: 0

        */

        // performing minimax on all possible moves computer can make based on the current board configuration
        for(int i = 0; i < gameBoard.length; i++)
        {
            // if spot is available
            if(gameBoard[i].getState() == ' ')
            {
                // depends on player move
                playerTurn = 1;
                updateBoard(i); // temporarily putting computer's marker to all available spots per iteration
                int score = miniMax(0, false);   // performing minimax on the current board state
                gameBoard[i].setState(' '); // removing computer's temporary marker


                // essentially checking the score for every possible move computer can do and replacing it with the higher score
                if(score > tempScore)
                {
                    tempScore = score;
                    move = i;

                }




            }

        }

        playerTurn = 1;
        return move;   // returns the best move for computer after considering all scenarios

    }


    public static int miniMax(int depth, boolean playerMaximizing)
    {

        // check if theres winner already
        String verdict = checkBoard();
        int result = checkWinner(verdict);

        // terminating statement if game is over
        if(result != -1)
        {
            if(result == 2)
                return scores.get("X");
            else if(result == 1)
                return scores.get("O");
            else
                return scores.get("TIE");
        }

        int tempScore; // temporary best score

        if(playerMaximizing)
        {
            tempScore = Integer.MIN_VALUE;

            for(int i = 0; i < gameBoard.length; i++)
            {
                if(gameBoard[i].getState() == ' ')
                {
                    playerTurn = 1;
                    updateBoard(i);
                    int score = miniMax(depth + 1, false);
                    gameBoard[i].setState(' ');
                    tempScore = Math.max(score, tempScore);

                }
            }

        }
        else // if player is minimizing , reverse the check process
        {
            tempScore = Integer.MAX_VALUE;
            for(int i = 0; i < gameBoard.length; i++)
            {
                if(gameBoard[i].getState() == ' ')
                {
                    // change player turn
                    playerTurn = -1;
                    updateBoard(i);
                    int score = miniMax(depth + 1, true);
                    gameBoard[i].setState(' ');

                    tempScore = Math.min(score, tempScore);

                }
            }


        }
        return tempScore;


    }


    // winner check
    public static int checkWinner(String verdict)
    {
        switch (verdict) {
            case "X" -> {
                //System.out.println("Computer Wins!");
                return 2;
            }
            case "O" -> {
               // System.out.println("You Win!");
                return 1;
            }
            case "TIE" -> {
               // System.out.println("It's a tie!");
                return 0;
            }
        }
        return -1;

    }




    public static boolean isMoveValid(int move)
    {

        if(move < 0 || move > 8)
        {
            System.out.println("\n\t\t\t\t\t\t\tMove is out of bounds! Please try another move.");
            return false;
        }






        if(gameBoard[move].getState() == ' ')
        {
            return true;
        }
        else
        {
            System.out.println("\n\t\t\t\t\t\t\tInvalid move! Please try another move.");
            return false;
        }





    }








    // start tic tac toe
    public static void startGame(int level)
    {


         // playerTurn  -1 for human, 1 for computer

        // COMPUTER or HUMAN
        String firstPlayer = "COMPUTER";


        int choice;

        do {
            int move = -1;
         //   System.out.println("\n\t\t\t\t\t\t\tSTART GAME");

            if(firstPlayer.equals("COMPUTER"))
            {
                playerTurn = 1;
            }
            else {
                System.out.println("\n\t\t\t\t\t\t************************************");
                displayBoard();
                playerTurn = -1;
            }


            while (!gameOver()) {

                if (playerTurn == 1) // computer's turn
                {
                    System.out.println("\n\t\t\t\t\t\t************************************");
                    System.out.println("\n\t\t\t\t\t\t\tCOMPUTER'S MOVE: \n");

                    // random
                    if(level == 0)
                    {
                        //System.out.println("LEVEL 0 MOVE");
                         move = computerTurnRandom();

                    }
                    // smart-ish
                    else if(level == 1)
                    {
                        //System.out.println("LEVEL 1 MOVE");
                        move = computerTurnHuman();

                    }


                    // smart
                    else if(level == 2)
                    {
                       // System.out.println("LEVEL 2 MOVE");
                        move = computerTurnMinimax();

                    }



                }
                else // human's turn
                {
                    System.out.println("\n\t\t\t\t\t\t************************************");
                    System.out.println("\n\t\t\t\t\t\t\tYOUR TURN:");

                        do{
                            System.out.print("\n\t\t\t\t\t\t\tEnter move: ");
                            move = sc.nextInt() - 1;

                        }while(!isMoveValid(move)); //check move validity

                }

                updateBoard(move);
                displayBoard();

                if(playerTurn == 1)
                {
                    playerTurn = -1;
                }
                else
                {
                    playerTurn = 1;
                }

                //check if theres winner
                String verdict = checkBoard();


                if(checkWinner(verdict) == 2 || checkWinner(verdict) == 1 || checkWinner(verdict) == 0)
                {
                    switch (verdict) {
                        case "X" -> System.out.println("\nComputer Wins!");

                        case "O" -> System.out.println("\nYou Win!");

                        case "TIE" -> System.out.println("\nIt's a tie!");

                    }


                    break;
                }

            }



            resetBoard();  // resetting the board for next round

            System.out.println("*****************************");
            System.out.println("\nPlay Again?");
            System.out.println("\n[1] Yes");
            System.out.println("[2] No");
            System.out.print("\nEnter Choice: ");
            choice = sc.nextInt();

            System.out.println("*****************************");

            if(firstPlayer.equals("COMPUTER"))
            {
                firstPlayer = "HUMAN";
            }
            else
            {
                firstPlayer = "COMPUTER";
            }


        } while(choice == 1);




    }




    public static void main (String [] args)
    {

        int nChoice;
        boolean exit = false;
        initBoard();   // initializing the board

        // for minimax algorithm
        scores.put("X", 1);
        scores.put("O", -1);
        scores.put("TIE", 0);


        while(!exit)
        {


            System.out.println("\n" +
                    "██╗    ██╗███████╗██╗      ██████╗ ██████╗ ███╗   ███╗███████╗    ████████╗ ██████╗     ████████╗██╗ ██████╗    ████████╗ █████╗  ██████╗    ████████╗ ██████╗ ███████╗\n" +
                    "██║    ██║██╔════╝██║     ██╔════╝██╔═══██╗████╗ ████║██╔════╝    ╚══██╔══╝██╔═══██╗    ╚══██╔══╝██║██╔════╝    ╚══██╔══╝██╔══██╗██╔════╝    ╚══██╔══╝██╔═══██╗██╔════╝\n" +
                    "██║ █╗ ██║█████╗  ██║     ██║     ██║   ██║██╔████╔██║█████╗         ██║   ██║   ██║       ██║   ██║██║            ██║   ███████║██║            ██║   ██║   ██║█████╗  \n" +
                    "██║███╗██║██╔══╝  ██║     ██║     ██║   ██║██║╚██╔╝██║██╔══╝         ██║   ██║   ██║       ██║   ██║██║            ██║   ██╔══██║██║            ██║   ██║   ██║██╔══╝  \n" +
                    "╚███╔███╔╝███████╗███████╗╚██████╗╚██████╔╝██║ ╚═╝ ██║███████╗       ██║   ╚██████╔╝       ██║   ██║╚██████╗       ██║   ██║  ██║╚██████╗       ██║   ╚██████╔╝███████╗\n" +
                    " ╚══╝╚══╝ ╚══════╝╚══════╝ ╚═════╝ ╚═════╝ ╚═╝     ╚═╝╚══════╝       ╚═╝    ╚═════╝        ╚═╝   ╚═╝ ╚═════╝       ╚═╝   ╚═╝  ╚═╝ ╚═════╝       ╚═╝    ╚═════╝ ╚══════╝\n" +
                    "                                                                                                                                                                       \n");


         //   System.out.println("____________________________");
            System.out.println("*********TIC-TAC-TOE*********");
            System.out.println("\nChoose computer level: ");
            System.out.println("\n[1] Level 0 ");
            System.out.println("[2] Level 1 ");
            System.out.println("[3] Level 2 ");
            System.out.println("[4] Exit ");

            System.out.print("\nEnter choice: ");
            nChoice = sc.nextInt();

            System.out.println("*****************************");


            switch (nChoice) {
                case 1 -> startGame(0);
                case 2 -> startGame(1);
                case 3 -> startGame(2);
                case 4 -> System.exit(1);
                default -> exit = true;
            }




        }










    }




}
