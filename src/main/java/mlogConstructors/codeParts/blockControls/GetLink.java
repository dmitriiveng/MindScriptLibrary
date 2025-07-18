package mlogConstructors.codeParts.blockControls;

import mlogConstructors.UncompiledCode;
import mlogConstructors.codeParts.ComplexCodePart;
import mlogConstructors.codeParts.operations.ComplexOperation;
import mlogConstructors.mathEngine.MathData;

import java.io.IOException;

import static mlogConstructors.codeParts.NameSpacesMethods.getVarNameWithPrefix;
import static mlogConstructors.mathEngine.MathematicalExpressionReader.readExpression;

public class GetLink extends ComplexCodePart {
    String returnVarName;
    ComplexOperation blockIndex;
    public GetLink(String returnVarName, String blockIndexExpression, MathData mathData) throws IOException {
        blockIndex = readExpression(blockIndexExpression, mathData);
        this.returnVarName = returnVarName;
        linesCount = 1 + blockIndex.linesCount;
    }

    @Override
    public String getAsCompiledCode(int previousCPLastLineIndex, int nameSpaceIndex, UncompiledCode uncompiledCode) throws IOException {
        allCycleCodeParts.add(blockIndex);
        return getAllCycleCodePartsAsCompiledCode(previousCPLastLineIndex, nameSpaceIndex, uncompiledCode) +
                String.format("getlink %s %s",
                        getVarNameWithPrefix(returnVarName, nameSpaceIndex, uncompiledCode),
                        getVarNameWithPrefix(blockIndex.finalVarName, nameSpaceIndex, uncompiledCode)
                ) + "\n";
    }
}
