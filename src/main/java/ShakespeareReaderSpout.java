import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ShakespeareReaderSpout extends BaseRichSpout {
    private SpoutOutputCollector collector;
    private Scanner shakespeareTextReader;

    @Override
    public void open(final Map conf, final TopologyContext context, final SpoutOutputCollector collector) {
        this.collector = collector;
        final InputStream shakespeareTextStream = getClass().getClassLoader().getResourceAsStream("shakespeare_cleanup.txt");
        shakespeareTextReader = new Scanner(shakespeareTextStream);
    }

    @Override
    public void close() {
        shakespeareTextReader.close();
    }

    @Override
    public void nextTuple() {
        if (shakespeareTextReader.hasNextLine()) {
            final List<Object> line = Collections.singletonList(shakespeareTextReader.nextLine());
            collector.emit(line);
        }
    }

    @Override
    public void declareOutputFields(final OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("line"));
    }
}
