require('./tracing.js');
const express = require('express')
const {countAllRequests} = require('./monitoring');

const PORT = process.env.PORT || '8080';
const app = express();
app.use(countAllRequests());

app.get('/', (req, res) => {
    res.send('Hello OpenTelemetry Demo');
})

app.listen(parseInt(PORT), '0.0.0.0', null, () => {
    console.log(`Listening on http://localhost:${PORT}`);
})
