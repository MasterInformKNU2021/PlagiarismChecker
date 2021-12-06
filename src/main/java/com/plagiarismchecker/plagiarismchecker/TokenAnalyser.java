package com.plagiarismchecker.plagiarismchecker;

import com.plagiarismchecker.lexer.Lexer;

import java.util.ArrayList;
import java.util.List;

public class TokenAnalyser {

    private final int perc_Rename=25;
    private final int perc_Condition=20;
    private final int perc_Cycle=25;
    private final int perc_Swap=5;
    private final int perc_Pointer=25;

    private final int renameOn=1;
    private final int conditionOn=1;
    private final int cycleOn=1;
    private final int swapOn=1;
    private final int pointerOn=1;



    List<ProgramCode> allProg=new ArrayList<>();

    public TokenAnalyser(List<ProgramCode> pc){
        if(pc==null) return;
        for(int i=0; i<pc.size(); i++){
            allProg.add(pc.get(0));
        }
    }

    public int fullEquals(Line l1, Line l2){
        if(l1.size()!=l2.size()) return 0;
        for(int i=0; i<l1.size(); i++){
            if(!l1.get(i).equals(l2.get(i))) return 0;
        }
        return 1;
    }

    public int getEqualsByRename(Line l1, Line l2){

        if(fullEquals(l1,l2)==0){
            String s=l1.get(0);
            int st=0;
            if(s.equals("Int")||s.equals("Double")||s.equals("Float")){

                st=1;}
            else if(s.equals("for")||s.equals("while")||s.equals("if")) return 0;


            for(int i=0; i<l1.size(); i++){
                if(i!=st&&!l1.get(i).equals(l2.get(i))) return 0;
            }
           // return 1;
        }
        return 1;
    }

    public int getEqualsByChangedCondition(Line l1, Line l2){
        return 0;
    }
    public int getEqualsByCycle(Line l1, Line l2){
        return 0;
    }

    public int getEqualsByPointer(Line l1, Line l2){
        int n1=l1.contains("BinaryAnd");
        int n2=l2.contains("BinaryAnd");
        if(n1>=0&&n2>=0) {
            System.out.println("YES");
            if(l1.amount("Multiply")-l1.amount("BinaryAnd")==l2.amount("Multiply")-l2.amount("BinaryAnd")) return 1;
        }
        return 0;
    }


    public static void main(String[] args) {

    }
}
