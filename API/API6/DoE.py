import tensorflow as tf
import logging
from collections import OrderedDict
import api6 as ap

tf.logging.set_verbosity(tf.logging.INFO)

def train(tm):
    pass

def init_svc(im):
    pass

def inference(df, params, batch_id):
    response = {}
    value = df.values[0,0]
    tuple = ap.api6(value)
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
