# -*- coding: utf-8 -*-

import os
from typing import List
import sys
import pandas as pd

platform_data_dir = '/data/aip/chemprop/'
sys.path.append(platform_data_dir)  # 환경변수 등록 0413


# -1. preprocessing-------------------------------------------------------------------------
class PreConfig:
    # ---data가 하나일 때----------------------------------------------------
    # data_path = None
    pkab = ['Most Basic', 'Most Acidic']
    ms = [f'Mass Solubility pH {i}' for i in range(1, 11)]
    ws = 'Water Solubility'
    mp = 'Melting Point (°C)'
    bp = 'Boiling Point (°C)'

    # preparing for preprocessing
    # data_path_for_process = data_path / 'for_process.csv'

    # after preprocessing - dataset split
    # for_process = 'for_process.csv'
    # classification_cols = ['SMILES', 'Bi Basic', 'Bi Acidic', 'Bioavailability']
    classification_cols = ['Bi Basic', 'Bi Acidic', 'Bioavailability']
    regression_cols = ['SMILES', 'Most Basic', 'Most Acidic'] + \
                      [f'Mass Solubility pH {i}' for i in range(1, 11)] + \
                      [f'LogD pH {i}' for i in range(1, 11)] + \
                      ['Water Solubility', 'Caco-2 permeable', 'Melting Point (°C)', 'Boiling Point (°C)']


def get_pre_paths_dict(tm):
    """
    tm.train_data_path
    :param tm: tm.train_data_path
    :return: dict : tm.train_data_path + classification.csv / tm.train_data_path + regression.csv
    """
    reg_data_save_path = os.path.join(tm.train_data_path, 'regression.csv')
    clf_data_save_path = os.path.join(tm.train_data_path, 'test_regression.csv')
    preprocessed_paths_dict = {'regression': reg_data_save_path, 'test_classification': clf_data_save_path}
    return preprocessed_paths_dict


# -2. train-------------------------------------------------------------------------


class HyperoptConfig:
    # hyp_path = Path('/work/api_train/hyperoptimization')
    # hyp_path = root_dir -> 알고리즘 경로를 어떻게 잡아야 하는가?
    hyp_path: str = platform_data_dir
    hyp_args = [
        '--dataset_type', 'regression',
        '--show_individual_scores',
        '--extra_metrics', 'r2',  #
        '--gpu', '0',
        '--hyperopt_checkpoint_dir', hyp_path,
        '--log_dir', hyp_path
    ]


def train_args_tm(name, data_path, tm):
    args_path_basic = [
        '--data_path', data_path,  # 전처리한 데이터 경로 넣기 (regression / classification)
        '--save_dir', os.path.join(tm.model_path, f'checkpoint/{name}_model'),  # logs
        '--epochs', tm.param_info['epoch'],
        '--gpu', '0',
        '--split_sizes', str(tm.param_info['train_size']), str(tm.param_info['validation_size']),
        str(tm.param_info['test_size']),
        '--init_lr', tm.param_info['init_lr'],  # 0.0001
        '--max_lr', tm.param_info['max_lr'],  # 0.001
        '--warmup_epochs', tm.param_info['warmup_epochs'],  # 2.0
        # Number of epochs during which learning rate increases linearly from init_lr to max_lr.
        # Afterwards, learning rate decreases exponentially from max_lr to final_lr.
        '--dropout', tm.param_info['dropout_ratio'],
    ]
    return args_path_basic


class TrainConfig:
    def __init__(self):
        self.regression_args: List[str] = [
            '--dataset_type', 'regression',
            '--extra_metrics', 'r2',
            '--show_individual_scores',
        ]
        self.classification_args = [
            '--dataset_type', 'classification',
            '--show_individual_scores',
        ]

    def get_regression_args(self, name, data_path, tm):
        return self.regression_args + train_args_tm(name, data_path, tm)

    def get_classification_args(self, name, data_path, tm):
        return self.classification_args + train_args_tm(name, data_path, tm)
    # -2. train-------------------------------------------------------------------------


def train_score(tm, name):
    save_dir = os.path.join(tm.model_path, f'checkpoint/{name}_model/test_scores.csv')
    score_df = pd.read_csv(save_dir, index_col='Task', engine='python')
    score_df_valid = score_df[['Mean rmse', 'Mean r2']]
    return score_df_valid


# -3. init---------------------------------------------------------------------------
"""
csv를 db로 대체함에 따라 사용 중지
class InitSvcConfig:
    # chemical_csv = '/data/aip/chemprop/chemical.csv'
    chemical_csv = os.path.join(platform_data_dir, 'chemical.csv')
"""

# -4. predict -------------------------------------------------------------


def errors(code_num, key=None):
    """
    에러 문구 출력

    :param code_num: code 001~007,999

    :param key: code_num이 001,002 일 때, ['reqid', 'type', 'value'] 중 하나
    :return:request={}, res_dict={}, code, msg
            {'request':request, 'result':res_dict, 'code':code_num, 'msg':msg}
            {request},{result},000,success
            {},{}, 001, {reqid/type/value}가 누락되었습니다.

            {},{}, 002, {reqid/type/value}의 value가 누락되었습니다.

            {},{}, 003, type은 chemical, smiles, sdf중 하나를 입력해주세요.

            {},{}, 004, chemical name을 다시 한번 확인해주세요.

            {},{}, 005, smiles string을 다시 한번 확인해주세요.

            {},{}, 006, file형식이 sdf가 아닙니다. 다시 한번 확인해주세요.

            {},{}, 007, path 형식의 마지막은 '/'로 끝나야 합니다. 다시 한번 확인해주세요.

    """
    code_errors = {}
    # {'code':'msg'} 나머지 request, result는 {}
    for i in range(1, 8):
        code_errors[f'00{i}'] = {}
    code_errors['999'] = {}

    for k in ['reqid', 'type', 'value', 'path']:  # 'path' 삭제 예정
        code_errors['001'][k] = f'{k}가 누락되었습니다.'
        code_errors['002'][k] = f'{k}의 value가 누락되었습니다.'

    code_errors['000'] = "success"
    code_errors['003'] = "type은 chemical, smiles, sdf중 하나를 입력해주세요."
    code_errors['004'] = "chemical name을 다시 한번 확인해주세요."

    code_errors['005'] = "smiles string을 다시 한번 확인해주세요."
    # 1. chemical.csv 파일에 있는 11만개의 smiles string에 존재하지 않을 때
    # 2. pubchem에서 smiles를 찾을 수 없을 때
    # 3. syntax error 일 때
    # 4. prediction module 에서 invalid smiles 뜰 때

    code_errors['006'] = "file형식이 sdf가 아닙니다. 다시 한번 확인해주세요."
    code_errors['007'] = "path 형식의 마지막은 '/'로 끝나야 합니다. 다시 한번 확인해주세요."  # 삭제 예정
    code_errors['999'] = '정의되지 않은 error 입니다.'

    request = "{}"
    res_dict = "{}"
    if code_num in ['001', '002']:
        msg = code_errors[code_num][key]
        return {'request': request, 'result': res_dict, 'code': code_num, 'msg': msg}
    elif code_num in ['999', '000']:
        msg = code_errors[code_num]
        return {'code': code_num, 'msg': msg}
    else:
        msg = code_errors[code_num]
        return {'request': request, 'result': res_dict, 'code': code_num, 'msg': msg}


def get_pred_reg_arg(model_path):
    regression_arguments = [
        '--test_path', '/dev/null',
        '--preds_path', '/dev/null',
        '--gpu', '0',
        '--checkpoint_dir', os.path.join(model_path, f'checkpoint/regression_model'),
        '--num_workers', '0'  # 추론이 되었다 안되었다 하는 현상 방지 테스트 0516
    ]
    return regression_arguments


class InferenceConfig:
    regression_cols = ['Most Basic', 'Most Acidic'] + \
                      [f'Mass Solubility pH {i}' for i in range(1, 11)] + \
                      [f'LogD pH {i}' for i in range(1, 11)] + \
                      ['Water Solubility', 'Caco-2 permeable', 'Melting Point (°C)', 'Boiling Point (°C)'] + \
                      ['Bi Basic', 'Bi Acidic', 'Bioavailability']

    classification_cols = ['Bi Basic', 'Bi Acidic', 'Bioavailability']

    class Classification:
        bi_ac = 'Bi Acidic'
        bi_bs = 'Bi Basic'
        bio = 'Bioavailability'

    class Pka:
        most_acidic = 'Most Acidic'
        most_basic = 'Most Basic'
        pka = 'pka'
        pkb = 'pkb'

    class Lipinski:
        mw = 'Molecular Weight'
        logp = 'LogP'
        Ro5 = "Lipinski's Rule of five"

    class Power:
        boiling = 'Boiling Point (°C)'  # 고친 부분
        melting = 'Melting Point (°C)'  # 고친 부분
        water_sol = 'Water Solubility'
        Mass_sol = [f'Mass Solubility pH {i}' for i in range(1, 11)]

    class PropUiOutput:
        new = ["Lipinski's Rule of 5", "Molecular weight(g/mol)", "Caco-2 permeability"]
        old = ["Lipinski's Rule of five", "Molecular Weight", "Caco-2 permeable"]
        changed = dict(zip(old, new))

    class Dosage:
        oral = ['Oral']
        parenteral = ['Parenteral']
        local = ['Local']

        mw = 'Molecular Weight'
        logP = 'LogP'
        logD = 'LogD pH 7'
        sol = 'Water Solubility'
        pH3_sol = 'Mass Solubility pH 3'
        pH7_sol = 'Mass Solubility pH 7'
        mp = 'Melting Point (°C)'
        caco2 = 'Caco-2 permeable'
        bio = 'Bioavailability'

        # 이름 변경
        # pH3 Solubility <- Mass Solubility pH 3
        # pH7 Solubility <- Mass Solubility pH 7
        # Permeability (Caco-2) <- Caco-2 permeable
        new_key = ['pH3 Solubility', 'pH7 Solubility', 'Permeability (Caco-2)', 'Log D (pH7)']
        old_key = [pH3_sol, pH7_sol, caco2, logD]
        change_keys = dict(zip(old_key, new_key))
