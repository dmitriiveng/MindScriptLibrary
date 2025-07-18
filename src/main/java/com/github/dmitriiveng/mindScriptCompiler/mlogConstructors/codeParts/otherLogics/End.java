package com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.otherLogics;

import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.UncompiledCode;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.SingleLineCodePart;

public class End extends SingleLineCodePart {
    public End(){
    }

    @Override
    public String getAsCompiledCode(int previousCPLastLineIndex, int nameSpaceIndex, UncompiledCode uncompiledCode){
        return "end" + "\n";
    }
}
