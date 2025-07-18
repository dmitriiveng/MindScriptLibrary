package com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.operations;

import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.UncompiledCode;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.NameSpacesMethods;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.SingleLineCodePart;

public class Operation extends SingleLineCodePart {
    public enum operatorType{
        add, sub, mul, div, idiv, mod, pow, equal, notEqual, land, lessThan, lessThanEq, greaterThan, greaterThanEq, strictEqual,
        shl, shr, or, and, xor, not, max, min, angle, angleDiff, len, noise, abs, log, log10, floor, ceil, sqrt, rand,
        sin, cos, tan, asin, acos, atan,
        set, convertToNegative
    };
    public enum operatorArgAmount{
        binary,
        unary
    }
    public enum operationIsAssignment{
        assignment,
        nonAssignment
    }

    String name;
    String firstArg;
    operatorType operator;
    operatorArgAmount operatorArg;
    operationIsAssignment operationAssignment;
    String secondArg;
    public Operation(String name, String firstArg, operatorType operator, String secondArg){
        this.name = name;
        this.firstArg = firstArg;
        this.operator = operator;
        this.secondArg = secondArg;
        this.operationAssignment = operationIsAssignment.nonAssignment;
        operatorArg = operatorArgAmount.binary;
    }//бинарные операции is nonAssignment
    public Operation(String name, String arg, operatorType operator, operationIsAssignment operationAssignment){
        this.name = name;
        this.firstArg = arg;
        this.operator = operator;
        this.operationAssignment = operationAssignment;
        operatorArg = operatorArgAmount.unary;
    }//унарные операции

    @Override
    public String getAsCompiledCode(int previousCPLastLineIndex, int nameSpaceIndex, UncompiledCode uncompiledCode) {
        if(operationAssignment == operationIsAssignment.assignment){
            if (operator == operatorType.set){
                return String.format("set %s %s",
                        NameSpacesMethods.getVarNameWithPrefix(name, nameSpaceIndex, uncompiledCode),
                        NameSpacesMethods.getVarNameWithPrefix(firstArg, nameSpaceIndex, uncompiledCode)
                ) + "\n";
            }
            else{
                return String.format("op %s %s %s %s",
                        operator,
                        NameSpacesMethods.getVarNameWithPrefix(name, nameSpaceIndex, uncompiledCode),
                        NameSpacesMethods.getVarNameWithPrefix(name, nameSpaceIndex, uncompiledCode),
                        NameSpacesMethods.getVarNameWithPrefix(firstArg, nameSpaceIndex, uncompiledCode)
                ) + "\n";
            }
        }
        else if (operationAssignment == operationIsAssignment.nonAssignment){
            if (operatorArg == operatorArgAmount.binary){
                return String.format("op %s %s %s %s",
                        operator,
                        NameSpacesMethods.getVarNameWithPrefix(name, nameSpaceIndex, uncompiledCode),
                        NameSpacesMethods.getVarNameWithPrefix(firstArg, nameSpaceIndex, uncompiledCode),
                        NameSpacesMethods.getVarNameWithPrefix(secondArg, nameSpaceIndex, uncompiledCode)
                ) + "\n";
            }
            else if (operatorArg == operatorArgAmount.unary){
                return String.format("op %s %s %s",
                        operator,
                        NameSpacesMethods.getVarNameWithPrefix(name, nameSpaceIndex, uncompiledCode),
                        NameSpacesMethods.getVarNameWithPrefix(firstArg, nameSpaceIndex, uncompiledCode)
                ) + "\n";
            }
        }
        return null;
    }
}
