const net = require('net');
const requestIP = require("request-ip");
const access = console;
const errorLog = console;

const SOCKET_TIMEOUT = process.env.SOCKET_TIMEOUT || 60000;
const destHost = process.env.DEST_HOST || '';
const destPort = process.env.DEST_PORT || 0;

function connect(socket) {
    const startAt = process.hrtime();
    let logLine = `${Date.now()} ${socket.remoteAddress} ${socket.remotePort} ${socket.remoteFamily}`;
    access.log(logLine);
    socket.setTimeout(SOCKET_TIMEOUT, () => {
        errorLog.log(`${logLine} Socket Timeout ${SOCKET_TIMEOUT}`);
    })

    const destSocket = net.connect(parseInt(destPort), destHost, () => {
        destSocket.pipe(socket);
    }).on('error', e => {
        socket.end();
    });
    socket.pipe(destSocket);

    socket.on('timeout', function () {
        logLine += ` ${_getTotalTime(startAt)}`
        errorLog.log(logLine);
        socket.end('Timed out!');
    });
    socket.on('error',function(error){
        errorLog.log('Error : ' + error);
    });
}
const server = net.createServer();
server.on('listening', () => console.log('Server is listening'));
server.on('connection', connect);
server.on('error', e => {
    console.log(`Error found ${e}`);
})

const port = parseInt(process.env.TCP_PORT) || 33555;
server.listen(port, () => {
    console.log('Server is listening at port ' + port);
})


function _getTotalTime(startAt) {
    // time elapsed from request start
    const elapsed = process.hrtime(startAt)
    // cover to milliseconds
    const ms = (elapsed[0] * 1e3) + (elapsed[1] * 1e-6)
    // return truncated value
    return ms.toFixed(3)
}
