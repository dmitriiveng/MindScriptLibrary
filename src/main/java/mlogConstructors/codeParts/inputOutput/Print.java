package mlogConstructors.codeParts.inputOutput;

import mlogConstructors.UncompiledCode;
import mlogConstructors.codeParts.ComplexCodePart;
import mlogConstructors.codeParts.operations.ComplexOperation;
import mlogConstructors.mathEngine.MathData;

import java.io.IOException;

import static mlogConstructors.codeParts.NameSpacesMethods.getVarNameWithPrefix;
import static mlogConstructors.mathEngine.MathematicalExpressionReader.readExpression;

public class Print extends ComplexCodePart {
    ComplexOperation text;
    public Print(String textExpression, MathData mathData) throws IOException {
        text = readExpression(textExpression, mathData);
        linesCount = 1 + text.linesCount;
    }

    @Override
    public String getAsCompiledCode(int previousCPLastLineIndex, int nameSpaceIndex, UncompiledCode uncompiledCode) throws IOException {
        allCycleCodeParts.add(text);
        return getAllCycleCodePartsAsCompiledCode(previousCPLastLineIndex, nameSpaceIndex, uncompiledCode) +
                String.format("print %s", getVarNameWithPrefix(text.finalVarName, nameSpaceIndex, uncompiledCode)) + "\n";
    }
}
