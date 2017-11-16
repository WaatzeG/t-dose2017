import com.google.common.base.Splitter;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;

import java.util.Collections;

public class WordSplitterBolt extends BaseBasicBolt {

    private char[] specialchars = new char[]{'-', ',', '.', '?', '"', '\'', '/', '!', '?', '(', ')', ':', ';', '[', ']'};

    @Override
    public void execute(final Tuple input, final BasicOutputCollector collector) {
        String line = input.getStringByField("line");
        line = replaceSpecialChar(line);

        final Iterable<String> words = Splitter.on(' ')
                .trimResults()
                .omitEmptyStrings()
                .split(line);

        for (String word : words) {
            word = word.toLowerCase();
            collector.emit(Collections.singletonList(word));
        }
    }

    @Override
    public void declareOutputFields(final OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word"));
    }

    private String replaceSpecialChar(String line) {
        for (final char specialChar : specialchars) {
            line = line.replace(specialChar, ' ');
        }
        return line;
    }
}
