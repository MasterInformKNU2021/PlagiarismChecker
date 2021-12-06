package com.plagiarismchecker.plagiarismchecker;

import java.util.ArrayList;
import java.util.List;

public class Line {
    int line_number=0;
    List<String> tokensList=new ArrayList<>();

    public Line(int number){this.line_number=number;}
    public Line(int number, List<String> inpList){
        this.line_number=number;
        if(inpList==null) return ;
        for(int i=0; i<inpList.size(); i++){
            tokensList.add(inpList.get(i));
        }
    }
    public void addToken(String oneToken){
        tokensList.add(oneToken);
    }
    public int size(){
        return this.tokensList.size();
    }
    public String get(int i){
        return this.tokensList.get(i);
    }
    public int contains(String s){
        for(int i=0; i<this.tokensList.size(); i++){
            if(s.equals(tokensList.get(i))) return i;
        }
        return -1;
    }
    public int containsLast(String s){
        int res=-1;
        for(int i=0; i<this.tokensList.size();i++){
            if(this.tokensList.get(i).equals(s)) res=i;
        }
        return res;
    }
    public int amount(String s){
        int am=0;
        for(int i=0; i<this.tokensList.size(); i++){
            if(tokensList.get(i).equals(s)) am++;
        }
        return am;
    }

    @Override
    public String toString() {
        return "Line{" +
                "line_number=" + line_number +
                ", tokensList=" + tokensList +
                '}';
    }
}
