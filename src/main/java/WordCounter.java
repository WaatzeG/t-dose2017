import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

public class WordCounter extends BaseBasicBolt {

    private HashMap<String, LongAdder> wordCounts;

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        wordCounts = new HashMap<>();
    }

    @Override
    public void execute(final Tuple input, final BasicOutputCollector collector) {
        if (!isTickTuple(input)) {
            final String word = input.getStringByField("word");
            wordCounts.computeIfAbsent(word, s -> new LongAdder()).increment();
            Utils.sleep(1);
        } else {
            wordCounts.forEach((word, counter) -> {
                        final List<Object> count = new ArrayList<>(2);
                        count.add(word);
                        count.add(counter.longValue());
                        collector.emit(count);
                    }
            );
        }
    }

    @Override
    public void declareOutputFields(final OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word", "count"));
    }

    private boolean isTickTuple(final Tuple input) {
        return "__system".equals(input.getSourceComponent()) && "__tick".equals(input.getSourceStreamId());
    }
}
