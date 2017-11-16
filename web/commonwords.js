var redis = require("redis"),
client = redis.createClient('//redis');

var http = require('http');
var ol = '';
var list = ''

client.on("error", function (err) {
    console.log("Error " + err);
});

var refresh = function (){
    client.zrevrange('words', 0, 10, function (err, replies) {
    if(replies) {
        ol = '<ol>';
        replies.forEach(function (reply, i){
            client.zscore('words', reply, function (err, score){
                ol += '<li>' + reply + ' - ' + score + '</li>';
            });
        });
        ol += '</ol>';
    }
    if(err){
        console.log('error: ' + err);
    }
    });
}


http.createServer(function (req, res) {
  refresh();
  res.writeHead(200, {'Content-Type': 'text/html'});
  res.write('<html><body><h1>Top 10 words:</h1><p id="top10">' + ol + '</p></body></html>');
  res.end();
})
.listen(8001);

