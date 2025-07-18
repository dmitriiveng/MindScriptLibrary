package mlogConstructors.codeParts.loops;

import java.io.IOException;
import java.util.List;

import mlogConstructors.UncompiledCode;
import mlogConstructors.codeParts.CodePart;
import mlogConstructors.codeParts.operations.ComplexOperation;
import mlogConstructors.codeParts.otherLogics.Jump;
import mlogConstructors.mathEngine.MathData;

import static mlogConstructors.mathEngine.MathematicalExpressionReader.readExpression;

public class ComplexForCycle extends CycleCodePart {
    ComplexOperation startValue, operation, condition;
    public ComplexForCycle(
            String startValueExpression,
            String conditionExpression,
            String operationExpression,
            List<CodePart> insideCode,
            MathData mathData) throws IOException {
        super(insideCode, mathData);
        condition = readExpression(conditionExpression, mathData);
        operation = readExpression(operationExpression, mathData);
        startValue = readExpression(startValueExpression, mathData);
        //linesCount
        linesCount = 1;//1 т. к. далее еще есть строчка Jump
        for (CodePart cycleCodePart : insideCode) {
            linesCount += cycleCodePart.linesCount;
        }
        linesCount += startValue.linesCount;
        linesCount += operation.linesCount;
        linesCount += condition.linesCount;
    }

    @Override
    public String getAsCompiledCode(int previousCPLastLineIndex, int nameSpaceIndex, UncompiledCode uncompiledCode) throws IOException {
        allCycleCodeParts.addAll(startValue.insideOperations);
        allCycleCodeParts.addAll(insideCode);
        allCycleCodeParts.addAll(operation.insideOperations);
        allCycleCodeParts.addAll(condition.insideOperations);
        int jumpLine = previousCPLastLineIndex+startValue.linesCount+1;
        allCycleCodeParts.add(new Jump(Jump.BoolOperationType.equal, condition.finalVarName, "true", jumpLine, mathData));
        return getAllCycleCodePartsAsCompiledCode(previousCPLastLineIndex, nameSpaceIndex, uncompiledCode);
    }
}
