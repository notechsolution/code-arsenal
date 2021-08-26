const http = require('http');
const net = require('net');
const {URL} = require('url');
const access = console;
const error = console;

function request(srcReq, srcRes) {
    const url = new URL(srcReq.url);
    const options = {
        hostname: url.hostname,
        port: url.port || 80,
        path: url.pathname,
        method: srcReq.method,
        headers: srcReq.headers
    }
    let logLine = `${new Date().toISOString()} ${srcReq.method} ${srcReq.url}`
    const destRequest = http.request(options, destResponse => {
        srcRes.writeHead(destResponse.statusCode, destResponse.headers);
        destResponse.pipe(srcRes)
        logLine += ` ${destResponse.statusCode}`
    }).on('error', e => {

        srcRes.end();
        logLine += ' ERROR';
        error.log(logLine+'\r\n'+ e)
    })
    access.log(logLine);

    srcReq.pipe(destRequest);
}

function connect(srcReq, srcSocket){
    const url = new URL(`http://${srcReq.url}`);
    let logLine = `${new Date().toISOString()} CONNECT ${url.hostname}:${url.port}`
    const destSocket = net.connect(parseInt(url.port), url.hostname, ()=>{
        srcSocket.write('HTTP/1.1 200 Connection Established\r\n\r\n');
        destSocket.pipe(srcSocket);
    }).on('error', e => {
        srcSocket.end();
        logLine += ' ERROR';
        error.log(logLine+'\r\n'+ e)
    })
    access.log(logLine);
    srcSocket.pipe(destSocket);
}
const PORT = parseInt(process.env.PORT) || 8888;
console.log(`Starting My Proxy...`)
http.createServer()
    .on('request', request)
    .on('connect', connect)
    .listen(PORT, '0.0.0.0', null, () => {
        console.log(`Started My Proxy at port ${PORT}.`)
    });
