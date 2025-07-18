package com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.otherLogics;

import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.UncompiledCode;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.ComplexCodePart;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.NameSpacesMethods;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.mathEngine.MathData;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.mathEngine.MathematicalExpressionReader;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.operations.ComplexOperation;

import java.io.IOException;

public class Wait  extends ComplexCodePart {
    ComplexOperation waitTime;
    public Wait(String waitTimeExpression, MathData mathData) throws IOException {
        waitTime = MathematicalExpressionReader.readExpression(waitTimeExpression, mathData);
        linesCount = 1 + waitTime.linesCount;
    }

    @Override
    public String getAsCompiledCode(int previousCPLastLineIndex, int nameSpaceIndex, UncompiledCode uncompiledCode) throws IOException {
        allCycleCodeParts.add(waitTime);
        return getAllCycleCodePartsAsCompiledCode(previousCPLastLineIndex, nameSpaceIndex, uncompiledCode) +
                String.format("wait %s", NameSpacesMethods.getVarNameWithPrefix(waitTime.finalVarName, nameSpaceIndex, uncompiledCode)) + "\n";
    }
}
