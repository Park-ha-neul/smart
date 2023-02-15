# -*- coding: utf-8 -*-
import os
from typing import List, Dict, Any
import sys


platform_data_dir = '/data/aip/chemprop/'
sys.path.append(platform_data_dir) # 환경변수 등록 0413

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
    classification_cols = ['SMILES', 'Bi Basic', 'Bi Acidic', 'Bioavailability']
    regression_cols = ['SMILES', 'Most Basic', 'Most Acidic'] + \
                      [f'Mass Solubility pH {i}' for i in range(1, 11)] + \
                      [f'LogD pH {i}' for i in range(1, 11)] + \
                      ['Water Solubility', 'Caco-2 permeable', 'Melting Point (°C)', 'Boiling Point (°C)']


def get_paths_dict(tm):
    """
    tm.train_data_path
    :param tm: tm.train_data_path
    :return: dict : tm.train_data_path + classification.csv / tm.train_data_path + regression.csv
    """
    classiciation_data_path = os.path.join(tm.train_data_path, 'classification.csv')
    regression_data_path = os.path.join(tm.train_data_path, 'regression.csv')
    preprocessed_paths_dict: Dict[str, Any] = {'classification': classiciation_data_path,
                                               'regression': regression_data_path}
    return preprocessed_paths_dict


class HyperoptConfig:
    # hyp_path = Path('/work/api_train/hyperoptimization')
    # hyp_path = root_dir -> 알고리즘 경로를 어떻게 잡아야 하는가?
    hyp_path: str = platform_data_dir
    hyp_args = [
        '--dataset_type', 'regression',
        '--show_individual_scores',
        '--extra_metrics', 'r2',  #
        '--gpu', '0',
        '--hyperopt_checkpoint_dir',hyp_path,
        '--log_dir',hyp_path
    ]


# --------------------------------------------------------------------------
def train_args_path(name, data_path, tm):
    args_path_basic = [
        '--data_path', data_path,  # 전처리한 데이터 경로 넣기 (regression / classification)
        '--save_dir', os.path.join(tm.model_path, f'checkpoint/{name}_model'),
        '--epochs', tm.param_info['epoch'],
        '--gpu', '0',
        '--split_sizes', str(tm.param_info['train_size']), str(tm.param_info['validation_size']),
        str(tm.param_info['test_size']),
        '--init_lr', tm.param_info['init_lr'],
        '--max_lr', tm.param_info['max_lr'],
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
        return self.regression_args + train_args_path(name, data_path, tm)

    def get_classification_args(self, name, data_path, tm):
        return self.classification_args + train_args_path(name, data_path, tm)


# ----------------------------------------------------------------------------

class InitSvcConfig:
    dataset = ['classification', 'regression']
    # chemical_csv = '/data/nfs/t3qai/aip/chemprop/chemical.csv'
    chemical_csv = os.path.join(platform_data_dir, 'chemical.csv')


# 이것도 class로 바꿀 수 있을 것 같은데?
def errors(code_num, key=None):
    """
    에러 문구 출력

    :param code_num: code 001~007,999

    :param key: code_num이 001,002 일 때, ['reqid', 'type', 'value', 'path'] 중 하나
    :return:request={}, res_dict={}, code, msg

            {},{}, 001, {reqid/type/value/path}가 누락되었습니다.

            {},{}, 002, {reqid/type/value/path}의 value가 누락되었습니다.

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

    for k in ['reqid', 'type', 'value', 'path']:
        code_errors['001'][k] = f'{k}가 누락되었습니다.'
        code_errors['002'][k] = f'{k}의 value가 누락되었습니다.'

    code_errors['000'] = "success"
    code_errors['003'] = "type은 chemical, smiles, sdf중 하나를 입력해주세요."
    code_errors['004'] = "chemical name을 다시 한번 확인해주세요."
    code_errors['005'] = "smiles string을 다시 한번 확인해주세요."  # train,predict 할 때  Invalid smiles 뜨면 에러창 나오게 하기
    code_errors['006'] = "file형식이 sdf가 아닙니다. 다시 한번 확인해주세요."
    code_errors['007'] = "path 형식의 마지막은 '/'로 끝나야 합니다. 다시 한번 확인해주세요."
    code_errors['999'] = '정의되지 않은 error 입니다.'

    request = "{}"
    res_dict = "{}"
    if code_num in ['001', '002']:
        msg = code_errors[code_num][key]
        return request, res_dict, code_num, msg
    elif code_num in ['999', '000']:
        msg = code_errors[code_num]
        return code_num, msg
    else:
        msg = code_errors[code_num]
        return request, res_dict, code_num, msg


# --------------------------------------------------------------


class InferenceConfig:
    regression_cols = ['Most Basic', 'Most Acidic'] + \
                      [f'Mass Solubility pH {i}' for i in range(1, 11)] + \
                      [f'LogD pH {i}' for i in range(1, 11)] + \
                      ['Water Solubility', 'Caco-2 permeable', 'Melting Point (°C)', 'Boiling Point (°C)']

    classification_cols = ['Bi Basic', 'Bi Acidic', 'Bioavailability']

    class Pka:
        most_acidic = 'Most Acidic'
        most_basic = 'Most Basic'
        bi_acidic = 'Bi Acidic'
        bi_basic = 'Bi Basic'

    class Lipinski:
        mw = 'Molecular Weight'
        logp = 'LogP'
        Ro5 = "Lipinski's Rule of five"

    class Power:
        boiling = 'Boiling Point (°C)'  # 고친 부분
        melting = 'Melting Point (°C)'  # 고친 부분
        water_sol = 'Water Solubility'
        Mass_sol = [f'Mass Solubility pH {i}' for i in range(1, 11)]

    class Dosage:
        oral = ['Oral']
        parenteral = ['Parenteral']
        local = ['Local']

        mw = 'Molecular Weight'
        logP = 'LogP'
        logD = 'LogD pH 7'  # 고친부분
        sol = 'Water Solubility'
        pH3_sol = 'Mass Solubility pH 3'
        pH7_sol = 'Mass Solubility pH 7'
        mp = 'Melting Point (°C)'
        caco2 = 'Caco-2 permeable'
        bio = 'Bioavailability'


'''
class LogConfig:
    """
    log 설정
    """
    # log_path =
'''
