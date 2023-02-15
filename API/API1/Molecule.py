import tensorflow as tf
import logging
import pandas as pd
import json
import csv
import datetime
from collections import OrderedDict
import os

##rdkit
from rdkit.Chem import AllChem
from rdkit import Chem
from rdkit.Chem.Draw import IPythonConsole
from rdkit.Chem import Draw
from rdkit.Chem import AllChem, DataStructs, MolToSmiles, Draw
from rdkit.Chem import PandasTools


import io
IPythonConsole.ipython_useSVG=True
import ClassifiType as ct
import datetime
import requests

# tf.logging.set_verbosity(tf.logging.INFO)

now = datetime.datetime.now()

## input으로 받은 smiles string을 분자그림으로 변환하기 위해서는 아래와 같은 작업들이 필요로 함
# step1. smiles string -> sdf file
# step2. sdf file -> rdkit모듈을 이용하여 png 파일 생성(분자그림)
# 이렇게 작업해야 하는 이유 : rdkit 모듈에서 분자그림을 생성해줄때는 sdf file을 가지고 그림을 그려주기 때문에 위와 같은 작업을 필요로함

# input으로 받은 smiles string을 sdf 형식으로 변환
def SmilesToSdf(smiles, sdf_path):
    data_frame = pd.DataFrame(columns=['Smiles','BA'])
    data_frame = data_frame.append({'Smiles':smiles,'BA':None}, ignore_index=True)
    PandasTools.AddMoleculeColumnToFrame(data_frame, 'Smiles', 'Molecule')
    PandasTools.WriteSDF(data_frame, sdf_path+now.strftime("%Y%m%d%H%M")+'.sdf', molColName='Molecule', properties=list(data_frame))
    sdf_return = sdf_path+now.strftime("%Y%m%d%H%M")+'.sdf'
    print('@@@@@@@@@@@@@@@@@@@@@ sdf_return : ', sdf_return)

    return sdf_return


# sdf file 형식으로 변환한 값을 가지고 png(분자그림) 생성
def SdfToPng(smiles, png_path, sdf_path):
    try:
        get_sdf = SmilesToSdf(smiles, sdf_path)
        with Chem.SDMolSupplier(get_sdf) as suppl:
            ms = [x for x in suppl if x is not None]
        Draw.MolToFile(ms[0], png_path+now.strftime("%Y%m%d%H%M")+ '.png')
        if os.path.isfile(get_sdf):
            os.remove(SmilesToSdf(smiles, sdf_path))
        else:
            pass
        return (png_path+now.strftime("%Y%m%d%H%M")+ '.png')
    except :
        result = "Fail"

        return result

## input type이 sdf인 경우에 분자그림 생성 순서
# step1. sdf file -> smiles string (이 부분은 output으로 smiles string을 추출해야하기 때문에 필요함)
# sdf file을 받은 경우는 이를 다시 sdf로 변환하는 과정이 필요하지 않음
# 그렇기 때문에 input으로 받은 sdf로 분자그림을 바로 생성해주는 코드

# input type = "sdf"인 경우
def SdfToPng2(png_path, sdf_path):
    try:
        with Chem.SDMolSupplier(sdf_path) as suppl:
            ms = [x for x in suppl if x is not None]
            for m in ms:
                tmp = AllChem.Compute2DCoords(m)
        Draw.MolToFile(ms[0], png_path+now.strftime("%Y%m%d%H%M")+ '.png')
        return (png_path+now.strftime("%Y%m%d%H%M")+ '.png')
    except ParseError :
        result = "Fail"

        return result

# input으로 sdf file을 받은 경우 이를 smiles string으로 변환하는 코드
def SdfToSmiles(sdf_path):
    mols = [mol for mol in Chem.SDMolSupplier(sdf_path)]
    mol1 = mols[0]
    sdf2Smiles = MolToSmiles(mol1)
    return sdf2Smiles
