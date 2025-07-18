package mlogConstructors.codeParts.blockControls;

import mlogConstructors.UncompiledCode;
import mlogConstructors.codeParts.SingleLineCodePart;

import static mlogConstructors.codeParts.NameSpacesMethods.getVarNameWithPrefix;

public class DrawFlush extends SingleLineCodePart {
    String blockVarName;
    public DrawFlush(String blockVarName){
        this.blockVarName = blockVarName;
    }

    @Override
    public String getAsCompiledCode(int previousCPLastLineIndex, int nameSpaceIndex, UncompiledCode uncompiledCode){
        return String.format("drawflush %s", getVarNameWithPrefix(blockVarName, nameSpaceIndex, uncompiledCode)) + "\n";
    }
}
