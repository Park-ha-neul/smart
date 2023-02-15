import tensorflow as tf
import logging
import csv
import ast
from source_code import *
import pandas as pd
# error 확인을 위한 추가 import
import traceback

tf.logging.set_verbosity(tf.logging.INFO)

def train(tm):
    pass

def init_svc(im):
    # 입력 받은 smiles 로 ingredient 와 kind 추출하기 위한 데이터
    rxlist_df = pd.read_csv('/data/aip/activate/rxlist_new.csv', encoding='cp949')
    # ingredient 와 제형에 해당하는 최대, 최소를 추출하기 위한 데이터, 데이터 단위 변환 필요
    change_log_df = pd.read_csv('/data/aip/activate/Change_Log.csv', encoding='cp949')
    # 입력 받은 smiles 가 chemical.csv 에 존재하지 않을 경우 error 처리
    chemical_df = pd.read_csv('/data/aip/activate/chemical.csv', encoding='cp949')
    # 입력 받은 투여경로에 해당하는 제형이 routes.csv 에 존재하지 않을 경우 error 처리
    routes_df = pd.read_csv('/data/aip/activate/routes.csv', encoding='cp949')

    return {
        "rxlist_df" : rxlist_df,
        "change_log_df" : change_log_df,
        "chemical_df" : chemical_df,
        "routes_df" : routes_df
    }

def inference(df, params, batch_id):
    res_dict = {}
    response = {}

    try:
        value = df.values[0,0]
        #api3 function에서 넘어오는 값이 tuple
        tuple = RecExcipients(value, params)

        formulation_list = tuple[0]
        code = tuple[1]
        msg = tuple[2]

        res_dict["formulation_list"]=formulation_list
        response["code"]=code
        response["msg"]=msg
        if len(formulation_list)==0:
            response["result"] = {}
        else:
            response["result"] = res_dict

    except Exception as e:
        print(e)
        print(traceback.format_exc())
        code = "999"
        msg = "정의되지 않은 error입니다."
        response["code"]=code
        response["msg"]=msg
    print('response : ', response)
    logging.info('########### type response : %s', type(response))
    return response

