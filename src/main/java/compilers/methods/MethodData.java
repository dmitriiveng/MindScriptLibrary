package compilers.methods;

import java.util.List;

public class MethodData {
    public String name;
    public List<String> args;
    public MethodData(String name, List<String> args){
        this.name = name;
        this.args = args;
    }
}
