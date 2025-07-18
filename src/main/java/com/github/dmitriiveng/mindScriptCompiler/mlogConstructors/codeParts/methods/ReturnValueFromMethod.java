package com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.methods;

import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.UncompiledCode;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.ComplexCodePart;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.operations.ComplexOperation;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.operations.Set;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.mathEngine.MathData;

import java.io.IOException;

import static com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.Settings.*;
import static com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.NameSpacesMethods.getVarNameWithPrefix;
import static com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.mathEngine.MathematicalExpressionReader.readExpression;

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
