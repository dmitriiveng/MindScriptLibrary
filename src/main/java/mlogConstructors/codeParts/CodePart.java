package mlogConstructors.codeParts;

import mlogConstructors.UncompiledCode;

import java.io.IOException;

public abstract class CodePart {
    public int linesCount; //должно быть определено в дочернем классе
    public abstract String getAsCompiledCode(int previousCPLastLineIndex, int nameSpaceIndex, UncompiledCode uncompiledCode) throws IOException;
}
