package com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.mathEngine;

import com.github.dmitriiveng.mindScriptCompiler.compilers.methods.MethodData;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.Settings;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.CodePart;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.methods.CallMethod;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.operations.ComplexOperation;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.operations.Operation;
import com.github.dmitriiveng.mindScriptCompiler.compilers.methods.MethodsReader;

import java.io.IOException;
import java.util.*;

public class MathematicalExpressionReader {
    public static int nextExpressionIndex = 0;//потом придумать что-то, привязанное к конкретному коду
    public static ComplexOperation readExpression(String expression, MathData mathData) throws IOException {
        ComplexOperation finalComplexOperation = null;
        //methods
        List<CodePart> finalCodePartsList = new ArrayList<>();
        List<String> methodsStrings = MethodsReader.findMethodsInExpression(expression, mathData);
        for(int i = 0; i < methodsStrings.size(); i++){
            MethodData methodData = MethodsReader.getMethodDataFromString(methodsStrings.get(i));
            CallMethod callMethod = new CallMethod(methodData.name, Settings.multipurposeMethodReturnVarNameString + i, methodData.args, mathData);
            finalCodePartsList.add(callMethod);
            expression = expression.replace(methodsStrings.get(i), Settings.multipurposeMethodReturnVarNameString + i);
        }
        //brackets expression
        ComplexOperation braketsExpressionComplexOperation = readBracketsExpression(expression, mathData);
        finalCodePartsList.addAll(braketsExpressionComplexOperation.insideOperations);
        finalComplexOperation = new ComplexOperation(
                finalCodePartsList,
                braketsExpressionComplexOperation.finalVarName,
                braketsExpressionComplexOperation.helpVarLastIndexPlusOne
                );
        nextExpressionIndex++;
        return finalComplexOperation;
    }
    //brackets expression
    private static ComplexOperation readBracketsExpression(String expression, MathData mathData) throws IOException {
        int helpVarCount = 0;
        ComplexOperation finalComplexOperation = null;
        while (expression.contains("(") || expression.contains(")")){
            String reviewExpression = expression;
            while (reviewExpression.contains("(") || reviewExpression.contains(")")){
                reviewExpression = getFirstInBracketsExpression(reviewExpression);
            }
            ComplexOperation complexOperation = readLineExpression(reviewExpression, mathData, helpVarCount);
            assert complexOperation != null;
            helpVarCount = complexOperation.helpVarLastIndexPlusOne;
            expression = expression.replace("(" + reviewExpression + ")", " " + complexOperation.finalVarName + " ");
            if(finalComplexOperation == null){finalComplexOperation = complexOperation;}
            else {finalComplexOperation.Add(complexOperation);}
        }
        ComplexOperation complexOperation = readLineExpression(expression, mathData, helpVarCount);
        assert complexOperation != null;
        helpVarCount = complexOperation.helpVarLastIndexPlusOne;
        if(finalComplexOperation == null){finalComplexOperation = complexOperation;}
        else {finalComplexOperation.Add(complexOperation);}
        return finalComplexOperation;
    }
    private static String getFirstInBracketsExpression(String expression) throws IOException {
        boolean firstBracketFound = false;
        int openedBracketsCount = 0;
        int closedBracketsCount = 0;
        StringBuilder currentExpression = new StringBuilder();
        for(int i = 0; i < expression.length(); i++){
            if (expression.charAt(i) == '('){
                if(!firstBracketFound){firstBracketFound = true;}
                else {currentExpression.append(expression.charAt(i));}
                openedBracketsCount ++;
            }
            else if (expression.charAt(i) == ')'){
                closedBracketsCount ++;
                if(firstBracketFound && openedBracketsCount == closedBracketsCount){
                    return currentExpression.toString();
                }
                else {currentExpression.append(expression.charAt(i));}
            }
            else if(firstBracketFound){
                currentExpression.append(expression.charAt(i));
            }
            if(firstBracketFound && openedBracketsCount < closedBracketsCount){
                throw new IOException("openedBracketsCount < closedBracketsCount for expression: " + expression);
            }
        }
        return expression;
    }
    //line expression
    private static ComplexOperation readLineExpression(String expression, MathData mathData, int helpVarCount) throws IOException {
        List<CodePart> operations = new ArrayList<CodePart>();
        List<MathUnit> mathUnits = divideLineExpressionIntoMathUnits(expression, mathData);
        String resultVarName = null;
        if(mathUnits.size() == 1 && mathUnits.get(0).type == MathUnit.mathUnitType.wordOrValue){
            resultVarName = mathUnits.get(0).stringValue;
        }
        for (int priority = 0; priority <= mathData.operationsPrioritiesCount; priority++) {
            int operationIndex = 0;
            while (operationIndex < mathUnits.size()) {
                MathUnit mathUnit = mathUnits.get(operationIndex);
                if (mathUnit.type == MathUnit.mathUnitType.operator && mathUnit.operatorPriority == priority) {
                    //assignment
                    if (mathUnit.operationIsAssignment == Operation.operationIsAssignment.assignment) {
                        if (operationIndex == 0 || operationIndex == mathUnits.size() - 1) {
                            throw new IOException("no args for operator: " + mathUnit.stringValue);
                        }
                        if (mathUnits.get(operationIndex - 1).type != MathUnit.mathUnitType.wordOrValue &&
                                mathUnits.get(operationIndex + 1).type != MathUnit.mathUnitType.wordOrValue) {
                            throw new IOException("no args for operator: " + mathUnit.stringValue);
                        }
                        resultVarName = Settings.helpVarString + nextExpressionIndex + helpVarCount;
                        operations.add(new Operation(
                                mathUnits.get(operationIndex - 1).stringValue,
                                mathUnits.get(operationIndex + 1).stringValue,
                                mathUnit.operatorType,
                                Operation.operationIsAssignment.assignment
                        ));
                        mathUnits.remove(operationIndex + 1);
                        mathUnits.remove(operationIndex);
                        //operationIndex does not change
                    }
                    //binary
                    else if (mathUnit.operatorArgAmount == Operation.operatorArgAmount.binary) {
                        if (operationIndex == 0 || operationIndex == mathUnits.size() - 1) {
                            throw new IOException("no args for operator: " + mathUnit.stringValue);
                        }
                        if (mathUnits.get(operationIndex - 1).type != MathUnit.mathUnitType.wordOrValue &&
                                mathUnits.get(operationIndex + 1).type != MathUnit.mathUnitType.wordOrValue) {
                            throw new IOException("no args for operator: " + mathUnit.stringValue);
                        }
                        resultVarName = Settings.helpVarString + nextExpressionIndex + helpVarCount;
                        operations.add(new Operation(
                                resultVarName,
                                mathUnits.get(operationIndex - 1).stringValue,
                                mathUnit.operatorType,
                                mathUnits.get(operationIndex + 1).stringValue
                        ));
                        mathUnits.remove(operationIndex + 1);
                        mathUnits.remove(operationIndex);
                        mathUnits.set(operationIndex - 1, new MathUnit(resultVarName));//заменяем оператор и переменную/число на новую переменную, которую мы получили
                        helpVarCount++;
                        //operationIndex does not change
                    }
                    //unary
                    else if (mathUnit.operatorArgAmount == Operation.operatorArgAmount.unary) {
                        if (mathUnit.operatorType == Operation.operatorType.not) {
                            if (operationIndex == mathUnits.size() - 1) {
                                throw new IOException("no args for operator: " + mathUnit.stringValue);
                            }
                            if (mathUnits.get(operationIndex + 1).type != MathUnit.mathUnitType.wordOrValue) {
                                throw new IOException("no args for operator: " + mathUnit.stringValue);
                            }
                            resultVarName = Settings.helpVarString + nextExpressionIndex + helpVarCount;
                            operations.add(new Operation(
                                    resultVarName,
                                    mathUnits.get(operationIndex + 1).stringValue,
                                    mathUnit.operatorType,
                                    Operation.operationIsAssignment.nonAssignment
                            ));
                            mathUnits.remove(operationIndex + 1);
                            mathUnits.set(operationIndex, new MathUnit(resultVarName));//заменяем оператор и переменную/число на новую переменную, которую мы получили
                            helpVarCount++;
                            operationIndex++;
                        }
                        else if (mathUnit.operatorType == Operation.operatorType.convertToNegative) {
                            if (operationIndex == mathUnits.size() - 1) {
                                throw new IOException("no args for operator: " + mathUnit.stringValue);
                            }
                            if (mathUnits.get(operationIndex + 1).type != MathUnit.mathUnitType.wordOrValue) {
                                throw new IOException("no args for operator: " + mathUnit.stringValue);
                            }
                            resultVarName = Settings.helpVarString + nextExpressionIndex + helpVarCount;
                            operations.add(new Operation(
                                    resultVarName,
                                    mathUnits.get(operationIndex + 1).stringValue,
                                    Operation.operatorType.mul,
                                    "-1"
                            ));
                            mathUnits.remove(operationIndex + 1);
                            mathUnits.set(operationIndex, new MathUnit(resultVarName));//заменяем оператор и переменную/число на новую переменную, которую мы получили
                            helpVarCount++;
                            operationIndex++;
                        }
                        else if (mathUnit.operatorType == Operation.operatorType.rand) {
                            if (operationIndex == mathUnits.size() - 1) {
                                throw new IOException("no args for operator: " + mathUnit.stringValue);
                            }
                            if (mathUnits.get(operationIndex + 1).type != MathUnit.mathUnitType.wordOrValue) {
                                throw new IOException("no args for operator: " + mathUnit.stringValue);
                            }
                            resultVarName = Settings.helpVarString + nextExpressionIndex + helpVarCount;
                            operations.add(new Operation(
                                    resultVarName,
                                    mathUnits.get(operationIndex + 1).stringValue,
                                    mathUnit.operatorType,
                                    Operation.operationIsAssignment.nonAssignment
                            ));
                            mathUnits.remove(operationIndex + 1);
                            mathUnits.set(operationIndex, new MathUnit(resultVarName));//заменяем оператор и переменную/число на новую переменную, которую мы получили
                            helpVarCount++;
                            operationIndex++;
                        }
                    }
                }
                else {
                    operationIndex++;
                }
            }
        }
        return new ComplexOperation(operations, resultVarName, helpVarCount);
    }
    private static List<MathUnit> divideLineExpressionIntoMathUnits(String expression, MathData mathData){
        List<MathUnit> mathUnits = new ArrayList<MathUnit>();

        expression = putSpacesBetweenParts(expression, mathData);
        
        String[] expressionParts = expression.split(" ");
        for (String mathUnitStr: expressionParts) {
            if(!mathUnitStr.isEmpty() && mathData.operators.contains(mathUnitStr)){
                MathUnit mathUnit = new MathUnit(mathData.operatorsMap.get(mathUnitStr));
                if(mathUnit.operatorType == Operation.operatorType.sub){
                    if(mathUnits.isEmpty()){
                        mathUnits.add(mathData.getConvertToNegativeMathUnit());
                    }
                    else if(mathUnits.get(mathUnits.size() - 1).type == MathUnit.mathUnitType.operator){
                        mathUnits.add(mathData.getConvertToNegativeMathUnit());
                    }
                    else {
                        mathUnits.add(new MathUnit(mathData.operatorsMap.get(mathUnitStr)));
                    }
                }// если это унарный оператор "-", который умножает число на -1, то создаем соответствующий MathUnit
                else {
                    mathUnits.add(new MathUnit(mathData.operatorsMap.get(mathUnitStr)));
                }
            }//если это оператор, то получаем его копию из mathData по ключу (mathUnitStr)
            else if(!mathUnitStr.isEmpty()){
                mathUnits.add(new MathUnit(mathUnitStr));
            }//если нет, пользуемся альтернативным коструктором для MathUnit
        }
        return mathUnits;
    }

    private static String putSpacesBetweenParts(String expression, MathData mathData) {
        StringBuilder newExpression = new StringBuilder();
        boolean previousCharIsInWord = mathData.wordsAndValuesChars.contains(String.valueOf(expression.charAt(0)));
        boolean currentCharIsInWord;
        newExpression.append(expression.charAt(0));
        for(int i = 1; i < expression.length(); i++){
            currentCharIsInWord = mathData.wordsAndValuesChars.contains(String.valueOf(expression.charAt(i)));
            if(previousCharIsInWord == currentCharIsInWord || expression.charAt(i) == ' '){
                newExpression.append(expression.charAt(i));
            }
            else {
                newExpression.append(" ");
                newExpression.append(expression.charAt(i));
            }
            previousCharIsInWord = currentCharIsInWord;
        }
        String newExpressionString = newExpression.toString();
        while (newExpressionString.contains("  ")){ newExpressionString = newExpressionString.replace("  ",  " ");}
        return newExpressionString;
    }
}