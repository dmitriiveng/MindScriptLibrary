package com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.blockControls;

import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.UncompiledCode;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.ComplexCodePart;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.NameSpacesMethods;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.mathEngine.MathematicalExpressionReader;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.operations.ComplexOperation;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.mathEngine.MathData;

import java.io.IOException;

public class GetLink extends ComplexCodePart {
    String returnVarName;
    ComplexOperation blockIndex;
    public GetLink(String returnVarName, String blockIndexExpression, MathData mathData) throws IOException {
        blockIndex = MathematicalExpressionReader.readExpression(blockIndexExpression, mathData);
        this.returnVarName = returnVarName;
        linesCount = 1 + blockIndex.linesCount;
    }

    @Override
    public String getAsCompiledCode(int previousCPLastLineIndex, int nameSpaceIndex, UncompiledCode uncompiledCode) throws IOException {
        allCycleCodeParts.add(blockIndex);
        return getAllCycleCodePartsAsCompiledCode(previousCPLastLineIndex, nameSpaceIndex, uncompiledCode) +
                String.format("getlink %s %s",
                        NameSpacesMethods.getVarNameWithPrefix(returnVarName, nameSpaceIndex, uncompiledCode),
                        NameSpacesMethods.getVarNameWithPrefix(blockIndex.finalVarName, nameSpaceIndex, uncompiledCode)
                ) + "\n";
    }
}
