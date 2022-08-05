public class MinMaxInfo {

    private final int Value; // value given to the winning board
    private final int BestMove; // best move from the possible state

    public MinMaxInfo(int util, int bestmove){
        this.Value = util;
        this.BestMove = bestmove;
    }

    public int GetValue(){
        return Value;
    }
    public int GetBestMove(){
        return BestMove;
    }
}
