import logging
import pandas as pd
import csv
import ast
# local 에서 다른 파일을 import 해올 때는 아래와 같이 입력
from . import source_code as sc
## 플랫폼에 등록할때 다른 파일을 import 해올 때는 아래와 같이 입력
# from RoutesToFormulation import *

# 학습 pass
def train(tm):
    pass

# 전처리 pass
def init_svc(im):
    pass

# 추론
def inference(df, params,batch_id):
    response = {}
    # df = 추론 테스트 시 입력 값 형태) [['입력 값']]
    value=df.values[0,0]
    # local 에서 다른 파일 import
    tuple = sc.frame(value, params)
    # 플랫폼 에서 다른 파일 import
#     tuple = RoutesToFormulation(value, params)
    res_dict=tuple[0]
    code = tuple[1]
    msg = tuple[2]
    try:
        response["result"]=res_dict
        response["code"]=code
        response["msg"]=msg
    except:
        code = "999"
        msg = "정의되지 않은 error입니다."
        response["code"]=code
        response["msg"]=msg

    return response