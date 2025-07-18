package mlogConstructors.codeParts;

import mlogConstructors.UncompiledCode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class ComplexCodePart extends CodePart{
    protected List<CodePart> allCycleCodeParts = new ArrayList<CodePart>();
    public String getAllCycleCodePartsAsCompiledCode(int previousCPLastLineIndex, int nameSpaceIndex, UncompiledCode compilerData) throws IOException {
        StringBuilder resultCode = new StringBuilder();
        for(int i = 0; i < allCycleCodeParts.size(); i++){
            CodePart codePart = allCycleCodeParts.get(i);
            resultCode.append(codePart.getAsCompiledCode(previousCPLastLineIndex, nameSpaceIndex, compilerData));
            previousCPLastLineIndex += codePart.linesCount;
        }
        return resultCode.toString();
    }
}
