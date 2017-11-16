import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

public class Main {

    private static final String SHAKESPEARE_READER = "shakespeare_reader";
    private static final String WORD_SPLITTER = "word_splitter";
    private static final String WORD_FILTER = "small_words_filter";
    private static final String WORD_COUNTER = "word_counter";
    private static final String WORD_COUNTER_DB = "word_counter_storage";
    private static final String TOPOLOGY_NAME = "word_counter";

    public static void main(final String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException, InterruptedException {
        TopologyBuilder builder = new TopologyBuilder();

        // read from resource file
        builder.setSpout(SHAKESPEARE_READER, new ShakespeareReaderSpout());

        // split into words
        builder.setBolt(WORD_SPLITTER, new WordSplitterBolt())
                .shuffleGrouping(SHAKESPEARE_READER);

        // filter words > 3 characters
        builder.setBolt(WORD_FILTER, new WordFilterBolt())
                .shuffleGrouping(WORD_SPLITTER);

        // word counter
        builder.setBolt(WORD_COUNTER, new WordCounter())
                .addConfiguration(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, 5)
                .fieldsGrouping(WORD_FILTER, new Fields("word"));

        // store in database
        builder.setBolt(WORD_COUNTER_DB, new StoreWordCount())
                .shuffleGrouping(WORD_COUNTER);

        final Config config = new Config();
        config.setNumWorkers(4);
        StormSubmitter.submitTopology(TOPOLOGY_NAME, config, builder.createTopology());
        //        new LocalCluster().submitTopology("word_counter", config, builder.createTopology());
        //        new CountDownLatch(1).await();
    }
}
