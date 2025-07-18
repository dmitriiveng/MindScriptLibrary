package mlogConstructors.codeParts.otherLogics;

import mlogConstructors.UncompiledCode;
import mlogConstructors.codeParts.ComplexCodePart;
import mlogConstructors.codeParts.operations.ComplexOperation;
import mlogConstructors.mathEngine.MathData;

import java.io.IOException;

import static mlogConstructors.codeParts.NameSpacesMethods.getVarNameWithPrefix;
import static mlogConstructors.mathEngine.MathematicalExpressionReader.readExpression;

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
        firstArg = readExpression(firstArgExpression, mathData);
        secondArg = readExpression(secondArgExpression, mathData);;
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
                                getVarNameWithPrefix(firstArg.finalVarName, nameSpaceIndex, uncompiledCode),
                                getVarNameWithPrefix(secondArg.finalVarName, nameSpaceIndex, uncompiledCode)
                        ) + "\n";
            }
        }
        return null;
    }
}
