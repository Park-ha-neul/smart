import tensorflow as tf
import logging
import pandas as pd
import csv
import ast
# from collections import OrderedDict
# local 에서 다른 파일 import
from . import FormulationToCQAs as fc
# 플랫폼 에서 다른 파일 import
# from FormulationToCQAs import *

# tf.logging.set_verbosity(tf.logging.INFO)

def train(tm):
    pass

def init_svc(im):
    pass

def inference(df, params, batch_id):
    try:
        value = df.values[0,0]
        res_dict = {}
        response = {}
        # local 에서 다른 파일 import
        tuple = fc.Formulation2CQAs(value, params)
        # 플랫폼에서 다른 파일 import
#         tuple = Formulation2CQAs(value, params)

        CQAs_List=tuple[0]
        code = tuple[1]
        msg = tuple[2]

        if len(CQAs_List)!=0: #CQAs_List 값이 있을 때만 넣어줌
            res_dict["CQAs List"]=CQAs_List

        response["result"]=res_dict
        response["code"]=code
        response["msg"]=msg

    except:
        code = "999"
        msg = "정의되지 않은 error입니다."
        response["code"]=code
        response["msg"]=msg

    return response
