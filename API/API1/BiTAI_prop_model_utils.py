'''
Copyright (c) 2020 MediRita, Inc. All right reserved.
'''
from __future__ import division, print_function, unicode_literals

import csv
import time
import warnings

import deepchem as dc
import numpy as np
import pandas as pd
from deepchem.models.graph_models import GraphConvModel
from deepchem.splits.splitters import RandomSplitter, ScaffoldSplitter
from deepchem.utils.save import load_from_disk
from IPython.display import SVG, display
from rdkit import Chem
from rdkit.Chem import Descriptors, Lipinski, rdDepictor
from rdkit.Chem.Draw import IPythonConsole, rdMolDraw2D

warnings.filterwarnings(action='ignore')

import os

# os.environ["CUDA_VISIBLE_DEVICES"] = "0"

import tensorflow as tf
# from keras import backend as k

# config = tf.ConfigProto()

# config.gpu_options.allow_growth = True

# config.gpu_options.per_process_gpu_memory_fraction = 0.2

# k.tensorflow_backend.set_session(tf.Session(config=config))

# kekule form of the SMILES ( Aromatic structures are specified directly in preference to the kekuli form)
def kekule(smiles):
    mol = Chem.MolFromSmiles(smiles)
    Chem.Kekulize(mol)
    k_smiles = Chem.MolToSmiles(mol, isomericSmiles=False, canonical=True, kekuleSmiles=True)
    
    return k_smiles

# https://ang-love-chang.tistory.com/54
# Each non-hydrogen atom is specified independently by its atomic symbol enclosed in square brackets.
# elements in the “organic subset”, B, C, N, 0, P, S, F, C1, Br, and I, may be written without brackets 
# if the number of attached hydrogens conforms to the lowest normal valence(A whole number that represents 
# the ability of an atom or a group of atoms to combine with other atoms or groups of atoms.다른 원자와 결합할 수 있는
# 전체수) consistent with explicit bonds. 
# 수소 원자를 제거하면 주로 처리할 원자가 적기 때문에 작업이 쉬워진다.

def braket_elimination(k_smiles):
    if '[C]' in k_smiles:
        k_smiles = k_smiles.replace('[C]',"C")
    if '[CH]' in k_smiles:
        k_smiles = k_smiles.replace('[CH]',"C")
    if '[CH2]' in k_smiles:
        k_smiles = k_smiles.replace('[CH2]',"C")
    if '[N]' in k_smiles:
        k_smiles = k_smiles.replace('[N]',"N")     
    if '[NH]' in k_smiles:
        k_smiles = k_smiles.replace('[NH]',"N")      
    if '[O]' in k_smiles:
        k_smiles = k_smiles.replace('[O]',"O")     
    if '[OH]' in k_smiles:
        k_smiles = k_smiles.replace('[OH]',"O")     
    if '[S]' in k_smiles:
        k_smiles = k_smiles.replace('[S]',"S")     
    if '[SH]' in k_smiles:
        k_smiles = k_smiles.replace('[SH]',"S")      
    if '[F]' in k_smiles:
        k_smiles = k_smiles.replace('[F]',"F")           
    if '[H]' in k_smiles:
        k_smiles = k_smiles.replace('[H]',"")     
    if '([H])' in k_smiles:
        k_smiles = k_smiles.replace('([H])',"")    
    if '[Br]' in k_smiles:
        k_smiles = k_smiles.replace('[Br]',"S") 
    if '[Cl]' in k_smiles:
        k_smiles = k_smiles.replace('[Cl]',"S")         
        
    return k_smiles  


# 경로 수정 요망


def MS_model_predictor(smiles,model_dir_name,trans_type):
    
    temp_path =os.path.join('C:/data/aip',model_dir_name)
    print(f'the temp_path is : {temp_path}')
    model_MS = GraphConvModel(n_tasks=1, mode='regression', dropout=0.2,model_dir=temp_path)
    model_MS.restore()

    # checkpoint reload from self.model_dir


    k_smiles = kekule(smiles)
    mol = [Chem.MolFromSmiles(k_smiles)]
    
    featurizer = dc.feat.ConvMolFeaturizer()

    x = featurizer.featurize(mol) 
    
    MS_predict = model_MS.predict_on_batch(x)
    

    if trans_type == 'Normal':
        temp_file_name =  temp_path + '/' + 'transformer_val_normal.csv'
        df = pd.read_csv(temp_file_name)
        out_trans = MS_predict[0][0] *df['val'][1] + df['val'][0]
    elif trans_type == 'MinMax':
        temp_file_name =  temp_path + '/' + 'transformer_val_minmax.csv'
        df = pd.read_csv(temp_file_name)
        out_trans = MS_predict[0][0] *(df['val'][1]-df['val'][0]) + df['val'][0]
    else:
        raise ValueError('Transfomer type should be specified.')
    
    return out_trans     

def logD_model_predictor(smiles,model_dir_name,trans_type):
    temp_path = os.path.join('C:/data/aip' , model_dir_name)

    model_logD = GraphConvModel(n_tasks=1, mode='regression', dropout=0.2,model_dir=temp_path)
    model_logD.restore()
    
    mol = [Chem.MolFromSmiles(smiles)]
    featurizer = dc.feat.ConvMolFeaturizer()
    x = featurizer.featurize(mol) 
    
    logD_predict = model_logD.predict_on_batch(x)
    
    if trans_type == 'Normal':
        temp_file_name = temp_path + '/' + 'transformer_val_normal.csv'
        df = pd.read_csv(temp_file_name)
        out_trans = logD_predict[0][0] *df['val'][1] + df['val'][0]
    elif trans_type == 'MinMax':
        temp_file_name = temp_path + '/' + 'transformer_val_minmax.csv'
        df = pd.read_csv(temp_file_name)
        out_trans = logD_predict[0][0] *(df['val'][1]-df['val'][0]) + df['val'][0]
    else:
        raise ValueError('Transfomer type should be specified.')
    
    return out_trans     


def pKa_model_predictor(smiles,model_dir_name,trans_type):
    temp_path = os.path.join('C:/data/aip' , model_dir_name)
    
    model_pKa = GraphConvModel(n_tasks=1, mode='regression', dropout=0.2,model_dir=temp_path)
    model_pKa.restore()    
    
    mol = [Chem.MolFromSmiles(smiles)]
    featurizer = dc.feat.ConvMolFeaturizer()
    x = featurizer.featurize(mol) 
    
    pKa_predict = model_pKa.predict_on_batch(x)
    
    if trans_type == 'Normal':
        temp_file_name = temp_path + '/' + 'transformer_val_normal.csv'
        df = pd.read_csv(temp_file_name)
        out_trans = pKa_predict[0][0] *df['val'][1] + df['val'][0] 
    elif trans_type == 'MinMax':
        temp_file_name = temp_path + '/' + 'transformer_val_minmax.csv'
        df = pd.read_csv(temp_file_name)
        out_trans = pKa_predict[0][0] *(df['val'][1]-df['val'][0]) + df['val'][0]
    else:
        raise ValueError('Transfomer type should be specified.')
    
    return out_trans   


def Caco2_model_predictor(smiles,model_dir_name):
    temp_path = os.path.join('C:/data/aip' , model_dir_name)
   
    model_Caco2 = GraphConvModel(n_tasks=1, mode='classification', dropout=0.2,model_dir=temp_path)
    model_Caco2.restore()
    
    mol = [Chem.MolFromSmiles(smiles)]
    featurizer = dc.feat.ConvMolFeaturizer()
    x = featurizer.featurize(mol) 
    
    Caco2_predict = model_Caco2.predict_on_batch(x)
    print(Caco2_predict)
    
    return Caco2_predict   

def BP_model_predictor(smiles,model_dir_name,trans_type):
    temp_path = os.path.join('C:/data/aip' , model_dir_name)
   
    model_BP = GraphConvModel(n_tasks=1, mode='regression', dropout=0.2,model_dir=temp_path)
    model_BP.restore() 
    
    mol = [Chem.MolFromSmiles(smiles)]
    featurizer = dc.feat.ConvMolFeaturizer()
    x = featurizer.featurize(mol) 
    
    BP_predict = model_BP.predict_on_batch(x)
    
    if trans_type == 'Normal':
        temp_file_name = temp_path + '/' + 'transformer_val_normal.csv'
        df = pd.read_csv(temp_file_name)
        out_trans = BP_predict[0][0] *df['val'][1] + df['val'][0]  
    elif trans_type == 'MinMax':
        temp_file_name = temp_path + '/' + 'transformer_val_minmax.csv'
        df = pd.read_csv(temp_file_name)
        out_trans = BP_predict[0][0] *(df['val'][1]-df['val'][0]) + df['val'][0]
    else:
        raise ValueError('Transfomer type should be specified.')
    
    return out_trans     

def MP_model_predictor(smiles,model_dir_name,trans_type):
    temp_path = os.path.join('C:/data/aip' , model_dir_name)
   
    model_MP = GraphConvModel(n_tasks=1, mode='regression', dropout=0.2,model_dir=temp_path)
    model_MP.restore()
    
    mol = [Chem.MolFromSmiles(smiles)]
    featurizer = dc.feat.ConvMolFeaturizer()
    
    x = featurizer.featurize(mol) 
    
    MP_predict = model_MP.predict_on_batch(x)
    
    
    if trans_type == 'Normal':
        temp_file_name = temp_path + '/' + 'transformer_val_normal.csv'
        df = pd.read_csv(temp_file_name)
        out_trans = MP_predict[0][0] *df['val'][1] + df['val'][0]  
    elif trans_type == 'MinMax':
        temp_file_name = temp_path + '/' + 'transformer_val_minmax.csv'
        df = pd.read_csv(temp_file_name)
        out_trans = MP_predict[0][0] *(df['val'][1]-df['val'][0]) + df['val'][0]
    else:
        raise ValueError('Transfomer type should be specified.')
    
    return out_trans    

def logP_model_predictor(smiles,model_dir_name,trans_type):
    temp_path = os.path.join('C:/data/aip' , model_dir_name)

    model_logP = GraphConvModel(n_tasks=1, mode='regression', dropout=0.2,model_dir=temp_path)
    model_logP.restore()

    mol = [Chem.MolFromSmiles(smiles)]
    featurizer = dc.feat.ConvMolFeaturizer()
    x = featurizer.featurize(mol)

    logP_predict = model_logP.predict_on_batch(x)
    

    if trans_type == 'Normal':
        temp_file_name = temp_path + '/' + 'transformer_val_normal.csv'
        df = pd.read_csv(temp_file_name)
        out_trans = logP_predict[0][0] *df['val'][1] + df['val'][0]  
    elif trans_type == 'MinMax':
        temp_file_name = temp_path + '/' + 'transformer_val_minmax.csv'
        df = pd.read_csv(temp_file_name)
        out_trans = logP_predict[0][0] *(df['val'][1]-df['val'][0]) + df['val'][0] 
    else:
        raise ValueError('Transfomer type should be specified.')

    return out_trans

def BA_model_predictor(smiles,model_dir_name):
    temp_path = os.path.join('C:/data/aip' , model_dir_name)
    
    model_BA = GraphConvModel(n_tasks=1, mode='classification', dropout=0.2,model_dir=temp_path)
    model_BA.restore()

    mol = [Chem.MolFromSmiles(smiles)]
    featurizer = dc.feat.ConvMolFeaturizer()
    x = featurizer.featurize(mol)

    BA_predict = model_BA.predict_on_batch(x)
    print(BA_predict)

    return BA_predict

def DF_model_predictor(smiles,model_dir_name):
    temp_path = os.path.join('C:/data/aip' , model_dir_name)
    
    model_DF = GraphConvModel(n_tasks=1, mode='classification', dropout=0.2,model_dir=temp_path)
    model_DF.restore() 
    
    mol = [Chem.MolFromSmiles(smiles)]
    featurizer = dc.feat.ConvMolFeaturizer()
    x = featurizer.featurize(mol) 
    
    DF_predict = model_DF.predict_on_batch(x)
    print(DF_predict)
    
    return DF_predict
    
def property_mol(smiles):
    
    temp = []
    
    mol = Chem.MolFromSmiles(smiles)
    
    temp.append(Descriptors.MolWt(mol))   
    temp.append(Chem.Crippen.MolLogP(mol))
    temp.append(Lipinski.NumHAcceptors(mol)) 
    temp.append(Lipinski.NumHDonors(mol))    
    temp.append(Lipinski.NumRotatableBonds(mol)) 
    temp.append(Lipinski.NumAromaticRings(mol))  
    temp.append(Lipinski.HeavyAtomCount(mol))   
    temp.append(Descriptors.TPSA(mol, includeSandP=True)) 
    QED_property = Chem.QED.properties(mol)       
    temp.append(Chem.QED.qed(mol, w = QED_property)) 
    if Lipinski.NumHDonors(mol)<=5 and Lipinski.NumHAcceptors(mol)<=10 and Descriptors.MolWt(mol)<500 and Chem.Crippen.MolLogP(mol)<5:
         temp.append('Yes')
    else:
         temp.append('No')
    return temp
