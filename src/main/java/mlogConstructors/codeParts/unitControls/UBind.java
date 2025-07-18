package mlogConstructors.codeParts.unitControls;

import mlogConstructors.UncompiledCode;
import mlogConstructors.codeParts.SingleLineCodePart;

import static mlogConstructors.codeParts.NameSpacesMethods.getVarNameWithPrefix;

public class UBind extends SingleLineCodePart {
    String bindType;
    public UBind(String bindType){
        this.bindType = bindType;
    }

    @Override
    public String getAsCompiledCode(int previousCPLastLineIndex, int nameSpaceIndex, UncompiledCode uncompiledCode){
        return String.format("ubind %s", getVarNameWithPrefix(bindType, nameSpaceIndex, uncompiledCode)) + "\n";
    }
}
