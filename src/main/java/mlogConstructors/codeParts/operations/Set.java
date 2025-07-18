package mlogConstructors.codeParts.operations;

import mlogConstructors.UncompiledCode;
import mlogConstructors.codeParts.SingleLineCodePart;

import static mlogConstructors.codeParts.NameSpacesMethods.getVarNameWithPrefix;

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
                getVarNameWithPrefix(name, nameSpaceIndex, uncompiledCode),
                getVarNameWithPrefix(value, nameSpaceIndex, uncompiledCode)
        ) + "\n";
    }
}
