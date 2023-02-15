#-*- coding: utf-8 -*-
# 소스 전체 수정 필요, if문이 너무 많고 어디가 어떤 부분인지 이해가 잘 되지 않음. R 스크립트 돌린 후 input, output 파악되면 수정 진행
import json
import logging
import pandas as pd
import ast
# local 다른 파일 import
from . import source_code
# 플랫폼
# import source_code

def train(tm):
    pass

def init_svc(im):
    pass

def inference(df, params, batch_id):
    ingredient_list=[]
    response={}
    try:
        value = df.values[0,0] #입력값 받아오기
        tuple = source_code.frame(value)
        res_dict=tuple[0] #api5가 return 해준 dict
        code = tuple[1] #api5가 return 해준 code
        msg = tuple[2] #api5가 return 해준 msg
        response["result"]=res_dict
        response["code"]=code
        response["msg"]=msg
    except:
        code = "999"
        msg = "정의되지 않은 error입니다."
        response["code"]=code
        response["msg"]=msg
    return response

