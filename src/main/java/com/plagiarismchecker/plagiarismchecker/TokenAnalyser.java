package com.plagiarismchecker.plagiarismchecker;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TokenAnalyser {

    private final int perc_Rename=90;
    private final int perc_Condition=80;
    private final int perc_PlusPlus =60;
    private final int perc_Pointer=70;

    private final int renameOn=1;
    private final int conditionOn=1;
    private final int plusPlusOn=1;
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
        if(l1.get(0).equals("If")&&l2.get(0).equals("If")||l1.get(0).equals("While")&&l2.get(0).equals("While")){

            if((l1.amount("Not")+l1.amount("NotEqual"))%2==(l2.amount("Not")+l2.amount("NotEqual"))%2) return 1;
            //return 1;
        }
        return 0;
    }
    public int getEqualsByPlusPlus(Line l1, Line l2){
        if(l1.amount("++")==l2.amount("++")) return 1;
        return 0;
    }

    public int getEqualsByPointer(Line l1, Line l2){
        int n1=l1.contains("BinaryAnd");
        int n2=l2.contains("BinaryAnd");
        if(n1>=0&&n2>=0) {

            if(l1.amount("Multiply")-l1.amount("BinaryAnd")==l2.amount("Multiply")-l2.amount("BinaryAnd")) return 1;
        }
        return 0;
    }

    public int makeOneComparison(Line l1, Line l2){

        if(fullEquals(l1, l2)>=1) return 100;
        int percent=0;
        if(renameOn>=1){
            if(percent<getEqualsByRename(l1,l2)*perc_Rename) percent=perc_Rename*getEqualsByRename(l1,l2);

        }
        if(conditionOn>=1){
            percent=Math.max(percent,perc_Condition* getEqualsByChangedCondition(l1,l2));
        }
        if(plusPlusOn>=1){
            percent=Math.max(percent, perc_PlusPlus *getEqualsByPlusPlus(l1,l2));
        }
        if(pointerOn>=1){
            percent=Math.max(percent,perc_Pointer* getEqualsByPointer(l1,l2));
        }

        return percent;
    }
    public String allRating(List<Line> listL1, List<Line> listL2, String name1, String name2){
        //if(listL1.size()!= listL2.size()) return 0;
        double similarity=0.0;
        for(int i=0; i<listL1.size(); i++){
            similarity+=(double)makeOneComparison(listL1.get(i), listL2.get(i))/listL1.size();

        }
        DecimalFormat df = new DecimalFormat("##.##");
        String s=("Programs: "+name1+" and : "+name2+" is similar at: "+df.format(similarity)+"%");
        return s;

    }

    public String computeAllFiles(List<ProgramCode> prog){
        String s="";
        for(int i=0; i<prog.size(); i++){
            for(int j=i+1; j< prog.size(); j++){
                s+=""+allRating(prog.get(i).getLineList(), prog.get(j).getLineList(),prog.get(i).getFile_name(), prog.get(j).getFile_name())+"\n";
            }
        }
        return s;
    }


    public static void main(String[] args) {

    }
}
