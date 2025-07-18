package mlogConstructors.codeParts.methods;

import mlogConstructors.UncompiledCode;
import mlogConstructors.codeParts.ComplexCodePart;
import mlogConstructors.codeParts.operations.ComplexOperation;
import mlogConstructors.codeParts.operations.Set;
import mlogConstructors.mathEngine.MathData;

import java.io.IOException;

import static mlogConstructors.Settings.*;
import static mlogConstructors.codeParts.NameSpacesMethods.getVarNameWithPrefix;
import static mlogConstructors.mathEngine.MathematicalExpressionReader.readExpression;

public class ReturnValueFromMethod extends ComplexCodePart {
    ComplexOperation valueToReturn;
    String methodName;

    public ReturnValueFromMethod(String valueToReturnExpression, String methodName, MathData mathData) throws IOException {
        valueToReturn = readExpression(valueToReturnExpression, mathData);
        this.methodName = methodName;
        linesCount = 2;//т. к. в конце кода этого CodePart будет строчка Set @counter и Set methodReturnVarNameString + methodIndex
        linesCount += valueToReturn.linesCount;
    }

    @Override
    public String getAsCompiledCode(int previousCPLastLineIndex, int nameSpaceIndex, UncompiledCode uncompiledCode) throws IOException {
        int methodIndex = uncompiledCode.getMethodByName(methodName).methodIndex;
        allCycleCodeParts.add(valueToReturn);
        allCycleCodeParts.add(new Set(methodReturnVarNameString + methodName, getVarNameWithPrefix(valueToReturn.finalVarName, nameSpaceIndex, uncompiledCode)));
        allCycleCodeParts.add(new Set("@counter", methodReturnLineString + methodName));
        return getAllCycleCodePartsAsCompiledCode(previousCPLastLineIndex, nameSpaceIndex, uncompiledCode);
    }
}
