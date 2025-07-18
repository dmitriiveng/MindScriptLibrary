package mlogConstructors.codeParts.inputOutput;

import mlogConstructors.UncompiledCode;
import mlogConstructors.codeParts.ComplexCodePart;
import mlogConstructors.codeParts.operations.ComplexOperation;
import mlogConstructors.mathEngine.MathData;

import java.io.IOException;

import static mlogConstructors.codeParts.NameSpacesMethods.getVarNameWithPrefix;
import static mlogConstructors.mathEngine.MathematicalExpressionReader.readExpression;

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
                        getVarNameWithPrefix(var, nameSpaceIndex, uncompiledCode),
                        getVarNameWithPrefix(blockVarName, nameSpaceIndex, uncompiledCode),
                        getVarNameWithPrefix(index.finalVarName, nameSpaceIndex, uncompiledCode)
                ) + "\n";
    }
}
