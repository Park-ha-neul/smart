# -*- coding: utf-8 -*-
from config import InitSvcConfig, errors

from api_predict.postprocessing import postprocess

from response.response_and_errors import req_res_and_errors, request_error
from ast import literal_eval # 0405_16

import logging

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
    pred_dict = postprocess(smiles, reg, clf)
    value = {'reqid': 'dd', 'type': 'smiles', 'value': 'CCC', 'path': '/data/test/'}
    # 성공할 경우의 response 구성
    code, msg = errors('000')
    # response_func - request, res_dict 한번에 묶어버리기. 분자그림 포함
    # (본디 sdf_path은 고정이나 input type이 sdf_file path면 sdf_path==input_value라  따로 빼기가 애매함)
    request_res_dict_or_error = req_res_and_errors(value, smiles, pred_dict)  # 리스트를 해줄 필요가 없음.
    print('---1----', request_res_dict_or_error)
    rrdict = [(
        {'reqid': 'dd', 'type': 'smiles', 'value': 'CCC', 'path': '/data/test/'},
        {'molecule': {'smiles': 'CCC', 'generated Molecule': '/data/test/202204042244.png'},
         'properties': {'Bioavailability': 0.64, 'pKa': None, 'pKb': 5.67, 'Molecular Weight': 44.1, 'LogP': 1.42,
                        "Lipinski's Rule of five": 'Yes', 'Mass Solubility pH 1': 4.23, 'Mass Solubility pH 2': 2.57,
                        'Mass Solubility pH 3': 2.7, 'Mass Solubility pH 4': 5.58, 'Mass Solubility pH 5': 1.78,
                        'Mass Solubility pH 6': 2.34, 'Mass Solubility pH 7': 4.5, 'Mass Solubility pH 8': 1.6,
                        'Mass Solubility pH 9': 1.47, 'Mass Solubility pH 10': 2.1, 'LogD pH 1': 0.38,
                        'LogD pH 2': 0.81,
                        'LogD pH 3': 1.02, 'LogD pH 4': 0.96, 'LogD pH 5': 0.43, 'LogD pH 6': 0.67, 'LogD pH 7': 1.12,
                        'LogD pH 8': 1.03, 'LogD pH 9': 0.92, 'LogD pH 10': 1.13, 'Water Solubility': 0.15,
                        'Caco-2 permeable': 0.05, 'Melting Point (°C)': 126.55, 'Boiling Point (°C)': 240.65},
         'dosage': {'Molecular Weight': ['Oral', 'Parenteral', 'Local'], 'LogP': ['Oral', 'Parenteral', 'Local'],
                    'LogD pH 7': ['Oral', 'Parenteral', 'Local'], 'Water Solubility': ['Oral', 'Parenteral', 'Local'],
                    'Mass Solubility pH 3': ['Oral', 'Parenteral', 'Local'],
                    'Mass Solubility pH 7': ['Oral', 'Parenteral', 'Local'],
                    'Melting Point (°C)': ['Oral', 'Parenteral', 'Local'],
                    'Caco-2 permeable': ['Oral', 'Parenteral', 'Local'], 0.64: ['Oral']},
         'count': {'Oral': 9, 'Parenteral': 8, 'Local': 8}}
    )
    ]

    if "error" in request_res_dict_or_error:
        # 에러 003 type은 chemical, smiles, sdf중 하나를 입력해주세요.
        request, res_dict, code, msg = errors('003')
    else:
        request = request_res_dict_or_error[0]
        res_dict = request_res_dict_or_error[1]
