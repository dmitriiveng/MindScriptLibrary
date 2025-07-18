package com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.operations;

import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.UncompiledCode;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.NameSpacesMethods;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.SingleLineCodePart;

public class Set extends SingleLineCodePart {
    String name;
    String value;
    public Set(String name, String value){
        this.name = name;
        this.value = value;
    }

    @Override
    public String getAsCompiledCode(int previousCPLastLineIndex, int nameSpaceIndex, UncompiledCode uncompiledCode) {
        return String.format("set %s %s",
                NameSpacesMethods.getVarNameWithPrefix(name, nameSpaceIndex, uncompiledCode),
                NameSpacesMethods.getVarNameWithPrefix(value, nameSpaceIndex, uncompiledCode)
        ) + "\n";
    }
}
