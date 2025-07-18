package com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.loops;

import java.io.IOException;
import java.util.List;

import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.UncompiledCode;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.CodePart;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.otherLogics.Jump;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.mathEngine.MathematicalExpressionReader;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.operations.ComplexOperation;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.mathEngine.MathData;

public class ComplexForCycle extends CycleCodePart {
    ComplexOperation startValue, operation, condition;
    public ComplexForCycle(
            String startValueExpression,
            String conditionExpression,
            String operationExpression,
            List<CodePart> insideCode,
            MathData mathData) throws IOException {
        super(insideCode, mathData);
        condition = MathematicalExpressionReader.readExpression(conditionExpression, mathData);
        operation = MathematicalExpressionReader.readExpression(operationExpression, mathData);
        startValue = MathematicalExpressionReader.readExpression(startValueExpression, mathData);
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
