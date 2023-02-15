#!/usr/bin/env python
# coding: utf-8

#-*-coding:utf-8-*-

'''
BiTAI_py36.py
모델 실행

# '''
from __future__ import print_function
from __future__ import division
from __future__ import unicode_literals

import deepchem as dc
import numpy as np
import time
import pandas as pd

from deepchem.models.graph_models import GraphConvModel
from deepchem.utils.save import load_from_disk
from deepchem.splits.splitters import RandomSplitter, ScaffoldSplitter


from rdkit import Chem
from rdkit.Chem import rdDepictor
from rdkit.Chem.Draw import IPythonConsole
from rdkit.Chem.Draw import rdMolDraw2D

import csv
from IPython.display import SVG
from IPython.display import display

from BiTAI_prop_model_utils import kekule,braket_elimination

import warnings
warnings.filterwarnings(action='ignore')

import os
import sys


# os.environ["CUDA_VISIBLE_DEVICES"] = "0"

ROOT_PYTHON_PATH = 'C:/data/aip'+'/mol_info'


# ##  2-2. 모델 실행하기

import tensorflow as tf
# from keras import backend as k
 
# config = tf.ConfigProto()

# config.gpu_options.allow_growth = True
 
# config.gpu_options.per_process_gpu_memory_fraction = 0.2

# k.tensorflow_backend.set_session(tf.Session(config=config))


# 각 모델별로 smiles를 돌리는 함수
def execute(orig_SMILES):

    # input으로 받은 smiles를 mol로 변환
    temp_mol = Chem.MolFromSmiles(orig_SMILES)
    # temp_mol이 비어있지 않는 경우
    if temp_mol != None:
        # 각 모델을 돌리기 위한 input 값 정제 변수 : origin_SMILES_kekule
        orig_SMILES_kekule = kekule(orig_SMILES)
    # temp_mol이 비어있는 경우 error 출력
    elif temp_mol == None:
        print('ERROR:: Invalid SMILES input')

    orig_SMILES_kekule = braket_elimination(orig_SMILES_kekule)

    from BiTAI_prop_model_utils import MS_model_predictor
    start = time.time()
    temp = [0 for j in range(10)]

    # 모델 1(속성 : mass_solubility)
    for i in range(0,10):
        model_dir_name = 'models/Mass_Solubility_pH' + str(i+1)
        out = MS_model_predictor(orig_SMILES_kekule,model_dir_name,'Normal')
        temp[i] = round(out,2)
        print(i)
    end = time.time()
    print("Elapsed time is %s" % (end - start))
    out_1_sol = temp

    # 모델 2 (속성 : logD)
    from BiTAI_prop_model_utils import logD_model_predictor
    start = time.time()
    temp = [0 for j in range(10)]
    for i in range(0,10):
        model_dir_name = 'models/logD_pH' + str(i+1)
        out = logD_model_predictor(orig_SMILES_kekule,model_dir_name,'Normal')

        temp[i] = round(out,2)
        print(i)
    end = time.time()
    print("Elapsed time is %s" % (end - start))
    out_2_logD = temp


    # 모델 3 (속성: pKa)
    from BiTAI_prop_model_utils import pKa_model_predictor

    start = time.time()

    model_dir_name = 'models/pKa' 
    out_3_pKa = pKa_model_predictor(orig_SMILES_kekule,model_dir_name,'Normal')

    model_dir_name = 'models/pKb' 
    out_4_pKb = pKa_model_predictor(orig_SMILES_kekule,model_dir_name,'Normal')

    end = time.time()
    print("Elapsed time is %s" % (end - start))


    # 모델 4 (속성: Caco-2 Permeability)

    from BiTAI_prop_model_utils import Caco2_model_predictor

    start = time.time()

    model_dir_name = 'models/Caco2' 
    out = Caco2_model_predictor(orig_SMILES_kekule,model_dir_name)
    if out[0][0][0] > 0.5:
        out_5_Caco2 = ['Non-permeable with Papp > 8 * 10^-6 (cm/s)',str(out[0][0][0])[0:4]]
    elif out[0][0][0] < 0.5:
        out_5_Caco2 = ['Permeable with Papp > 8 * 10^-6 (cm/s)',str(out[0][0][1])[0:4]]

    end = time.time()
    print("Elapsed time is %s" % (end - start))


    # 모델 5 (속성: Boiling Point)
    from BiTAI_prop_model_utils import BP_model_predictor

    start = time.time()

    model_dir_name = 'models/BP' 
    out_6_BP = BP_model_predictor(orig_SMILES_kekule,model_dir_name,'Normal')

    end = time.time()
    print("Elapsed time is %s" % (end - start))


    # 모델 6 (속성 : logP)
    from BiTAI_prop_model_utils import logP_model_predictor

    model_dir_name = 'models/logP'
    out_7_logP = logP_model_predictor(orig_SMILES_kekule,model_dir_name,'Normal')


    # 모델 7 (속성: Bioavailability)
    from BiTAI_prop_model_utils import BA_model_predictor

    model_dir_name = 'models/BA'
    out =BA_model_predictor(orig_SMILES,model_dir_name)

    if out[0][0][0] > 0.5:
        out_8_BA = ['Orally non-bioavailable', str(out[0][0][0])[0:4]]
    elif out[0][0][0] < 0.5:
        out_8_BA = ['Orally bioavailable', str(out[0][0][1])[0:4]]


    end = time.time()
    print("Elapsed time is %s" % (end - start))


    # 모델 8 (속성 : property_mol- weight, lipinski's rule)
    from BiTAI_prop_model_utils import property_mol

    out_9_mol = property_mol(orig_SMILES_kekule)
    print("out_9 complete !")


    # 모델 9 (속성:Dosage Form)
    from BiTAI_prop_model_utils import DF_model_predictor

    model_dir_name = 'models/DF'
    out =DF_model_predictor(orig_SMILES,model_dir_name)
    if out[0][0][0] > 0.5:
        out_10_DF =['Dosage Form: Oral',str(out[0][0][0])[0:4]]
    elif out[0][0][0] < 0.5:
        out_10_DF = ['Dosage Form: Non-oral',str(out[0][0][1])[0:4]]


    end = time.time()
    print("Elapsed time is %s" % (end - start))

    print("out_1_sol @@@@@@@@@@@@@@@@@@@@@@@@@ " , out_1_sol)



    # 각 모델로 도출한 값을 out_array에 리스트로 묶은 다음에 properties를 딕셔너리로 정리한다.
    out_array = [out_1_sol,out_2_logD,np.round(out_3_pKa,2),np.round(out_4_pKb,2),out_5_Caco2[1],np.round(out_6_BP,2),
            np.round(out_7_logP,2),out_8_BA[1],out_10_DF[1],np.round(out_9_mol[0],2), out_9_mol[9]
            ]
    
    # property를 딕셔너리로 담아줌.
    properties_dict = {"pH Mass Solubility": out_1_sol,
    "pH LogD": out_2_logD,
    "pKa": np.round(out_3_pKa,2),
    "pKb": np.round(out_4_pKb,2),
    "Caco-2 Permeability": out_5_Caco2,
    "Boiling point(°C)": round(out_6_BP,2),
    "LogP": np.round(out_7_logP,2),
    "Bioavailability":out_8_BA,
    "Dosage Form": out_10_DF,
    "Molecular weight(g/mol)": np.round(out_9_mol[0],2),
    "Lipinski's Rule of 5": out_9_mol[9]}





    # 꼭 필요한 파일인가?
    # f = open(ROOT_PYTHON_PATH + '/BiTAI_out.txt' , "w")
    #
    # for i in range(len(out_array)):
    #     data = str(out_array[i]).replace("\r", "")
    #     f.write("%s\n" % data)
    # f.close()

    # PH Mass Solubility 시각화
    # import matplotlib.pyplot as plt
    # plt.switch_backend('agg')
    # fig,ax = plt.subplots()
    #
    # labels = ['pH1','pH2','pH3','pH4','pH5','pH6','pH7','pH8','pH9','pH10']
    # ax.set_xticks([0,1,2,3,4,5,6,7,8,9])
    # ax.set_xticklabels(labels)
    #
    # pH_predicted_val = out_1_sol
    #
    # plt.title('pH Mass_Solubility (g/L) plot')
    # plt.plot(pH_predicted_val,'go-')
    # plt.savefig(ROOT_PYTHON_PATH + "/pH.svg", format="svg")
    #
    # # LogD 시각화
    # import matplotlib.pyplot as plt_1
    # plt_1.switch_backend('agg')
    # fig,ax = plt_1.subplots()
    #
    # labels = ['pH1','pH2','pH3','pH4','pH5','pH6','pH7','pH8','pH9','pH10']
    # ax.set_xticks([0,1,2,3,4,5,6,7,8,9])
    # ax.set_xticklabels(labels)
    #
    # logD_predicted_val = out_2_logD
    #
    # plt_1.title('logD plot')
    # plt_1.plot(logD_predicted_val,'go-')
    # plt_1.savefig(ROOT_PYTHON_PATH + "/logD.svg", format="svg")
    
    return properties_dict, out_array


'''
       "dosage": {
          "Molecular weight": ["oral", "parenteral", "local"],      -> out_array[-2]
          "Log P": ["oral", "parenteral", "local"],                 -> out_array[6]
          "Log D(pH7)": ["parenteral"],                             -> out_array[1][6]
          "pH 3 solubility": ["oral", "parenteral", "local"],       -> out_array[0][2]
          "pH 7 solubility": ["oral", "parenteral", "local"],       -> out_array[0][6]
          "Pearmeability (Caco-2)":["oral", "parenteral", "local"], -> out_array[4]
          "Bioavailability": ["oral"]                               -> out_array[7]
'''

