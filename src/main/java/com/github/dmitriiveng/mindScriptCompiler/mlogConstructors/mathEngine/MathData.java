package com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.mathEngine;

import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.operations.Operation.operatorType;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.operations.Operation.operatorArgAmount;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.operations.Operation.operationIsAssignment;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// нельзя устанавливать гибридные операторы, например, "and" - можно, "&&" - можно, а "and&" - нельзя!
public class MathData {
    protected List<String> operators = List.of(
            "!",
            "rand",
            "-!",//умножение на -1
            "*",
            "/",
            "%",
            "+",
            "-",
            ">",
            ">=",
            "<",
            "<=",
            "==",
            "!=",
            "&&",
            "||",
            "=",
            "+=",
            "-=",
            "*=",
            "/=",
            "%="
    );
    protected Map<String, MathUnit> operatorsMap = Stream.of(new Object[][] {
            { "!", new MathUnit("!", operatorType.not, operatorArgAmount.unary, operationIsAssignment.nonAssignment, 1)},
            { "rand", new MathUnit("rand", operatorType.rand, operatorArgAmount.unary, operationIsAssignment.nonAssignment, 1)},
            { "-!", new MathUnit("-!", operatorType.convertToNegative, operatorArgAmount.unary, operationIsAssignment.nonAssignment, 1)},//умножение на -1
            { "*", new MathUnit("*", operatorType.mul, operatorArgAmount.binary, operationIsAssignment.nonAssignment, 2) },
            { "/", new MathUnit("/", operatorType.div, operatorArgAmount.binary, operationIsAssignment.nonAssignment, 2) },
            { "%", new MathUnit("%", operatorType.mod, operatorArgAmount.binary, operationIsAssignment.nonAssignment, 2) },
            { "+", new MathUnit("+", operatorType.add, operatorArgAmount.binary, operationIsAssignment.nonAssignment, 3) },
            { "-", new MathUnit("-", operatorType.sub, operatorArgAmount.binary, operationIsAssignment.nonAssignment, 3) },
            { ">", new MathUnit(">", operatorType.greaterThan, operatorArgAmount.binary, operationIsAssignment.nonAssignment, 4) },
            { ">=", new MathUnit(">=", operatorType.greaterThanEq, operatorArgAmount.binary, operationIsAssignment.nonAssignment, 4) },
            { "<", new MathUnit("<", operatorType.lessThan, operatorArgAmount.binary, operationIsAssignment.nonAssignment, 4) },
            { "<=", new MathUnit("<=", operatorType.lessThanEq, operatorArgAmount.binary, operationIsAssignment.nonAssignment, 4) },
            { "==", new MathUnit("==", operatorType.equal, operatorArgAmount.binary, operationIsAssignment.nonAssignment, 4) },
            { "!=", new MathUnit("!=", operatorType.notEqual, operatorArgAmount.binary, operationIsAssignment.nonAssignment, 4) },
            { "&&", new MathUnit("&&", operatorType.and, operatorArgAmount.binary, operationIsAssignment.nonAssignment, 5) },
            { "||", new MathUnit("||", operatorType.or, operatorArgAmount.binary, operationIsAssignment.nonAssignment, 5) },
            { "=", new MathUnit("=", operatorType.set, operatorArgAmount.binary, operationIsAssignment.assignment, 6) },
            { "+=", new MathUnit("+=", operatorType.add, operatorArgAmount.binary, operationIsAssignment.assignment, 6) },
            { "-=", new MathUnit("-=", operatorType.sub, operatorArgAmount.binary, operationIsAssignment.assignment, 6) },
            { "*=", new MathUnit("*=", operatorType.mul, operatorArgAmount.binary, operationIsAssignment.assignment, 6) },
            { "/=", new MathUnit("/=", operatorType.div, operatorArgAmount.binary, operationIsAssignment.assignment, 6) },
            { "%=", new MathUnit("%=", operatorType.mod, operatorArgAmount.binary, operationIsAssignment.assignment, 6) },
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (MathUnit) data[1]));
    //operators от нуля до n - максимальный приоритет до минимального
    public String wordsAndValuesChars = "@qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM_1234567890йцукенгшщзхъфывапролджэячсмитьбюёЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮЁ";
    public int operationsPrioritiesCount;

    public MathData(){
        operationsPrioritiesCount = getOperationsPrioritiesCount();
    }

    public MathData(List<String> operators, Map<String, MathUnit> operatorsMap, String wordsAndValuesCars){
        this.operators = operators;
        this.operatorsMap = operatorsMap;
        this.wordsAndValuesChars = wordsAndValuesCars;
        operationsPrioritiesCount = getOperationsPrioritiesCount();
    }

    private int getOperationsPrioritiesCount(){
        int operationsPrioritiesCount = 0;
        for(String operator: operators){
            int priority = operatorsMap.get(operator).operatorPriority;
            if(priority > operationsPrioritiesCount){operationsPrioritiesCount = priority;}
        }
        return operationsPrioritiesCount;
    }

    public MathUnit getConvertToNegativeMathUnit(){
        for(Map.Entry<String, MathUnit> entry : operatorsMap.entrySet()) {
            String key = entry.getKey();
            MathUnit value = entry.getValue();
            if(value.operatorType == operatorType.convertToNegative){
                return value;
            }
        }
        System.out.println("Error, MathData does not have operation with type convertToNegative");
        return null;
    }
}
