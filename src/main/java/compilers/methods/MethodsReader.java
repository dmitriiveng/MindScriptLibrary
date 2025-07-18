package compilers.methods;

import mlogConstructors.mathEngine.MathData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface MethodsReader {
    public static MethodData getMethodDataFromString(String methodString) throws IOException {
        //method name
        StringBuilder methodNameStringBuilder = new StringBuilder();
        int i = 0;
        while (methodString.charAt(i) != '(' && i < methodString.length()){
            methodNameStringBuilder.append(methodString.charAt(i));
            i++;
        }
        if(methodNameStringBuilder.isEmpty()){
            throw new IOException("method name in empty for callMethodString: " + methodString);
        }
        String methodName = methodNameStringBuilder.toString();
        while (methodName.contains(" ")){ methodName = methodName.replace(" ",  "");}
        //args
        StringBuilder argsStringBuilder = new StringBuilder();
        while (i < methodString.length()){
            argsStringBuilder.append(methodString.charAt(i));
            i++;
        }
        //delete left "("
        for(int i1 = 0; i1 < argsStringBuilder.length(); i1++){
            if(argsStringBuilder.charAt(i1) == '('){
                argsStringBuilder.deleteCharAt(i1);
                break;
            }
        }
        //delete right ")"
        for(int i1 = argsStringBuilder.length() - 1; i1 >= 0; i1--){
            if(argsStringBuilder.charAt(i1) == ')'){
                argsStringBuilder.deleteCharAt(i1);
                break;
            }
        }
        //split into args
        List<String> argsListString = new ArrayList<>();
        StringBuilder arg = new StringBuilder();
        int openedBracketsCount = 0;
        int closedBracketsCount = 0;
        for(int i1 = 0; i1 < argsStringBuilder.length(); i1++){
            if (argsStringBuilder.charAt(i1) == '('){
                openedBracketsCount ++;
                arg.append(argsStringBuilder.charAt(i1));
            }
            else if (argsStringBuilder.charAt(i1) == ')'){
                closedBracketsCount ++;
                arg.append(argsStringBuilder.charAt(i1));
            }
            else if(argsStringBuilder.charAt(i1) == ',') {
                if (openedBracketsCount == closedBracketsCount) {
                    argsListString.add(arg.toString());
                    arg = new StringBuilder();
                }
                else {
                    arg.append(argsStringBuilder.charAt(i1));
                }
            }
            else {
                arg.append(argsStringBuilder.charAt(i1));
            }
        }
        if(!arg.isEmpty()){argsListString.add(arg.toString());}//добавляем последний аргумент, который не мог быть добавлен в цикле
        return new MethodData(methodName, argsListString);
    }

    static List<String> findMethodsInExpression(String expression, MathData mathData) throws IOException {
        List<String> methods =  new ArrayList<>();
        if(expression.isEmpty()){return methods;}
        StringBuilder word = new StringBuilder();//потенциально имя метода
        boolean lastElementIsWord = mathData.wordsAndValuesChars.contains(String.valueOf(expression.charAt(0)));
        boolean currentCharIsInWord;
        if(lastElementIsWord){word.append(expression.charAt(0));}
        int i = 1;
        while(i < expression.length()){
            currentCharIsInWord = mathData.wordsAndValuesChars.contains(String.valueOf(expression.charAt(i)));
            if(currentCharIsInWord || expression.charAt(i) == ' '){
                if(currentCharIsInWord){lastElementIsWord = true;}
                word.append(expression.charAt(i));
                i++;
            }
            else if(expression.charAt(i) == '(' && lastElementIsWord){
                List<String> strBlocksFiltered = new ArrayList<>();
                for(String strBlock: word.toString().split(" ")){
                    if(!strBlock.isEmpty()){
                        strBlocksFiltered.add(strBlock);
                    }
                }
                if(strBlocksFiltered.size() > 1){
                    word = new StringBuilder(strBlocksFiltered.get(strBlocksFiltered.size() - 1));
                }
                word.append(expression.charAt(i));
                int openedBracketsCount = 1;
                int closedBracketsCount = 0;
                i++;
                while (openedBracketsCount != closedBracketsCount && i < expression.length()){
                    if (expression.charAt(i) == '('){
                        openedBracketsCount ++;
                    }
                    else if (expression.charAt(i) == ')'){
                        closedBracketsCount ++;
                    }
                    word.append(expression.charAt(i));
                    i++;
                }
                if(openedBracketsCount == closedBracketsCount){
                    methods.add(word.toString());
                }
                else {
                    throw new IOException("closedBracketsCount < openedBracketsCount for expression: " + expression);
                }
            }
            else {
                lastElementIsWord = false;
                word = new StringBuilder();
                i++;
            }
        }
        return methods;
    }
}
