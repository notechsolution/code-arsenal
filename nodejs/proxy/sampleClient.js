const http = require("http");
const Tls = require('tls');

console.log(`#1: get HTTP request`);
const options = {
    host: "localhost",
    port: 8888,
    path: "http://www.baidu.com",
    headers: {
        Host: "www.baidu.com"
    },
    rejectUnauthorized: true
};
http.get(options, function(res) {
    // console.log(res);
    res.pipe(process.stdout);
});

console.log(`#2: get HTTPS request`);
const targetHost = 'www.baidu.com';
const req = http.request({
    host: 'localhost',
    port: 8888,
    method: 'CONNECT',
    path: `${targetHost}:443`,
});

req.on('connect', function (res, socket, head) {
    const tlsSocket = Tls.connect({
        host:targetHost,
        socket: socket
    }, function () {
        tlsSocket.write(`GET / HTTP/1.1\r\nHost: ${targetHost}:443+\r\nConnection: close\r\n\r\n`);
    });

    tlsSocket.on('secureConnect', function (data) {
        console.log(`[event]-secureConnect: ${tlsSocket.authorized}, ${tlsSocket.authorizationError}`);
    })
    tlsSocket.on('data', function (data) {
        console.log(data.toString());
    });
    tlsSocket.on('error', function (error) {
        console.error(error);
    });
});

req.end();
