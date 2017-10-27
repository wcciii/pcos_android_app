function A(callback){
    setTimeout(function(){
        var resultValue="AAA";
        if(Math.floor(Math.random()*10)%2){
           resultValue="AAA";
        }else{
            resultValue="AAAA";
        }
        callback(resultValue);
    },1000);
}

function B(input_value,callback){
    setTimeout(function(){
        callback("BBB");
    },1000);
}


function C(input_value,callback){
    setTimeout(function(){
        callback("CCC");
    },1000);
}

var promiseA=new Promise(function(resolve,reject){
   A(function(resultValue){
       resolve(resultValue);
   });
});

var promiseB=promiseA.then(function(resultValueA){
    if(resultValueA=="AAA"){
        
        return new Promise(function(resolve,reject){
            //执行B
            B(resultValueA,function(result){
                console.log("~~执行B~~");
                resolve(result);
            });
        });
    }else if(resultValueA=="AAAA"){
        return new Promise(function(resolve, reject){
            //或者执行C
            C(resultValueA,function(result){
                console.log("~~执行C~~");
                resolve(result);
            });
        });
        
    }
});

promiseB.then();

promiseB.then(function(result){
    console.log("~~~BBB~~~"+result);
});