package com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.otherLogics;

import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.UncompiledCode;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.NameSpacesMethods;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.mathEngine.MathematicalExpressionReader;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.ComplexCodePart;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.operations.ComplexOperation;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.mathEngine.MathData;

import java.io.IOException;

public class Jump extends ComplexCodePart {
    public enum BoolOperationType{
        equal,
        notEqual,
        lessThan,
        lessThanEq,
        greaterThan,
        greaterThanEq,
        strictEqual,
        always
    }

    BoolOperationType boolOperation;
    ComplexOperation firstArg, secondArg;
    int jumpToIndex;
    public Jump(BoolOperationType boolOperation, String firstArgExpression, String secondArgExpression, int jumpToIndex, MathData mathData) throws IOException {
        this.boolOperation = boolOperation;
        firstArg = MathematicalExpressionReader.readExpression(firstArgExpression, mathData);
        secondArg = MathematicalExpressionReader.readExpression(secondArgExpression, mathData);;
        this.jumpToIndex = jumpToIndex;
        linesCount = 1 + firstArg.linesCount + secondArg.linesCount;
    }
    public Jump(int jumpToIndex){
        this.boolOperation = BoolOperationType.always;
        this.jumpToIndex = jumpToIndex;
        linesCount = 1;
    }

    @Override
    public String getAsCompiledCode(int previousCPLastLineIndex, int nameSpaceIndex, UncompiledCode uncompiledCode) throws IOException {
        switch (boolOperation){
            case always -> {
                return String.format("jump %s",
                        boolOperation
                ) + "\n";}
            case equal, notEqual, lessThan, lessThanEq, greaterThan, greaterThanEq, strictEqual -> {
                allCycleCodeParts.add(firstArg);
                allCycleCodeParts.add(secondArg);
                return getAllCycleCodePartsAsCompiledCode(previousCPLastLineIndex, nameSpaceIndex, uncompiledCode) +
                        String.format("jump %s %s %s %s",
                                jumpToIndex,
                                boolOperation,
                                NameSpacesMethods.getVarNameWithPrefix(firstArg.finalVarName, nameSpaceIndex, uncompiledCode),
                                NameSpacesMethods.getVarNameWithPrefix(secondArg.finalVarName, nameSpaceIndex, uncompiledCode)
                        ) + "\n";
            }
        }
        return null;
    }
}
