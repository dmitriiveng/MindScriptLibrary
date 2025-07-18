package mlogConstructors.codeParts.operations;

import mlogConstructors.UncompiledCode;
import mlogConstructors.codeParts.ComplexCodePart;
import mlogConstructors.mathEngine.MathData;

import java.io.IOException;

import static mlogConstructors.codeParts.NameSpacesMethods.getVarNameWithPrefix;
import static mlogConstructors.mathEngine.MathematicalExpressionReader.readExpression;

public class Lookup extends ComplexCodePart {
    public enum LookupType {
        block, unit, item, liquid
    }
    LookupType type;
    ComplexOperation index;
    String resultVarName;

    public Lookup(LookupType type, String resultVarName, String indexExpression, MathData mathData) throws IOException {
        index = readExpression(indexExpression, mathData);
        this.type = type;
        this.resultVarName = resultVarName;
        linesCount = 1 + index.linesCount;
    }

    public String getAsCompiledCode(int previousCPLastLineIndex, int nameSpaceIndex, UncompiledCode uncompiledCode) throws IOException {
        allCycleCodeParts.add(index);
        return getAllCycleCodePartsAsCompiledCode(previousCPLastLineIndex, nameSpaceIndex, uncompiledCode) +
                String.format("lookup %s %s %s",
                        type,
                        getVarNameWithPrefix(resultVarName, nameSpaceIndex, uncompiledCode),
                        getVarNameWithPrefix(index.finalVarName, nameSpaceIndex, uncompiledCode)
                ) + "\n";
    }
}
