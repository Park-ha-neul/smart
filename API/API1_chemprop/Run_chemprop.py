# -*- coding: utf-8 -*-
import datetime
import csv
from config import InitSvcConfig, errors

from api_train.preprocessing import preprocess
from api_train.chemprop_train import regression_train, classification_train
from api_train.hyperoptimization import hyperopt_reg as hpot

from api_predict import input2smiles
from api_predict.chemprop_predict import PredictArgsLoadModel, predict
from api_predict.postprocessing import postprocess

from response.response_and_errors import req_res_and_errors, request_error
from ast import literal_eval # 0405_16

import logging
# today = datetime.date.today() data = { 'date': today.strftime('%Y-%m-%d')}

# now = datetime.date.today()
now = datetime.datetime.now()

logging.basicConfig(level=logging.INFO)

# logging.info('now : %s', now)


def train(tm):
    hyperopt_ = tm.param_info['hyperopt']
    hyperopt_str = '---hyperopt is ---{}{}{}'.format(str(hyperopt_),'/',type(hyperopt_))

    logging.info(hyperopt_str)
    # preprocessing
    # 전처리는 transform()에서 구현하지 않고 여기에서 구현하겠음.
    # {name:data_path} dictionary
    data_paths = preprocess(tm)

    # hyperoptimization 적용유무는 regression에만 적용.
    # model 생성, 학습, 저장 => tensorflow 문법 그대로 안따라가도 되고 모듈에 맞춰서 arguments를 넣어주면 알아서 model생성, 학습, 저장까지 한번에 가능함.
    # tm.model_path: 학습 수행후 학습모델을 저장하는 경로

    for name, data_path in data_paths.items():
        # api_train - regression, classification 별로 나눠서 진행.
        # regression dataset
        if 'regression' in name:
            if tm.param_info['hyperopt']==str(True):  # 플랫폼 상에서 True를 적어주면 str으로 True를 받는다. 0413
                logging.info(f'----IF HYPEROPT 1-- HYPEROPT--')
                hpo_param_path = hpot.hyp(name, data_path)  # return f'./hyperopt_{name}.json'
                regression_train(name, data_path, tm, hpo_param_path)
            else:  # hyperoptimization은 regression 일 때만 진행
                logging.info(f'----ELSE-- NO HYPEROPT--')
                regression_train(name, data_path, tm, None)
        elif 'classification' in name:
            classification_train(name, data_path, tm)




def init_svc(im):
    # 1. params 구성하기
    # 1) 모델 로드하기.
    model_param = {'args_model': PredictArgsLoadModel(im)} # 0405 im
    # 2) 플랫폼에서 지정한 파라미터 딕셔너리 가져오기
    param = im.param_info

    # 3) 모두 합쳐버리기.
    params = {**param, **model_param}

    # 4) chemical name csv call
    # 11만개의 {chemical name : smiles} 를 mapping 한 파일 열기
    with open(InitSvcConfig.chemical_csv, mode='r') as inp:
        reader = csv.reader(inp)
        dict_from_csv: dict = {rows[1]: rows[2] for rows in reader}
    params['chemical_name_smiles_dict'] = dict_from_csv

    return params


def inference(df, params, batch_id):
    """

     :param df: df.values[0,0] -> reqid,type,value,path

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

     request : reqid, type,value,path

     res_dict : 물성 예측값

     code: 000

     msg : success

     """
    logging.info(f'----------THIS IS DF : {df}-----------') #0405
    response = {}
    value_0 = df.values[0, 0]
    value = literal_eval(value_0) #0405_16

    logging.info(value['reqid'])

    # 에러처리 먼저 시행
    errors_or_input = [request_error(value)]

    if '{}' not in errors_or_input:  # request 에러가 아닐 경우
        # smiles 로 변환
        errors_or_smiles = input2smiles.to_smiles(value, params)
        if '{}' in [errors_or_smiles]:
            # 에러일 때 : 004 chemical name, 006 sdf file 형식, 003 type 입력 에러
            request, res_dict, code, msg = errors_or_smiles
        else:
            smiles = errors_or_smiles
            # 물성예측
            predicted_regression_dict, predicted_classification_dict = predict([[smiles]], params)
            # predict 할 때 smiles를 2차원 배열로 담아줘야 한다. 0412

            # 후처리
            pred_dict = postprocess(smiles, predicted_regression_dict, predicted_classification_dict)

            # 성공할 경우의 response 구성
            code, msg = errors('000')
            # response_func - request, res_dict 한번에 묶어버리기. 분자그림 포함
            # (본디 sdf_path은 고정이나 input type이 sdf_file path면 sdf_path==input_value라  따로 빼기가 애매함)
            request_res_dict_or_error = req_res_and_errors(value, smiles, pred_dict)  # 0404

            if "error" in request_res_dict_or_error:
                # 에러 003 type은 chemical, smiles, sdf중 하나를 입력S해주세요.
                request, res_dict, code, msg = errors('003')
            else:
                request = request_res_dict_or_error[0]
                res_dict = request_res_dict_or_error[1]
    else:
        # request 에러 001,002,007 일 경우 -
        request = errors_or_input[0]
        res_dict = errors_or_input[1]
        code = errors_or_input[2]
        msg = errors_or_input[3]

    try:
        response["request"] = request
        response["result"] = res_dict
        response["code"] = code
        response["msg"] = msg
    except:
        response['code'], response['msg'] = errors('999')

    return response


