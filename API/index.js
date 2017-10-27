var express = require('express');
var app = express();
const bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

app.post('/authorize', function(req, res) {
  console.log("login");
  var bpp = require("./verify.js");
  bpp.verify(req.body.username,req.body.password, function(result){
  	if(result)
  		res.json({ message: "true"});
  	else
  		res.json({message: "false"});
  });
  
});

app.post('/verify', function(req, res) {
  console.log("register");
  var bpp = require("./verification.js");
  bpp.verify(req.body.username,req.body.verifyCode, req.body.password, function(result){
  	if(result)
  		res.json({ message: "true"});
  	else
  		res.json({message: "false"});
  });
  
});

app.post('/notification', function(req, res) {
  console.log("notification");
  var bpp = require("./notification.js");
  bpp.notification(req.body.patientID, function(result){
  		res.json({ message: result});
  });
  
});

app.post('/appointment', function(req, res) {
  console.log("appointment");
  var bpp = require("./appointment.js");
  bpp.Appointment(req.body.clinId,req.body.timeSlot,req.body.date,req.body.patient,function(result){
    if(result)
      res.json({ message: "true"});
    else
      res.json({message: "false"});
  });
  
});

app.post('/getverifycode', function(req, res) {
  console.log("getverifycode");
  var bpp = require("./getVerifyCode.js");
  bpp.getVerify(req.body.email,function(result){
      res.json({ message: result});
  });
  
});

app.post('/senddevicetoken', function(req, res) {
  console.log("senddevicetoken");
  var bpp = require("./sendDeviceToken.js");
  bpp.sendDeviceToken(req.body.email, req.body.deviceToken, function(result){
    console.log(req.body.email);
    console.log(req.body.deviceToken);
    res.json({ message: result});
  });
  
});
 
app.listen(3000);