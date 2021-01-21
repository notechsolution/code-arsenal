const fs = require('fs')

function someAsyncOperation(callback){
    fs.readFile('C:\githome\github\code-arsenal\nodejs\package.json',callback);
}

const timeoutScheduled = Date.now();

setTimeout(() => {
    const delay = Date.now() - timeoutScheduled;
    console.log(`${delay}ms have passed since I was scheduled`);
}, 100)

someAsyncOperation(() => {
    const startCallback = Date.now();
    while( Date.now() - startCallback < 10){
        // do nothing, just empty loop
    }
})