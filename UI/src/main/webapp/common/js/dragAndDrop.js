$(document).ready(function() {
	$("#input_file").bind('change', function() {
		selectFile(this.files);
	});
});

// 파일 리스트 번호
var fileIndex = 0;
// 등록할 전체 파일 사이즈
var totalFileSize = 0;
// 파일 리스트
var fileList = new Array();
// 파일 사이즈 리스트
var fileSizeList = new Array();
// 등록 가능한 파일 사이즈 MB
var uploadSize = 500;
// 등록 가능한 총 파일 사이즈 MB
var maxUploadSize = 500;
//정렬순서 기본입력 변수
var lastSortOrdr = 0;
//다중 드롭다운 파일 리스트 번호
var fileIndexMulti = [0, 0];
//다중 드롭다운 파일 리스트
var fileListMulti = [new Array(), new Array()];
//다중 드롭다운 파일 사이즈 리스트
var fileSizeListMulti = [new Array(), new Array()];

$(function() {
	$("#upload_w, #upload_m").change(function(){
		var files = this.files;

		if($(this).attr("id")=="upload_w"){
			selectFileMulti(files, 1);	//웹 이미지 첨부
		}else{
			selectFileMulti(files, 2);	//모바일 이미지 첨부
		}
		$(this).val("");
	});

	// 파일 드롭 다운
	fileDropDown();
	// 웹 파일 드롭 다운
	fileMultiDropDown($("#dropZone_w"),1);
	// 모바일 파일 드롭 다운
	fileMultiDropDown($("#dropZone_m"),2);
});

// 파일 드롭 다운
function fileDropDown() {
	var dropZone = $("#dropZone");
	//Drag기능
	dropZone.on('dragenter', function(e) {
		e.stopPropagation();
		e.preventDefault();
		// 드롭다운 영역 css
		dropZone.css('background-color', '#E3F2FC');
	});
	dropZone.on('dragleave', function(e) {
		e.stopPropagation();
		e.preventDefault();
		// 드롭다운 영역 css
		dropZone.css('background-color', '#FFFFFF');
	});
	dropZone.on('dragover', function(e) {
		e.stopPropagation();
		e.preventDefault();
		// 드롭다운 영역 css
		dropZone.css('background-color', '#E3F2FC');
	});
	dropZone.on('drop', function(e) {
		e.preventDefault();
		// 드롭다운 영역 css
		dropZone.css('background-color', '#FFFFFF');

		var files = e.originalEvent.dataTransfer.files;
		if (files != null) {
			if (files.length < 1) {
				alert("폴더 업로드는 불가능합니다.");
				return;
			} else {
				selectFile(files)
			}
		} else {
			alert("파일 업로드 에러가 발생하였습니다.");
		}
	});
}


//파일 다중 드롭 다운
function fileMultiDropDown(dropZone, uploadZoneNo) {
	//Drag기능
	dropZone.on('dragenter', function(e) {
		e.stopPropagation();
		e.preventDefault();
		// 드롭다운 영역 css
		dropZone.css('background-color', '#E3F2FC');
	});
	dropZone.on('dragleave', function(e) {
		e.stopPropagation();
		e.preventDefault();
		// 드롭다운 영역 css
		dropZone.css('background-color', '#FFFFFF');
	});
	dropZone.on('dragover', function(e) {
		e.stopPropagation();
		e.preventDefault();
		// 드롭다운 영역 css
		dropZone.css('background-color', '#E3F2FC');
	});
	dropZone.on('drop', function(e) {
		e.preventDefault();
		// 드롭다운 영역 css
		dropZone.css('background-color', '#FFFFFF');

		var files = e.originalEvent.dataTransfer.files;
		if (files != null) {
			if (files.length < 1) {
				alert("폴더 업로드는 불가능합니다.");
				return;
			} else {
				selectFileMulti(files, uploadZoneNo);
			}
		} else {
			alert("파일 업로드 에러가 발생하였습니다.");
		}
	});
}


// 파일 선택시
function selectFile(fileObject) {
	var files = null;

	var type = $("#fileType").val();

	//$("#fileDragDesc").hide();
	$("fileListTable").show();

	if(fileObject != null){
		// 파일 Drag 이용하여 등록시
		files = fileObject;
		}else{
			// 직접 파일 등록시
			files = $('#multipaartFileList_' + fileIndex)[0].files;
		}

	// 다중파일 등록
	if(files != null){
		for(var i = 0; i < files.length; i++){
			// 파일 이름
			var fileName = files[i].name;
			var fileNameArr = fileName.split("\.");
			// 확장자
			var ext = fileNameArr[fileNameArr.length - 1];
			// 파일 사이즈(단위 :MB)
			var fileSize = files[i].size / 1024 / 1024;

			//메뉴 타입 별 확장자 검사
			if('img' == type){
				if($.inArray(ext.toLowerCase(), ['jpg','jpeg','gif','png']) < 0){
					// 확장자 체크
					alert("jpg, jpeg, gif, png 이미지 확장자만 업로드 가능합니다.");
					break;
				}
			}

			if(fileSize > uploadSize){
				// 파일 사이즈 체크
				alert("용량 초과\n업로드 가능 용량 : " + uploadSize + " MB");
				break;
			}else{
				// 전체 파일 사이즈
				totalFileSize += fileSize;

				// 파일 배열에 넣기
				fileList[fileIndex] = files[i];

				// 파일 사이즈 배열에 넣기
				fileSizeList[fileIndex] = fileSize;

				// 업로드 파일 목록 생성
				addFileList(fileIndex, fileName, fileSize);

				// 파일 번호 증가
				fileIndex++;
			}
		}

		//첨부파일 목록 박스 크기 늘리기
//		if(fileIndex>2){
//			temp_height = 110+55*(fileIndex-2);
//			$('.dropZone11').css('height', temp_height );
//		}

	} else {
		alert("업로드가 불가능한 파일입니다.");
	}

	// 용량을 500MB를 넘을 경우 업로드 불가
	if (totalFileSize > maxUploadSize) {
		// 파일 사이즈 초과 경고창
		alert("총 용량 초과\n총 업로드 가능 용량 : " + maxUploadSize + " MB");
		return;
	}

	// 등록할 파일 리스트
	var uploadFileList = Object.keys(fileList);
	// 파일이 있는지 체크
	if (uploadFileList.length == 0) {
		// 파일등록 경고창
		alert("파일이 없습니다.");
		$("#attach_file").show();
		return;
	}
}

//다중 드롭다운에서 파일 선택시
function selectFileMulti(files, uploadZoneNo){
	var k = Number(uploadZoneNo)-1;
	setLastSortOrdr(uploadZoneNo);

	var type = $("#fileType").val();

	// 다중파일 등록
	if(files != null){
		for(var i = 0; i < files.length; i++){
			// 파일 이름
			var fileName = files[i].name;
			var fileNameArr = fileName.split("\.");
			// 확장자
			var ext = fileNameArr[fileNameArr.length - 1];
			// 파일 사이즈(단위 :MB)
			var fileSize = files[i].size / 1024 / 1024;

			if("img" == type){
				if($.inArray(ext.toLowerCase(), ['jpg','jpeg','gif','png']) < 0){
					// 확장자 체크
					alert("jpg, jpeg, gif, png 이미지 확장자만 업로드 가능합니다.");
					break;
				}
			}

			if(fileSize > uploadSize){
				// 파일 사이즈 체크
				alert("용량 초과\n업로드 가능 용량 : " + uploadSize + " MB");
				break;
			}else{
				// 전체 파일 사이즈
				totalFileSize += fileSize;
				// 파일 배열에 넣기
				fileListMulti[k][fileIndexMulti[k]] = files[i];
				// 파일 사이즈 배열에 넣기
				fileSizeListMulti[k][fileIndexMulti[k]] = fileSize;
				// 업로드 파일 목록 생성
				addFileList(fileIndexMulti[k], fileName, fileSize, uploadZoneNo);
				// 파일 번호 증가
				fileIndexMulti[k]++;
			}
		}
	}else{
		alert("ERROR");
	}
}

//각 메뉴 별 업로드 파일 목록 타입이 달라 메뉴별 페이지에서 처리
// 업로드 파일 목록 생성
/*function addFileList(fIndex, fileName, fileSizeStr) {

	var html = "";
	html += "<tr id='fileTr_" + fIndex + "'>";
	html += "    <td id='dropZone' class='left' >";
	html += fileName + " (" + fileSizeStr +") "
			+ "<input value='삭제' type='button' href='#' onclick='deleteFile(" + fIndex + "); return false;'>"
	html += "    </td>"
	html += "</tr>"

	$('#fileTableTbody').append(html);
}*/

// 업로드 파일 삭제
function deleteFile(fIndex) {
	// 전체 파일 사이즈 수정
	totalFileSize -= fileSizeList[fIndex];

	// 파일 배열에서 삭제
	delete fileList[fIndex];

	// 파일 사이즈 배열 삭제
	delete fileSizeList[fIndex];

	// 업로드 파일 테이블 목록에서 삭제
	$("#fileTr_" + fIndex).remove();

	if (totalFileSize > 0) {
//		$("#fileDragDesc").hide();
		$("fileListTable").show();
	} else {
		$("#fileDragDesc").show();
		$("fileListTable").hide();
	}
}

//다중 드롭다운 업로드 파일 삭제
function deleteFileMulti(fIndex, uploadZoneNo){
	var k = Number(uploadZoneNo)-1;

	// 전체 파일 사이즈 수정
	totalFileSize -= fileSizeListMulti[k][fIndex];
	// 파일 배열에서 삭제
	delete fileListMulti[k][fIndex];
	// 파일 사이즈 배열 삭제
	delete fileSizeListMulti[k][fIndex];

	// 업로드 파일 테이블 목록에서 삭제
	$("#fileTr_" + uploadZoneNo + "_" + fIndex).remove();
	setRowNo(uploadZoneNo);
}

//정렬순서 max값
function setLastSortOrdr(uploadZoneNo){
	lastSortOrdr = 0;
	$('[name=sort_ordr_'+uploadZoneNo+'], #fileTableTbody_'+uploadZoneNo+' [name=sort_ordr_update]').each(function(){
		lastSortOrdr = lastSortOrdr > Number($(this).val()) ? lastSortOrdr : Number($(this).val());
	});
	lastSortOrdr = Math.ceil((lastSortOrdr+10) / 10) * 10;
}

// 업로드 파일 목록 row number
function setRowNo(uploadZoneNo){
	$('#fileTableTbody_'+uploadZoneNo+' .row-no').each(function(index){
		$(this).html(index+1);
	});
}

//서버에 있는 파일 삭제
function fnFileDel(obj, file_id){
	if(confirm("삭제하시겠습니까?")){
		var param = { 'file_id' : file_id };

		jQuery.ajax( {
			type : 'POST',
			dataType : 'json',
			url : '/common/file/deleteFileInfAjax.do',
			data : param,
			success : function(param) {

				if(param.resultStats.resultCode == 'error'){
					alert(param.resultStats.resultMsg);
					return;
				}else{
					alert(param.resultStats.resultMsg);
					// 첨부파일 삭제
					$(obj).parent().remove();

					// 첨부파일 select 박스 다시 그림
					var n = $('.attach_file').length;
					fnComboStrFile('.fileBoxWrap', n, 5);
				}
			},
			error : function(jqXHR, textStatus, thrownError){
				ajaxJsonErrorAlert(jqXHR, textStatus, thrownError)
			}
		});
	}
}

