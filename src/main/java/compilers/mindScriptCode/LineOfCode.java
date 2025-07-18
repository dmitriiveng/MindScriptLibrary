package compilers.mindScriptCode;

import static compilers.mindScriptCode.MindScriptData.*;

public class LineOfCode {
    public enum lineType{
        cycleOrConstruction,
        builtInMethodCall,
        method,
        operation,
        returnValueFromMethod
    }
    boolean isEmpty;
    public lineType type;
    public String firstWord;
    public boolean hasInsideCode;//для строк, начинающих цикл, метод, конструкцию иф елс и т. д.
    public int spacing;//отступ
    public String value;

    public LineOfCode(String value){
        //value
        this.value = value.split("//")[0];
        //is empty test
        isEmpty = true;
        for(int i = 0; i < this.value.length(); i++){
            if (this.value.charAt(i) != ' '){
                isEmpty = false;
            }
        }
        //spacing
        spacing = getSpacing(this.value);
        //firstWord
        firstWord = getFirstWord(this.value);
        //type
        if(cyclesAndConstructions.contains(firstWord)){
            type = lineType.cycleOrConstruction;
            hasInsideCode = true;
        }
        else if(builtInMethods.contains(firstWord)){
            type = lineType.builtInMethodCall;
            hasInsideCode = false;
        }
        else if(newMethodKeyWord.equals(firstWord)){
            type = lineType.method;
            hasInsideCode = true;
        }
        else if(methodReturnValueKeyWord.equals(firstWord)){
            type = lineType.returnValueFromMethod;
            hasInsideCode = false;
        }
        else {
            type = lineType.operation;
            hasInsideCode = false;
        }
    }

    private int getSpacing(String value){
        int spacesCount = 0;
        for(int i = 0; i < value.length(); i++){
            if(value.charAt(i) != ' '){
                return spacesCount;
            }
            spacesCount++;
        }
        return spacesCount;
    }
    private String getFirstWord(String value){
        StringBuilder firstWord = new StringBuilder();
        for(int i = 0; i < value.length(); i++){
            if(MindScriptData.mathData.wordsAndValuesChars.contains(String.valueOf(value.charAt(i)))){
                firstWord.append(value.charAt(i));
            }
            else if(!firstWord.isEmpty() || value.charAt(i) != ' '){
                return firstWord.toString();
            }
        }
        return firstWord.toString();
    }
}
