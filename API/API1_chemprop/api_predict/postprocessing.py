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
'''def pka(regression_dict, classification_dict):
    ac = regression_dict[InferenceConfig.Pka.most_acidic]
    bs = regression_dict[InferenceConfig.Pka.most_basic]
    bi_ac = classification_dict[InferenceConfig.Pka.bi_acidic]
    bi_bs = classification_dict[InferenceConfig.Pka.bi_basic]
    ac = ac if bi_ac > 0.5 else None
    bs = bs if bi_bs > 0.5 else None  # 0으로 해야하는 것인가? 하지만 0 자체도 value가 되는것 아닌가?
    return ac, bs

'''
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
                                                  and Lipinski.NumRotatableBonds(mol) <= 10 and CalcTPSA(
        mol) < 140 else 'No'

    return temp


def power_10(res):
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
    # ---고친부분----

    return res


# 결과값 두자리수 반올림
def round_2(res):
    """
    :param res: res_dict
    :return: 모든 결과값 두자리수 반올림
    """
    res1 = {}
    for k, v in res.items():
        print(k, type(v))
        try:
            res1[k] = np.round(v, 2)
        except:
            res1[k] = v

    return res1


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
    dosage_form[logd_key] = parenteral if logD < 1 \
        else oral_paren_local if 1 < logD < 5 else local

    # solubility (mg을 기준으로 한다?)
    sol_key = InferenceConfig.Dosage.sol
    sol = res[sol_key]
    dosage_form[sol_key] = oral + local if sol < 0.1 \
        else oral_paren_local if 0.1 < sol < 100 else parenteral

    # pH3 Solubility - mg/ml단위가 맞는것인가?
    ph3_sol_key = InferenceConfig.Dosage.pH3_sol
    pH3_sol = res[ph3_sol_key]
    dosage_form[ph3_sol_key] = oral + local if pH3_sol < 0.1 \
        else oral_paren_local if 0.1 < pH3_sol < 100 else parenteral

    # pH7 solubility
    ph7_sol_key = InferenceConfig.Dosage.pH7_sol
    pH7_sol = res[ph7_sol_key]
    dosage_form[ph7_sol_key] = oral + local if pH7_sol < 0.1 \
        else oral_paren_local if 0.1 < pH7_sol < 100 else parenteral

    # melting point
    melting_key = InferenceConfig.Dosage.mp
    mp = res[melting_key]
    dosage_form[melting_key] = parenteral + local if mp < 30 else oral_paren_local

    # permeability (CaCo-2) # dosage from selection rule에선 pear- typo 있음
    caco2_key = InferenceConfig.Dosage.caco2
    caco2 = res[caco2_key]
    dosage_form[caco2_key] = parenteral if caco2 < 0 else oral_paren_local
    # 보통 1.1 이런 식으로 나오는데 저 형식은 잘못된 것 같다? - 데이터 제대로 모르는 듯하다.

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


# 설명 붙이기, 앞글자 대문자로 붙이기 -> 설명은 어떻게  UI에 보여주지?
def caco2_ba_capital(res1):
    # caco2, BA -> classification & 설명 붙여주기
    caco2 = res1['Caco-2 permeable']
    res1[
        'Caco-2 permeable'] = f'Non-permeable with Papp < 8 * 10^-6 (cm/s) for {-caco2 * 100} % of possibility' if caco2 < 0 \
        else f'Permeable with Papp > 8 * 10^-6 (cm/s) for {caco2 * 100} % of possibility'
    # 나중에 데이터 전처리 - % 변환 : +,-를 + 로 통일해서 => -확률 = +(1-(-확률))확률로 변환. 나중에 바꿔주기!

    ba = res1['Bioavailability']
    res1['Bioavailability'] = 'Orally Bioavailable, 1' if ba > 0.5 else 'Orally Non-Bioavailable, 0'

    return res1


def postprocess(smiles, predicted_regression_dict, predicted_classification_dict):
    ac = predicted_regression_dict[InferenceConfig.Pka.most_acidic]
    bs = predicted_regression_dict[InferenceConfig.Pka.most_basic]
    bi_ac = predicted_classification_dict[InferenceConfig.Pka.bi_acidic]
    bi_bs = predicted_classification_dict[InferenceConfig.Pka.bi_basic]
    res = {
        'Bioavailability': predicted_classification_dict['Bioavailability'],
        'pKa': ac,
        'pKb': bs,
        'Bi Acidic': bi_ac,
        'Bi Basic': bi_bs
    }
    # pka 조합하기 - classification, regression

    # rule-based calculation
    lipin = lipinski(smiles)

    # res 정리 - dictionary 다 합치고 나서, smiles,pka most basic, pka most acidic 제거
    res = {**res, **lipin, **predicted_regression_dict}
    for i in ['Most Basic', 'Most Acidic']:
        res.pop(i)

    # log10 -> power
    # 원래값 100 -> log 10해서 2 -> power 10의 2승 100
    # boiling point, melting point, water solubility, mass solubility
    print(res)
    res_power = power_10(res)

    # 모든 결과값 두자리수로 반올림
    res_round = round_2(res_power)

    # dosage form 계산
    dsg = dosage(res_round)
    cnt = count_dsg(dsg)

    # caco2, BA -> 설명 붙여주기
    # res_final = post.caco2_ba_capital(res_round)
    pred_dict = {'res': res_round, 'dsg': dsg, 'cnt': cnt}
    return pred_dict


if __name__ == '__main__':
    smiles = 'CCC'
    reg = {"Most Basic": 5.67154777736599, 'Most Acidic': 8.8848746419794,
           'Mass Solubility pH 1': 0.6260808540057923, 'Mass Solubility pH 2': 0.4095796640854183,
           'Mass Solubility pH 3': 0.43080572751148455, 'Mass Solubility pH 4': 0.7468751757532257,
           'Mass Solubility pH 5': 0.2508542515741932, 'Mass Solubility pH 6': 0.36903002463409806,
           'Mass Solubility pH 7': 0.6534240530169197, 'Mass Solubility pH 8': 0.2047036739463794,
           'Mass Solubility pH 9': 0.1681891054538692, 'Mass Solubility pH 10': 0.3213087564468919,
           'LogD pH 1': 0.3783463789347325, 'LogD pH 2': 0.8066004863781275, 'LogD pH 3': 1.022036269695358,
           'LogD pH 4': 0.9611075795448467, 'LogD pH 5': 0.4349099126147646, 'LogD pH 6': 0.6654507543890517,
           'LogD pH 7': 1.1154719579355807, 'LogD pH 8': 1.033208661983504, 'LogD pH 9': 0.9227778645706668,
           'LogD pH 10': 1.1334634055366426, 'Water Solubility': -0.8301367839853816,
           'Caco-2 permeable': 0.05447964746004248, 'Melting Point (°C)': 2.1022501493886807,
           'Boiling Point (°C)': 2.381390836897904}

    clf = {'Bi Basic': 0.5087283849716187, 'Bi Acidic': 0.4857167601585388, 'Bioavailability': 0.6394670009613037}
    posted = postprocess(smiles, reg, clf)
    print(posted)
