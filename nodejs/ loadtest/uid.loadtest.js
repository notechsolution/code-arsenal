const loadtest = require('loadtest');
const options = {
  url: 'http://localhost:3060/api/caches/uid',
  maxRequests: 1000,
  concurrency: 10,
  statusCallback: printResult
};
loadtest.loadTest(options, function(error, result)
{
  if (error)
  {
    return console.error('Got an error: %s', error);
  }
  console.log(result);
});

function printResult(error, result, latency){
  console.log(result.body);
}