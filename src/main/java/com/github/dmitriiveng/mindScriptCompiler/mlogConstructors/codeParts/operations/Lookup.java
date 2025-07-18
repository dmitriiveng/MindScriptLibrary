package com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.operations;

import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.UncompiledCode;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.ComplexCodePart;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.NameSpacesMethods;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.mathEngine.MathData;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.mathEngine.MathematicalExpressionReader;

import java.io.IOException;

public class Lookup extends ComplexCodePart {
    public enum LookupType {
        block, unit, item, liquid
    }
    LookupType type;
    ComplexOperation index;
    String resultVarName;

    public Lookup(LookupType type, String resultVarName, String indexExpression, MathData mathData) throws IOException {
        index = MathematicalExpressionReader.readExpression(indexExpression, mathData);
        this.type = type;
        this.resultVarName = resultVarName;
        linesCount = 1 + index.linesCount;
    }

    public String getAsCompiledCode(int previousCPLastLineIndex, int nameSpaceIndex, UncompiledCode uncompiledCode) throws IOException {
        allCycleCodeParts.add(index);
        return getAllCycleCodePartsAsCompiledCode(previousCPLastLineIndex, nameSpaceIndex, uncompiledCode) +
                String.format("lookup %s %s %s",
                        type,
                        NameSpacesMethods.getVarNameWithPrefix(resultVarName, nameSpaceIndex, uncompiledCode),
                        NameSpacesMethods.getVarNameWithPrefix(index.finalVarName, nameSpaceIndex, uncompiledCode)
                ) + "\n";
    }
}
