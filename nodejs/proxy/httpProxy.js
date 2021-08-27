const http = require('http');
const net = require('net');
const {URL} = require('url');
const requestIP = require('request-ip');
const _ = require('lodash');
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
    // timestamp clientAddress requestMethod URI result/statusCodes responseTime
    const startAt = process.hrtime();
    let logLine = `${Date.now()} ${requestIP.getClientIp(srcReq)} ${srcReq.method} ${srcReq.url}`
    const destRequest = http.request(options, destResponse => {
        srcRes.writeHead(destResponse.statusCode, destResponse.headers);
        destResponse.pipe(srcRes)
        logLine += ` ${destResponse.statusCode} ${_.get(destResponse.headers,"content-type",'-')} ${_getTotalTime(startAt)}`
        access.log(logLine);
    }).on('error', e => {
        srcRes.end();
        logLine += ` ERROR ${_getTotalTime(startAt)}`;
        error.log(logLine + '\r\n' + e)
    })
    srcReq.pipe(destRequest);
}

function _getTotalTime(startAt){
    // time elapsed from request start
    const elapsed = process.hrtime(startAt)

    // cover to milliseconds
    const ms = (elapsed[0] * 1e3) + (elapsed[1] * 1e-6)

    // return truncated value
    return ms.toFixed(3)
}
function connect(srcReq, srcSocket) {
    const url = new URL(`http://${srcReq.url}`);
    const startAt = process.hrtime();
    let logLine = `${Date.now()} CONNECT ${url.hostname}:${url.port}`
    const destSocket = net.connect(parseInt(url.port), url.hostname, () => {
        srcSocket.write('HTTP/1.1 200 Connection Established\r\n\r\n');
        destSocket.pipe(srcSocket);
        logLine += ` ${_getTotalTime(startAt)}`
        access.log(logLine);
    }).on('error', e => {
        srcSocket.end();
        logLine += ` ERROR ${_getTotalTime(startAt)}`;
        error.log(logLine + '\r\n' + e)
    })

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
