import logging
# 0404 전체

# rdkit
from rdkit import Chem
from rdkit.Chem import MolToSmiles, Draw, MolFromSmiles
from matplotlib.colors import ColorConverter
import datetime
import os

now = datetime.datetime.now()

# 엔티시스랑 협의된 분자그림 경로 :  '/home/data/t3q/uploads/pharmAi/{프로젝트_id}/'
def mkdir(path):
    if os.path.isdir(path):
        return path
    else:
        os.makedirs(path)
        return path


# smiles-> mol ->png
def smiles2png(smiles, png_path):  # 이런식으로 바꿔주기.
    """

    :param smiles:
    :param png_path:/path/to/png/path/ <- 마지막에 /를 넣어야 정상적으로 경로가 생성됨.
    :return: /path/to/png/path/YYYYMMDDHHMM.png
    """
    try:
        m = MolFromSmiles(smiles)
        img = Draw.MolToImage(m, highlightAtoms=[1, 2],
                              highlightColor=ColorConverter().to_rgb('aqua'))
        mkdir(png_path)  # 0407
        png_saved_path = os.path.join(png_path, 'molecule_drawing.png')
        img.save(png_saved_path)
        print('success')
        return png_saved_path
    except:
        print('fail')
        return "Fail"


# -------------------------------------------------------------
# input으로 sdf file을 받은 경우 이를 smiles string으로 변환하는 코드
# to_smiles에서 씀. 남기기 0404
def sdf2smiles(sdf_path):
    for mol in Chem.SDMolSupplier(sdf_path):
        print(mol)
    mols = [mol for mol in Chem.SDMolSupplier(sdf_path)]
    mol1 = mols[0]
    sdf2Smiles = MolToSmiles(mol1)
    print(sdf2Smiles)
    return sdf2Smiles


if __name__ == "__main__":
    import os

    # png_path = "C:/Users/coco7/PycharmProjects/pharmai/platform/API_platform_5/test_code/png2"
    # smiles2png('CCC', png_path)
    # # smiles2png2('CCC',png_path)
    path = r'C:\Users\coco7\PycharmProjects\pharmai\platform\API1\API1_chemprop\Caffeine.sdf'
    sdf2smiles(path)