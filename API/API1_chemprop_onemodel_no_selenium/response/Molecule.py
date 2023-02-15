# 0404 전체

import base64
import datetime
import io
import os

from matplotlib.colors import ColorConverter
# rdkit
from rdkit import Chem
from rdkit.Chem import MolToSmiles, Draw, MolFromSmiles

now = datetime.datetime.now()


# smiles-> mol ->png

def png_mkdir(path):
    if os.path.isdir(path):
        return path
    else:
        os.makedirs(path)
        return path


# def smiles2png(smiles, png_path):
def smiles2png(smiles):
    m = MolFromSmiles(smiles)
    img = Draw.MolToImage(m, highlightAtoms=[1, 2],
                          highlightColor=ColorConverter().to_rgb('aqua'))

    # 삭제 예정
    # # 1. png_path 이미지 세이브 리턴
    # png_mkdir(png_path)  # 0407
    # png_saved_path = os.path.join(png_path, 'molecule_drawing.png')
    # img.save(png_saved_path)

    # 2. txt return
    # 22/4/15장지연 이사님 요청 : 모델에서 그림파일을 로컬에 떨구지 않고, 리턴값에 같이 담아서 전송하는걸 검토했으면 합니다.
    # img를 bytes로 변화
    buf = io.BytesIO()
    img.save(buf, format='png')
    byte_im = buf.getvalue()
    # img를 base64 인코딩하여 텍스트로 변환
    # -> phamai UI에서 텍스트를 이미지로 변환하여 웹에 보여주고 프로젝트 단위로 저장
    img2txt = base64.b64encode(byte_im).decode('utf-8')
    # txt_path = os.path.join(png_path,'img2txt.txt')
    # with open(txt_path,'w') as f:
    #     f.write(img2txt)
    # return img2txt, png_saved_path
    return img2txt