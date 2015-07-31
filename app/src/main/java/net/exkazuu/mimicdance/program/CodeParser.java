package net.exkazuu.mimicdance.program;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeParser {

    private int lineIndex;

    private CodeParser() {
    }

    public static Block parse(String commandsText) {
        List<String> lines = Arrays.asList(commandsText.split("\n")); // List型配列に変換
        return new CodeParser().parseBlock(lines, new String[]{});
    }

    private Block parseBlock(List<String> lines, String[] endLineTokens) {
        ArrayList<Statement> statements = new ArrayList<>();
        for (; lineIndex < lines.size(); lineIndex++) {
            String line = lines.get(lineIndex);
            if (contains(line, endLineTokens)) {
                break;
            }
            statements.add(parseStatement(lines));
        }
        return new Block(statements);
    }

    private Statement parseStatement(List<String> lines) {
        String line = lines.get(lineIndex);
        if (line.contains("くりかえし")) {
            return parseLoop(lines);
        } else if (line.contains("もしも")) {
            return parseIf(lines);
        }
        return new Action(line, lineIndex);
    }

    private IfStatement parseIf(List<String> lines) {
        String firstLine = lines.get(lineIndex++);
        assert firstLine.contains("もしも");
        Block trueBlock = parseBlock(lines, new String[]{"もしくは", "もしおわり"});
        Block falseBlock;
        if (lineIndex < lines.size() && lines.get(lineIndex++).contains("もしくは")) {
            falseBlock = parseBlock(lines, new String[]{"もしおわり"});
        } else {
            falseBlock = new Block();
        }
        return new IfStatement(trueBlock, falseBlock, readCondition(firstLine));

    }

    private LoopStatement parseLoop(List<String> lines) {
        String firstLine = lines.get(lineIndex++);
        assert firstLine.contains("くりかえし");
        Block block = parseBlock(lines, new String[]{"ここまで"});
        return new LoopStatement(block, readCount(firstLine));
    }

    private static int readCount(String loopCount) {
        Pattern p = Pattern.compile("[0-9]");
        Matcher m = p.matcher(loopCount);
        if (!m.find()) {
            return 0; // 0回繰り返し
        } else {
            return Integer.parseInt(m.group());
        }
    }

    private static boolean readCondition(String conditionString) {
        return !conditionString.contains("きいろ");
    }

    private static boolean contains(String line, String[] endLineTokens) {
        for (String endLineToken : endLineTokens) {
            if (line.contains(endLineToken)) {
                return true;
            }
        }
        return false;
    }
}
