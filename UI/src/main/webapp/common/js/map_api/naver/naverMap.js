/******************************************************************************************************************

	naver map javaScript API v3	
	작성자 : 서기원
	수정일자 : 2018-07-24

******************************************************************************************************************/
/* naver map */
var NaverMap = function(options, map_options)
{
	this.map = null;
	
	this.obj = 
	{
		icon : null							
		, marker : []						// 마커 리스트
		, info_window : []					// 정보창 리스트
		, circle : null						// 반경
		, my_marker : null					// 내위치 마커
		, my_info_window : null				// 내위치 정보창 리스트
	};
	
	this.options = 
	{
		id : 'map'							// map을 적용할 'id' 
		, myLat : null						// 나의 지점(lat)
		, myLng : null						// 나의 지점(lng)
	};
	
	$.extend(this.options, options);
	
	// default 맵 옵션	
	this.map_options = 
	{
		center : new naver.maps.LatLng(37.3595704, 127.105399)	// 중심 설정
		, size : new naver.maps.Size(500, 500)					// 지도 크기
		, zoom : 10 											// default 줌 단위
		, scrollWheel : true 									// scrollWheel 확대 가능여부
		, draggable : true 										// draggable 가능여부
		, disableDoubleClickZoom : true							// 더블클릭시 지도 확대 여부
		, mapTypeId :  naver.maps.MapTypeId.NORMAL 				// 지도유형
		, zoomControl : true									// 줌 컨트롤의 표시 여부입니다.
		, zoomControlOptions: { 								// 줌 컨트롤의 옵션
			position: naver.maps.Position.TOP_RIGHT 			// 위치
		}
	};
	
	$.extend(this.map_options, map_options);
	
	this.init();
};

NaverMap.prototype = 
{
	/**
	 * init
	 * comment : 맵을 초기화한다.
	 */
	init : function(){ 
		
		this.map = new naver.maps.Map(this.options.id ,this.map_options); // map 객체 생성
		
	}

	/**
	 * initEvent
	 * comment : 이벤트를 추가한다.
	 * @param : method : String (추가할 이벤트 메소드명(ex:click..) )
	 * @param : fun : function (callback 함수)
	 */
	, initEvent : function( method, fun){
		
		// Event 추가 
		naver.maps.Event.addListener(this.map, method , function(oEvent){
			fun.call(this,oEvent);
		} );
		
	}
	
	/**
	 * Marker 설정
	 * comment : 새로운를 설정한다.
	 * @param : _lat : String (위도)
	 * @param : _lng : String (경도)
	 * @param : title : String (해당마커 title(=마우스 오버시 나타나는 툴팁 문자열))
	 * @param : index : Int
	 */
	, setMarker : function(_lat, _lng , title, index)
	{	
		var length = this.obj.marker.length;
		
		// 위치 지정이 있을경우
		if(index != null)
		{
			if(this.obj.marker[index] != null ){
				// 해당 인덱스에 넣을것이기에
				length = index;
				// 먼저 해당 인덱스 마크를 삭제
				this.delMarker(index);
				// 삭제후 해당 위치에 널객체를 생성
				this.obj.marker.splice(index, 0, null);
			}
		}
		
		var coord = new naver.maps.LatLng(_lat, _lng);
		var markerOptions = {
			position : coord
			, map : this.map
			, title : title
		}
		
		var marker = new naver.maps.Marker(markerOptions);
		
		this.obj.marker[length] = marker;
	}
	/**
	 * Marker를 제거
	 * comment : marker obj를 제거 한다. 
	 * @param : num : number ( 지우고싶은 마커의 index )
	 */
	, delMarker : function(num)
	{
		this.obj.marker[num].setMap(null);
	}
	/**
	 * 지도크기 재설정
	 * comment : 지도의 크기를 재설정한다.
	 * @param : width : number (지도의 너비값)
	 * @param : height : number (지도의 높이값)
	 */
	, resizeMap : function(width, height){
		this.map.setSize(new naver.maps.Size(width,height));
	}
	
	/**
	 * 주소를 검색
	 * comment : 주소를 검색한다.
	 * @param : addSearch : String (검색 할 주소)
	 * @param : fun : function (callBack 함수)
	 */
	, addrSearch : function(addr, fun){
		naver.maps.Service.geocode({ address: addr }, function(status, response) {
			if (status === naver.maps.Service.Status.ERROR) {
				fun.call(this,null);
				return false;
			}
			var result = response.result;
			
			if( result == null || result =='') fn.call(this,null); // 검색결과가 없을때
			
			var addrList = result.items;
			var items = new Array();
			
			for( var i = 0 ; i < result.total; i ++){
				
				if(addrList[i].isRoadAddress == true){
					var coord = new naver.maps.LatLng(addrList[i].point.y, addrList[i].point.x);
				
					var tm = naver.maps.TransCoord.fromLatLngToTM128(coord); // 위도/경도 -> TM128 반환
					addrList[i].tm = tm;
					items.push(addrList[i]);
				}
			}
			
			
			fun.call(this, items);
		});
	}
	
	/**
	 * searchFromCoordToAddr
	 * comment : 위/경도를 통해서 주소를 검색한다.
	 * @param : coord : naver.maps.LatLng (위/경도)
	 * @param : fun : function (callBack 함수)
	 */
	, searchFromCoordToAddr : function (coord, fun){
		naver.maps.Service.reverseGeocode({location : coord} , function(status, response){
			if (status === naver.maps.Service.Status.ERROR) {
				return alert('Something Wrong!');
			}
			
			// var items = response.result.items;
			
			var addrList = response.result.items;
			var items = new Array();
			
			for( var i = 0 ; i < response.result.total; i++){
				if(addrList[i].isRoadAddress == true){ // 도로명 주소만 
					items.push(addrList[i]);
				}
			}
			
			fun.call(this,items);
		});
	}
	
	/**
	 * moveMapCenter
	 * comment : 지도의 중심을 바꾼다..
	 * @param : _lat : number (위도)
	 * @param : _lng : number (경도)
	 */
	, moveMapCenter : function(_lat, _lng){
		var coord = new naver.maps.LatLng(_lat, _lng)
		this.map.setCenter(coord);
	}
	
	/**
	 * setInfoWindow
	 * comment : 새로운 정보창을 설정한다.
	 * @param : content : String (info창에 들어갈 content(html형식))
	 * @param : index : number (인텍스)
	 * @param : option : 
	 */
	, setInfoWindow : function(content, index, option){
		
		var length = this.obj.info_window.length;
		
		// 위치 지정이 있을경우
		if(index != null){
			if(this.obj.info_window[index] != null ){
				// 해당 인덱스에 넣을것이기에
				length = index;
				// 삭제후 해당 위치에 널객체를 생성
				this.obj.info_window.splice(index, 0, null);
			}
		}
		
		var info_window = new naver.maps.InfoWindow({
			content: content,							// 정보창의 content (html 형식)
			maxWidth: 400, 								// 정보창의 최대 너비 px
			backgroundColor: "#eee",					// 정보창 배경색
			borderWidth: 1,								// 정보창 테두리 두께
			anchorSize: new naver.maps.Size(30, 30),	// 기본 말풍선 꼬리의 크기
			anchorSkew: true,							// 기본 말풍선 꼬리의 기울임 효과 사용 여부
			anchorColor: "#eee",						// 기본 말풍선 꼬리의 색상
			pixelOffset: new naver.maps.Point(20, -20)	// 정보창의 꼬리에서 정보창이 위치한 지점까지의 오프셋
		});
		
		this.obj.info_window[length] = info_window;
	}
	
	/**
	 * closeInfoWindow
	 * comment : 열려있는 Info창을 닫는다.
	 */
	, closeInfoWindow : function(){
		length = this.obj.info_window.length;
		
		if(length != null){
			for( var i = 0 ; i < length ; i ++){
				this.obj.info_window[i].close();
			}
		}
	}
};