/* 
         功能：将货币数字（阿拉伯数字）(小写)转化成中文(大写） 
    
         参数：Num为字符型,小数点之后保留两位,例：Arabia_to_Chinese("1234.06") 
         说明：1.目前本转换仅支持到 拾亿（元） 位，金额单位为元，不能为万元，最小单位为分 
                 2.不支持负数 
     */ 
function numtochinese(Num) 
{ 
        for(i=Num.length-1;i>=0;i--) 
        { 
     Num = Num.replace(",","")//替换tomoney()中的“,” 
     Num = Num.replace(" ","")//替换tomoney()中的空格 
        } 
    
        Num = Num.replace("￥","")//替换掉可能出现的￥字符 
        if(isNaN(Num))    
        { 
     //验证输入的字符是否为数字 
     alert("请检查小写金额是否正确"); 
     return; 
        } 
        //---字符处理完毕，开始转换，转换采用前后两部分分别转换---// 
        part = String(Num).split("."); 
        newchar = "";    
        //小数点前进行转化 
        for(i=part[0].length-1;i>=0;i--) 
        { 
         if(part[0].length > 10){ alert("位数过大，无法计算");return "";}//若数量超过拾亿单位，提示 
     tmpnewchar = "" 
     perchar = part[0].charAt(i); 
     switch(perchar){ 
     case "0": tmpnewchar="零" + tmpnewchar ;break; 
     case "1": tmpnewchar="壹" + tmpnewchar ;break; 
     case "2": tmpnewchar="贰" + tmpnewchar ;break; 
     case "3": tmpnewchar="叁" + tmpnewchar ;break; 
     case "4": tmpnewchar="肆" + tmpnewchar ;break; 
     case "5": tmpnewchar="伍" + tmpnewchar ;break; 
     case "6": tmpnewchar="陆" + tmpnewchar ;break; 
     case "7": tmpnewchar="柒" + tmpnewchar ;break; 
     case "8": tmpnewchar="捌" + tmpnewchar ;break; 
     case "9": tmpnewchar="玖" + tmpnewchar ;break; 
         } 
         switch(part[0].length-i-1) 
    { 
     case 0: tmpnewchar = tmpnewchar +"元" ;break; 
     case 1: if(perchar!=0)tmpnewchar= tmpnewchar +"拾" ;break; 
     case 2: if(perchar!=0)tmpnewchar= tmpnewchar +"佰" ;break; 
     case 3: if(perchar!=0)tmpnewchar= tmpnewchar +"仟" ;break;    
     case 4: tmpnewchar= tmpnewchar +"万" ;break; 
     case 5: if(perchar!=0)tmpnewchar= tmpnewchar +"拾" ;break; 
     case 6: if(perchar!=0)tmpnewchar= tmpnewchar +"佰" ;break; 
     case 7: if(perchar!=0)tmpnewchar= tmpnewchar +"仟" ;break; 
     case 8: tmpnewchar= tmpnewchar +"亿" ;break; 
     case 9: tmpnewchar= tmpnewchar +"拾" ;break; 
         } 
         newchar = tmpnewchar + newchar; 
        } 
        //小数点之后进行转化 
        if(Num.indexOf(".")!=-1) 
        { 
         if(part[1].length > 2) 
         { 
        alert("小数点之后只能保留两位,系统将自动截段"); 
        part[1] = part[1].substr(0,2) 
     } 
         for(i=0;i<part[1].length;i++) 
         { 
        tmpnewchar = "" 
        perchar = part[1].charAt(i) 
        switch(perchar){ 
        case "0": tmpnewchar="零" + tmpnewchar ;break; 
        case "1": tmpnewchar="壹" + tmpnewchar ;break; 
        case "2": tmpnewchar="贰" + tmpnewchar ;break; 
        case "3": tmpnewchar="叁" + tmpnewchar ;break; 
        case "4": tmpnewchar="肆" + tmpnewchar ;break; 
        case "5": tmpnewchar="伍" + tmpnewchar ;break; 
        case "6": tmpnewchar="陆" + tmpnewchar ;break; 
        case "7": tmpnewchar="柒" + tmpnewchar ;break; 
        case "8": tmpnewchar="捌" + tmpnewchar ;break; 
        case "9": tmpnewchar="玖" + tmpnewchar ;break; 
     } 
     if(i==0)tmpnewchar =tmpnewchar + "角"; 
     if(i==1)tmpnewchar = tmpnewchar + "分"; 
     newchar = newchar + tmpnewchar; 
         } 
        } 
        //替换所有无用汉字 
        while(newchar.search("零零") != -1) 
            newchar = newchar.replace("零零", "零"); 
        newchar = newchar.replace("零亿", "亿"); 
        newchar = newchar.replace("亿万", "亿"); 
        newchar = newchar.replace("零万", "万");    
        newchar = newchar.replace("零元", "元"); 
        newchar = newchar.replace("零角", ""); 
        newchar = newchar.replace("零分", ""); 
    
        if (newchar.charAt(newchar.length-1) == "元" || newchar.charAt(newchar.length-1) == "角") 
         newchar = newchar+"整" 
        return newchar; 
};
     
     function addComm(sid){
			var text = $("#commText").val();
			if(text == null || text.trim() == ""){
				alert("请输入跟踪记录的内容");
				return false;
			}
			var date = $("#commDate").val();
			if(date == null || date.trim() == ""){
				alert("请设置跟踪日期");
				return false;
			}
		 	/*var data = {
				 	action:'addCommunicate',
				 	sid:sid,
				 	content:text,
				 	commDate:date
			}*/
		 
			$.ajax({
	   			  type: "POST",
	   			  contentType: "application/json;charset=UTF-8",
	   			  url: "rest/crm/comm/", //{"action":"searchStudent","doesBill":true}
	   			  data: "{\"action\":\"addCommunicate\",\"sid\":"+sid+",\"content\":\""+text+"\",\"commDate\":\""+date+"\"}",
	   			  success: function(result){
	   				  if(result.length == 0){
	   					  alert("跟踪记录已成功添加,如果需要看到新增的记录，请刷新浏览器页面。");
	   					 $("#commText").val("");// = null;
	   					 $("#commDate").val("");// = null;
	   				  }else{
	   				  	alert("增加失败，详情： "+result);
	   				  }
	   			  },
	   			  error:function(result){
	   				alert("增加失败，详情： ："+result);
	   			  }
	   			  //dataType: 'json'
			});
		}
	    function  printFrame(frameId, payAmount,purpose,startDate,endDate,payDate,payer,receiver)
	    {
	    	//alert(payAmount+":"+payAmount+":"+purpose+":"+startDate+":"+endDate+":"+payDate+":"+payer+":"+receiver);
	         var doc = $('#'+frameId).get(0).contentWindow.document;
	        var $body = $('body',doc);
	      //  var payment = students[$index].paymentHistorys[idx];
	      //  $body.html(' <p style="color:red;position:relative;left:100px;top:50px;">'+payment.receiver+'</p>');
			var html =  '<p style="color:red;position:absolute;left:500px;top:30px;">'+payDate+'</p>';
			html +=  '<p style="color:red;position:absolute;left:200px;top:50px;">'+payer+'</p>';
			html += '<p style="color:red;position:absolute;left:150px;top:80px;">'+purpose+'</p>';
			html += '<p style="color:red;position:absolute;left:350px;top:80px;">('+startDate+'~'+endDate+')</p>';
			html += '<p style="color:red;position:absolute;left:150px;top:110px;">'+numtochinese(payAmount)+'</p>';
			html += '<p style="color:red;position:absolute;left:350px;top:110px;">'+payAmount+'</p>';
			html += '<p style="color:red;position:absolute;left:600px;top:300px;">'+receiver+'</p>';
	        $body.html(html);
	        setTimeout(function(){
	            $('#'+frameId).get(0).contentWindow.focus();
	            $('#'+frameId).get(0).contentWindow.print();
	        }, 1000); 
	        return false;
	    }
	    $(function(){
	    	$("tbody > tr:odd").addClass("odd");
	    	$("tbody > tr:even").addClass("even");
	    })
	    
	    function logout(){
	    	var r=confirm("确定退出系统吗?");
	    	if(r != true)return;
	    	$.ajax({
	   			  type: "GET",
	   			//  contentType: "application/json;charset=UTF-8",
	   			  url: "truscreen/doctor/1", //{"action":"searchStudent","doesBill":true}
	   			  //data: "{\"action\":\"addCommunicate\",\"sid\":"+sid+",\"content\":\""+text+"\",\"commDate\":\""+date+"\"}",
	   			  success: function(result){
	   				document.cookie="doctor" + "=" + null;
	   				if(result.isSuccess){
		    			alert("成功退出系统");
//		    			$("#reportLink").hide();
//		    			$("#mypageLink").hide();
//		    			$("#doctorLink").hide();
//		    			$("#logout").hide();
		    			location.href = "#/crm/login";
		    			$("#reportLink").hide();
		    			$("#mypageLink").hide();
		    			$("#doctorLink").hide();
		    			$("#logout").hide();
		    			$("#systemsetting").hide();
		    		}else{
			    		alert("退出登录失败，详情： "+result.description);
			    	}
	   				document.cookie="doctor" + "=" + null;
	   			  },
	   			  error:function(result){
	   				alert("增加失败，详情： ："+result);
	   			  }
	   			  //dataType: 'json'
			});
	    };
	    
	    function checkfloat(e,obj) {
		    var reg = new RegExp("^[0-9]+(\.\[0-9]+)?$");  // ^-\d*\.{0,1}\d+$
	       if(!reg.test(obj.value)){
	           alert("请输入数字!");
	           obj.value = null;
	       }
	    }; 
	    
	    function checkNumber(e,obj){
	    	  var reg = new RegExp("^[0-9]+$");  // ^-\d*\.{0,1}\d+$
		       if(!reg.test(obj.value)){
		           alert("请输入一个大于等于0 的整数!");
		           obj.value = null;
		           return false;
		       }
		       return true;
	    };
	    function checkPointNumber(e,obj){
	    	if(checkNumber(e,obj)){
//	    		if(obj.value < 15 || obj.value > 32){
//	    			alert("点探数量值必须在15和32之间，请填入正确的值。");
//	    			  obj.value = null;
//	    		}
	    	}
	    };
	    
	   function doesNeedStopFormSubmit(doesNotCancleModify,isModify){
		   if(doesNotCancleModify)
			   return !isModify;
			  
		   else
			   return false;
		};
	  function isImage(e,obj){
		  var imagePath = obj.value;
		  var lowerValue = obj.value.toString().toLowerCase();
		  var reg = new RegExp("[^\\s]+\\.(jpg|png|gif|bmp)$");
		  if(!reg.test(lowerValue)){
//			  alert("请选择图片上传");
//			  obj.value = null;
//			  $("#preveiwLogo").css("display","none");
//			  return;
		  }else{
			  $("#preveiwLogo").css("display","");
			  readURL(obj);
		  }
	  };
	  
	  function readURL(input) {
		    if (input.files && input.files[0]) {
		        var reader = new FileReader();
		        reader.onload = function (e) {
		            $('#blah').attr('src', e.target.result);
		        }
		        reader.readAsDataURL(input.files[0]);
		    }
		};
