package com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.unitControls;

import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.UncompiledCode;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.NameSpacesMethods;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.SingleLineCodePart;

public class UBind extends SingleLineCodePart {
    String bindType;
    public UBind(String bindType){
        this.bindType = bindType;
    }

    @Override
    public String getAsCompiledCode(int previousCPLastLineIndex, int nameSpaceIndex, UncompiledCode uncompiledCode){
        return String.format("ubind %s", NameSpacesMethods.getVarNameWithPrefix(bindType, nameSpaceIndex, uncompiledCode)) + "\n";
    }
}
