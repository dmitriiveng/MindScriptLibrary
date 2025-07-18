package mlogConstructors.codeParts.blockControls;

import mlogConstructors.UncompiledCode;
import mlogConstructors.codeParts.SingleLineCodePart;
import mlogConstructors.mathEngine.MathData;

import static mlogConstructors.codeParts.NameSpacesMethods.getVarNameWithPrefix;

public class PrintFlush  extends SingleLineCodePart {
    String blockVarName;
    public PrintFlush(String blockVarName)
    {
        this.blockVarName = blockVarName;
    }

    @Override
    public String getAsCompiledCode(int previousCPLastLineIndex, int nameSpaceIndex, UncompiledCode uncompiledCode){
        return String.format("printflush %s", getVarNameWithPrefix(blockVarName, nameSpaceIndex, uncompiledCode)) + "\n";
    }
}
