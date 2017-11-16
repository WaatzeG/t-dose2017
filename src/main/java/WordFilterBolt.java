import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;

import java.util.Collections;

public class WordFilterBolt extends BaseBasicBolt {
    @Override
    public void execute(final Tuple input, final BasicOutputCollector collector) {
        final String word = input.getStringByField("word");

        //prevent 'a' and 'the' from reaching the top of the list
        if (word.length() > 3) {
            collector.emit(Collections.singletonList(word));
        }
    }

    @Override
    public void declareOutputFields(final OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word"));
    }
}
