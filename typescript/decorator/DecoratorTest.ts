function log(target: Object, funcName: string, descriptor: any){
    const originalMethod = descriptor.value;
    descriptor.value = function (...args: any[]){
        const result = originalMethod.apply(this, args);
        console.log(`Call: ${funcName}(${JSON.stringify(args)}) => ${result}`)
    }
    return descriptor;
}

class Teacher {

    @log
    teach(className:string) {
        console.log(`start teaching...`);
        const result = `I am teaching class ${className}`
        return result;
    }
}

const teachA = new Teacher();
teachA.teach('English');
