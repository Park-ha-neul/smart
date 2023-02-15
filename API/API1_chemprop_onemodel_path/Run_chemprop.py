# -*- coding: utf-8 -*-
import datetime
import csv
from typing import Dict, Any

import torch.cuda
import torch

from api_train.classification_test import clf_test
# from config import InitSvcConfig, errors, train_score
from config import errors, train_score

from api_train.preprocessing import preprocess
from api_train.chemprop_train import regression_train
from api_train.hyperoptimization import hyperopt_reg as hpot

from api_predict import input2smiles
from api_predict.chemprop_predict import PredictArgsLoadModel, predict
from api_predict.postprocessing import postprocess

# from ast import literal_eval  # 0405_16

import logging
import time
from response.Molecule import smiles2png  # 0404

# today = datetime.date.today() data = { 'date': today.strftime('%Y-%m-%d')}

logging.getLogger('PngImagePlugin').setLevel(logging.ERROR)


def set_logger():
    cp_logger = logging.getLogger()  # platform log에서도 찍힌다.
    # cp_logger = logging.getLogger('cp_log')
    cp_logger.setLevel(logging.INFO)
    stream_formatter = logging.Formatter('<%(name)s>[%(levelname)s][%(filename)s:%(lineno)s][%(asctime)s] %(message)s')
    stream_handler = logging.StreamHandler()
    stream_handler.setFormatter(stream_formatter)
    cp_logger.addHandler(stream_handler)
    cp_logger.propagate = False
    return cp_logger


log = set_logger()

# now = datetime.date.today()
now = datetime.datetime.now()


# logging.info('now : %s', now)


def train(tm):
    # preprocessing
    # 전처리는 transform()에서 구현하지 않고 여기에서 구현하겠음.
    # {name:data_path} dictionary
    data_paths = preprocess(tm)

    # hyperoptimization 적용 유무는 regression에만 적용.
    # model 생성, 학습, 저장 => tensorflow 문법 그대로 안따라가도 되고 모듈에 맞춰서 arguments를 넣어주면 알아서 model생성, 학습, 저장까지 한번에 가능함.
    # tm.model_path: 학습 수행후 학습모델을 저장하는 경로
    for name, data_path in data_paths.items():
        if name in 'regression':
            if tm.param_info['hyperopt'] == 'True':  # 플랫폼 상에서 True를 적어주면 str으로 True를 받는다. 0413
                log.info(f"---- Hyperopt Processing Now --------")
                hpo_param_path = hpot.hyp(name, data_path)  # return f'./hyperopt_{name}.json'
                regression_train(name, data_path, tm, hpo_param_path)
                score_df_valid = train_score(tm, name)  # platform log에서 성능 보이게 설정.
                log.info(f'Score info : \n {score_df_valid.to_string()}')  # 0419 df가 아니라 to_string해서 보여줘야 로깅으로 보임.

            else:  # hyperoptimization은 regression 일 때만 진행
                log.info(f'----Hyperopt Not Processed ------')
                regression_train(name, data_path, tm, None)
        else:
            pass
            # 'classification'
            clf_test(data_path, tm)


def init_svc(im):
    # 1. params 구성하기
    # 1) 모델 로드하기.
    start = time.time()
    model_param = {'args_model': PredictArgsLoadModel(im)}  # 0405 im
    # 2) 플랫폼에서 지정한 파라미터 딕셔너리 가져오기
    param = im.param_info

    # 3) 모두 합쳐버리기.
    params = {**param, **model_param}

    """
    # 4) chemical name csv call - 삭제 예정
    # 11만개의 {chemical name : smiles} 를 mapping 한 파일 열기
    with open(InitSvcConfig.chemical_csv, mode='r') as inp:
        reader = csv.reader(inp)
        dict_from_csv: dict = {rows[1]: rows[2] for rows in reader}
    params['chemical_name_smiles_dict'] = dict_from_csv
    """
    end = time.time()

    log.warning(f'------ Model Loading Time : {end - start} SECONDS ------')
    # 0413 # 0422 init_svc에서는 logging.warning 레벨 이상만 표시됨.

    return params


# ------아래는 고친 버전---이동훈선임님----0504----------------------------------------------------


def inference(df, params, batch_id):
    """

     :param df: df.values[0,0] -> request :  reqid,type,value,path

         reqid : transaction id / api가 잘 전송됐는 지 확인

         type: input의 type : chemical, sdf file path, smiles 셋 중 하나

         value : input의 실제값 : Caffeine, path/to/sdf/file, ['ccc'] 등 셋 중 하나 \
                 **주의 : smiles는 리스트안에 str 형태로 있어야만 한다. str으로만 있으면 분절해서 예측한다. \
                         ex) 'CCC' -> 'c','c','c' 각각 하나씩 예측함

         path : 분자 그림을 저장할 디렉토리

     :param params: 플랫폼 파라미터,
                    args_model : 추론시 필요한 model, args 포함.
     :param batch_id: 없으면 플랫폼 오류나서 임의로 배치함

     :return: request, res_dict, code, msg

         request : reqid, type, value, path

         res_dict : 물성 예측값

         code: 000

         msg : success

     """

    log.info(f'----------INPUT DF : {df} -----------')  # 0405
    start = time.time()
    response = {}

    """ 
    request_str = df.values[0, 0]
    # request = value 어차피 key,value도 같은데 같은 걸로 취급함.
    # -> 변수명 value 를 request로 변경함. 0510
    
    
    request = literal_eval(request_str)  # 0405_16
    # 현재 입력값 방식 : [["{'reqid' : 'dd','type':'chemical', 'value':'CLOBETASOL PROPIONATE', 'path':'/home/data/t3q/uploads/pharmAi/'}"]]
    # 기존 입력값 방식: [[{'reqid': 'dd', 'type': 'chemical', 'value': 'CLOBETASOL PROPIONATE', 'path': '/home/data/t3q/uploads/pharmAi/'}]]
    # 입력값을 기존 방식(2차원 배열안의 딕셔너리)으로 받기 위해서 코드 변환함 0511
    
    # [[{'type':'smiles','value':'CCC(=O)OC1(C(CC2C1(CC(C3(C2CCC4=CC(=O)C=CC43C)F)O)C)C)C(=O)CCl'}]]
    # [[{'type':'sdf','value':'sdf binary code'}]] UI에선 sdf-> smiles python으로 구현할 수 없다? 
    input 값 변화: UI에서 input값 smiles로 변화
    """

    request = df.values[0, 0]

    # smiles 로 변환 - UI로 이관 - 삭제 예정
    result = input2smiles.to_smiles2(request)
    if type(result) != str:  # smiles는 str, errors code는 dict
        # 에러일 때 : 004 chemical name,
        #           006 sdf file 형식,
        #           003 type 입력 에러
        error_code = result
        return error_code
    else:
        smiles = result
        try:
            # 물성예측
            predicted_regression_dict = predict([[smiles]], params, log)
            # predict 할 때 smiles를 2차원 배열로 담아줘야 한다. 0412
            torch.cuda.empty_cache()  # GPU 점유 캐시 비우기 / multiprocessing error 해결용?
        except SyntaxError:
            # invalid smiles -> results : [None], cols_value_dict에서 에러남.
            log.debug('Invalid SMILES. Please input SMILES correctly.')
            return errors('005')

        # 후처리
        pred_dict = postprocess(smiles, predicted_regression_dict)  #

        # 성공할 경우의 response 구성
        code, msg = errors('000').values()
        # response_func - request, res_dict 한번에 묶어버리기. 분자그림 포함
        # (본디 sdf_path은 고정이나 input type이 sdf_file path면 sdf_path==input_value라  따로 빼기가 애매함)

        try:
            if request['type'] in ['smiles', 'sdf']:
                img2txt, png_saved_path = smiles2png(smiles, request['path'])

                # # 2번 모델 path 삭제 예정
                # mol_img_dict = {'molecule': {
                #     'smiles': smiles,
                #     'generated Molecule': png_saved_path}
                # }
                # 3번 모델 txt + path
                mol_img_dict = {'molecule': {
                    'smiles': smiles,
                    'molecule image to text': img2txt,
                    'generated Molecule': png_saved_path}
                }

                # 분자그림과 물성예측 결과 딕셔너리를 하나로 합쳐버림.
                res_dict: Dict[Any, Any] = {**mol_img_dict, **pred_dict}

                response = {"request": request, "result": res_dict, "code": code, "msg": msg}

                end = time.time()
                log.info(f'PREDICTION TIME : {end - start} SEC')
                log.info(f'RESPONSE : {response}')
                # # 2번 모델 path 삭제 예정
                # mol_img = response['result']['molecule']['generated Molecule']
                # log.info(f'MOL IMG (path) :  {mol_img}')

                # 3번 모델 path + txt
                mol_img = response['result']['molecule']['generated Molecule']
                log.info(f'MOL IMG (txt) :  {mol_img}')
                return response

        except SyntaxError:
            return errors('003')  # type 셋 중에 하나는 입력하시오

    # ----------------------------


"""
    try:
        response = {"request": request, "result": res_dict, "code": code, "msg": msg}

        end = time.time()
        log.info(f'PREDICTION TIME : {end - start} SEC')
        log.info(f'RESPONSE : {response}')
        # # 2번 모델 path
        # mol_img = response['result']['molecule']['generated Molecule']
        # log.info(f'MOL IMG (path) :  {mol_img}')
    
        # 3번 모델 path + txt
        mol_img = response['result']['molecule']['molecule image to text']
        log.info(f'MOL IMG (txt) :  {mol_img}')
        return response
    


    
    except:  # UI으로 이관하기.
        if request is not None:
            # input error 처리
            for key in ['reqid', 'type', 'value', 'path']:  # 'path' 삭제 예정
                # 1) input key error(reqid, type, value)
                if key not in request.keys():
                    return errors('001', key)
                # 2) input value error (reqid, type, value)
                elif request[key] == '':
                    return errors('002', key)
                # 3) errors 007
                elif key == 'path' and not request['path'].endswith('/'):
                    return errors('007')
        else:
            return errors('999')
"""
