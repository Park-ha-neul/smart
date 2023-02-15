# import tensorflow as tf
import logging
import pandas as pd
import json
import csv
from collections import OrderedDict
import datetime
from ClassifiType import ClassifiType as ct

# tf.logging.set_verbosity(tf.logging.INFO)

# today = datetime.date.today() data = { 'date': today.strftime('%Y-%m-%d')}

#now = datetime.date.today()
now = datetime.datetime.now()
# logging.info('now : %s', now)

def train(tm):
    pass

def train_tmp():
    pass


def init_svc(im):
    pass

def inference(df, params, batch_id):
    response = {}
    value = df.values[0,0]
    tuple = ct(value)
    request = tuple[0]
    res_dict=tuple[1]
    code = tuple[2]
    msg = tuple[3]

    try:
        response["request"] = request
        response["result"]=res_dict
        response["code"]=code
        response["msg"]=msg

    except:
        code = "999"
        msg = "정의되지 않은 error입니다."
        response["code"]=code
        response["msg"]=msg

    return response