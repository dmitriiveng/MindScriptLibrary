import compilers.mindScriptCode.LineOfCode;
import mlogConstructors.UncompiledCode;
import mlogConstructors.codeParts.methods.Method;
import mlogConstructors.codeParts.methods.ReturnValueFromMethod;
import mlogConstructors.mathEngine.MathData;
import mlogConstructors.mathEngine.MathematicalExpressionReader;

import java.io.IOException;
import java.util.*;

import static compilers.mindScriptCode.MindScriptCompiler.convertCodeIntoUncompiledCode;
import static mlogConstructors.mathEngine.MathematicalExpressionReader.readExpression;

public class Main {
    public static void main(String[] args) {
        boolean exit = false;
        while (!exit) {
            UncompiledCode uncompiledCode;
            Scanner scanner = new Scanner(System.in);
            StringBuilder sb = new StringBuilder();

            System.out.println("Enter your code, and then print ..compile\nPrint ..exit to exit");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (Objects.equals(line, "..exit")) {
                    exit = true;
                    break;
                }
                if (Objects.equals(line, "..compile")) {
                    break;
                }
                sb.append(line).append("\n");
            }
            if (exit){break;}
            sb.deleteCharAt(sb.length() - 1); // delete the last \n character
            String code = sb.toString();

            try {
                uncompiledCode = convertCodeIntoUncompiledCode(code);
                System.out.println(uncompiledCode.compile());
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        }
    }
}
