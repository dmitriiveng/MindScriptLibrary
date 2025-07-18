package mlogConstructors.codeParts.otherLogics;

import mlogConstructors.UncompiledCode;
import mlogConstructors.codeParts.ComplexCodePart;
import mlogConstructors.codeParts.operations.ComplexOperation;
import mlogConstructors.mathEngine.MathData;

import java.io.IOException;

import static mlogConstructors.codeParts.NameSpacesMethods.getVarNameWithPrefix;
import static mlogConstructors.mathEngine.MathematicalExpressionReader.readExpression;

public class Wait  extends ComplexCodePart {
    ComplexOperation waitTime;
    public Wait(String waitTimeExpression, MathData mathData) throws IOException {
        waitTime = readExpression(waitTimeExpression, mathData);
        linesCount = 1 + waitTime.linesCount;
    }

    @Override
    public String getAsCompiledCode(int previousCPLastLineIndex, int nameSpaceIndex, UncompiledCode uncompiledCode) throws IOException {
        allCycleCodeParts.add(waitTime);
        return getAllCycleCodePartsAsCompiledCode(previousCPLastLineIndex, nameSpaceIndex, uncompiledCode) +
                String.format("wait %s", getVarNameWithPrefix(waitTime.finalVarName, nameSpaceIndex, uncompiledCode)) + "\n";
    }
}
