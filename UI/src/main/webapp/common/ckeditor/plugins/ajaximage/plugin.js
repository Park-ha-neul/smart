// 객체 생성
var ckEditorAjaxImage = {};
// ckeditor textarea id
// 한 번에 업로드할 수 있는 이미지 최대 수
ckEditorAjaxImage["imgMaxN"] = 10;
// 허용할 이미지 하나의 최대 크기(MB)
ckEditorAjaxImage["imgMaxSize"] = 10;

CKEDITOR.plugins.add('ajaximage',
{
	init: function (editor) {

		ckEditorAjaxImage["id"] = editor.name;
		var pluginName = 'ajaximage';
		editor.ui.addButton('Ajaximage',
			{
				label: '이미지 업로드',
				command: 'OpenWindow',
				icon: CKEDITOR.plugins.getPath('ajaximage') + 'ajaximage.gif'
			});
		var cmd = editor.addCommand('OpenWindow', { exec: showMyDialog });
	}
});

function showMyDialog(e) {
	$('#img_file').trigger("click");
}

$("#img_file").change(function(){
	var dot_pos;
	var ext;
	var allowed_ext = ['jpg','jpeg','png','gif'];

	if(this.files.length > ckEditorAjaxImage["imgMaxN"]){
		this.value = "";
		alert("이미지는 한번에 최대 " + ckEditorAjaxImage["imgMaxN"] + "개까지 업로드할 수 있습니다.");
		return;
	}

	for(var i=0; i < this.files.length ; i++){
		dot_pos = this.files[i].name.lastIndexOf(".");
		ext = this.files[i].name.substr(dot_pos+1,3).toLowerCase();
		if(allowed_ext.indexOf(ext) == -1){
			this.value = "";
			alert("허용되지 않는 확장자입니다.");
			return;
		}
	}

	for(var i=0; i < this.files.length ; i++){
		if(this.files[i].size > ckEditorAjaxImage["imgMaxSize"]*1024*1024){
			this.value = "";
			alert("이미지 하나의 최대 크기는 " + ckEditorAjaxImage["imgMaxSize"] + "MB입니다.");
		}
	}


	if(this.files.length >= 1){

		var $imgForm = $('#img_upload_form');
		var $imgPath = $('#img_upload_form').children()


		var rqst = new XMLHttpRequest();
		rqst.open('POST', '/CKEditor/multipartHttpPhotoUpload.do', true);

		var formData = new FormData();

		formData.append('content-type','multipart/form-data');
		formData.append('upload',this.files);

		$(this.files).each(function(index, file) {

			formData.append("upload", file);

		   });

		rqst.send(formData);

		rqst.onreadystatechange = function(e) {
			if (rqst.readyState === XMLHttpRequest.DONE) {
				// status는 response 상태 코드를 반환 : 200 => 정상 응답
				if(rqst.status === 200) {
					if(rqst.response.indexOf('ErrorExtFile') > -1){
						alert("이미지 파일(jpg,gif,png,bmp)만 업로드 하실 수 있습니다.");
					}else if(rqst.response.indexOf('ErrorSizeFile') > -1){
						alert("파일의 용량이 너무 큽니다. ");
					}else{
						//성공 시에  responseText를 가지고 array로 만드는 부분.
						makeArrayFromString(rqst.responseText);
					}
				} else {
					console.log('Error!');
				}
			}
		};


		//$('#img_upload_form').submit();
		$('#img_file').val('');
	}
});

function makeArrayFromString(sResString){
	var	aTemp = [],
		aSubTemp = [],
		htTemp = {},
		aResult = [],
		aResultleng = 0;


	var imgData = "";
	var $imgBoxAdd = "";
	var $openerData = $('#imageBox');


		try{
			if(!sResString || sResString.indexOf("sFileURL") < 0){
				return ;
			}
			aTemp = sResString.split("&");
		for (var i = 1; i < aTemp.length; i){
			for(var j = 0; j < 2; j++){
				if( !!aTemp[i] && aTemp[i] != "" && aTemp[i].indexOf("=") > 0){
					aSubTemp = aTemp[i].split("=");
					htTemp[aSubTemp[0]] = aSubTemp[1];
				}
				i++;
			}

			$imgBoxAdd = $('<input type="hidden" name="imgPath" class="imgPath" value="'
					+ htTemp.sFileURL +'" />');

			$openerData.append($imgBoxAdd);

			imgData += "<figure class='image'>"
					+  "<img class='v_img' style='max-width:100%' src='"+ htTemp.sFileURL + "' alt='" + htTemp.sFileName +"' >"
					+  "<figcaption>사진설명</figcaption>"
					+  "</figure>";
		}


		var originData = CKEDITOR.instances[ckEditorAjaxImage.id].getData();
		CKEDITOR.instances[ckEditorAjaxImage.id].setData(originData + imgData);

		}catch(e){}
}
