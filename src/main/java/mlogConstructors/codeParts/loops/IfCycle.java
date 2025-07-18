package mlogConstructors.codeParts.loops;

import mlogConstructors.UncompiledCode;
import mlogConstructors.codeParts.CodePart;
import mlogConstructors.codeParts.operations.ComplexOperation;
import mlogConstructors.codeParts.otherLogics.Jump;
import mlogConstructors.mathEngine.MathData;

import java.io.IOException;
import java.util.List;

import static mlogConstructors.mathEngine.MathematicalExpressionReader.readExpression;

public class IfCycle extends CycleCodePart {
    ComplexOperation condition;
    public IfCycle(String conditionExpression, List<CodePart> insideCode, MathData mathData) throws IOException {
        super(insideCode, mathData);
        condition = readExpression(conditionExpression, mathData);
        //linesCount
        linesCount = 1;//1 т. к. далее еще есть строчка Jump
        for (CodePart cycleCodePart : insideCode) {
            linesCount += cycleCodePart.linesCount;
        }
        linesCount += condition.linesCount;
    }

    @Override
    public String getAsCompiledCode(int previousCPLastLineIndex, int nameSpaceIndex, UncompiledCode uncompiledCode) throws IOException {
        allCycleCodeParts.addAll(condition.insideOperations);
        int jumpLine = previousCPLastLineIndex + linesCount + 1;
        allCycleCodeParts.add(new Jump(Jump.BoolOperationType.equal, condition.finalVarName, "false", jumpLine, mathData));
        allCycleCodeParts.addAll(insideCode);
        return getAllCycleCodePartsAsCompiledCode(previousCPLastLineIndex, nameSpaceIndex, uncompiledCode);
    }
}
