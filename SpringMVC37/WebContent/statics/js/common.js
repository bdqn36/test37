var path = $("#path").val();
var imgYes = "<img width='15px' src='"+path+"/statics/images/y.png' />";
var imgNo = "<img width='15px' src='"+path+"/statics/images/n.png' />";
/**
 * 鎻愮ず淇℃伅鏄剧ず
 * element:鏄剧ず鎻愮ず淇℃伅鐨勫厓绱狅紙font锛�
 * css锛氭彁绀烘牱寮�
 * tipString:鎻愮ず淇℃伅
 * status锛歵rue/false --楠岃瘉鏄惁閫氳繃
 */
function validateTip(element,css,tipString,status){
	element.css(css);
	element.html(tipString);
	
	element.prev().attr("validateStatus",status);
}
var referer = $("#referer").val();