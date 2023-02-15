CKEDITOR.plugins.add('imageList',
{
    init: function (editor) {
        var pluginName = 'imageList';
        editor.ui.addButton('ImageList',
            {
                label: '이미지 리스트',
                command: 'OpenList',
                icon: CKEDITOR.plugins.getPath('imageList') + 'imageList.gif'
            });
        var cmd = editor.addCommand('OpenList', { exec: showMyList });
    }
});


function showMyList(e) {
    window.open('/admin/cnts/'+fnSysMappingCode()+'selectPageListPhotoMng.do', 'imageList', 'width=500, height=500, left=200, top=200');
}

