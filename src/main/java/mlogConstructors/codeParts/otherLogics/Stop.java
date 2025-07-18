package mlogConstructors.codeParts.otherLogics;

import mlogConstructors.UncompiledCode;
import mlogConstructors.codeParts.SingleLineCodePart;

public class Stop  extends SingleLineCodePart {
    public Stop(){

    }

    @Override
    public String getAsCompiledCode(int previousCPLastLineIndex, int nameSpaceIndex, UncompiledCode uncompiledCode){
        return "stop" + "\n";
    }
}
