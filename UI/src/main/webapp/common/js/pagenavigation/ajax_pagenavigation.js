/**
 * Ajax PageNavigation
 */

var PageNavigation = (function(PageNavigation, $, undefined){
// $ = jQuery임 전역변수를 지역변수로 전달하면 실행함수내에서 지역변수로 사용 하기때문에 탐색작업이 좀더 빨라진다
	var _obj, _stData, _goUrl, _currDataNo;
    
    PageNavigation.setObj = function(obj){ 						//외부 노출 함수 public
        this._obj = obj;										// 네비게이션 Object
    };
    
    PageNavigation.setStData = function(stData){
    	this._stData = stData;		// 네비게이션 기본 데이터
    };
    
    PageNavigation.setGoUrl = function(goUrl){
    	this._goUrl = goUrl;		// 이동 Url
    };
    
    PageNavigation.setcurrDataNo = function(currDataNo){
    	this._currDataNo = currDataNo;		// 이동 Url
    };
    
    PageNavigation.getcurrDataNo = function(){
    	return this._currDataNo;		// 이동 Url
    };
    
    function createNavigationBar(pnv){
    	
    	var rtnStr 	 = "";
    	var nextPage = 0;
    	
    	if(pnv.totalCount > 0){
    		rtnStr += "<ul>";
    	}
    	
    	if( pnv.firstPage + pnv.naviCount >  pnv.lastPage ){
    		nextPage = pnv.lastPage + 1;
    	} else {
    		nextPage = pnv.firstPage + pnv.naviCount;
    	}
    	
    	rtnStr += "<li><a href=\"#\" title=\"맨앞으로가기\" onclick=\"f_GoPage('1'); return false;\"><img src=\"/common/images/icon_back2.jpg\" alt=\"맨앞으로가기\"/></a></li>";
    	
    	if ( pnv.firstPage > pnv.naviCount ){
    		rtnStr += "<li><a href=\"#\" title=\"앞으로가기\" onclick=\"f_GoPage('" + (pnv.firstPage - 1)
						+ "'); return false;\"><img src=\"/common/images/icon_back1.jpg\" alt=\"앞으로가기\"/></a></li>";
    	}
    	
    	for ( var i = pnv.firstPage; i < nextPage; i+=1 ){
    		if( pnv.currentPage === i){
    			rtnStr += "<li class=\"on\"><a href=\"#\" onclick=\"f_GoPage('" + i + "'); return false;\">" + i + "</a></li>";
    		}else {
    			rtnStr += "<li><a href=\"#\" onclick=\"f_GoPage('" + i + "'); return false;\">" + i + "</a></li>";
    		}
    	}
    	
    	if( pnv.firstPage + pnv.naviCount - 1 < pnv.lastPage ){
    		rtnStr += "<li><a href=\"#\" title=\"뒤로가기\" onclick=\"f_GoPage('" + ( pnv.firstPage + pnv.naviCount )
			          + "'); return false;\"><img src=\"/common/images/icon_next1.jpg\" alt=\"뒤로가기\"/></a></li>";
    	}
    	
    	rtnStr += "<li><a href=\"#\" title=\"맨뒤로가기\" onclick=\"f_GoPage('" + pnv.lastPage + "'); return false;\"><img src=\"/common/images/icon_next2.jpg\" alt=\"맨뒤로가기\"/></a></li>";
    	rtnStr += "</ul>";
    	rtnStr += "<input type=\"hidden\" name=\"currentPage\" id=\"currentPage\" value=\"" + pnv.currentPage + "\"/>";
    	
    	return rtnStr;
    	
    };
    
    PageNavigation.createNavi = function(){
    	
    	var DEFROWPERPAGE, DEFCURRENTPAGE, DEFNAVICOUNT;			// 상수
    	DEFROWPERPAGE 	= 10;										// 디폴트 한화면에 보여줘야할 컨텐츠 수
    	DEFCURRENTPAGE	= 1;										// 디폴트 현재페이지
    	DEFNAVICOUNT	= 10; 										// 디폴트 페이지 갯수    
    	
    	var totalCount, rowPerPage, currentPage, naviCount, lastPage, dummyPage;
    	totalCount 	= Number( (typeof this._stData.totalCount === "undefined" || this._stData.totalCount === "" )	? 0 : this._stData.totalCount );
    	rowPerPage 	= Number( (typeof this._stData.rowPerPage === "undefined" || this._stData.rowPerPage === "" ) 	? DEFROWPERPAGE : this._stData.rowPerPage );
    	currentPage = Number( (typeof this._stData.currentPage === "undefined"|| this._stData.currentPage === "" ) 	? DEFCURRENTPAGE : this._stData.currentPage );
    	naviCount 	= Number( (typeof this._stData.naviCount === "undefined" 	|| this._stData.naviCount === "" ) 	? DEFNAVICOUNT : this._stData.naviCount );

    	lastPage  = parseInt(totalCount / rowPerPage);
    	dummyPage = 0;
    	if( totalCount % rowPerPage > 0){	//나머지가 존재할경우 1페이지 추가
    		dummyPage = 1;
    	}
    	lastPage  += dummyPage;
    	
    	var plusPage, firstPage, currDataNo; 
    	plusPage = (currentPage % naviCount === 0) ? naviCount + 1 : 1;
    	firstPage = parseInt(currentPage /  naviCount * ( naviCount + plusPage ));
    	currDataNo = totalCount - ( ( currentPage  -1 ) * rowPerPage );
    	PageNavigation.setcurrDataNo(currDataNo);
    	
    	var pnv;
    	pnv = 	{ 
	    			"totalCount" 	: totalCount,
	    	        "rowPerPage" 	: rowPerPage,
	    	        "currentPage" 	: currentPage,
	    	        "naviCount" 	: naviCount,
	    	        "plusPage" 		: plusPage ,
	    	        "firstPage" 	: firstPage,
	    	        "lastPage" 		: lastPage,
	    	        "currDataNo" 	: currDataNo
    			};
    	
    	$(this._obj).html( createNavigationBar(pnv) );
    	
    };
    
    PageNavigation.f_GoPage = function(currentPage){
    	$('#currentPage').val(currentPage);
		$('#aform').attr({action : this._goUrl , method : 'post'}).submit();
    };
    
    
   
    return PageNavigation; //리턴을 해야함
    
})(window.PageNavigation || {},jQuery); //객체 없으면 생성
