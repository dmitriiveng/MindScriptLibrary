package com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.operations;

import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.UncompiledCode;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.ComplexCodePart;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.NameSpacesMethods;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.mathEngine.MathData;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.mathEngine.MathematicalExpressionReader;

import java.io.IOException;

public class PackColor extends ComplexCodePart {
    String resultVarName;
    ComplexOperation r, g, b, a;// 0 - 1 float

    public PackColor(String resultVarName, String rExpression, String gExpression, String bExpression, String aExpression, MathData mathData) throws IOException {
        this.resultVarName = resultVarName;
        r = MathematicalExpressionReader.readExpression(rExpression, mathData);
        g = MathematicalExpressionReader.readExpression(gExpression, mathData);
        b = MathematicalExpressionReader.readExpression(bExpression, mathData);
        a = MathematicalExpressionReader.readExpression(aExpression, mathData);
        linesCount = 1 + r.linesCount + g.linesCount + b.linesCount + a.linesCount;
    }

    public String getAsCompiledCode(int previousCPLastLineIndex, int nameSpaceIndex, UncompiledCode uncompiledCode) throws IOException {
        allCycleCodeParts.add(r);
        allCycleCodeParts.add(g);
        allCycleCodeParts.add(b);
        allCycleCodeParts.add(a);
        return getAllCycleCodePartsAsCompiledCode(previousCPLastLineIndex, nameSpaceIndex, uncompiledCode) +
                String.format("packcolor %s %s %s %s %s",
                        NameSpacesMethods.getVarNameWithPrefix(resultVarName, nameSpaceIndex, uncompiledCode),
                        NameSpacesMethods.getVarNameWithPrefix(r.finalVarName, nameSpaceIndex, uncompiledCode),
                        NameSpacesMethods.getVarNameWithPrefix(g.finalVarName, nameSpaceIndex, uncompiledCode),
                        NameSpacesMethods.getVarNameWithPrefix(b.finalVarName, nameSpaceIndex, uncompiledCode),
                        NameSpacesMethods.getVarNameWithPrefix(a.finalVarName, nameSpaceIndex, uncompiledCode)
                ) + "\n";
    }
}

