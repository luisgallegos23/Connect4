import java.util.HashMap;
import java.util.Scanner;
/** I have neither given nor received unauthorized aid on this program.**/
public class Connect4 {

    private static HashMap<Board, MinMaxInfo> Table;
    private static MinMax Current; //minmax object for board
    private static String Type; // type of algorithm being ran
    public static boolean FirstMove; // used if type ="C" to ask the depth only once
    private static boolean Debug; // to print debuging information or not

    public static void main(String[] args){
        Setup();
    }

    /** The chosen algorithm is ran and the transposition table is created**/
    public static void Setup(){
        Type = GetType();
        Debug = GetDebug();
        int[] rowcol = GetSizeofBoard();
        int num = GetNumberConnect();
        if(Type.equals("A") || Type.equals("B")){
            Current = new MinMax(Type, num, rowcol[1],rowcol[0]);
            Table = Current.GetTable();
            if(Debug){
                PrintDebug();
            }
            RunGame(rowcol[0],rowcol[1],num);
        }
        else{
            Current = new MinMax(Type, num, rowcol[1], rowcol[0]);
            FirstMove = true;
            RunGame(rowcol[0], rowcol[1],num);
        }

    }

    /** Runs Connect 4 game **/
    public static void RunGame(int row, int col, int connect){
        Board currentboard = new Board(row, col, connect);
        int currentplayer = GoesFirst();
        int nextplayer;
        if(currentplayer == 2){
            currentboard = ComputerMove(currentboard);
            currentplayer = 1;
            nextplayer = 2;

        }else{
            currentboard = UserMove(currentboard);
            currentplayer =2;
            nextplayer = 1;
        }
        FirstMove=false;
        while(!currentboard.hasWinner() ){
            if(currentplayer == 2){
                currentboard = ComputerMove(currentboard);
            }else{
                currentboard = UserMove(currentboard);
            }
            int copy = currentplayer;
            currentplayer = nextplayer;
            nextplayer = copy;
            if (currentboard.getGameState().equals(GameState.TIE)) {
                break;
            }
        }
        System.out.println("GAME OVER!");
        System.out.println(currentboard.to2DString());
        if(currentboard.getGameState().equals(GameState.TIE)){
            System.out.println("The Game is a tie.");
        }else if (currentboard.getGameState().equals(GameState.MAX_WIN) && nextplayer == 2){
            System.out.println("The winner is MAX (Computer)!");
        }else if (currentboard.getGameState().equals(GameState.MIN_WIN) && nextplayer == 1){
            System.out.println("The winner is MIN (Computer)!");
        }else if (currentboard.getGameState().equals(GameState.MAX_WIN) && nextplayer == 1){
            System.out.println("The winner is MAX (User)!");
        }else if (currentboard.getGameState().equals(GameState.MIN_WIN) && nextplayer == 2){
            System.out.println("The winner is MIN (User)!");
        }
        if(PlayAgain()){
            Setup();
        }
    }

    /** User determines which algorithm to use **/
    public static String GetType(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Run part A, B, or C: ");
        String algor = scan.nextLine();
        scan.close();
        return algor;
    }

    /** User determines if debugging info should be printed**/
    public static boolean GetDebug(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Run debugging Y or N: ");
        String debug = scan.nextLine();
        if(debug.equals("Y")){
            scan.close();
            return true;
        }else{
            scan.close();
            return false;
        }
    }

    /** User determines how many tokes in a row to WIN **/
    public static int GetNumberConnect(){
       Scanner scan = new Scanner(System.in);
       System.out.println("Number in a row to win 2-5: ");
       int num = Integer.parseInt(scan.nextLine());
       scan.close();
       return num;
    }

    /**  User determines the size of the playing board Row x Column **/
    public static int[] GetSizeofBoard(){
        int[] rowbycol = new int[2];
        Scanner scan = new Scanner(System.in);
        System.out.println("Number of Rows: ");
        rowbycol[0] = Integer.parseInt(scan.nextLine());
        System.out.println("Number of Columns: ");
        rowbycol[1] = Integer.parseInt(scan.nextLine());
        scan.close();
        return rowbycol;

    }

    /** User determines who goes first **/
    public static int GoesFirst(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Who plays first? 1=Human 2=Computer ");
        int num =  Integer.parseInt(scan.nextLine());
        scan.close();
        return num;
    }

    /** The computer makes a move **/
    public static Board ComputerMove(Board board){
        System.out.println(board.to2DString());
        if(Type.equals("C")){
            Current.RunC(board,FirstMove);
            Table = Current.GetTable();
            if(Debug){
                PrintDebug();
            }
        }
        if(!Table.containsKey(board)){
            System.out.println("This is a state that was previously pruned; re-running Search Algorithm.");
            Current.Rerun(board);
            Table = Current.GetTable();
        }
        System.out.println("Minmax value for this state: "+Table.get(board).GetValue()+", optimal move: "+Table.get(board).GetBestMove());
        MinMaxInfo info = Table.get(board);
        int move = info.GetBestMove();
        System.out.println("Computer chooses move: "+move);
        return board.makeMove(move);
    }

    /** Lets the user take their turn and make a move **/
    public static Board UserMove(Board board){
        System.out.println(board.to2DString());
        if(Type.equals("C")){
            Current.RunC(board, FirstMove);
            Table = Current.GetTable();
            if(Debug){
                PrintDebug();
            }
        }
        if(!Table.containsKey(board)){
            System.out.println("This is a state that was previously pruned; re-running Search Algorithm.");
            Current.Rerun(board);
            Table = Current.GetTable();
        }

        System.out.println("Minmax value for this state: "+Table.get(board).GetValue()+", optimal move: "+Table.get(board).GetBestMove());
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter move: ");
        int move = Integer.parseInt(scan.nextLine());
        Board newboard = board.makeMove(move);
        scan.close();
        return newboard;
    }

    /** Returns boolean weather to play agian.**/
    public static boolean PlayAgain(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Play Again (Y/N): ");
        String debug = scan.nextLine();
        if(debug.equals("Y")){
            scan.close();
            return true;
        }
        scan.close();
        return false;
    }

    /** Prints the transposition table with its keys and corresponding values **/
    public static void PrintDebug(){
        System.out.println(Table);
    }
    
}
