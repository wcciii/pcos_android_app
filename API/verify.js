var bcrypt = require('bcrypt');


exports.verify = function test (username,password,callback)
{
	var hash = '';
	var res = false;
	var promise = new Promise(function(resolve, reject){});
	var mysql = require('mysql');
	var bcrypt = require('bcrypt');
	const saltRounds = 12;

	function connFunc(callback){
		var ifmatch = false;

		var connect = mysql.createConnection(
		{
			host:"localhost",
			user:"root",
			password:"123qweasd",
			database:"pcos"
		}
		);

		connect.connect(function(err)
		{
			
			var testTemp = username;
			var emailadd = "\'" + testTemp + "\'";
			if (err) throw err;
			connect.query("SELECT password FROM pcos.security_user where email ="+emailadd,function(err,result,fields)
			{
				hash = getJsonValue(result,"password");
				/*promise.then(function (message){
					ifmatch = bcrypt.compare(password,hash);
				}
				);*/
				//ifmatch = verify(password,hash);
				verify(password,hash,function(res){
					callback(res);
				});
			}
			);
		}
		);
	}

	connFunc(function(res){
		callback(res);
	});

}

function addTask(task){

         tasks.push(task);

}

function next(){

         if(tasks.length > 0){

               tasks.shift()();

       }else{

             return;

     }

}

function verify(password,hash, callback)
{
	var result = false;
	bcrypt.genSalt(12, function(err, salt) 
	{
        bcrypt.compare(password, hash, function(err, res) 
        {
     		callback(res);
     		result = res;

     		//console.log("111");
		}
		);
	}
	);
	//console.log(result);
	return result;
}



function getJsonValue (obj,name)
{
    var result = null;
    var value  = null;
    for(var key in obj)
    {        
        value = obj[key];
        if(key == name)
        {
        	return value;
        } 
        else 
        {
        	if( typeof value == "object" )
        	{
            	result = getJsonValue(value,name);
            }
        }
    }
    return result;
}
