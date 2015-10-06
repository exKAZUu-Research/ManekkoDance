package net.exkazuu.mimicdance.program;

import net.exkazuu.mimicdance.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class CodeParserTest {
    @Test
    public void parseSimpleProgram() {
        String code = "左腕を上げる右腕を上げる\n左腕を下げる\n右腕を下げる";
        Block block = CodeParser.parse(code);
        assertThat(block.unroll(true).getCode(), is(code));
    }

    @Test
    public void parseLoopProgram() {
        String code = "ジャンプする\nくりかえし2\n左腕を上げる右腕を上げる\n右腕を下げる\nここまで";
        String unrolledCode =
            "ジャンプする\n左腕を上げる右腕を上げる\n右腕を下げる\n左腕を上げる右腕を上げる\n右腕を下げる";
        Block block = CodeParser.parse(code);
        assertThat(block.unroll(true).getCode(), is(unrolledCode));
    }

    @Test
    public void parseIfProgram() {
        String code = "右腕を上げる\nもしもしろ\n右腕を下げる\nもしくは\n左腕を上げる\nもしおわり";
        String unrolledNormalCode = "右腕を上げる\n右腕を下げる";
        String unrolledAltCode = "右腕を上げる\n左腕を上げる";
        Block block = CodeParser.parse(code);
        assertThat(block.unroll(true).getCode(), is(unrolledNormalCode));
        assertThat(block.unroll(false).getCode(), is(unrolledAltCode));
    }

    @Test
    public void parseEmptyIfProgram() {
        String code = "もしもしろ\nもしおわり";
        String unrolledNormalCode = "";
        String unrolledAltCode = "";
        Block block = CodeParser.parse(code);
        assertThat(block.unroll(true).getCode(), is(unrolledNormalCode));
        assertThat(block.unroll(false).getCode(), is(unrolledAltCode));
    }

    @Test
    public void parseIncompleteIfProgram() {
        String code = "もしもしろ\n";
        String unrolledNormalCode = "";
        String unrolledAltCode = "";
        Block block = CodeParser.parse(code);
        assertThat(block.unroll(true).getCode(), is(unrolledNormalCode));
        assertThat(block.unroll(false).getCode(), is(unrolledAltCode));
    }

    @Test
    public void parseLoopIfProgram() {
        String code = "くりかえし2\nもしもきいろ\n左腕を上げる\nもしくは\n右腕を上げる\n右腕を下げる\nもしおわり\nここまで";
        String unrolledNormalCode = "右腕を上げる\n右腕を下げる\n右腕を上げる\n右腕を下げる";
        String unrolledAltCode = "左腕を上げる\n\n左腕を上げる\n";
        Block block = CodeParser.parse(code);
        assertThat(block.unroll(true).getCode(), is(unrolledNormalCode));
        assertThat(block.unroll(false).getCode(), is(unrolledAltCode));
    }
}
