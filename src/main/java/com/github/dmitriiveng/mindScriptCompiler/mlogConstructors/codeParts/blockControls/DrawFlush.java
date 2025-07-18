package com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.blockControls;

import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.UncompiledCode;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.NameSpacesMethods;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.SingleLineCodePart;

public class DrawFlush extends SingleLineCodePart {
    String blockVarName;
    public DrawFlush(String blockVarName){
        this.blockVarName = blockVarName;
    }

    @Override
    public String getAsCompiledCode(int previousCPLastLineIndex, int nameSpaceIndex, UncompiledCode uncompiledCode){
        return String.format("drawflush %s", NameSpacesMethods.getVarNameWithPrefix(blockVarName, nameSpaceIndex, uncompiledCode)) + "\n";
    }
}
