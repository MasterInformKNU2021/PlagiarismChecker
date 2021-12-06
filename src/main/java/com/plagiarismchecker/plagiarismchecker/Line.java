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

    @Override
    public String toString() {
        return "Line{" +
                "line_number=" + line_number +
                ", tokensList=" + tokensList +
                '}';
    }
}
