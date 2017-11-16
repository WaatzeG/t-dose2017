import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class StoreWordCount extends BaseBasicBolt {

    private Jedis jedis;

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        jedis = new Jedis("redis");
    }

    @Override
    public void execute(final Tuple input, final BasicOutputCollector collector) {
        final String word = input.getStringByField("word");
        final long count = input.getLongByField("count");
        jedis.zadd("words", count, word);
    }

    @Override
    public void declareOutputFields(final OutputFieldsDeclarer declarer) {
    }

    @Override
    public void cleanup() {
        jedis.close();
    }
}
