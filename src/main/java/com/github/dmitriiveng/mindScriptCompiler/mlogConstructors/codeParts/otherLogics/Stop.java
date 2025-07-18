package com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.otherLogics;

import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.UncompiledCode;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.SingleLineCodePart;

public class Stop  extends SingleLineCodePart {
    public Stop(){

    }

    @Override
    public String getAsCompiledCode(int previousCPLastLineIndex, int nameSpaceIndex, UncompiledCode uncompiledCode){
        return "stop" + "\n";
    }
}
