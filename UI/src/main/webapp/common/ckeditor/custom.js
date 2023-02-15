// The DOM element (textarea), its ID, or name.
// textarea, 혹은 div 태그 안에 CKEditor 생성
// ex) <input textarea id='test'name='test'>
//      initCKEditor('test')
function initCKEditor(target){

	var editor = CKEDITOR.replace(target);

	addCKEditorEvent(editor);
}

function addCKEditorEvent(editor){

	/*
	 * fileUpload 이후 발생하는 이벤트
	 * Editor 에 있는 사진정보를 ImageBox 태그에 입력
	 * ImageBox에 있는 태그들을 이용해서, 이미지 파일 경로 수정
	 */
	editor.on( 'fileUploadResponse', function( evt ) {
		// Get XHR and response.
		var data = evt.data,
				xhr = data.fileLoader.xhr,
				response = xhr.responseText.split( '|' );

		if ( response[ 1 ] ) {

			evt.cancel();
		} else {
			var responseJSON = JSON.parse(response[0]);

			var fileImgUrl = responseJSON.url;

			var $imgBoxAdd = "";
			var $openerData = $('#imageBox');

			$imgBoxAdd = $('<input type="hidden" name="imgPath" class="imgPath" value="'+ fileImgUrl +'" />');
			$openerData.append($imgBoxAdd);
		}
	});

}
