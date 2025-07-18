package mlogConstructors.codeParts;

import mlogConstructors.UncompiledCode;
import mlogConstructors.codeParts.methods.Method;

import java.util.Objects;

import static mlogConstructors.Settings.*;

public interface NameSpacesMethods {
    public static String getVarNameWithPrefix(String varName, int nameSpaceIndex, UncompiledCode uncompiledCode){
        varName = varName.replace(" ", "");
        if(canGetNameSpacePrefix(varName, uncompiledCode)){
            return nameSpacePrefix + nameSpaceIndex + varName;
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
                !mindustrySystemWords.contains(name) &&
                !hasPrefix(name) &&
                name.charAt(0) != '@' &&
                !isMethodWord(name, uncompiledCode);
    }
    public static boolean isLinkedBlockName(String name){
        name = name.replace(" ", "");
        if(!numbers.contains(String.valueOf(name.charAt(name.length()-1)))){return false;}
        int index = name.length() - 1;
        for (int i = name.length() - 1; i >= 0; i--){
            if(!numbers.contains(String.valueOf(name.charAt(i)))){
                index = i;
                break;
            }
        }
        StringBuilder word = new StringBuilder();
        for (int i = 0; i <= index; i ++){
            word.append(name.charAt(i));
        }
        return blockNames.contains(word.toString());
    }

    public static boolean isNumeric(String name){
        name = name.replace(" ", "");
        for (int i = 0; i < name.length(); i ++){
            if (!numbers.contains(String.valueOf(name.charAt(i))) && name.charAt(i) != '-'){
                return false;
            }
        }
        return true;
    }
    public static boolean isSystemWord(String name){
        name = name.replace(" ", "");
        if(!numbers.contains(String.valueOf(name.charAt(name.length()-1)))){return false;}
        int index = name.length() - 1;
        for (int i = name.length() - 1; i >= 0; i--){
            if(!numbers.contains(String.valueOf(name.charAt(i)))){
                index = i;
                break;
            }
        }
        StringBuilder word = new StringBuilder();
        for (int i = 0; i <= index; i ++){
            word.append(name.charAt(i));
        }
        return systemWords.contains(word.toString());
    }
    public static boolean isMethodWord(String name, UncompiledCode uncompiledCode){
        for(String methodWord: methodWords){
            for(Method method: uncompiledCode.methods){
                if(Objects.equals(name, methodWord + method.methodName)){return true;}
            }
        }
        return false;
    }
    public static boolean hasPrefix(String name){
        if(name.length() <= nameSpacePrefix.length()){return false;}
        for(int i = 0; i < nameSpacePrefix.length(); i++){
            if(name.charAt(i) != nameSpacePrefix.charAt(i)){
                return false;
            }
        }
        if(!numbers.contains(String.valueOf(name.charAt(nameSpacePrefix.length())))){return false;}
        return true;
    }
}
