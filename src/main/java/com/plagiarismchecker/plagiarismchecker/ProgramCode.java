package com.plagiarismchecker.plagiarismchecker;

import java.util.ArrayList;
import java.util.List;

public class ProgramCode {
    private String file_name="";
    private List<Line> lineList=new ArrayList<>();

    public ProgramCode(String fileName){this.file_name=fileName;}
    ProgramCode(String fileNAme, List<Line> lineList2){
        this.file_name=fileNAme;
        if(lineList2==null) return;
        for (Line l1:lineList2) {
            lineList.add(l1);
        }
    }
    public void addLine(Line l){
        lineList.add(l);
    }

    @Override
    public String toString() {
        return "ProgramCode{" +
                "file_name='" + file_name + '\'' +
                ", lineList=" + lineList +
                '}';
    }
}
