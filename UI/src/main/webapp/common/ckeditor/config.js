/**
 * @license Copyright (c) 2003-2018, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see https://ckeditor.com/legal/ckeditor-oss-license
 */

CKEDITOR.editorConfig = function( config ) {
	config.toolbar = [
		{ name: 'document', items: [ 'Source', '-', 'NewPage', 'Preview', 'Print', '-', 'Templates' ] },
		{ name: 'clipboard', items: [ 'Cut', 'Copy', 'Paste', /*'PasteText', 'PasteFromWord',*/ '-', 'Undo', 'Redo' ] },
		{ name: 'editing', items: [ 'Find', 'Replace', '-', 'SelectAll', '-', 'Scayt' ] },
		{ name: 'forms', items: [ 'Form', 'Checkbox', 'Radio', 'TextField', 'Textarea', 'Select', 'Button', 'ImageButton', 'HiddenField' ] },
		'/',
		{ name: 'basicstyles', items: [ 'Bold', 'Italic', 'Underline', 'Strike', 'Subscript', 'Superscript', '-', 'CopyFormatting', 'RemoveFormat' ] },
		{ name: 'paragraph', items: [ 'NumberedList', 'BulletedList', '-', 'Outdent', 'Indent', '-', 'Blockquote', 'CreateDiv', '-', 'JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock', '-', 'BidiLtr', 'BidiRtl', 'Language' ] },
		{ name: 'links', items: [ 'Link', 'Unlink', 'Anchor' ] },
		// AS-IS
		// { name: 'insert', items: [ 'Ajaximage', 'ImageList', /*'Flash',*/ 'Table', 'HorizontalRule', 'Smiley', 'SpecialChar', 'PageBreak'/*, 'Iframe'*/ ] },
		{ name: 'insert', items: [ 'Ajaximage', 'ImageList', /*'Flash',*/ 'Table', 'HorizontalRule', 'Smiley', 'SpecialChar', 'PageBreak'/*, 'Iframe'*/ ] },
		'/',
		{ name: 'styles', items: [ 'Styles', 'Format', 'Font', 'FontSize' ] },
		{ name: 'colors', items: [ 'TextColor', 'BGColor' ] },
		{ name: 'tools', items: [ 'Maximize', 'ShowBlocks' ] },
		{ name: 'about', items: [ 'About' ] }
	];

    config.extraPlugins = 'ajaximage, imageList,image2, font';

    // 작성자 : 서기원
    // 해당 plugins가 있으면, 이미지 Drag&Drop기능을 사용할때 CKEditor 에서 제공해주는 cloudservice 랑 연동을 해야함
    config.removePlugins = 'easyimage, cloudservices';

    // filebrowserUploadUrl 선언 위치를 config.js 로 옮김 ( 기존에는 editor 적용하는 파일에 적용)
    config.filebrowserUploadUrl = '/CKEditor/multipartHttpPhotoUpload.do?';

    config.extraAllowedContent = 'img[src,alt]'; // setData()에 img 허용
    config.font_defaultLabel = '맑은 고딕'; // 기본 폰트 지정
    config.font_names =  '맑은 고딕; 돋움; 바탕; 돋음; 궁서;'; // 폰트 목록
    config.fontSize_defaultLabel = '12px'; // 기본 폰트 크기 지정

    config.language = "ko"; // 언어타입
    config.resize_enabled = true; // 에디터 크기 조절 사용여부
    config.enterMode = CKEDITOR.ENTER_BR; // 엔터시 <br>
    config.shiftEnterMode = CKEDITOR.ENTER_P; // 쉬프트+엔터시 <p>
    config.toolbarCanCollapse = false; // 툴바 클릭시 접히는 여부
    config.menu_subMenuDelay = 0; // 메뉴 클릭 할 때 딜레이 값
    config.autoParagraph = false;
    config.allowedContent = true; // 태그 자동 삭제 방지 여부
    config.fillEmptyBlocks = false; // 태그 치환 시 빈 값 추가 유무
    config.protectedSource.push(/<\?[\s\S]*?\?>?/g);
    config.height = 400;
    config.allowedContent = true;      //class 적용됨
    CKEDITOR.dtd.$removeEmpty.span = 0;    //span 태그 적용됨

};