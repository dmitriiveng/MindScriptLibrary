package mlogConstructors.codeParts.otherLogics;

import mlogConstructors.UncompiledCode;
import mlogConstructors.codeParts.SingleLineCodePart;

public class End extends SingleLineCodePart {
    public End(){
    }

    @Override
    public String getAsCompiledCode(int previousCPLastLineIndex, int nameSpaceIndex, UncompiledCode uncompiledCode){
        return "end" + "\n";
    }
}
