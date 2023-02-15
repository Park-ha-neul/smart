# 후처리
# expotential, clf x reg, multiclass, 캘빈에서 섭씨.

from rdkit import Chem
from rdkit.Chem import Descriptors, Lipinski, Crippen
from rdkit.Chem.rdMolDescriptors import CalcTPSA
from collections import Counter
import numpy as np

from config import InferenceConfig


# 예측 후 후처리 ---------------------------------


# pka 조합하기 : 값 존재 유무 classification, regression
def pka_pkb(predicted_regression_dict):
    ac = predicted_regression_dict[InferenceConfig.Pka.most_acidic]
    bs = predicted_regression_dict[InferenceConfig.Pka.most_basic]
    bi_ac = predicted_regression_dict[InferenceConfig.Classification.bi_ac]
    bi_bs = predicted_regression_dict[InferenceConfig.Classification.bi_bs]
    pka = ac if bi_ac > 0.5 else 'Null'
    pkb = bs if bi_bs > 0.5 else 'Null'  # 0으로 해야하는 것인가? 하지만 0 자체도 value가 되는것 아닌가?
    return pka, pkb


# 값이 1 이상이거나 음수로 나올 경우 0~1사이로 범위 맞춰주기
def caco2_clip(caco2):
    caco2_binary = (lambda x: 0 if x < 0 else (1 if x > 1 else x))(caco2)
    return caco2_binary


# Lipinski's Rule of Five
def lipinski(smiles):
    """
    :param smiles: smiles string
    :return: 'MW', 'LogP','Lipinski's Rule of Five'을 포함한 dict
    """
    temp = {}
    mol = Chem.MolFromSmiles(smiles)
    temp[InferenceConfig.Lipinski.mw] = round(Descriptors.MolWt(mol), 2)
    temp[InferenceConfig.Lipinski.logp] = Chem.Crippen.MolLogP(mol)
    temp[InferenceConfig.Lipinski.Ro5] = 'Yes' if Lipinski.NumHDonors(mol) <= 5 and Lipinski.NumHAcceptors(mol) <= 10 \
                                                  and Descriptors.MolWt(mol) < 500 and Chem.Crippen.MolLogP(mol) < 5 \
                                                  and Lipinski.NumRotatableBonds(mol) <= 10 and CalcTPSA(mol) < 140 \
        else 'No'

    return temp


def power_and_round(res):
    """
    # Log 전처리한 물성을 후처리로 power 하기

    # drugbank : boiling point, melting point, water solubility

    # scifinder : Mass solubility

    :param res: res dict
    :return: boiling point, melting point, water solubility, mass solubility를 power 한 값
    """
    bp = InferenceConfig.Power.boiling
    mp = InferenceConfig.Power.melting
    ws = InferenceConfig.Power.water_sol
    ms = InferenceConfig.Power.Mass_sol
    # ---고친부분----
    for k in [bp, mp, ws]:  # 고친부분
        res[k] = np.power(10, res[k])
    # np.power(list,3) => list 의 요소들이 다 3승되어 나옴.
    for k in ms:
        res[k] = np.power(10, res[k])

    # 결과값 두자리수 반올림

    res1 = {}
    for k, v in res.items():
        print(k, type(v))
        if type(v) != str:
            res1[k] = np.round(v, 2)
        else:
            res1[k] = v

    return res1


def ph1_10_list(res):
    """ 0512
    :param res: logD pH 1~10 , Mass Solubility pH 1~10 까지 값들
    {
        "LogD pH 1": -1.29,
        "LogD pH 10": -1.09,
        "LogD pH 2": -1.03,
        "LogD pH 3": -0.56,
        "LogD pH 4": -0.44,
        "LogD pH 5": -0.21,
        "LogD pH 6": -0.2,
        "LogD pH 7": -0.28,
        "LogD pH 8": -0.47,
        "LogD pH 9": -0.69,
        "LogP": -1.03,
        "Mass Solubility pH 1": 67.86,
        "Mass Solubility pH 10": 17.66,
        "Mass Solubility pH 2": 26.21,
        "Mass Solubility pH 3": 11.25,
        "Mass Solubility pH 4": 6.85,
        "Mass Solubility pH 5": 5.76,
        "Mass Solubility pH 6": 6.37,
        "Mass Solubility pH 7": 4.97,
        "Mass Solubility pH 8": 5.17,
        "Mass Solubility pH 9": 11.23,
        }

    :return: "pH LogD": [
            1.33,
            6.4,
            0.25,
            5.61,
            3.68,
            -0.05,
            0.62,
            -0.43,
            3.94,
            1.1
        ],
        "pH Mass Solubility": [
            660.93,
            -478.99,
            759.51,
            136.61,
            676.03,
            -530.34,
            284.31,
            -374.75,
            842.99,
            1077.39
            ]
    """
    logd_keys = [f'LogD pH {i}' for i in range(1, 11)]
    ms_keys = [f'Mass Solubility pH {i}' for i in range(1, 11)]
    res["pH LogD"] = [res[i] for i in logd_keys]
    res["pH Mass Solubility"] = [res[i] for i in ms_keys]

    # 실질값 리스트로 담아주고 기존 값 삭제하기.
    for keys in [logd_keys, ms_keys]:
        for key in keys:
            res.pop(key)
    return res
# Dosage Form



def dosage(res):
    """
    MW, LogP, LogD(pH7), Solubility,pH3 solubility, pH7 solubility, melting point, permeability,Bioavailability

    9개 물성으로 각각 제형설계를 함

    :param res: res dict
    :return: dosage_form : based by the properties
    """
    dosage_form = {}
    oral = InferenceConfig.Dosage.oral
    parenteral = InferenceConfig.Dosage.parenteral
    local = InferenceConfig.Dosage.local
    oral_paren_local = oral + parenteral + local

    # molecular weight
    mw_key = InferenceConfig.Dosage.mw
    mw = res[mw_key]
    dosage_form[mw_key] = oral_paren_local if mw < 500 else parenteral + local

    # Log P
    logp_key = InferenceConfig.Dosage.logP
    logP = res[logp_key]
    dosage_form[logp_key] = parenteral if logP < 1 \
        else oral_paren_local if 1 < logP < 5 else local

    # Log D(PH7)
    logd_key = InferenceConfig.Dosage.logD
    logD = res[logd_key]
    dosage_ph7_logd_key = InferenceConfig.Dosage.change_keys[logd_key]
    dosage_form[dosage_ph7_logd_key] = parenteral if logD < 1 \
        else oral_paren_local if 1 < logD < 5 else local

    # solubility (mg을 기준으로 한다?)
    sol_key = InferenceConfig.Dosage.sol
    sol = res[sol_key]
    dosage_form[sol_key] = oral + local if sol < 0.1 \
        else oral_paren_local if 0.1 < sol < 100 else parenteral

    # pH3 Solubility - mg/ml단위가 맞는것인가?
    ph3_sol_key = InferenceConfig.Dosage.pH3_sol
    pH3_sol = res[ph3_sol_key]
    # pH3 Solubility <- Mass Solubility pH 3
    dosage_ph3_sol_key = InferenceConfig.Dosage.change_keys[ph3_sol_key]
    dosage_form[dosage_ph3_sol_key] = oral + local if pH3_sol < 0.1 \
        else oral_paren_local if 0.1 < pH3_sol < 100 else parenteral

    # pH7 solubility
    ph7_sol_key = InferenceConfig.Dosage.pH7_sol
    pH7_sol = res[ph7_sol_key]
    # pH7 Solubility <- Mass Solubility pH 7
    dosage_ph7_sol_key = InferenceConfig.Dosage.change_keys[ph7_sol_key]
    dosage_form[dosage_ph7_sol_key] = oral + local if pH7_sol < 0.1 \
        else oral_paren_local if 0.1 < pH7_sol < 100 else parenteral

    # melting point
    melting_key = InferenceConfig.Dosage.mp
    mp = res[melting_key]
    dosage_form[melting_key] = parenteral + local if mp < 30 else oral_paren_local

    # permeability (CaCo-2) # dosage from selection rule에선 pear- typo 있음
    caco2_key = InferenceConfig.Dosage.caco2
    caco2 = res[caco2_key]
    # Permeability (Caco-2) <- Caco-2 permeable
    dosage_caco2_key = InferenceConfig.Dosage.change_keys[caco2_key]
    dosage_form[dosage_caco2_key] = parenteral if caco2 < 0 else oral_paren_local

    # Bioavailability
    bio_key = InferenceConfig.Dosage.bio
    bio = res[bio_key]  # 밑에 bio_key 0405_21
    dosage_form[bio_key] = parenteral + local if bio < 0.1 \
        else oral_paren_local if 0.1 < bio < 0.6 else oral

    return dosage_form
    # before dosage form


# Dosage 갯수 세기
def count_dsg(dsg):
    """
    :param dsg: dosage 함수를 거쳐서 나온 dosage_form dictionary 
    :return: 각 물성에 따른 dosage form을 Local, oral, parenteral 별로 갯수를 세어서 반환한다.
    """
    # res 값을 dosage 함수에 넣어 나온 dosage 딕셔너리를 가져온다.
    # dosage 딕셔너리의 value만 뽑아내어
    v = list(dsg.values())
    # 2차원의 리스트를 1차원으로 변환한다.
    v = sum(v, [])
    # Counter 함수로 각 요소별로 갯수를 세어 딕셔너리로 담는다.
    cnt = dict(Counter(v))
    return cnt


def postprocess(smiles, predicted_regression_dict):
    # Bioavailability
    bio_key = InferenceConfig.Classification.bio
    bio = predicted_regression_dict[bio_key]

    # pka, pkb
    pka, pkb = pka_pkb(predicted_regression_dict)

    # caco2-permeability
    caco2_key = InferenceConfig.Dosage.caco2
    caco2 = predicted_regression_dict[caco2_key]
    caco2_bin = caco2_clip(caco2)

    res_clf = {
        bio_key: (lambda x: 1 if x > 0.5 else 0)(bio),
        # pka, pkb로 naming하는게 맞는건지?
        InferenceConfig.Pka.pka: pka,
        InferenceConfig.Pka.pkb: pkb,
        caco2_key: caco2_bin
        # 'Bi Acidic': bi_ac, # 이건 그냥 빼는걸로. 원래 없던거니깐.
        # 'Bi Basic': bi_bs
    }

    # 필요 없는 값도 삭제
    # 밑에서 딕셔너리 합쳐지면서 미리 추가한 key가 덮어씌워지는 현상 때문에 예측값의 딕셔너리에서 해당 key 없애줌 0517
    [predicted_regression_dict.pop(i) for i in
     ['Most Acidic', 'Most Basic', 'Bi Acidic', 'Bi Basic',bio_key,caco2_key]]

    # rule-based calculation
    lipin = lipinski(smiles)

    # res 정리 - dictionary 다 합치기,
    res = {**res_clf, **lipin, **predicted_regression_dict}

    # log10 -> power
    # 원래값 100 -> log 10해서 2 -> power 10의 2승 100
    # boiling point, melting point, water solubility, mass solubility
    # 모든 결과값 두자리수로 반올림
    res_power = power_and_round(res)

    # dosage form 계산
    dsg = dosage(res_power)
    cnt = count_dsg(dsg)
    # pH mass solubility, pH log D -> array 로 변환 0512
    ph1_10_list(res_power)

    # properties[dosage form] -> null 0512 삭제 예정
    # res_power['Dosage Form'] = ['', '']


    # key 이름 바꾸기 - Ro5, MW, caco2
    changed_dict = InferenceConfig.PropUiOutput.changed
    for old, new in changed_dict.items():
        res_power[new] = res_power.pop(old)
    """
    # 설명 붙이기 - UI 작업후 삭제 예정
    # bio_ui(0.8) -> Orally bioavailable
    bio_x = res_power['Bioavailability']
    bio_ui = lambda x: ['Orally non-bioavailable', f'{x}'] if x < 0.5 else ['Orally bioavailable', f'{x}']
    res_power['Bioavailability'] = bio_ui(bio_x)
    
    # caco2_ui(0.8) -> permeable with papp~~
    caco2_x = res_power[InferenceConfig.PropUiOutput.new[2]]
    caco2_ui = lambda x: ["Non-permeable with Papp > 8 * 10^-6 (cm/s)", f'{x}'] if x < 0.5 else \
        ["Permeable with Papp > 8 * 10^-6 (cm/s)", f'{x}']
    res_power[InferenceConfig.PropUiOutput.new[2]] = caco2_ui(caco2_x)
    """


    pred_dict = {'properties': res_power, 'dosage': dsg, 'count': cnt}

    return pred_dict


if __name__ == "__main__":
    res = {'Most Acidic': 1.0,
           'Most Basic': 1.0,
           "Bi Acidic": 0.95,
           "Bi Basic": 1.08,
           "Bioavailability": 0.53,
           "Boiling Point (\u00b0C)": 270.73,
           "Caco-2 permeable": -0.02,
           "Lipinski's Rule of five": "Yes",
           "LogD pH 1": -1.29,
           "LogD pH 10": -1.09,
           "LogD pH 2": -1.03,
           "LogD pH 3": -0.56,
           "LogD pH 4": -0.44,
           "LogD pH 5": -0.21,
           "LogD pH 6": -0.2,
           "LogD pH 7": -0.28,
           "LogD pH 8": -0.47,
           "LogD pH 9": -0.69,
           "LogP": -1.03,
           "Mass Solubility pH 1": 67.86,
           "Mass Solubility pH 10": 17.66,
           "Mass Solubility pH 2": 26.21,
           "Mass Solubility pH 3": 11.25,
           "Mass Solubility pH 4": 6.85,
           "Mass Solubility pH 5": 5.76,
           "Mass Solubility pH 6": 6.37,
           "Mass Solubility pH 7": 4.97,
           "Mass Solubility pH 8": 5.17,
           "Mass Solubility pH 9": 11.23,
           "Melting Point (\u00b0C)": 148.58,
           "Molecular Weight": 194.19,
           "Water Solubility": 14.13,
           "pKa": 1.0,
           "pKb": 1.0
           }

    print(postprocess('CCC(=O)OC1(C(C)CC2C1(C)CC(O)C1(C2CCC2=CC(=O)C=CC12C)F)C(=O)CCl', res))

    output = {'res':
                  {'Bioavailability': 0.53, 'pKa': 1.0, 'pKb': 1.0, 'Bi Acidic': 0.95, 'Bi Basic': 1.08,
                   'Molecular Weight': 194.19, 'LogP': -1.03, "Lipinski's Rule of five": 'Yes',
                   'Boiling Point (°C)': 5.370317963702752e+270, 'Caco-2 permeable': -0.02,
                   'Melting Point (°C)': 3.8018939632057213e+148, 'Water Solubility': 134896288259165.6,
                   'pH LogD': [-1.29, -1.03, -0.56, -0.44, -0.21, -0.2, -0.28, -0.47, -0.69, -1.09],
                   'pH Mass Solubility': [7.244359600749891e+67, 1.621810097358933e+26, 177827941003.89, 7079457.84,
                                          575439.94, 2344228.82, 93325.43, 147910.84, 169824365246.17,
                                          4.570881896148752e+17],
                   'Dosage Form': ['', '']},
              'dsg':
                  {'Molecular Weight': ['Oral', 'Parenteral', 'Local'],
                   'LogP': ['Parenteral'], 'LogD pH 7': ['Parenteral'],
                   'Water Solubility': ['Parenteral'],
                   'Melting Point (°C)': ['Oral', 'Parenteral', 'Local'],
                   'pH3 Solubility': ['Parenteral'], 'pH7 Solubility': ['Parenteral'],
                   'Permeability (Caco-2)': ['Parenteral'],
                   'Bioavailability': ['Oral', 'Parenteral', 'Local']},
              'cnt': {'Oral': 3, 'Parenteral': 9, 'Local': 3}
              }
