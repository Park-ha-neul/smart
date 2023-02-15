## CKEDITOR 4 기본 정보
version 4.10.0

## CKEDITOR 4 수정내용

작성자 : 서 기 원

수정 내용

1) Image Widget 제거
- Toolbar에서 기존에있던 3가지 이미지 위젯 에서 , "Image"(Default Image Widget) 를 제거하고 AjaxImage, ImageList 만남김


2) 이미지 Drag & drop 기능 추가
- ckeditor/config.js 파일 수정

3) ajaximage/plugin.js 파일 수정
- 객체생성 부분 추가(기존에는 에디터를 적용하는 JSP에 선언함)

4) ckeditor/custom.js 파일 추가
- 용도) 공통 CKEDitor 이벤트 등록하는 용도
   ex) fileUploadResponse
   - 파일 업로드이후 Responce 값을 받아서, 파일 정보를 $("#imageBox"); 태그에 append\
   - CKEditor에서 업로드시에는 tmp 위치에 저장하고, 실제 저장버튼을 누르면 해당 파일을 올바른 위치에 저장

