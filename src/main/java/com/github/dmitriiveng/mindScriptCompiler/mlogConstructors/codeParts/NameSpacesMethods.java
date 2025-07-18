package com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts;

import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.Settings;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.UncompiledCode;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.methods.Method;

import java.util.Objects;

public interface NameSpacesMethods {
    public static String getVarNameWithPrefix(String varName, int nameSpaceIndex, UncompiledCode uncompiledCode){
        varName = varName.replace(" ", "");
        if(canGetNameSpacePrefix(varName, uncompiledCode)){
            return Settings.nameSpacePrefix + nameSpaceIndex + varName;
        }
        else {
            return varName;
        }
    }
    public static boolean canGetNameSpacePrefix(String name, UncompiledCode uncompiledCode){
        name = name.replace(" ", "");
        return
                !isLinkedBlockName(name) &&
                !isNumeric(name) &&
                !uncompiledCode.globalVars.contains(name) &&
                !Settings.mindustrySystemWords.contains(name) &&
                !hasPrefix(name) &&
                name.charAt(0) != '@' &&
                !isMethodWord(name, uncompiledCode);
    }
    public static boolean isLinkedBlockName(String name){
        name = name.replace(" ", "");
        if(!Settings.numbers.contains(String.valueOf(name.charAt(name.length()-1)))){return false;}
        int index = name.length() - 1;
        for (int i = name.length() - 1; i >= 0; i--){
            if(!Settings.numbers.contains(String.valueOf(name.charAt(i)))){
                index = i;
                break;
            }
        }
        StringBuilder word = new StringBuilder();
        for (int i = 0; i <= index; i ++){
            word.append(name.charAt(i));
        }
        return Settings.blockNames.contains(word.toString());
    }

    public static boolean isNumeric(String name){
        name = name.replace(" ", "");
        for (int i = 0; i < name.length(); i ++){
            if (!Settings.numbers.contains(String.valueOf(name.charAt(i))) && name.charAt(i) != '-'){
                return false;
            }
        }
        return true;
    }
    public static boolean isSystemWord(String name){
        name = name.replace(" ", "");
        if(!Settings.numbers.contains(String.valueOf(name.charAt(name.length()-1)))){return false;}
        int index = name.length() - 1;
        for (int i = name.length() - 1; i >= 0; i--){
            if(!Settings.numbers.contains(String.valueOf(name.charAt(i)))){
                index = i;
                break;
            }
        }
        StringBuilder word = new StringBuilder();
        for (int i = 0; i <= index; i ++){
            word.append(name.charAt(i));
        }
        return Settings.systemWords.contains(word.toString());
    }
    public static boolean isMethodWord(String name, UncompiledCode uncompiledCode){
        for(String methodWord: Settings.methodWords){
            for(Method method: uncompiledCode.methods){
                if(Objects.equals(name, methodWord + method.methodName)){return true;}
            }
        }
        return false;
    }
    public static boolean hasPrefix(String name){
        if(name.length() <= Settings.nameSpacePrefix.length()){return false;}
        for(int i = 0; i < Settings.nameSpacePrefix.length(); i++){
            if(name.charAt(i) != Settings.nameSpacePrefix.charAt(i)){
                return false;
            }
        }
        if(!Settings.numbers.contains(String.valueOf(name.charAt(Settings.nameSpacePrefix.length())))){return false;}
        return true;
    }
}
