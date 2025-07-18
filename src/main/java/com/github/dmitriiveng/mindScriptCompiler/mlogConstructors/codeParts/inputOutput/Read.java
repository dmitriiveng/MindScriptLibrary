package com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.inputOutput;

import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.UncompiledCode;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.NameSpacesMethods;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.ComplexCodePart;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.operations.ComplexOperation;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.mathEngine.MathData;

import java.io.IOException;

import static com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.mathEngine.MathematicalExpressionReader.readExpression;

public class Read  extends ComplexCodePart {
    ComplexOperation index;
    String blockVarName;
    String var;
    public Read(String var, String blockVarName, String indexExpression, MathData mathData) throws IOException {
        index = readExpression(indexExpression, mathData);
        this.var = var;
        this.blockVarName = blockVarName;
        linesCount = 1 + index.linesCount;
    }
    @Override
    public String getAsCompiledCode(int previousCPLastLineIndex, int nameSpaceIndex, UncompiledCode uncompiledCode) throws IOException {
        allCycleCodeParts.add(index);
        return getAllCycleCodePartsAsCompiledCode(previousCPLastLineIndex, nameSpaceIndex, uncompiledCode) +
                String.format("read %s %s %s",
                        NameSpacesMethods.getVarNameWithPrefix(var, nameSpaceIndex, uncompiledCode),
                        NameSpacesMethods.getVarNameWithPrefix(blockVarName, nameSpaceIndex, uncompiledCode),
                        NameSpacesMethods.getVarNameWithPrefix(index.finalVarName, nameSpaceIndex, uncompiledCode)
                ) + "\n";
    }
}
