import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class MinMax {

    private final HashMap<Board,MinMaxInfo> TranspositionTable = new HashMap<>();
    private int FullDepth;// used for depth for type "C"
    private final String Type;
    private  int Guranteewin;
    private final int Connect;
    private int Prunes; // number of times the tree was pruned

    /** Create the Transposition table based on the desired algorithm **/
    public MinMax(String type, int numconnect, int col, int row){
        Type =type;
        Connect = numconnect;
        Board board = new Board(row,col,Connect);
        if(type.equals("A")){
            MinMaxSearch(board);
            Guranteewin = TranspositionTable.get(board).GetValue();
            PrintTableInfo();
        }else if(type.equals("B")){
            AlphaBetaSearch(board,Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            Guranteewin = TranspositionTable.get(board).GetValue();
            PrintTableInfo();
        }

    }


    /** Runs the Minmax Algorithm for all possible moves**/
    public MinMaxInfo MinMaxSearch( Board currentboard){
        if (TranspositionTable.containsKey(currentboard)){
            return TranspositionTable.get(currentboard);
        }

        else if(currentboard.hasWinner() || currentboard.getGameState().equals(GameState.TIE)){
            int util = (int) GetUtility(currentboard);
            MinMaxInfo info = new MinMaxInfo(util,-1);
            TranspositionTable.put(currentboard,info);
            return info;
        }

        else if(currentboard.getPlayerToMoveNext().getNumber() == 1){
            double v = Double.NEGATIVE_INFINITY;
            int best_move = -1;
            for(Integer move: PossibleActions(currentboard)){
                Board child_state = currentboard.makeMove(move);
                MinMaxInfo info = MinMaxSearch(child_state);
                double v2 = info.GetValue();
                if(v2 > v){
                    v = v2;
                    best_move = move;
                }
            }
            MinMaxInfo info = new MinMaxInfo((int) v,best_move);
            TranspositionTable.put(currentboard,info);
            return info;
        }

        else{
            double v = Double.POSITIVE_INFINITY;
            int best_move = -1;
            for(Integer move: PossibleActions(currentboard)){
                Board board = currentboard.makeMove(move);
                MinMaxInfo info = MinMaxSearch(board);
                double v2 = info.GetValue();
                if(v2 < v){
                    v = v2;
                    best_move = move;
                }
            }
            MinMaxInfo info = new MinMaxInfo((int) v,best_move);
            TranspositionTable.put(currentboard,info);
            return info;
        }

    }

    /** Runs the MaxMin Algorithm with pruning
     Makes the algorithm faster by preventing all possible nodes to be compared **/
    public MinMaxInfo AlphaBetaSearch(Board currentboard, double alpha, double beta){
        if (TranspositionTable.containsKey(currentboard)){
            return TranspositionTable.get(currentboard);
        }

        else if(currentboard.hasWinner() || currentboard.getGameState().equals(GameState.TIE)){
            int util = (int) GetUtility(currentboard);
            MinMaxInfo info = new MinMaxInfo(util,-1);
            TranspositionTable.put(currentboard,info);
            return info;
        }

        else if(currentboard.getPlayerToMoveNext().getNumber() == 1){
            double v = Double.NEGATIVE_INFINITY;
            int best_move = -1;
            for(Integer move: PossibleActions(currentboard)){
                Board child_state = currentboard.makeMove(move);
                MinMaxInfo info = AlphaBetaSearch(child_state,alpha,beta);
                double v2 = info.GetValue();
                if(v2 > v){
                    v = v2;
                    best_move = move;
                    if(v > alpha){
                        alpha=v;
                    }
                }if(v >= beta){
                    Prunes++;
                    return new MinMaxInfo((int) v, best_move);
                }
            }
            MinMaxInfo info = new MinMaxInfo((int) v,best_move);
            TranspositionTable.put(currentboard,info);
            return info;
        }

        else{
            double v = Double.POSITIVE_INFINITY;
            int best_move = -1;
            for(Integer move: PossibleActions(currentboard)){
                Board child_state = currentboard.makeMove(move);
                MinMaxInfo info = AlphaBetaSearch(child_state, alpha,beta);
                double v2 = info.GetValue();
                if(v2 < v){
                    v = v2;
                    best_move = move;
                    if(v < beta){
                        beta=v;
                    }
                }if(v <= alpha){
                    Prunes++;
                    return new MinMaxInfo((int) v, best_move);
                }
            }
            MinMaxInfo info = new MinMaxInfo((int) v,best_move);
            TranspositionTable.put(currentboard,info);
            return info;
        }
    }

    /** MaxMin w/ pruning and w/ heuristic **/
    public MinMaxInfo ABHeuristicSearch(Board currentboard, double alpha , double beta, int depth){
        if (TranspositionTable.containsKey(currentboard)){
            return TranspositionTable.get(currentboard);
        }

        else if(currentboard.hasWinner() || currentboard.getGameState().equals(GameState.TIE)){
            int util = (int) GetUtility(currentboard);
            MinMaxInfo info = new MinMaxInfo(util,-1);
            TranspositionTable.put(currentboard,info);
            return info;
        }

        else if(FullDepth == depth){
            int value = Heuristic(currentboard);
            MinMaxInfo info = new MinMaxInfo(value,-1);
            TranspositionTable.put(currentboard,info);
            return info;
        }
        else if(currentboard.getPlayerToMoveNext().getNumber() == 1){
            double v = Double.NEGATIVE_INFINITY;
            int best_move = -1;
            for(Integer move: PossibleActions(currentboard)){
                Board child_state = currentboard.makeMove(move);
                MinMaxInfo info = ABHeuristicSearch(child_state,alpha,beta, depth++);
                double v2 = info.GetValue();
                if(v2 > v){
                    v = v2;
                    best_move = move;
                    if(v > alpha){
                        alpha=v;
                    }
                }if(v >= beta){
                    Prunes++;
                    return new MinMaxInfo((int) v, best_move);
                }
            }
            MinMaxInfo info = new MinMaxInfo((int) v,best_move);
            TranspositionTable.put(currentboard,info);
            return info;
        }

        else{
            double v = Double.POSITIVE_INFINITY;
            int best_move = -1;
            for(Integer move: PossibleActions(currentboard)){
                Board child_state = currentboard.makeMove(move);
                MinMaxInfo info = ABHeuristicSearch(child_state, alpha,beta, depth++);
                double v2 = info.GetValue();
                if(v2 < v){
                    v = v2;
                    best_move = move;
                    if(v < beta){
                        beta=v;
                    }
                }if(v <= alpha){
                    Prunes++;
                    return new MinMaxInfo((int) v, best_move);
                }
            }
            MinMaxInfo info = new MinMaxInfo((int) v,best_move);
            TranspositionTable.put(currentboard,info);
            return info;
        }
    }

    /** Returns the value for a possible chosen action **/
    public double GetUtility(Board state){
        double util = ((10000.0 * state.getRows() * state.getCols())/ state.getNumberOfMoves());
        if(state.getGameState().equals(GameState.MIN_WIN)){
            return util * -1;
        }if (state.getGameState().equals(GameState.MAX_WIN)){
            return util;
        }

        return 0;
    }

    /** Returns a list of boards with the possible action that can occur **/
    public List<Integer> PossibleActions(Board state){
        List<Integer> moves = new ArrayList<>();
        for(int x = 0; x < state.getCols(); x++){
            if(!state.isColumnFull(x)){
              moves.add(x);
            }
        }
        return moves;
    }

    /** Heuristic function that returns a value based on the number of moves and empty spots **/
    public int Heuristic(Board state){
        int possiblewin = 0;
        byte[][] board = state.GetBoard();
        for (int r = 1; r < state.getRows() - 1; r++) {
            for (int c = 0; c < state.getCols() - 1; c++) {
                if(board[r][c] != state.getPlayerToMoveNext().getNumber()){
                    continue;
                }
               if(board[r][c] == board[r][c+1]){
                   possiblewin+=5;
               }if(board[r][c] == board[r+1][c]){
                   possiblewin+=5;
                }if(board[r][c] == board[r+1][c+1]){
                    possiblewin+=5;
                }if((board[r][c] == board[r - 1][c + 1])){
                    possiblewin+=5;
                }
            }
        }

        return possiblewin;
    }

    /** Ask the user for a depth search **/
    public void SetDepth(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Depth to search:");
        FullDepth = Integer.parseInt(scan.nextLine());
        scan.close();
    }

    /** Returns the transposition table **/
    public HashMap<Board,MinMaxInfo> GetTable(){
        return TranspositionTable;
    }

    /** Runs the type of algorithm again due to state being pruned**/
    public void Rerun(Board board){
        TranspositionTable.clear();
        if (Type.equals("B")){
            AlphaBetaSearch(board,Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        }
        else if(Type.equals("C")){
            ABHeuristicSearch(board,Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0);
        }
    }

    /** Prints the info of the Transposition Table **/
    public void PrintTableInfo(){
        System.out.println("Transposition table has "+TranspositionTable.size()+" states.");
        if (Type.equals("B")){
            System.out.println("The tree was pruned "+Prunes+" times.");
        }
        if(Type.equals("A")||Type.equals("B")){
        if(Guranteewin > 0){
            System.out.println("First player has a guaranteed win with perfect play.");
        }else if(Guranteewin < 0){
            System.out.println("Second player has a guaranteed win with perfect play.");
        }else{
            System.out.println("There is a guaranteed tie with perfect play on both sides.");
        }}
    }

    /** Runs Algorithm with heuristic at every turn**/
    public void RunC(Board board, boolean first) {
        Prunes=0;
        if(first){
            SetDepth();
        }
        TranspositionTable.clear();
        ABHeuristicSearch(board, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0);
        PrintTableInfo();
    }


}
