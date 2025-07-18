package com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts;

import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.UncompiledCode;

import java.io.IOException;

public abstract class CodePart {
    public int linesCount; //должно быть определено в дочернем классе
    public abstract String getAsCompiledCode(int previousCPLastLineIndex, int nameSpaceIndex, UncompiledCode uncompiledCode) throws IOException;
}
